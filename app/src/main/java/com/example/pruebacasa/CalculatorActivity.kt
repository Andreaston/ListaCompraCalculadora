package com.example.pruebacasa

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CalculatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        val num1EditText = findViewById<EditText>(R.id.num1EditText)
        val num2EditText = findViewById<EditText>(R.id.num2EditText)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        val addButton = findViewById<Button>(R.id.addButton)
        val subtractButton = findViewById<Button>(R.id.subtractButton)
        val navigateBackButton = findViewById<Button>(R.id.navigateBackButton)

        // Botones de operación
        addButton.setOnClickListener {
            val num1 = num1EditText.text.toString().toDoubleOrNull()
            val num2 = num2EditText.text.toString().toDoubleOrNull()
            if (num1 != null && num2 != null) {
                resultTextView.text = "Resultado: ${num1 + num2}"
            } else {
                resultTextView.text = "Por favor, introduce números válidos."
            }
        }

        subtractButton.setOnClickListener {
            val num1 = num1EditText.text.toString().toDoubleOrNull()
            val num2 = num2EditText.text.toString().toDoubleOrNull()
            if (num1 != null && num2 != null) {
                resultTextView.text = "Resultado: ${num1 - num2}"
            } else {
                resultTextView.text = "Por favor, introduce números válidos."
            }
        }

        // Botón para volver a la lista
        navigateBackButton.setOnClickListener {
            finish() // Cierra la actividad actual y vuelve a la anterior
        }
    }
}