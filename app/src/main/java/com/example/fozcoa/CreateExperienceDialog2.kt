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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fozcoa.adapters.ScrollGalleryAdapter
import kotlinx.android.synthetic.main.upload_experiencia_modal.nextStep
import kotlinx.android.synthetic.main.upload_experiencia_modal2.*
import com.yarolegovich.discretescrollview.transform.ScaleTransformer




class CreateExperienceDialog2 (var arrayListImages : ArrayList<Bitmap>) : BottomSheetDialogFragment(), ScrollGalleryAdapter.OnActionListener {

    private var fragmentView: View? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       fragmentView = inflater.inflate(R.layout.upload_experiencia_modal2, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter = ScrollGalleryAdapter(context!!, this)
        picker.setSlideOnFling(true);
        picker.adapter = itemAdapter
        itemAdapter.setList(arrayListImages)
        picker.setOffscreenItems(10)
        picker.scrollToPosition(1)
        picker.setItemTransformer(  
            ScaleTransformer.Builder()
                .setMinScale(0.6f)
                .build()
        )


        nextStep.setOnClickListener {

        }


    }



    override fun startActivity(context: Context, bitmap: Bitmap) {

    }


}