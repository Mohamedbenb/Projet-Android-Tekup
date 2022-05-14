package com.projet2.venteplantes

import android.content.Entity

class PlantModel (
    val id: String = "plant0",
    val name: String = "Tulip",
    val description: String = "Petite description",
    val imageUrl: String = "http://graven/plant.jpg",
    val grow: String="Faible",
    val water: String="Moyenne",
    var liked: Boolean = false
        )
{
    override fun toString(): String {
        return "PlantModel(id='$id', name='$name', description='$description', imageUrl='$imageUrl', grow='$grow', water='$water', liked=$liked)"
    }
}