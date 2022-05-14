package com.projet2.venteplantes



import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.projet2.venteplantes.adapter.PlantAdapter


// Classe de gestion de la popup
class PlantPopup(
    private val adapter: PlantAdapter,
    private val currentPlant : PlantModel
) : Dialog(adapter.context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Le titre de notre popup existe déjà dans le popup_plants_details.xml
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Initialisation des fonctionnalités de la Classe/Vue
        setContentView(R.layout.popup_plants_details)
        setupComponents()
        setupCloseButton()
        setupDeleteButton()
        setupLikeButton()
    }

    private fun updateLike(likeButton : ImageView) {
        // Chargement de l'image like/unlike associée à l'attribut isLiked de la classe plante
        if (currentPlant.liked) likeButton.setImageResource(R.drawable.ic_like)
        else likeButton.setImageResource(R.drawable.ic_dislike)
    }

    private fun setupLikeButton() {
        // Récupération du bouton like
        val likeButton: ImageView = findViewById<ImageView>(R.id.like_button)

        // Appel de la fonction de mise à jour du like/unlike
        updateLike(likeButton)

        // Description de l'évènement lors du clic sur ce bouton :
        likeButton.setOnClickListener {
            // Inversion du  statut like/unlike
            currentPlant.liked = !currentPlant.liked
            // Mise à jour en BDD
            val repo = PlantRepository()
            repo.updatePlant(currentPlant)
            // Réaffichage de la bonne image (étoile vide ou pleine)
            updateLike(likeButton)
        }
    }

    private fun setupDeleteButton() {
        findViewById<ImageView>(R.id.delete_button).setOnClickListener {
            // Supprimer la plante de la base de données
            // Instanciation de la classe Plantrepository()
            val repo : PlantRepository = PlantRepository()
            // Appel de la méthode de suppression dans cette classe
            repo.deletePlant(currentPlant)
            // Fermeture automatique de la popup
            dismiss()
        }
    }

    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.close_button).setOnClickListener {
            // Fermeture de la popup :
            dismiss()
        }
    }

    private fun setupComponents() {

        // Actualiser l'image de la plante courante dans la popup
        val plantImage: ImageView = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentPlant.imageUrl)).into(plantImage)

        // Actualiser le nom de la plante courante dans la popup
        findViewById<TextView>(R.id.popup_plant_name).text = currentPlant.name

        // Actualiser la description de la plante courante dans la popup
        findViewById<TextView>(R.id.popup_plant_description_subtitle).text = currentPlant.description

        // Actualiser l'ensoleillement de la plante courante dans la popup
        findViewById<TextView>(R.id.popup_plant_grow_subtitle).text = currentPlant.grow

        // Actualiser l'arrosage de la plante courante dans la popup
        findViewById<TextView>(R.id.popup_plant_water_subtitle).text = currentPlant.water

    }
}