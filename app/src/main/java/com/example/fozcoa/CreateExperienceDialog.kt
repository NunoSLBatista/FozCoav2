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


class CreateExperienceDialog () : BottomSheetDialogFragment(), UploadListAdapter.OnActionListener {

    private var fragmentView: View? = null
    val PICK_IMAGE = 1
    var arrayListImages = ArrayList<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       fragmentView = inflater.inflate(R.layout.upload_experiencia_modal, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nextStep.setOnClickListener {
            val bottomSheetDialogFragment = CreateExperienceDialog2(arrayListImages)
            bottomSheetDialogFragment.show(activity!!.supportFragmentManager, bottomSheetDialogFragment.tag)
        }

        uploadImage.setOnClickListener {
            val cameraIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(cameraIntent, PICK_IMAGE)


        }

        previewImages.setHasFixedSize(true)

        previewImages.layoutManager = GridLayoutManager(context!!, 3)
        previewImages.adapter = UploadListAdapter(context!!, arrayListImages, this)

    }

    override fun startActivity(context: Context, bitmap: Bitmap) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        if (requestCode == 1) {
            val returnUri = data!!.getData()
            val bitmapImage =
                MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)

            // Do something with the bitmap
            arrayListImages.add(bitmapImage!!)
            previewImages.layoutManager = GridLayoutManager(context!!, 3)
            previewImages.adapter = UploadListAdapter(context!!, arrayListImages, this)


            }
        }


}