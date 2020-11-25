package com.fozcoa.fozcoa.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.net.URI

@Parcelize
class Videos(var id: Int, var name: String, var uri: Uri) : Parcelable {

}