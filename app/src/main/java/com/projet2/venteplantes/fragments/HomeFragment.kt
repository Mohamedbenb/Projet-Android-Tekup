package com.projet2.venteplantes.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.projet2.venteplantes.MainActivity
import com.projet2.venteplantes.PlantModel
import com.projet2.venteplantes.PlantRepository.Singleton.plantList
import com.projet2.venteplantes.R
import com.projet2.venteplantes.adapter.PlantAdapter
import com.projet2.venteplantes.adapter.PlantItemDecoration

//sealed interface a la place de class
class HomeFragment(
   private val context: MainActivity
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.fragment_home, container, false)


        //enregistrer une premiere plante dans notre liste(pissenlit)




        //recuperer le recyclerview
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = PlantAdapter(context, plantList.filter { !it.liked }, R.layout.item_horizontal_plant)

        //recuperer le second recyclerview
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter = PlantAdapter(context, plantList, R.layout.item_vertical_plant)
        verticalRecyclerView.addItemDecoration(PlantItemDecoration())

        return view
    }

}

