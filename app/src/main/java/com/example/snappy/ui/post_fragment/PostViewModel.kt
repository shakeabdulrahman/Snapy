package com.example.snappy.ui.post_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.snappy.base.PrivateSharedPrefManager
import com.example.snappy.data.model.PetsDetail
import com.example.snappy.data.repository.MainRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    @Inject
    lateinit var privateSharedPreferencesManager: PrivateSharedPrefManager

    private var db: FirebaseFirestore = Firebase.firestore
    private var tempAddedPet: PetsDetail? = null

    fun addPet(petsDetail: PetsDetail) {
        tempAddedPet = petsDetail

        db.collection("PETS_LIST")
            .add(petsDetail)
            .addOnSuccessListener { documentReference ->
                Log.d("WISHLIST", "Wishlist added with ID: ${documentReference.id}")
                tempAddedPet?.id = documentReference.id
                db.collection("PETS_LIST").document(documentReference.id)
                    .set(tempAddedPet!!)
                    .addOnSuccessListener {
                        Log.d("PET_ADDED", "Pet added!")
                    }
                    .addOnFailureListener { e ->
                        Log.w("PET_ADDED", "Pet adding failed", e)
                    }

            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding wishlist", e)
            }
    }
}