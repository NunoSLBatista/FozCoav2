package com.fozcoa.fozcoa

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import kotlinx.android.synthetic.main.activity_miradouro_viewer.*

class MiradouroViewer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_miradouro_viewer)

        val miradouro = intent.getStringExtra("miradouro")

        val option = VrPanoramaView.Options().also {
            it.inputType = VrPanoramaView.Options.TYPE_MONO
        }

        val DEMO_PANORAMA_LINK : String = miradouro!!

        Glide.with(this).asBitmap().load(DEMO_PANORAMA_LINK).into(object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(placeholder: Drawable?) { }
            override fun onResourceReady(
                resource: Bitmap,
                transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
            ) {
                vrPanoramaView.loadImageFromBitmap(resource, option)
            }
        })

        goBack.setOnClickListener(View.OnClickListener {
            this.finish()
        })

    }



}
