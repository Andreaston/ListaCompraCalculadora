package com.example.pruebacasa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebacasa.ui.theme.PruebaCasaTheme

class MainActivity : ComponentActivity() {

    // Definir adapter como una propiedad de la clase, si no pita en la lamda
    private lateinit var adapter: ShoppingItemAdapter

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Guarda la lista de elementos en el estado
        outState.putParcelableArrayList(
            "shopping_items",
            ArrayList(adapter.getItems()) // Adaptador debe tener un método para obtener los items
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //LLamamos al .xml con el front

        //Iniciamos el adaptador y el RecyclerView
        adapter = ShoppingItemAdapter(mutableListOf(), { position ->
            adapter.deleteItem(position)
        }, this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Configurar el botón de añadir
        val addButton = findViewById<Button>(R.id.addButton)
        val itemInput = findViewById<EditText>(R.id.itemInput)

        addButton.setOnClickListener{
            val itemText = itemInput.text.toString()
            val defaultQuantity  = 1 //Cantidad incial
            if (itemText.isNotBlank()) {
                adapter.addItem(itemText) //Añadir un elemento
                itemInput.text.clear() //Limpiar el campo del texto
            }
        }

        // Restaura la lista si hay datos guardados
        if (savedInstanceState != null) {
            val restoredItems = savedInstanceState.getParcelableArrayList<ShoppingItem>("shopping_items")
            restoredItems?.let {
                adapter.
                updateItems(it) // Actualiza los elementos en el adaptador
            }
        }

        //Configurar el botón para abrir la calculadora
        val openCalculatorButton = findViewById<Button>(R.id.openCalculatorButton)

        openCalculatorButton.setOnClickListener {
            val intent = Intent(this, CalculatorActivity::class.java)
            startActivity(intent) // Inicia la actividad de la calculadora
        }

    }

}


