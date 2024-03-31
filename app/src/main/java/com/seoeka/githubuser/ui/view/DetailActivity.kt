package com.seoeka.githubuser.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.seoeka.githubuser.R
import com.seoeka.githubuser.data.local.room.FavoriteUser
import com.seoeka.githubuser.data.response.DetailUserResponse
import com.seoeka.githubuser.databinding.ActivityDetailBinding
import com.seoeka.githubuser.ui.adapter.SectionsPagerAdapter
import com.seoeka.githubuser.ui.mvvm.factory.FavoriteViewModelFactory
import com.seoeka.githubuser.ui.mvvm.viewmodel.DetailViewModel

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private val viewModel by viewModels<DetailViewModel> {
        FavoriteViewModelFactory.getInstance(application)
    }
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setSupportActionBar(binding!!.topAppBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val usernameData = intent.getStringExtra(username)
        val avatarUrlData = intent.getStringExtra(avatarURL)

        if (usernameData != null) {
            viewModel.getUser(usernameData)
        }

        viewModel.userDetail.observe(this){
                userDetail -> setUserData(userDetail)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        if (usernameData != null) {
            sectionsPagerAdapter.username = usernameData
        }

        val viewPager: ViewPager2 = binding!!.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding!!.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val ivFavorite = binding!!.fabAdd
        var checkUser: FavoriteUser?

        viewModel.getFavoriteUser(usernameData!!).observe(this) {
            checkUser = it
            if (checkUser == null) {
                ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.ic_favorite_border))
                ivFavorite.setOnClickListener {
                    viewModel.getFavoriteUser(FavoriteUser(usernameData, avatarUrlData))
                    Toast.makeText(this, "$usernameData added to favorites", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.ic_favorite))
                isFavorite = true
                ivFavorite.setOnClickListener {
                    viewModel.deleteFavoriteUser(checkUser as FavoriteUser)
                    Toast.makeText(this, "$usernameData removed from favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.share -> {
                val detailUser = viewModel.userDetail.value
                if (detailUser != null) {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Check out ${detailUser.login}'s profile at ${detailUser.htmlUrl}")
                        type = "text/plain"
                    }
                    startActivity(Intent.createChooser(sendIntent, "Share"))
                    true
                } else {
                    false
                }

            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUserData(detailUser: DetailUserResponse) {
        binding!!.tvUsername.text = detailUser.login
        binding!!.tvUserName.text = detailUser.name
        binding!!.tvUserFollowing.text = "${detailUser.following.toString()} Following"
        binding!!.tvUserFollower.text = "${detailUser.followers.toString()} Followers"
        Glide.with(binding!!.root)
            .load(detailUser.avatarUrl)
            .into(binding!!.userImg)
    }

    private fun showLoading(isLoading: Boolean) {
        binding!!.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding!!.tvUserName.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding!!.tvUsername.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding!!.tvUserFollowing.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding!!.tvUserFollower.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding!!.userImg.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    companion object {
        const val username = "username"
        const val avatarURL = "url"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_title_1,
            R.string.tab_title_2
        )
    }
}