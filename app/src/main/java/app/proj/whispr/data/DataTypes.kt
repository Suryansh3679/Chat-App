package app.proj.whispr.data

data class UserData(
    var userId : String ?= "",
    var name : String ?= "",
    var number : String ?= "",
    var imageUrl : String ?= ""
){
    //In Firebase by default we cant use our UserData directly so we map it to give to Firebase
    fun toMap()= mapOf(
        //cast userId to userId
        "userId" to userId,
        "name" to name,
        "number" to number,
        "imageUrl" to imageUrl,
    )
}