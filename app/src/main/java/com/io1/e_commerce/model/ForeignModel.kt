package com.io1.e_commerce.model


data class FirebaseResponse(
    val ProductID: String,
    val ProductImage: String,
    var ProductPrice: String,
    val ProductStock: String,
    val ProductName: String,
    var ProductAmount: Int
)

data class FirebaseAllProductResponse(
    val ProductID: String,
    val ProductImage: String,
    var ProductPrice: String,
    val ProductOrigin: String,
    val ProductStock: String,
    val ProductName: String,
    var ProductAmount: Int
)


data class Profile(
    val fullName: String,
    val email: String,
    val address: String,
    val phone: String,
    val profileImage : String
)

data class Fields(
    val ProductID : ProductID,
    val ProductImage: ProductImage,
    val ProductPrice: ProductPrice,
    val ProductStock: ProductStock,
    val ProductName: ProductName
)

data class AllFields(
    val ProductID : ProductID,
    val ProductImage: ProductImage,
    val ProductOrigin: ProductOrigin,
    val ProductPrice: ProductPrice,
    val ProductStock: ProductStock,
    val ProductName: ProductName
)

data class ProductImage(
    val stringValue: String
)

data class ProductOrigin(
    val stringValue: String
)

data class ProductPrice(
    val stringValue: String
)

data class ProductStock(
    val integerValue: String
)

data class ProductName(
    val stringValue: String
)

data class ProductID(
    val stringValue: String
)


