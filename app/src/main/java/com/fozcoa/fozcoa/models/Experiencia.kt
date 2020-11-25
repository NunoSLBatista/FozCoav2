package com.fozcoa.fozcoa.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Experiencia(var id : Int, var name: String, var mainImage: String, var description: String, var listImages: ArrayList<ImageGallery>, var userId: Int) : Parcelable {

}