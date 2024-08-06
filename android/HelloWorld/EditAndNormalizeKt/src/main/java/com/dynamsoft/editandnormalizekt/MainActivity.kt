package com.dynamsoft.editandnormalizekt

import android.os.Bundle
import android.widget.TextView
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
            // Initialize the license.
            // The license string here is a trial license. Note that network connection is required for this license to work.
            // You can request an extension via the following link: https://www.dynamsoft.com/customer/license/trialLicense?product=ddn&utm_source=samples&package=android
            LicenseManager.initLicense(
                "DLS2eyJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSJ9",
                this
            ) { isSuccess: Boolean, error: Exception? ->
                if (!isSuccess) {
                    error?.printStackTrace()
                    runOnUiThread { findViewById<TextView>(R.id.tv_license_error).text = "License initialization failed: ${error!!.message}" }
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
}