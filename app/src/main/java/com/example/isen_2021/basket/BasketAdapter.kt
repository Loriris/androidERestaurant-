package com.example.isen_2021.basket

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.isen_2021.R
import com.example.isen_2021.databinding.BasketCellBinding
import com.squareup.picasso.Picasso

/*
interface BasketCellInterface {
    fun onDeleteItem(item: BasketItem)
    fun onShowDetail(item: BasketItem) // Optional
}

 */

class BasketAdapter(private val basket: Basket,
                    private val context:Context,
                    /*private val delegate: BasketCellInterface*/
                    private val entryClickListener: (BasketItem) -> Unit): RecyclerView.Adapter<BasketAdapter.BasketViewHolder>() {

    class BasketViewHolder(binding: BasketCellBinding): RecyclerView.ViewHolder(binding.root) {
        private val itemTitle: TextView = binding.basketItemTitle
        private val itemPrice: TextView = binding.basketItemPrice
        private val itemQuantity: TextView = binding.basketItemQuantity
        private val itemImageView: ImageView = binding.basketItemImageView
        private val deleteButton: ImageView = binding.basketItemDelete
        val layout = binding.root

        fun bind(item: BasketItem, context: Context, entryClickListener: (BasketItem) -> Unit/*, delegate: BasketCellInterface*/) {
            itemTitle.text = item.dish.name
            itemPrice.text = "${item.dish.prices.first().price}€"
            itemQuantity.text = "${context.getString(R.string.quantity)} ${item.count.toString()}"
            Picasso.get()
                .load(item.dish.getThumbnailUrl())
                .placeholder(R.drawable.android_logo_restaurant)
                .into(itemImageView)
            deleteButton.setOnClickListener {
                //delegate.onDeleteItem(item)
                entryClickListener.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        return BasketViewHolder(BasketCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val item = basket.items[position]
        /*
        holder.layout.setOnClickListener {
            // Click sur detail item
            delegate.onShowDetail(item)
        }
        */
        holder.bind(item, context, entryClickListener)
    }

    override fun getItemCount(): Int {
        return basket.items.count()
    }
}