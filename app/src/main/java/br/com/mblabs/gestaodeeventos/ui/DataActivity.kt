package br.com.mblabs.gestaodeeventos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import br.com.mblabs.gestaodeeventos.R
import br.com.mblabs.gestaodeeventos.databinding.ActivityDataBinding
import com.google.android.material.textfield.TextInputLayout

class DataActivity : AppCompatActivity() {

    data class DropDownMenu(
        val inputLayout: TextInputLayout,
        val items: List<String>
    )

    private lateinit var binding: ActivityDataBinding
    private lateinit var listDropDownMenu: List<DropDownMenu>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data)
        listDropDownMenu = listOf(
            DropDownMenu(binding.edtUF, listOf("BA", "SC", "MG", "SP", "RJ")),
            DropDownMenu(binding.edtCity, listOf("BA")),
            DropDownMenu(binding.edtSkin, listOf("SC")),
            DropDownMenu(binding.edtSex, listOf("MG")),
            DropDownMenu(binding.edtGender, listOf("SP")),
            DropDownMenu(binding.edtDP, listOf("RJ"))
        )
        configureWindow()
        setAdapters()
    }

    private fun configureWindow() {
        this.window.statusBarColor = this.getColor(R.color.medium_slate_blue)

        val winController = WindowInsetsControllerCompat(window, binding.root)
        winController.isAppearanceLightStatusBars = false
        winController.isAppearanceLightNavigationBars = false
    }

    private fun setAdapters() {
        for (item in listDropDownMenu) {
            val adapter = ArrayAdapter(this, R.layout.list_item, item.items)
            (item.inputLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
    }
}