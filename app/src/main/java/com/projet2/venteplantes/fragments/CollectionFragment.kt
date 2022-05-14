package com.projet2.venteplantes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.projet2.venteplantes.MainActivity
import com.projet2.venteplantes.PlantRepository.Singleton.plantList
import com.projet2.venteplantes.R
import com.projet2.venteplantes.adapter.PlantAdapter
import com.projet2.venteplantes.adapter.PlantItemDecoration


class CollectionFragment(
    private val context: MainActivity
) : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_collection, container, false)

        // recuperer ma recyclerView
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.collection_recycle_list)
        collectionRecyclerView.adapter = PlantAdapter(context, plantList.filter { it.liked }, R.layout.item_vertical_plant)
        collectionRecyclerView.layoutManager = LinearLayoutManager(context)
        collectionRecyclerView.addItemDecoration(PlantItemDecoration())

        return view
    }
}