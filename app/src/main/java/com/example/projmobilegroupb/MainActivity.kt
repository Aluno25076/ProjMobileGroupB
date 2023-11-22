package com.example.projmobilegroupb


import android.os.Bundle
import android.widget.ImageButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.projmobilegroupb.databinding.ActivityMainBinding
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController






class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      /* binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.fragmentHolder)*/
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar= findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView : BottomNavigationView= findViewById(R.id.nav_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController



        /*val navController = findNavController(R.id.nav_host_fragment_activity_main)*/

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.fragmentHolder))

       /* supportFragmentManager.beginTransaction().replace(R.id.fragmentHolder, frag).commit()*/

      setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val btnAdd: ImageButton= findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener{navController.navigate(R.id.add_stock)}
    }


override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}