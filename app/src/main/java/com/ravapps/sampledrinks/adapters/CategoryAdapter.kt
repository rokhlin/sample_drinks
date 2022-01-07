package com.ravapps.sampledrinks.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ravapps.sampledrinks.R
import com.ravapps.sampledrinks.useLocalImage
import com.ravapps.sampledrinks.model.ItemModel


class CategoryAdapter(private val numColumns: Int = 1): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var _items: List<ItemModel> = emptyList()
    private var adapterClickListener: ((id: Int, item: ItemModel?, isGrid: Boolean) -> Unit)? = null
    private var adapterLongClickListener: ((id: Int, iitem: ItemModel?, sGrid: Boolean) -> Unit)? = null
    private fun isGrid() = numColumns > 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return if (isGrid()) ViewHolder(layoutInflater.inflate(R.layout.grid_item, parent, false))
        else ViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(_items[position])
    }

    override fun getItemCount() = _items.size

    fun setOnClickListener(action: (id: Int, item: ItemModel?, isGrid: Boolean) -> Unit){
        adapterClickListener = action
    }

    fun setOnLongClickListener(action: (id: Int, item: ItemModel?, isGrid: Boolean) -> Unit){
        adapterLongClickListener = action
    }

    fun updateDataset(items: List<ItemModel>) {
        _items = items
        notifyDataSetChanged()
    }



    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(itemView: View) {
            adapterClickListener?.apply {
                val id = getId(itemView)
                val data = _items.first{ item -> item.id == id}
                invoke(id, data, isGrid())
            }
        }

        override fun onLongClick(itemView: View): Boolean {
            adapterLongClickListener?.apply {
                val id = getId(itemView)
                val data = _items.first{ item -> item.id == id}
                invoke(id, data, isGrid())
            }

            return false
        }

        private fun getId(itemView: View): Int {
            val id = itemView.tag as Int
            return id
        }

        fun bind(item: ItemModel) {
            itemView.findViewById<TextView>(R.id.cName).text = item.title
            itemView.findViewById<TextView>(R.id.cSubtitle)?.apply { text = item.subTitle }
            itemView.tag = item.id
            itemView.resources.displayMetrics.densityDpi

            val screenWidth = itemView.resources.displayMetrics.widthPixels
            val delta = (numColumns + 1) * 10
            val cardWidth = (screenWidth - delta) / numColumns

            itemView.findViewById<ImageView>(R.id.cImage).apply {
                if(isGrid()) {
                    layoutParams.height = cardWidth
                    layoutParams.width = cardWidth
                }
                useLocalImage(item.imageName)
            }
        }





    }
}