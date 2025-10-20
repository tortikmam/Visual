package com.example.kukulator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class KukulatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kukulator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()

        var TextV: TextView = findViewById(R.id.textView2)
        TextV.text = "0"

        var bZero: Button = findViewById(R.id.button_zero)
        bZero.setOnClickListener({
            if (TextV.text == "0" || TextV.text == "ERROR") {
                TextV.setText("0")
            } else {
                TextV.setText(TextV.text.toString() + "0")
            }
        })

        var bOne: Button = findViewById(R.id.button_one)
        bOne.setOnClickListener({
            if (TextV.text == "0" || TextV.text == "ERROR") {
                TextV.setText("1")
            } else {
                TextV.setText(TextV.text.toString() + "1")
            }
        })

        var bTwo: Button = findViewById(R.id.button_two)
        bTwo.setOnClickListener({
            if (TextV.text == "0" || TextV.text == "ERROR") {
                TextV.setText("2")
            } else {
                TextV.setText(TextV.text.toString() + "2")
            }
        })

        var bThree: Button = findViewById(R.id.button_three)
        bThree.setOnClickListener({
            if (TextV.text == "0" || TextV.text == "ERROR") {
                TextV.setText("3")
            } else {
                TextV.setText(TextV.text.toString() + "3")
            }
        })

        var bFour: Button = findViewById(R.id.button_four)
        bFour.setOnClickListener({
            if (TextV.text == "0" || TextV.text == "ERROR") {
                TextV.setText("4")
            } else {
                TextV.setText(TextV.text.toString() + "4")
            }
        })

        var bFive: Button = findViewById(R.id.button_five)
        bFive.setOnClickListener({
            if (TextV.text == "0" || TextV.text == "ERROR") {
                TextV.setText("5")
            } else {
                TextV.setText(TextV.text.toString() + "5")
            }
        })

        var bSix: Button = findViewById(R.id.button_six)
        bSix.setOnClickListener({
            if (TextV.text == "0" || TextV.text == "ERROR") {
                TextV.setText("6")
            } else {
                TextV.setText(TextV.text.toString() + "6")
            }
        })

        var bSeven: Button = findViewById(R.id.button_seven)
        bSeven.setOnClickListener({
            if (TextV.text == "0" || TextV.text == "ERROR") {
                TextV.setText("7")
            } else {
                TextV.setText(TextV.text.toString() + "7")
            }
        })

        var bEight: Button = findViewById(R.id.button_eight)
        bEight.setOnClickListener({
            if (TextV.text == "0" || TextV.text == "ERROR") {
                TextV.setText("8")
            } else {
                TextV.setText(TextV.text.toString() + "8")
            }
        })

        var bNine: Button = findViewById(R.id.button_nine)
        bNine.setOnClickListener({
            if (TextV.text == "0" || TextV.text == "ERROR") {
                TextV.setText("9")
            } else {
                TextV.setText(TextV.text.toString() + "9")
            }
        })

        var bPlus: Button = findViewById(R.id.button_plus)
        bPlus.setOnClickListener({
            val currentText = TextV.text.toString()
            if (currentText.isNotEmpty()) {
                val lastChar = currentText.last()
                if (lastChar.isDigit() || lastChar == ')') {
                    TextV.setText(TextV.text.toString() + "+")
                }
                else if (lastChar in "+-*/") {
                    val newText = currentText.dropLast(1) + "+"
                    TextV.setText(newText)
                }
            }
        })

        var bMinus: Button = findViewById(R.id.button_minus)
        bMinus.setOnClickListener({
            val currentText = TextV.text.toString()
            if (currentText.isNotEmpty()) {
                val lastChar = currentText.last()
                if (lastChar.isDigit() || lastChar == ')' || lastChar in "+*/") {
                    TextV.setText(TextV.text.toString() + "-")
                }
            } else {
                TextV.setText("-")
            }
        })

        var bMultiply: Button = findViewById(R.id.button_multiply)
        bMultiply.setOnClickListener({
            val currentText = TextV.text.toString()
            if (currentText.isNotEmpty()) {
                val lastChar = currentText.last()
                if (lastChar.isDigit() || lastChar == ')') {
                    TextV.setText(TextV.text.toString() + "*")
                }
                else if (lastChar in "+-*/") {
                    val newText = currentText.dropLast(1) + "*"
                    TextV.setText(newText)
                }
            }
        })

        var bDivide: Button = findViewById(R.id.button_divide)
        bDivide.setOnClickListener({
            val currentText = TextV.text.toString()
            if (currentText.isNotEmpty()) {
                val lastChar = currentText.last()
                if (lastChar.isDigit() || lastChar == ')') {
                    TextV.setText(TextV.text.toString() + "/")
                }
                else if (lastChar in "+-*/") {
                    val newText = currentText.dropLast(1) + "/"
                    TextV.setText(newText)
                }
            }
        })

        var bClear: Button = findViewById(R.id.button_clear)
        bClear.setOnClickListener({
            TextV.setText("0")
        })

        var bEquals: Button = findViewById(R.id.button_equals)
        bEquals.setOnClickListener({
            var one = ""
            var two = ""
            var counter = 0
            var operat = ' '
            val text = TextV.text.toString()
            val primer = text.replace("\\s+".toRegex(), "")
            val sim = "+-/*"

            for (i in primer){
                if (i !in sim && counter == 0){
                    one = one + i
                }else if(i !in sim && counter == 1){
                    two = two + i
                }else{
                    operat = i
                    counter++
                }
            }

            if (one.isNotEmpty() && two.isNotEmpty() && operat in sim) {
                val num1 = one.toInt()
                val num2 = two.toInt()
                val otvet = when(operat){
                    '+' -> num1 + num2
                    '-' -> num1 - num2
                    '*' -> num1 * num2
                    '/' -> num1 / num2
                    else -> "ERROR"
                }
                TextV.setText(otvet.toString())
            } else {
                TextV.setText("ERROR")
            }
        })
    }
}