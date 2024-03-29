package com.example.snappy.ui.home

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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    @Inject
    lateinit var privateSharedPreferencesManager: PrivateSharedPrefManager

    var bannerImageUrl = arrayListOf(
        "https://picsum.photos/id/237/200/300",
        "https://picsum.photos/id/237/200/300",
        "https://picsum.photos/id/237/200/300"
    )

    private var db: FirebaseFirestore = Firebase.firestore
    private var tempWishListedPet: PetsDetail? = null
    private var tempWishListedPetList: ArrayList<PetsDetail> = arrayListOf()
    private var currentUserUID: String = FirebaseAuth.getInstance().currentUser?.uid!!

    private val _petsList = MutableLiveData<ArrayList<PetsDetail>>()
    val petsList: LiveData<ArrayList<PetsDetail>> = _petsList

    init {
        _petsList.value = arrayListOf()
        startPetsSnapListener()
    }

    fun loadFeedsList(list: ArrayList<PetsDetail>) {
        _petsList.value = list
    }

    private fun startPetsSnapListener() {
        val docRef = db.collection("PETS_LIST")

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("FIREBASE_LISTENER", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                _petsList.value?.clear()

                for (item in snapshot) {
                    val data = item.toObject<PetsDetail>()
                    _petsList.value?.add(data)
                }

                _petsList.value = _petsList.value
            } else {
                Log.d("FIREBASE_LISTENER", "Current data: null")
            }
        }
    }

    fun getWishList(petsDetail: PetsDetail) {
        tempWishListedPet = petsDetail

        val docRef = db.collection("USERS").document(currentUserUID).collection("wishListed")
        docRef.get()
            .addOnSuccessListener { result ->
                tempWishListedPetList.clear()

                if (result.isEmpty) {
                    addToWishList()
                }

                for (document in result) {
                    val data = document.toObject<PetsDetail>()
                    tempWishListedPetList.add(data)
                }

                for (data in tempWishListedPetList) {
                    if (tempWishListedPet?.id == data.id) {
                        removeFromWishList()
                        break
                    }
                }

                if (!tempWishListedPetList.contains(tempWishListedPet)) {
                    addToWishList()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("LIST", "Error getting documents: ", exception)
            }
    }

    private fun addToWishList() {
        db.collection("PETS_LIST").document(tempWishListedPet!!.id)
            .set(tempWishListedPet!!)
            .addOnSuccessListener { documentReference ->
                Log.d("WISHLIST", "Pet wishListed: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding to wishlist", e)
            }

        db.collection("USERS").document(currentUserUID).collection("wishListed")
            .add(tempWishListedPet!!)
            .addOnSuccessListener { documentReference ->
                Log.d("WISHLIST", "Wishlist added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding wishlist", e)
            }
    }

    private fun removeFromWishList() {
        db.collection("USERS").document(currentUserUID).collection("wishListed").document(tempWishListedPet!!.id)
            .delete()
            .addOnSuccessListener { documentReference ->
                Log.d("WISHLIST", "removed from wishlist: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error removing from wishlist", e)
            }

        db.collection("PETS_LIST").document(tempWishListedPet!!.id)
            .set(tempWishListedPet!!)
            .addOnSuccessListener { documentReference ->
                Log.d("WISHLIST", "removed from petList: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error removing from wishlist", e)
            }
    }
}