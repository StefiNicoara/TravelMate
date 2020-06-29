package com.example.travelmate.ui.profile

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.example.travelmate.ui.splashscreen.SplashScreen
import com.example.travelmate.utils.*


class ProfileFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
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

        binding.button.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.navigation_add_journey)
        }

        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        viewModel.getCurrentUser()
        observeUser()
        addGreetAnimation()
        showPopup()
        observeLogOut()
    }

    private fun observeUser() {
        viewModel.user.observe(this, Observer { result ->
            if (result != null) {
                binding.nickname = result.nickname
            }
        })
    }

    private fun observeLogOut() {
        viewModel.logOut.observe(this, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    val intent = Intent(activity, SplashScreen::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun showPopup() {
        binding.menuButton.setOnClickListener {
            val wrapper = ContextThemeWrapper(context, R.style.PopupStyle)
            val popup = PopupMenu(wrapper, it)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.profile_menu)
            popup.show()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.log_out -> {
                viewModel.logOut()
                true
            }
            else -> false
        }
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