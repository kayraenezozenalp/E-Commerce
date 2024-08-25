package com.io1.e_commerce.model

data class FirebaseProductsResponse(
    val documents: List<Document>
)

data class FirebaseAllProductsResponse(
    val documents: List<AllDocument>
)

data class Document(
    val name: String,
    val fields: Fields,
    val createTime: String,
    val updateTime: String
)

data class AllDocument(
    val name: String,
    val fields: AllFields,
    val createTime: String,
    val updateTime: String
)

