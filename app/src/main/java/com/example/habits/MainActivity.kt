package com.example.habits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.habits.database.Habit
import com.example.habits.database.HabitDatabase
import com.example.habits.databinding.ActivityMainBinding
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    companion object {
        val log: Logger = Logger.getLogger(MainActivity::class.java.name)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            HabitDatabase::class.java, "habit-database"
        ).build()

        // db.habitDao().insertAll(hab)

        val viewModel: MainActivityViewModel by lazy {
            val activity = requireNotNull(this) {
                "You can only access the viewModel after onActivityCreated()"
            }
            ViewModelProvider(
                this,
                MainActivityViewModel.Factory(activity.application)
            )[MainActivityViewModel::class.java]
        }
        // val viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

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
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}