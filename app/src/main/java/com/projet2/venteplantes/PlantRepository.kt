package com.projet2.venteplantes

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.projet2.venteplantes.PlantRepository.Singleton.databaseRef
import com.projet2.venteplantes.PlantRepository.Singleton.downloadUri
import com.projet2.venteplantes.PlantRepository.Singleton.plantList
import com.projet2.venteplantes.PlantRepository.Singleton.storageReference
import java.util.*

class PlantRepository {


    object Singleton {
        // donner le lien pour acceder au bucket (firebase-cloudStorage)
        private val BUCKET_URL: String ="gs://naturecollection-e5688.appspot.com"

        //se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        // se connecter a la reference "plants"
        val databaseRef = FirebaseDatabase.getInstance().getReference("plants")

        // créer une liste qui va contenir nos plantes
        val plantList = arrayListOf<PlantModel>()

        //contenir le lien de l'image courante
        var downloadUri: Uri? = null
    }
    fun updateData(callback: () -> Unit) {
        // absorber les données depuis la databaseRef -> liste de plantes
        databaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // retirer les anciennes
                plantList.clear()
                // recolter la liste
                for (ds in snapshot.children) {
                    // construire un objet plante
                    val plant = ds.getValue(PlantModel::class.java)

                    // verifier que la plante n'est pas null
                    if (plant != null) {
                        // ajouter la plante à notre liste
                        plantList.add(plant)
                    }
                }
                // actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    //  creer une fonction pour envoyer des fichiers sur le storage
    fun uploadImage(file: Uri, callback: () -> Unit) {
        // verifier que ce fichier n'est pas null
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val ref = storageReference.child(fileName)
        val uploadTask = ref.putFile(file)


        // demarer la tache d'envoie
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            // si il y as eu un probleme lors de l'envoi du fichier
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }

            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            // si tout à bienfonctionné
            if(task.isSuccessful) {
                //recuper l'image
                downloadUri = task.result
                callback()
            }
        }
    }

    // mettre à jour un objet plante en base de données
    fun updatePlant(plant: PlantModel) = databaseRef.child(plant.id).setValue(plant)


    // inserer une nouvel plante en bdd
    fun insertPlant(plant: PlantModel) = databaseRef.child(plant.id).setValue(plant)

    // Supprimer une plante de la bas
    fun deletePlant(plant: PlantModel) = databaseRef.child(plant.id).removeValue()
}