package com.seoeka.githubuser.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.seoeka.githubuser.R
import com.seoeka.githubuser.data.response.UserItems
import com.seoeka.githubuser.databinding.ActivityFavoriteBinding
import com.seoeka.githubuser.ui.adapter.ListUserAdapter
import com.seoeka.githubuser.ui.mvvm.factory.FavoriteViewModelFactory
import com.seoeka.githubuser.ui.mvvm.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private val favoriteUserViewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModelFactory.getInstance(application)
    }
    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this)
        binding?.rvFavUsers?.layoutManager = layoutManager

        favoriteUserViewModel.getAllFavUser().observe(this) { users ->
            val items = arrayListOf<UserItems>()
            users!!.map {
                val item = UserItems(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            binding?.rvFavUsers?.adapter = ListUserAdapter(items)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_share, menu)
        menu.removeItem(R.id.share)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}