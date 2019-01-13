package edu.rose_hulman.tee.sociallitter

import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            changeFragment(PlaceholderFragment.newInstance(0))
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
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
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


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Log.d("QQQQQ", "Position: " + position)
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
