package edu.rose_hulman.tee.social_litter

import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.rose_hulman.tee.social_litter.MapFragment
import edu.rose_hulman.tee.social_litter.MessageFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        var manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var locationService : LocationListener = LocationService()

        //manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, locationService)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        var navigateTo : Fragment? = null
        when (item.itemId) {
            R.id.navigation_group -> {
                navigateTo = PlaceholderFragment.newInstance(1)
            }
            R.id.navigation_messages -> {
                navigateTo = MessageFragment.newInstance(0)
            }
            R.id.navigation_profile -> {
                navigateTo = PlaceholderFragment.newInstance(0)
            }
            R.id.navigation_map -> {
                navigateTo = MapFragment.newInstance(0)
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


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {

            return when (position) {
                2 -> MessageFragment.newInstance(position)
                3 -> MapFragment.newInstance(position)
                else -> PlaceholderFragment.newInstance(position)
            }
        }

        override fun getCount(): Int {
            return 4
        }
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