package com.example.travelmate.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentProfileBinding
import com.example.travelmate.utils.ANIMATION_DURATION
import com.example.travelmate.utils.ANIMATION_DURATION_PROFILE
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment() {

    private val viewModel by inject<ProfileViewModel>()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val sectionsPagerAdapter = context?.let {
            SectionsPagerAdapter(
                it,
                childFragmentManager
            )
        }
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        addGreetAnimation()
    }

    private fun addGreetAnimation() {
        binding.hiLabel.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).start()

        binding.userNameLabel.animate().alpha(1.0f).setDuration(ANIMATION_DURATION)
            .setStartDelay(ANIMATION_DURATION_PROFILE)
            .start()

        binding.adventurousLabel.animate().alpha(1.0f).setDuration(ANIMATION_DURATION)
            .setStartDelay(2 * ANIMATION_DURATION)
            .start()
    }
}