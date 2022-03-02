package com.example.pidonor.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.pidonor.R
import com.example.pidonor.Utils
import com.example.pidonor.viewModel.MatchingBloodGroupSVM

class HolderActivity : AppCompatActivity() {
    private lateinit var navHostFragment : NavHostFragment
    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holder)
        val sharedViewModel: MatchingBloodGroupSVM by viewModels()
        val intent = intent
        if (intent != null) {
            sharedViewModel.setEmailId(intent.getStringExtra(Utils.LOGGED_EMAIL_ID).toString())

        }
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this,navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Handle the back button event and return true to override
        // the default behavior the same way as the OnBackPressedCallback.

        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}