package com.projet2.venteplantes.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.projet2.venteplantes.*

@GlideModule
class PlantAdapter(
    val context: MainActivity,
    private val plantList: List<PlantModel>,
    private val layoutId: Int)
    : RecyclerView.Adapter<PlantAdapter.ViewHolder>(){

    //boite pour ranger tout les composants à controler
    class ViewHolder(view: View) :RecyclerView.ViewHolder(view){
        val plantImage: ImageView? =view.findViewById<ImageView>(R.id.image_item)
        val plantName:TextView? = view.findViewById<TextView>(R.id.name_item)
        val plantDescription:TextView? = view.findViewById<TextView>(R.id.description_item)
        val starIcon: ImageView? = view.findViewById<ImageView>(R.id.star_icon)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //recuperer les information de la plante
        val currentPlant = plantList[position]

        // recuperer le repository
        val repo = PlantRepository()

        //utiliser glide pour recuperer l'image a partir de son lien -> composant
        holder.plantImage?.let {
            Glide.with(context).load(Uri.parse(currentPlant.imageUrl)).into(it)
        }
        //mettre à jour le nom de la plante
        holder.plantName?.text = currentPlant.name

        //mettre à jour la description de la plante
        holder.plantDescription?.text = currentPlant.description

        //verifier si la plante a été liké
        if (currentPlant.liked) {
            holder.starIcon?.setImageResource(R.drawable.ic_like)

        } else {
            holder.starIcon?.setImageResource(R.drawable.ic_dislike)
        }
        // rajouter une interaction sur cette etoile
        holder.starIcon?.setOnClickListener {
            // inverser si le bouton est like ou non
            currentPlant.liked = !currentPlant.liked
            // mettre à jour l'objet plante
            repo.updatePlant(currentPlant)
        }

            // Lors du clic sur une plante
            holder.itemView.setOnClickListener {

                // Afficher la popup
                PlantPopup(this, currentPlant).show()
            }
        }

        override fun getItemCount(): Int = plantList.size



}