package com.fozcoa.fozcoa

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fozcoa.fozcoa.adapters.GalleryListAdapter
import com.fozcoa.fozcoa.adapters.TypeGalleryListAdapter
import com.fozcoa.fozcoa.models.Experiencia
import com.fozcoa.fozcoa.models.ImageGallery
import com.fozcoa.fozcoa.models.TypeGalleryItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_experiencia_detail.*
import kotlinx.android.synthetic.main.activity_miradouro_detail.description
import kotlinx.android.synthetic.main.activity_miradouro_detail.galleryView
import kotlinx.android.synthetic.main.activity_miradouro_detail.goBack
import kotlinx.android.synthetic.main.activity_miradouro_detail.titleMiradouro
import kotlinx.android.synthetic.main.activity_miradouro_detail.typeListView
import kotlinx.android.synthetic.main.upload_experiencia_modal2.*

class ExperienciaDetail : AppCompatActivity(), GalleryListAdapter.OnActionListener, TypeGalleryListAdapter.OnActionListener, CreateExperienceDialog2.OnActionListener {
    var experienciaItem : Experiencia? = null
    var currentGalleryList: ArrayList<ImageGallery>? = null
    var typeItemList = ArrayList<TypeGalleryItem>()
    var galleryAdapter : GalleryListAdapter? = null
    var sharedPreferences : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experiencia_detail)

        galleryView.setHasFixedSize(true)

        experienciaItem = intent.getParcelableExtra("experiencia")
        val bgImage = findViewById(R.id.bgImage) as ImageView

        Glide
            .with(applicationContext)
            .load(experienciaItem!!.mainImage)
            .centerCrop()
            .placeholder(R.drawable.bg_miradouro_1)
            .into(bgImage);
        titleMiradouro.text = experienciaItem!!.name
        description.text = experienciaItem!!.description

        typeItemList.add(TypeGalleryItem("todos"))

        for(i in 0 until experienciaItem!!.listImages.size){

            if(!checkType(experienciaItem!!.listImages.get(i).type)){
                typeItemList.add(TypeGalleryItem(experienciaItem!!.listImages.get(i).type))
            }

        }

        if(typeItemList.size == 2){
            typeItemList.removeAt(0)
        }

        val typeAdapter = TypeGalleryListAdapter(applicationContext, typeItemList, 0, this)
        typeListView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        typeListView.adapter = typeAdapter

        galleryAdapter = GalleryListAdapter(applicationContext, experienciaItem!!.listImages, this)
        galleryView.layoutManager = GridLayoutManager(applicationContext, 3)
        galleryView.adapter = galleryAdapter


        createExperiencia.setOnClickListener {
            sharedPreferences = getSharedPreferences("ecoa", Context.MODE_PRIVATE)
            val blocked = sharedPreferences!!.getInt("blocked", 0)
            if(blocked < 1){
                val bottomSheetDialogFragment = CreateExperienceDialog()
                bottomSheetDialogFragment.listener = this;
                bottomSheetDialogFragment.postID = experienciaItem!!.id
                bottomSheetDialogFragment.experienciaDetail = experienciaItem
                bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
            }else{
                Snackbar.make(createExperiencia, "Esta conta está bloqueada", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.themeRegister))
                    .setTextColor(resources.getColor(R.color.white))
                    .show()
            }
        }

        goBack.setOnClickListener {
            this.finish()
        }

    }

    override fun startActivity(context: Context, experienciaItem: Experiencia) {
        this.experienciaItem = experienciaItem
        this.currentGalleryList = experienciaItem.listImages

        val typeAdapter = TypeGalleryListAdapter(applicationContext, typeItemList, 0, this)
        typeListView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        typeListView.adapter = typeAdapter

        galleryAdapter = GalleryListAdapter(applicationContext, experienciaItem!!.listImages, this)
        galleryView.layoutManager = GridLayoutManager(applicationContext, 3)
        galleryView.adapter = galleryAdapter


        Snackbar.make(bgImage, "Submeteste uma nova experiência", Snackbar.LENGTH_LONG)
            .setBackgroundTint(resources.getColor(R.color.themeRegister))
            .setTextColor(resources.getColor(R.color.white))
            .show()

    }

    override fun startActivity(context: Context, galleryItem: ImageGallery) {
        //Toast.makeText(context, "teste", Toast.LENGTH_LONG).show()
        if(galleryItem.type == "video"){
            val intentDetailVideo = Intent(context, VideoPlayerActivity::class.java)
            intentDetailVideo.putExtra("video2", galleryItem)
            startActivity(intentDetailVideo)
        }else{
            val detailPhotoDialogFragment = DetailPhotoDialog()
            detailPhotoDialogFragment.galleryItem = galleryItem
            detailPhotoDialogFragment.experienciaDetail = experienciaItem
            detailPhotoDialogFragment.show(supportFragmentManager, detailPhotoDialogFragment.tag)
        }
    }

    override fun startActivity(context: Context, typeItem: TypeGalleryItem, position: Int) {
        updateGallery(typeItem, position)
    }


    fun checkType(type: String): Boolean{

        for (i in 0 until typeItemList.size){
            if(typeItemList.get(i).name == type){
                return true
            }
        }
        return false
    }

    fun updateGallery(typeItem: TypeGalleryItem, position: Int){

        currentGalleryList = ArrayList<ImageGallery>()

        for(i in 0 until experienciaItem!!.listImages.size){

            if(experienciaItem!!.listImages.get(i).type == typeItem.name || typeItem.name == "todos"){
               currentGalleryList!!.add(experienciaItem!!.listImages.get(i))
            }

        }

        val typeAdapter = TypeGalleryListAdapter(applicationContext, typeItemList, position, this)
        typeListView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        typeListView.adapter = typeAdapter

        val galleryAdapter = GalleryListAdapter(applicationContext, currentGalleryList!!, this)
        galleryView.layoutManager = GridLayoutManager(applicationContext, 3)
        galleryView.adapter = galleryAdapter

    }

    fun deletePost2(galleryItem: ImageGallery){
        Log.d("size1", experienciaItem!!.listImages.size.toString())
        for (i in 0 until experienciaItem!!.listImages.size){
            if(experienciaItem!!.listImages.get(i) == galleryItem ){
                experienciaItem!!.listImages.removeAt(i)
                currentGalleryList = experienciaItem!!.listImages
                galleryView.removeViewAt(i)
                galleryAdapter!!.notifyItemRemoved(i)
                galleryAdapter!!.notifyItemRangeChanged(i, experienciaItem!!.listImages.size)
                break
            }
        }
    }



}
