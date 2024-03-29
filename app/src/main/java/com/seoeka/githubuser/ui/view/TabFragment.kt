package com.seoeka.githubuser.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.seoeka.githubuser.R
import com.seoeka.githubuser.data.response.UserItems
import com.seoeka.githubuser.databinding.FragmentTabBinding
import com.seoeka.githubuser.ui.adapters.ListUserAdapter
import com.seoeka.githubuser.ui.mvvm.viewmodel.DetailViewModel


class TabFragment : Fragment() {
    private lateinit var binding: FragmentTabBinding
    private val viewModel: DetailViewModel by activityViewModels()
    private var position: Int? = null
    private var username: String? = null

    companion object {
        const val ARG_POSITION = "param1"
        const val ARG_USERNAME = "param2"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        binding.rvFollow.setHasFixedSize(true)

        if (position == 1){
            viewModel.getUserFollower(username!!)
            viewModel.userFollower.observe(viewLifecycleOwner){
                    followList -> setFollowData(followList)
            }
            viewModel.isLoadingFollow.observe(viewLifecycleOwner) {
                showLoadingTab(it)
            }
        } else {
            viewModel.getUserFollowing(username!!)
            viewModel.userFollowing.observe(viewLifecycleOwner){
                    followList -> setFollowData(followList)
            }
            viewModel.isLoadingFollow.observe(viewLifecycleOwner) {
                showLoadingTab(it)
            }
        }

    }

    private fun setFollowData(userFollow: List<UserItems>) {
        val adapter = ListUserAdapter(userFollow)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoadingTab(isLoading: Boolean) {
        binding.progressBarTab.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabBinding.inflate(layoutInflater)

        return binding.root
    }
}