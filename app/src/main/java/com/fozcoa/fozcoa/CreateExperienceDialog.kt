package com.fozcoa.fozcoa

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.fozcoa.fozcoa.adapters.UploadListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.upload_experiencia_modal.*
import android.content.Intent
import android.graphics.Bitmap
import android.app.Activity.RESULT_OK
import android.provider.MediaStore
import com.fozcoa.fozcoa.models.Experiencia
import android.R.attr.data



class CreateExperienceDialog () : BottomSheetDialogFragment(), UploadListAdapter.OnActionListener {

    private var fragmentView: View? = null
    val PICK_IMAGE = 1
    var arrayListImages = ArrayList<Bitmap>()
    var postID = 0
    var experienciaDetail : Experiencia? = null

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
            this.dismiss()
            val bottomSheetDialogFragment = CreateExperienceDialog2(arrayListImages)
            bottomSheetDialogFragment.postID = postID
            bottomSheetDialogFragment.experienciaDetail = experienciaDetail
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
            val path = data!!.getData()!!.getPath()
            if(path!!.contains("/video/")){

            }else if(path.contains("/images/")){
                val returnUri = data.getData()
                val bitmapImage =
                    MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)

                // Do something with the bitmap
                arrayListImages.add(bitmapImage!!)
                previewImages.layoutManager = GridLayoutManager(context!!, 3)
                previewImages.adapter = UploadListAdapter(context!!, arrayListImages, this)
            }

        }
    }
}