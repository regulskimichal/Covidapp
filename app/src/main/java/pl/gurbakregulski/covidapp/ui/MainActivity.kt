package pl.gurbakregulski.covidapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.gurbakregulski.covidapp.R
import pl.gurbakregulski.covidapp.databinding.MainActivityBinding
import pl.gurbakregulski.covidapp.setupWithNavController
import pl.gurbakregulski.covidapp.viewmodel.MainActivityViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: MainActivityBinding by lazy { bind() }
    private val viewModel: MainActivityViewModel by viewModels()

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        viewModel.updateData {
            showErrorMessage("Data fetching failed. Try again later")
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    // Code taken from: github.com/android/architecture-components-samples/tree/master/NavigationAdvancedSample
    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(R.navigation.map, R.navigation.stats)

        val controller =
            findViewById<BottomNavigationView>(R.id.bottomNavigation).setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.fragmentContainerView,
                intent = intent
            )

        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun bind() = MainActivityBinding.inflate(layoutInflater).apply {
        lifecycleOwner = this@MainActivity
        viewModel = viewModel
    }

    private fun showErrorMessage(message: String) {
        Snackbar
            .make(
                binding.fragmentContainerView,
                message,
                Snackbar.LENGTH_LONG
            )
            .setAnchorView(binding.bottomNavigation)
            .show()
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 101
    }

}
