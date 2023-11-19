package com.yourbynn.byy_appstory.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourbynn.byy_appstory.R
import com.yourbynn.byy_appstory.data.story.StoriesAdapter
import com.yourbynn.byy_appstory.databinding.ActivityMainBinding
import com.yourbynn.byy_appstory.view.activities.UploadStoriesActivity
import com.yourbynn.byy_appstory.view.activities.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private lateinit var binding: ActivityMainBinding
    private val adapter = StoriesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)
        viewModel.getSession().observe(this) { user ->
            val token = user.token
            viewModel.getStories(token)
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = adapter

        viewModel.listStories.observe(this) { stories ->
            adapter.setStories(stories)
            showLoading(false)
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, UploadStoriesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            viewModel.logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}