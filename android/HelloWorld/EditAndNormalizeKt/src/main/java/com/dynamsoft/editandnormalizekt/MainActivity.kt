package com.dynamsoft.editandnormalizekt

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.dynamsoft.dce.utils.PermissionUtil
import com.dynamsoft.editandnormalizekt.databinding.ActivityMainBinding
import com.dynamsoft.license.LicenseManager
import com.dynamsoft.license.LicenseVerificationListener

class MainActivity : AppCompatActivity() {
    private var appBarConfiguration: AppBarConfiguration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            // Initialize license for Dynamsoft Document Normalizer.
            LicenseManager.initLicense(
                LICENSE,
                this
            ) { isSuccess: Boolean, error: Exception? ->
                if (!isSuccess) {
                    error?.printStackTrace()
                }
            }
        }
        PermissionUtil.requestCameraPermission(this)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController: NavController =
            findNavController(this, R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        setupActionBarWithNavController(this, navController, appBarConfiguration!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController: NavController =
            findNavController(this, R.id.nav_host_fragment_content_main)
        return (navigateUp(navController, appBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    companion object {
        // The license string here is a time-limited trial license. Note that network connection is required for this license to work.
        // You can also request a 30-day trial license via the Request a Trial License link: https://www.dynamsoft.com/customer/license/trialLicense?product=ddn&utm_source=github&package=android
        private const val LICENSE = "DLS2eyJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSJ9"
    }
}