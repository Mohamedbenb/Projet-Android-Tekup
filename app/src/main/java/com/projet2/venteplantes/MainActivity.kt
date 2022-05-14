package com.projet2.venteplantes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.projet2.venteplantes.fragments.AddPlantFragment
import com.projet2.venteplantes.fragments.CollectionFragment
import com.projet2.venteplantes.fragments.HomeFragment


class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        loadFragment(HomeFragment(this), R.string.home_page_title)

        // importer le bottomnavigationview
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener {
            when  (it.itemId)
            {
                R.id.home_page -> {
                    loadFragment(HomeFragment(this), R.string.home_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.collection_page -> {
                    loadFragment(CollectionFragment(this), R.string.collection_page_title)
                    return@setOnNavigationItemSelectedListener true
                }R.id.add_plant_page -> {
                loadFragment(AddPlantFragment(this), R.string.add_plant_page_title)
                return@setOnNavigationItemSelectedListener true
                }
                R.id.logoutBtn -> {
                    firebaseAuth.signOut()
                    checkUser()
                }
                else -> false
            } ;true
        }
        //var btn = findViewById<Button>(R.id.logoutBtn)



    }

    private fun checkUser() {

        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity((Intent(this,LoginActivity::class.java)))
        }
        else{
            val email = firebaseUser.email
        }

    }

    private fun loadFragment(fragment: Fragment, string: Int){
        // charger notre Repository
        val repo = PlantRepository()

        // actualiser le titre de la page
        findViewById<TextView>(R.id.page_title).text = resources.getString(string)

        // mettre à jour la liste de plantes
        repo.updateData {
            // injecter le fragment dans notre boite (fragment_container)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commitAllowingStateLoss()
        }
    }
}