package com.example.jsonviewer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private var menuItems: List<String> = emptyList()

    fun submitList(items: List<String>) {
        menuItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuItems[position])
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val menuItemTextView: TextView = itemView.findViewById(R.id.menuItemTextView)

        fun bind(menuItem: String) {
            menuItemTextView.text = menuItem
        }
    }
}
