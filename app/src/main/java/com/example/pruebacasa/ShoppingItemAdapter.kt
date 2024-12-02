package com.example.pruebacasa

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebacasa.R // Asegúrate de que el nombre de tu paquete coincida


// Data class para representar un item de la lista
data class ShoppingItem(
    val name: String,
    var quantity: Int = 1, //Esto van a ser las cantidades
    val isDeleted: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(quantity) //Guardar las cantidades
        parcel.writeByte(if (isDeleted) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ShoppingItem> {
        override fun createFromParcel(parcel: Parcel): ShoppingItem {
            return ShoppingItem(parcel)
        }

        override fun newArray(size: Int): Array<ShoppingItem?> {
            return arrayOfNulls(size)
        }
    }
} //La parcela es para al rotar la pantalla no perder los elementos en la actualizacion

class ShoppingItemAdapter(
    private val shoppingItems: MutableList<ShoppingItem>,
    private val onDeleteClicked: (Int) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemViewHolder>() {

    // Método para agregar un item a la lista
    fun addItem(itemText: String) {

        val itemExiste = shoppingItems.any{it.name.equals(itemText,ignoreCase = true)}

        if (itemExiste){
            Toast.makeText(context, "El producto ya existe en la lista", Toast.LENGTH_SHORT).show()
        }else{
            shoppingItems.add(0,ShoppingItem(name = itemText))
            notifyItemInserted(0) // Notificar que un nuevo item se ha insertado
        }


    }

    // Inflar el layout de cada item nuevo
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingItemAdapter.ShoppingItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shopping, parent, false)
        return ShoppingItemViewHolder(view)
    }

    // Asignar los datos del item
    override fun onBindViewHolder(
        holder: ShoppingItemAdapter.ShoppingItemViewHolder,
        position: Int
    ) {
        // Verificar que el índice sea válido antes de asignar datos
        if (position >= 0 && position < shoppingItems.size) {
            val item = shoppingItems[position]
            holder.bind(item)

            // Configurar el botón de borrar
            holder.deleteButton.setOnClickListener {
                onDeleteClicked(position) // Llamar a la función para eliminar
            }
        }
    }

    // Devolver el número de items en la lista
    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    // ViewHolder que representa cada item
    inner class ShoppingItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val itemNameTextView: TextView = item.findViewById(R.id.itemName)
        private val itemQuantityEditText: EditText = item.findViewById(R.id.itemQuantity)
        val deleteButton: Button = item.findViewById(R.id.deleteButton)

        fun bind(shoppingItem: ShoppingItem) {
            itemNameTextView.text = shoppingItem.name
            itemQuantityEditText.setText(shoppingItem.quantity.toString()) // Mostrar la cantidad actual

            // Escuchar cambios en la cantidad
            itemQuantityEditText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val newQuantity = itemQuantityEditText.text.toString().toIntOrNull() ?: 1
                    shoppingItem.quantity = newQuantity // Actualizar el modelo
                }
            }

            deleteButton.isEnabled = !shoppingItem.isDeleted
        }
    }

    // Función para eliminar un item de la lista
    fun deleteItem(position: Int) {
        if (position >= 0 && position < shoppingItems.size) { // Validar posición
            shoppingItems.removeAt(position) // Eliminar el ítem
            notifyItemRemoved(position) // Notificar al RecyclerView
            notifyItemRangeChanged(position, shoppingItems.size - position) // Sincronizar índices
        }
    }
    //necesario para la actualización al rotar la pantalla
    fun getItems(): List<ShoppingItem> {
        return shoppingItems
    }

    fun updateItems(newItems: List<ShoppingItem>) {
        shoppingItems.clear() // Borra los elementos existentes
        shoppingItems.addAll(newItems) // Añade los nuevos elementos
        notifyDataSetChanged() // Notifica al adaptador que la lista ha cambiado
    }
}