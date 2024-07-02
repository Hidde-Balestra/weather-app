package com.hva.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.hva.weather.databinding.ActivityMainBinding
import com.hva.weather.model.GeoLocation
import com.hva.weather.viewModel.WeatherViewModel


class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private val viewModelWeather: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController!!.graph)
        setupActionBarWithNavController(navController!!, appBarConfiguration)

        navController!!.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.navigationIcon = null
            if(destination.id==R.id.loadingFragment){
                supportActionBar?.hide()
            } else{
                supportActionBar?.show()
            }

            if (destination.id==R.id.settingsFragment || destination.id==R.id.AddLocationListFragment || destination.id==R.id.WeatherForecastViewFragment){
                binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            }
        }

        getLocation()
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        } else {
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (isGpsEnabled){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 5f, this)
            } else {
                viewModelWeather.geoLocation.value = GeoLocation(false, null, null)
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        viewModelWeather.weatherFromGeoLocation(location.latitude, location.longitude)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                viewModelWeather.geoLocation.value = GeoLocation(false, null, null)
            }
        }

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
       when (item.itemId) {
            R.id.action_settings -> navController!!.navigate(R.id.settingsFragment)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}