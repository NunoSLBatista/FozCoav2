package com.example.fozcoa.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Miradouro(var id: Int, var name : String, var mainPicture: String, var description: String, var listImages: ArrayList<ImageGallery>) : Parcelable {



}