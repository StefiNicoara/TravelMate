package com.example.travelmate.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentAddAttractionBinding
import com.example.travelmate.databinding.FragmentDashboardBinding
import com.example.travelmate.model.Attraction
import com.example.travelmate.model.AttractionTag
import com.example.travelmate.model.City
import com.example.travelmate.utils.recyclerViewAdapters.DashboardRVAdapter

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private var attractionsList: MutableList<Attraction> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)


        attractionsList.add(
            Attraction(
                "CACA",
                City("caca", "caca"),
                "caca",
                listOf(AttractionTag.ACCOMODATION),
                0,
                "ssddfs",
                null
            )
        )

        attractionsList.add(
            Attraction(
                "CACA",
                City("caca", "caca"),
                "caca",
                listOf(AttractionTag.ACCOMODATION, AttractionTag.BAR, AttractionTag.FUN),
                1,
                "ssddfs",
                null
            )
        )

        attractionsList.add(
            Attraction(
                "CACA",
                City("caca", "caca"),
                "caca",
                listOf(AttractionTag.ACCOMODATION),
                1,
                "ssddfs",
                null
            )
        )

        attractionsList.add(
            Attraction(
                "CACA",
                City("caca", "caca"),
                "caca",
                listOf(AttractionTag.ACCOMODATION, AttractionTag.CAFE),
                1,
                "ssddfs",
                null
            )
        )

        attractionsList.add(
            Attraction(
                "CACA",
                City("caca", "caca"),
                "caca",
                listOf(AttractionTag.ACCOMODATION),
                1,
                "ssddfs",
                null
            )
        )

        attractionsList.add(
            Attraction(
                "CACA",
                City("caca", "caca"),
                "caca",
                listOf(AttractionTag.ACCOMODATION),
                1,
                "ssddfs",
                null
            )
        )

        attractionsList.add(
            Attraction(
                "CACA",
                City("caca", "caca"),
                "caca",
                listOf(AttractionTag.ACCOMODATION),
                1,
                "ssddfs",
                null
            )
        )

        attractionsList.add(
            Attraction(
                "CACA",
                City("caca", "caca"),
                "caca",
                listOf(AttractionTag.ACCOMODATION),
                1,
                "ssddfs",
                null
            )
        )

        attractionsList.add(
            Attraction(
                "CACA",
                City("caca", "caca"),
                "caca",
                listOf(AttractionTag.ACCOMODATION),
                1,
                "ssddfs",
                null
            )
        )

//        foodRV.layoutManager = LinearLayoutManager(this)
//        val foodList = repository.getAllFood() ?: listOf<Food>()
//        foodRV.adapter = RecyclerViewAdapter(this, foodList)

        val adapter = DashboardRVAdapter(attractionsList)
        binding.attractionsRV.layoutManager = LinearLayoutManager(context)
        binding.attractionsRV.adapter = adapter

        return binding.root
    }
}