package dev.kkarot.installmentapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.kkarot.installmentapp.R
import dev.kkarot.installmentapp.databinding.ActivityMainBinding
import dev.kkarot.installmentapp.viewmodels.SharedViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            (supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment)

        sharedViewModel.onActivity()
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navHostFragment.navController.navigateUp()
    }
}