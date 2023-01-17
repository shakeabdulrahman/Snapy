package com.example.snappy.data.model

data class PetsDetail(
    var id: String = "",
    var image: String = "",
    var name: String = "",
    var age: String = "",
    var price: String = "",
    var location: String = "",
    var mobileNumber: String = "",
    @field:JvmField // use this annotation if your Boolean field is prefixed with 'is'
    var isWishListed:Boolean = false
)
