package com.kozel.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MenuActivity : AppCompatActivity() {

    private lateinit var resultField1: TextView
    private lateinit var resultField2: TextView
    private lateinit var raz: TextView
    private lateinit var numberField1: EditText
    private lateinit var numberField2: EditText
    private lateinit var name1: TextView
    private lateinit var name2: TextView
    private lateinit var main: LinearLayout
    private var s = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        resultField1 = findViewById(R.id.resultField1)
        resultField2 = findViewById(R.id.resultField2)
        numberField1 = findViewById(R.id.numberField1)
        numberField2 = findViewById(R.id.numberField2)
        name1 = findViewById(R.id.name1)
        name2 = findViewById(R.id.name2)
        raz = findViewById(R.id.textViewRaz)
        main = findViewById(R.id.main)
        loadText(this)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val animDrawable = main.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(10)
        animDrawable.setExitFadeDuration(5000)
        animDrawable.start()


        numberField1.setOnEditorActionListener { textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                onClick(numberField1)
                numberField1.hideKeyboard()
                handled = true
            }
            handled
        }
        numberField2.setOnEditorActionListener { textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                onClick(numberField2)
                numberField2.hideKeyboard()
                handled = true
            }
            handled
        }
    }

    private fun saveText() {
        val sPref = getSharedPreferences("Save2.txt", Context.MODE_PRIVATE)
        val ed = sPref.edit()
        ed.putString("res1", resultField1.text.toString())
        ed.putString("res2", resultField2.text.toString())
        ed.putString("name1", name1.text.toString())
        ed.putString("name2", name2.text.toString())
        ed.putInt("s", s)
        ed.apply()
    }

    private fun loadText(context: Context) {
        val sPref = context.getSharedPreferences("Save2.txt", Context.MODE_PRIVATE)
        name1.text = sPref.getString("name1", "")
        name2.text = sPref.getString("name2", "")
        if (sPref.getString("res1", "") != "")
            resultField1.text = sPref.getString("res1", "")
        if (sPref.getString("res2", "") != "")
            resultField2.text = sPref.getString("res2", "")
        if (sPref.getInt("s", 0) != 0) {
            raz.text = "Баллы за раздачу ${sPref.getInt("s", 0)}:"
            s = sPref.getInt("s", 0)
        }
    }

    fun onNewClick(view: View) {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setTitle("Начать новую игру?")
        dialogBuilder.setPositiveButton("Да") { dialog, whichButton ->
            numberField1.setText("")
            numberField2.setText("")
            name1.text = ""
            name2.text = ""
            resultField1.text = "0"
            resultField2.text = "0"
            raz.text = "Баллы за раздачу 1:"
            s = 1
        }
        dialogBuilder.setNegativeButton("Нет") { dialog, whichButton -> }
        val b = dialogBuilder.create()
        b.show()
    }

    fun onClick(view: View)
    {
        numberField1.hideKeyboard()
        numberField2.hideKeyboard()
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setTitle("Добавить очки?")
        dialogBuilder.setPositiveButton("Да") { dialog, whichButton ->
            toSum()
        }
        dialogBuilder.setNegativeButton("Нет") { dialog, whichButton -> }
        val b = dialogBuilder.create()
        b.show()
    }

    private fun toSum() {
        var supp: Int
        if (numberField1.text.toString() != "") {
            supp = resultField1.text.toString().toInt()
            supp += numberField1.text.toString().toInt()
            resultField1.text = supp.toString()
        }
        if (numberField2.text.toString() != "") {
            supp = resultField2.text.toString().toInt()
            supp += numberField2.text.toString().toInt()
            resultField2.text = supp.toString()
        }
        numberField1.setText("")
        numberField2.setText("")
        s++
        raz.text = "Баллы за раздачу $s:"
    }

    override fun onStop() {
        super.onStop()
        saveText()
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onBackPressed() {
        saveText()
        AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Выход")
            .setMessage("Вы уверены что хотите выйти?")
            .setPositiveButton(
                "Да"
            ) { _, _ ->
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }.setNegativeButton("Нет", null).show()
    }
}

