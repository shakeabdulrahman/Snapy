package com.example.snappy.ui.wishlist

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
class WishListViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    @Inject
    lateinit var privateSharedPreferencesManager: PrivateSharedPrefManager

    private var db: FirebaseFirestore = Firebase.firestore
    private var tempWishListedPet: PetsDetail? = null
    private var tempWishListedPetList: ArrayList<PetsDetail> = arrayListOf()
    private var currentUserUID: String = FirebaseAuth.getInstance().currentUser?.uid!!

    private val _myWishList = MutableLiveData<ArrayList<PetsDetail>>()
    val myWishList: LiveData<ArrayList<PetsDetail>> = _myWishList

    init {
        _myWishList.value = arrayListOf()

        val docRef = db.collection("USERS").document(currentUserUID).collection("wishListed")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("FIREBASE_LISTENER", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                _myWishList.value?.clear()

                for (item in snapshot) {
                    val data = item.toObject<PetsDetail>()
                    _myWishList.value?.add(data)
                }

                _myWishList.value = _myWishList.value
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
            }
            .addOnFailureListener { exception ->
                Log.d("LIST", "Error getting documents: ", exception)
            }
    }

    private fun addToWishList() {
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
        val itemRef = db.collection("USERS").document(currentUserUID).collection("wishListed")
        db.collection("USERS").document(currentUserUID).collection("wishListed").whereEqualTo("id",tempWishListedPet!!.id)
            .get()
            .addOnCompleteListener { documentReference ->
                for (item in documentReference.result) {
                    itemRef.document(item.id).delete()
                    Log.d("WISHLIST", "removed from wishlist: $documentReference")
                }
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

    /** Search */

    fun filter(text: String) {
        val filteredList: ArrayList<PetsDetail> = ArrayList()

        for (item in myWishList.value!!) {
            if (item.name.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }

        if (filteredList.isEmpty()) {
            _myWishList.value = filteredList
        } else {
            _myWishList.value = filteredList
        }
    }
}