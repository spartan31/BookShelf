package com.mbanna.bookshelf.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mbanna.bookshelf.R
import com.mbanna.bookshelf.databinding.FragmentDetailPageBinding
import com.mbanna.bookshelf.model.BookItem
import com.mbanna.bookshelf.utils.CommonUtils
import com.mbanna.bookshelf.utils.FavoriteManager
import com.mbanna.bookshelf.viewModel.DetailViewModel
import com.mbanna.bookshelf.viewModel.factory.DetailViewModelFactory

class DetailPage : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentDetailPageBinding
    lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailPageBinding.inflate(inflater, container, false)
        val bookItem = arguments?.getParcelable<BookItem>("bookItem")
        viewModel = ViewModelProvider(this, DetailViewModelFactory())[DetailViewModel::class.java]
        viewModel.bookItem = bookItem!!
        viewModel.performUpdate()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        try {
            attachListeners()
            markFavourite()
            CommonUtils.loadImage(requireContext(), viewModel.bookItem.image, binding.imageView)
        } catch (e: Exception) {
            Log.i("Detail", e.printStackTrace().toString())
        }


        return binding.root
    }

    private fun markFavourite() {
        if (FavoriteManager.isFavorite(requireContext(), viewModel.bookItem.id + "")) {
            binding.btnFav.setImageResource(R.drawable.ic_fav_icon_filled)
        } else {
            binding.btnFav.setImageResource(R.drawable.ic_fav_icon)
        }
    }

    private fun attachListeners() {
        binding.btnFav.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailPage()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnFav -> {
                if (FavoriteManager.isFavorite(requireContext(), viewModel.bookItem.id + "")) {
                    FavoriteManager.removeFavorite(requireContext(), viewModel.bookItem.id + "")
                    binding.btnFav.setImageResource(R.drawable.ic_fav_icon)
                } else {
                    FavoriteManager.addFavorite(requireContext(), viewModel.bookItem.id + "")
                    binding.btnFav.setImageResource(R.drawable.ic_fav_icon_filled)
                }
            }
        }
    }
}
