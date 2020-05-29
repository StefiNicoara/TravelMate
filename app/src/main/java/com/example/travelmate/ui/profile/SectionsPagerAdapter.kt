package com.example.travelmate.ui.profile

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.travelmate.R
import com.example.travelmate.ui.profile.favorites.FavoritesFragment
import com.example.travelmate.ui.profile.journeys.JourneysFragment
import com.example.travelmate.ui.profile.uploads.UploadsFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        var fragment = Fragment()

        when (position) {
            0 -> {
                fragment = JourneysFragment()
            }
            1 -> {
                fragment = FavoritesFragment()
            }
            2 -> {
                fragment = UploadsFragment()
            }
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}