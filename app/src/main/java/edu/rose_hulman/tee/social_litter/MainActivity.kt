package edu.rose_hulman.tee.social_litter

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import edu.rose_hulman.tee.social_litter.MapFragment
import edu.rose_hulman.tee.social_litter.MessageFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception



class MainActivity : AppCompatActivity(), GroupFragment.GroupClickListener {

    lateinit var locationService: LocationService
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mAuthListener : FirebaseAuth.AuthStateListener
    private val RC_SIGN_IN = 1
    private var mapFragment: MapFragment? = null
    lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        initializeListeners()

        navigation.selectedItemId = R.id.navigation_map

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        var manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationService = LocationService()
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, locationService)
        }else {
            var permissions = Array<String>(1){android.Manifest.permission.ACCESS_FINE_LOCATION}
            ActivityCompat.requestPermissions(this, permissions, 1)
        }

        Database.populateTestData()
    }


    private fun initializeListeners(){
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth : FirebaseAuth ->
            val user = firebaseAuth.currentUser
            if(user != null){
                Database.setUser(user.uid, this)
            }else{
                setTheme(R.style.AppTheme)
                launchLoginScreen()
            }
        }
    }

    private fun launchLoginScreen(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val loginIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.icon_launcher)
            .build()

        startActivityForResult(loginIntent, RC_SIGN_IN)
    }

    public override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }

    public override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(mAuthListener)
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        var navigateTo : Fragment? = null
        menu.findItem(R.id.navigation_filter).isVisible = false
        when (item.itemId) {
            R.id.navigation_group -> {
                navigateTo = GroupFragment()
            }
            R.id.navigation_messages -> {
                navigateTo = MessageFragment.newInstance(locationService)
            }
            R.id.navigation_map -> {
                menu.findItem(R.id.navigation_filter).isVisible = true
                navigateTo = mapFragment
                mapFragment?.mapController?.centerOnUser()
            }
        }

        changeFragment(navigateTo)

        true
    }

    fun changeFragment(frag : Fragment?) {
        if (frag != null) {
            val fragTrans = supportFragmentManager.beginTransaction()
            fragTrans.replace(R.id.container, frag, "about")
            fragTrans.commit()
        }
    }

    fun swapToMap() {
        setTheme(R.style.AppTheme)
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(locationService)
        }
        changeFragment(mapFragment)
    }

    override fun onGroupSelected(group: String) {
        var group = Database.showGroupDetails(group, this)
    }

    fun displayGroup(group : Group) {
        var frag = GroupInfoFragment.newInstance(group)
        if (frag != null) {
            val fragTrans = supportFragmentManager.beginTransaction()
            fragTrans.replace(R.id.container, frag, "about")
            fragTrans.addToBackStack("group")
            fragTrans.commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        this.menu = menu!!
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_filter -> {
                mapFragment?.createFilterDialog()
            }
            R.id.navigation_logout -> {
                mAuth.signOut()
            }
            else -> { }
        }
        return true
    }

    class PlaceholderFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            val index = arguments?.getInt(ARG_SECTION_NUMBER)
            val id = when(index){
                0 -> R.layout.fragment_profile
                1 -> R.layout.fragment_groups
                2 -> R.layout.fragment_messages
                else -> R.layout.fragment_map
            }

            val rootView = inflater.inflate(id, container, false)
            return rootView
        }

        companion object {

            private val ARG_SECTION_NUMBER = "section_number"

            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

}
