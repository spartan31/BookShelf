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
import com.mbanna.bookshelf.utils.Constants
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
        binding.btnFavBy.setOnClickListener(this)
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
        startActivity(intent)
        finish()
    }


    private fun initialise() {
        viewModel = ViewModelProvider(
            this,
            BookShelfViewModelFactory( )
        )[BookShelfViewModel::class.java]

        val books = JsonParser.parseJsonToBookItems(this,"ZEDF.json")
        viewModel.bookList.clear()
        viewModel.bookList.addAll(books)
        viewModel.favoriteIds = HashSet()
        viewModel.favoriteIds = FavoriteManager.getFavoriteIds(this)
        adapter = BookShelvesAdapter( bookList = books , cellClickListener = this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        refreshList(viewModel.bookSortType)
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
                refreshList(Constants.SORT_BY_TITLE)
            }

            binding.btnHits -> {
                refreshList(Constants.SORT_BY_HITS)
            }
            binding.btnFavBy -> {
                refreshList(Constants.SORT_BY_FAVOURITE)
            }
        }
    }

    private fun refreshList(sortBy: Int) {
        when(sortBy){
            Constants.SORT_BY_TITLE -> {
                binding.btnHits.backgroundTintList =ColorStateList.valueOf(Color.GRAY)
                binding.btnFavBy.backgroundTintList =ColorStateList.valueOf(Color.GRAY)
                binding.btnTitle.backgroundTintList = null
                viewModel.bookSortType = Constants.SORT_BY_TITLE
            }

            Constants.SORT_BY_HITS -> {
                binding.btnTitle.backgroundTintList =ColorStateList.valueOf(Color.GRAY)
                binding.btnFavBy.backgroundTintList =ColorStateList.valueOf(Color.GRAY)
                binding.btnHits.backgroundTintList = null
                viewModel.bookSortType = Constants.SORT_BY_HITS
            }

            Constants.SORT_BY_FAVOURITE -> {
                binding.btnTitle.backgroundTintList =ColorStateList.valueOf(Color.GRAY)
                binding.btnHits.backgroundTintList =ColorStateList.valueOf(Color.GRAY)
                binding.btnFavBy.backgroundTintList = null
                viewModel.bookSortType = Constants.SORT_BY_FAVOURITE
            }
        }

        viewModel.favoriteIds = FavoriteManager.getFavoriteIds(this)
        viewModel.sortBasedOnSelection()
        adapter.update(viewModel.bookList)
    }
}