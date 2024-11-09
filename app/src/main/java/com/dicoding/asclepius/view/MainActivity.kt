package com.dicoding.asclepius.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.fragment.HistoryFragment
import com.dicoding.asclepius.fragment.HomeFragment
import com.dicoding.asclepius.fragment.NewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setip bottom navigation bar
        bottomNavBar = binding.bottomNavigation
        bottomNavBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_menu_home -> {
                    setupBottomNavBar(HomeFragment())
                    true
                }
                R.id.bottom_menu_news -> {
                    setupBottomNavBar(NewsFragment())
                    true
                }
                R.id.bottom_menu_history -> {
                    setupBottomNavBar(HistoryFragment())
                    true
                }
                else -> false
            }
        }
        // set default fragment
        setupBottomNavBar(HomeFragment())
    }

    private fun setupBottomNavBar(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment !is HomeFragment) {
            bottomNavBar.selectedItemId = R.id.bottom_menu_home
        } else {
            super.onBackPressed()
        }
    }

}