package com.mbanna.bookshelf.views.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mbanna.bookshelf.R
import com.mbanna.bookshelf.utils.FavoriteManager
import com.mbanna.bookshelf.utils.JsonParser
import com.mbanna.bookshelf.viewModel.BookShelfViewModel
import com.mbanna.bookshelf.viewModel.factory.BookShelfViewModelFactory
import com.mbanna.bookshelf.views.adapters.BookShelvesAdapter
import com.mbanna.bookshelf.views.fragments.DetailPage

class BookShelf : AppCompatActivity(), BookShelvesAdapter.SelfAdapterCallback, View.OnClickListener {

    lateinit var binding: com.mbanna.bookshelf.databinding.ActivityBookShelfBinding
    lateinit var viewModel: BookShelfViewModel
    private lateinit var adapter: BookShelvesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_book_shelf)

        initialise()
        attachListeners()
     }

    private fun attachListeners() {
        binding.btnHits.setOnClickListener(this)
        binding.btnTitle.setOnClickListener(this)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                performLogout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun performLogout() {
        val pref = getSharedPreferences("login", MODE_PRIVATE)
        val editor = pref.edit()
        editor?.putBoolean("flag", false)
        editor?.apply()
        val intent = Intent( this, LoginScreen::class.java)
        finish()
        startActivity(intent)
    }


    private fun initialise() {
        viewModel = ViewModelProvider(
            this,
            BookShelfViewModelFactory( )
        )[BookShelfViewModel::class.java]

        val books = JsonParser.parseJsonToBookItems(this,"ZEDF.json")
        viewModel.bookList.addAll(books)
        adapter = BookShelvesAdapter( bookList = books , cellClickListener = this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    override fun onFavouriteClick(position: Int) {
        if (FavoriteManager.isFavorite(this,viewModel.bookList[position].id + "")){
            FavoriteManager.removeFavorite(this,viewModel.bookList[position].id + "")
        }else{
            FavoriteManager.addFavorite(this,viewModel.bookList[position].id + "")
        }
    }

    override fun onBookClick(position: Int) {
        val detailPageFragment = DetailPage.Companion.newInstance()

        val args = Bundle()
        args.putParcelable("bookItem", viewModel.bookList[position])
        detailPageFragment.arguments = args

        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, detailPageFragment)
            .addToBackStack("Detail")
            .commit()
    }

    override fun onClick(p0: View?) {
        when(p0){
            binding.btnTitle ->{
                refreshList(true)
            }

            binding.btnHits -> {
                refreshList(false)
            }
        }
    }

    private fun refreshList(button: Boolean) {
        if (button){
            binding.btnHits.backgroundTintList =ColorStateList.valueOf(Color.GRAY)
            binding.btnTitle.backgroundTintList = null
            viewModel.bookSortType = true
        }else{
            binding.btnTitle.backgroundTintList =ColorStateList.valueOf(Color.GRAY)
            binding.btnHits.backgroundTintList = null
            viewModel.bookSortType = false
        }
        viewModel.sortBasedOnSelection()
        adapter.update(viewModel.bookList)
    }
}