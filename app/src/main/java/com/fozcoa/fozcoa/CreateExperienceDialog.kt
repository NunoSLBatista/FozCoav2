package com.fozcoa.fozcoa

import android.Manifest
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
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import android.R.attr.path
import android.net.Uri
import android.content.pm.PackageManager
import android.Manifest.permission
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.os.Build
import android.app.Activity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_experiencia_detail.*


class CreateExperienceDialog () : BottomSheetDialogFragment(), UploadListAdapter.OnActionListener {
    var listener : CreateExperienceDialog2.OnActionListener? = null
    private var fragmentView: View? = null
    val PICK_IMAGE = 1
    var arrayListImages = ArrayList<Bitmap>()
    var videoList = ArrayList<String>()
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
            if(arrayListImages.count() == 0 && videoList.count() == 0) {
                Log.d("Create", "Failed")

                Snackbar.make(parentView, "Necessário adicionar pelo menos uma foto ou vídeo.", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.themeRegister))
                    .setTextColor(resources.getColor(R.color.white))
                    .show()

            }
            else {
                this.dismiss()
                val bottomSheetDialogFragment = CreateExperienceDialog2(arrayListImages, videoList, this.listener!!)
                bottomSheetDialogFragment.postID = postID
                bottomSheetDialogFragment.experienciaDetail = experienciaDetail
                bottomSheetDialogFragment.show(activity!!.supportFragmentManager, bottomSheetDialogFragment.tag)
            }
        }

        uploadImage.setOnClickListener {
            val cameraIntent =
                Intent(Intent.ACTION_PICK)
            cameraIntent.setType("*/*");
            startActivityForResult(cameraIntent, PICK_IMAGE)
        }

        previewImages.setHasFixedSize(true)

        previewImages.layoutManager = GridLayoutManager(context!!, 3)
        previewImages.adapter = UploadListAdapter(context!!, arrayListImages, videoList, this)


        if(!checkPermissionForReadExtertalStorage()) {
            requestPermissionForReadExtertalStorage();
        }
    }

    fun checkPermissionForReadExtertalStorage(): Boolean {
        Log.d("Create", Build.VERSION.SDK_INT.toString() + ":" + Build.VERSION_CODES.M.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = context!!.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getPath(uri: Uri?): String? {
        if(uri == null)
            return "";

        val cursor = getActivity()!!.getContentResolver().query(uri, null, null, null, null)
        var idx = 0

        //Source not from device capture or selection
        if (cursor == null) {
            return uri.getPath()
        } else {
            cursor!!.moveToFirst()
            idx = cursor!!.getColumnIndex(MediaStore.Video.VideoColumns.DATA)
            if (idx == -1) {
                return uri.getPath()
            }
        }
        val path = cursor!!.getString(idx)
        cursor!!.close()
        return path
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
            Log.d("Create:", path);
            if(path!!.contains("/video/")){
                Log.d("Create:", getPath(data!!.getData()));
                val path = getPath(data!!.getData());
                if(!path.isNullOrEmpty())
                    videoList.add(path);
                previewImages.layoutManager = GridLayoutManager(context!!, 3)
                previewImages.adapter = UploadListAdapter(context!!, arrayListImages, videoList, this)
            }else if(path.contains("/images/")){
                val returnUri = data.getData()
                val bitmapImage =
                    MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)

                // Do something with the bitmap
                arrayListImages.add(bitmapImage!!)
                previewImages.layoutManager = GridLayoutManager(context!!, 3)
                previewImages.adapter = UploadListAdapter(context!!, arrayListImages, videoList, this)
            }
            else {
                Snackbar.make(parentView, "O ficheiro deverá ser uma imagem ou vídeo", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.themeRegister))
                    .setTextColor(resources.getColor(R.color.white))
                    .show()
            }

        }
    }
}