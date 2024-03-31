package com.seoeka.githubuser.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.seoeka.githubuser.R
import com.seoeka.githubuser.data.response.UserItems
import com.seoeka.githubuser.databinding.ActivityMainBinding
import com.seoeka.githubuser.ui.adapter.ListUserAdapter
import com.seoeka.githubuser.ui.mvvm.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        binding.buttonSearch.setOnClickListener {
            getUsers()
        }

        binding.editTextQuery.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
                ) {
                    getUsers()
                    return true
                }
                return false
            }
        })

        viewModel.listUsers.observe(this) { users ->
            setUserData(users as List<UserItems>)
        }
        viewModel.isLoadingState.observe(this) {
            showLoading(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUserData(users: List<UserItems>) {
        if (users.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.textEmptyResult.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.textEmptyResult.visibility = View.GONE
            val adapter = ListUserAdapter(users)
            binding.recyclerView.adapter = adapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getUsers() {
        val query = binding.editTextQuery.text.toString().trim()
        if (query.isNotEmpty()) {
            viewModel.getUsers(query)
        } else {
            viewModel.showEmptyQueryError()
        }
    }
}