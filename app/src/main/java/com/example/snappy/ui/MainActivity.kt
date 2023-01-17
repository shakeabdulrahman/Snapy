package com.example.snappy.ui

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.snappy.R
import com.example.snappy.base.PrivateSharedPrefManager
import com.example.snappy.databinding.ActivityMainBinding
import com.example.snappy.databinding.DialogLogoutSuccessBinding
import com.example.snappy.ui.login.LoginActivity
import com.example.snappy.ui.splash.SplashActivity
import com.example.snappy.viewmodel.SharedViewModel
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    lateinit var dexter : DexterBuilder
    private lateinit var actionBarToggle: ActionBarDrawerToggle

    var toolbar: Toolbar? = null
    var drawerLayout: DrawerLayout? = null
    var navigationView: NavigationView? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var menu: Menu? = null

    private var privateSharedPrefManager: PrivateSharedPrefManager? = null

    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        privateSharedPrefManager = PrivateSharedPrefManager(this)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.hostFragmentLanding) as NavHostFragment
        navController = navHostFragment.navController

        getPermission()
        setupNavigation()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp() || navController.navigateUp(appBarConfiguration)
    }

    private fun getPermission() {
        dexter = Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    report.let {
                        if (report.areAllPermissionsGranted()) {
                            //showMessage("Permissions Granted")
                        } else {
                            AlertDialog.Builder(this@MainActivity, R.style.AlertDialogCustom).apply {
                                setMessage("please allow the required permissions")
                                    .setCancelable(false)
                                    .setPositiveButton("Settings") { _, _ ->
                                        val reqIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                            .apply {
                                                val uri = Uri.fromParts("package", packageName, null)
                                                data = uri
                                            }
                                        resultLauncher.launch(reqIntent)
                                    }
                                // setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                                val alert = this.create()
                                alert.show()
                            }
                        }
                    }
                }
                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest?>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener{
                Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            }
        dexter.check()
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
        dexter.check()
    }

    fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setupNavigation() {
        toolbar = binding.appBarLayout.toolbar
        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment), drawerLayout)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(navigationView!!, navController)
        navigationView!!.setNavigationItemSelectedListener(this)
        binding.bottomNavView.setupWithNavController(navController)

        val headerView = navigationView!!.inflateHeaderView(R.layout.navigation_drawer_header)
        val profileName = headerView.findViewById<TextView>(R.id.profile_name)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        showLogoutSuccessDialog()
                    }
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        this.menu = menu
//        if (viewModel.isPrinterOn.value!!) {
//            this.menu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_on)
//        } else {
//            this.menu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_off)
//        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.notification) {
            //viewModel.togglePrinterSwitch()
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun showLogoutSuccessDialog() {
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels

        val dialogBinding = DialogLogoutSuccessBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding.root)
        dialog.show()
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.window
            ?.setLayout(
                6 * width / 6,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

        val okayButton = dialogBinding.buttonOk
        okayButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}