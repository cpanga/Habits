package com.example.habits

import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.habits.databinding.ActivityMainBinding
import com.example.habits.util.shouldBeNotifiedToday
import java.time.Instant
import java.time.ZoneId
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    companion object {
        val log: Logger = Logger.getLogger(MainActivity::class.java.name)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        // Instantiate viewModel using a Factory
        val viewModel: HabitViewModel by lazy {
            val activity = requireNotNull(this) {
                "You can only access the viewModel after onActivityCreated()"
            }
            ViewModelProvider(
                this,
                HabitViewModel.HabitViewModelFactory((activity.application as HabitApplication).database.habitDao())
            )[HabitViewModel::class.java]
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Find navController using supportFragmentManager, as it is a FragmentContainerView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // TODO - FAB should be part of each fragment separately. Ideally i'd have a details fragment, then allow editing from there? Or perhaps just have an expandable list
        binding.fab
        binding.fab.setOnClickListener {
            log.info("USER: Clicked FAB, moving to ${navController.currentDestination}")
            navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
            viewModel.fabVisible.postValue(false)
        }

        val fabObserver = Observer<Boolean> {
            if (it!!) binding.fab.show()
            else binding.fab.hide()
        }
        viewModel.fabVisible.observe(this, fabObserver)

        setupActionBarWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_debug -> {
                val calendar = Calendar.getInstance()
                val time = calendar.timeInMillis
                log.info("@@@ Day ${shouldBeNotifiedToday("1010101",time, log)}")
                log.info("@@@ sunTest ${shouldBeNotifiedToday("1010101",1295145217000, log)}")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
