package com.seoeka.githubuser.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.seoeka.githubuser.R
import com.seoeka.githubuser.data.response.DetailUserResponse
import com.seoeka.githubuser.databinding.ActivityDetailBinding
import com.seoeka.githubuser.ui.adapter.SectionsPagerAdapter
import com.seoeka.githubuser.ui.mvvm.viewmodel.DetailViewModel

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
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
        binding.tvUsername.text = detailUser.login
        binding.tvUserName.text = detailUser.name
        binding.tvUserFollowing.text = "${detailUser.following.toString()} Following"
        binding.tvUserFollower.text = "${detailUser.followers.toString()} Followers"
        Glide.with(binding.root)
            .load(detailUser.avatarUrl)
            .into(binding.userImg)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.tvUserName.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.tvUsername.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.tvUserFollowing.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.tvUserFollower.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.userImg.visibility = if (isLoading) View.GONE else View.VISIBLE
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