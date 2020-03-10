package com.example.fozcoa

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fozcoa.adapters.GalleryListAdapter
import com.example.fozcoa.adapters.UploadListAdapter
import com.example.fozcoa.models.ImageGallery
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.upload_experiencia_modal.*
import android.content.Intent
import android.graphics.Bitmap
import android.R.attr.data
import android.app.Activity.RESULT_OK
import android.util.Base64
import android.util.Log
import androidx.core.app.NotificationCompat.getExtras
import com.google.android.gms.common.util.IOUtils.toByteArray
import java.io.ByteArrayOutputStream
import android.provider.MediaStore
import android.R.attr.data
import java.io.IOException
import android.R.attr.data
import androidx.core.app.NotificationCompat.getExtras
import android.graphics.BitmapFactory
import android.R.attr.data
import android.R.attr.data
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_layout.*


class ImageDialog () : DialogFragment() {

    private var fragmentView: View? = null
    var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       fragmentView = inflater.inflate(R.layout.image_layout, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.with(context).load(url).into(bigImage)

    }




}