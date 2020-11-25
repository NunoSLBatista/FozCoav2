package com.fozcoa.fozcoa.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ImageGallery (var id : Int, var url : String, var type: String, var user: User) : Parcelable{

}