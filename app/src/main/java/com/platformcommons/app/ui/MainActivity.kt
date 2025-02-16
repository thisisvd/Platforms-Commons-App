package com.platformcommons.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.platformcommons.app.R
import com.platformcommons.app.core.events.RxBus
import com.platformcommons.app.core.events.RxEvents
import com.platformcommons.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navHostFragment: NavHostFragment

    private lateinit var navController: NavController

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.apply {

            setSupportActionBar(toolbar)
            navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            navController = navHostFragment.navController
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            NavigationUI.setupActionBarWithNavController(
                this@MainActivity, navController, appBarConfiguration
            )

            // listen to events
            onEventListeners()
        }
    }

    private fun onEventListeners() {
        binding.apply {
            try {
                disposable =
                    RxBus.listen(RxEvents.OnSyncCompleteEvent::class.java).subscribe { event ->
                        if (event.synced) {
                            Snackbar.make(
                                binding.root, "User Synced Successful", Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
            } catch (e: Exception) {
                Timber.d("Error : ${e.message}")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}