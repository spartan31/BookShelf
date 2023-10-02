package com.mbanna.bookshelf.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mbanna.bookshelf.R
import com.mbanna.bookshelf.databinding.ItemBookRowBinding
import com.mbanna.bookshelf.model.BookItem
import com.mbanna.bookshelf.utils.CommonUtils
import com.mbanna.bookshelf.utils.FavoriteManager


class BookShelvesAdapter(
    private val cellClickListener: SelfAdapterCallback,
    private var bookList: List<BookItem>
) :
    RecyclerView.Adapter<BookShelvesAdapter.BookViewHolder>() {

    class BookViewHolder(val binding: ItemBookRowBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemBookRowBinding.inflate(layoutInflater)

        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = bookList[position]
        holder.binding.bookTitle.text = currentBook.title

        holder.binding.bookHits.text = "Hits :  ${currentBook.hits}"

        if (FavoriteManager.isFavorite(holder.binding.root.context ,currentBook.id +"")){
            holder.binding.btnFav.setImageResource(R.drawable.ic_fav_icon_filled)
        }else{
            holder.binding.btnFav.setImageResource(R.drawable.ic_fav_icon)
        }

        CommonUtils.loadImage(holder.binding.bookImage.context , currentBook.image , holder.binding.bookImage)

        holder.binding.btnFav.setOnClickListener {
            if (!FavoriteManager.isFavorite(holder.binding.root.context ,currentBook.id +"")){
                holder.binding.btnFav.setImageResource(R.drawable.ic_fav_icon_filled)
            }else{
                holder.binding.btnFav.setImageResource(R.drawable.ic_fav_icon)
            }
            cellClickListener.onFavouriteClick(position)
        }
        holder.binding.root.setOnClickListener{
            cellClickListener.onBookClick(position)
        }
        holder.binding.executePendingBindings()
    }

    fun update(data: List<BookItem>) {
        clearList()
        val tempList : ArrayList<BookItem> = arrayListOf()
        tempList.addAll(bookList);
        tempList.addAll(data)
        bookList = tempList
        notifyDataSetChanged()
    }
    private fun clearList(){
        bookList = emptyList();
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    interface SelfAdapterCallback {
        fun onFavouriteClick(position: Int)
        fun onBookClick(position: Int)
    }
}