package com.fozcoa.fozcoa.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (var id: Int, var name : String, var email : String, var password: String, var userPhoto: Bitmap?, var url: String, var admin: Int?) : Parcelable {

}