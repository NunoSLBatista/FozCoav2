package com.example.fozcoa

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fozcoa.adapters.GalleryListAdapter
import com.example.fozcoa.adapters.MiradouroListAdapter
import com.example.fozcoa.adapters.TypeGalleryListAdapter
import com.example.fozcoa.models.Experiencia
import com.example.fozcoa.models.ImageGallery
import com.example.fozcoa.models.Miradouro
import com.example.fozcoa.models.TypeGalleryItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_experiencia_detail.*
import kotlinx.android.synthetic.main.activity_miradouro_detail.*
import kotlinx.android.synthetic.main.activity_miradouro_detail.description
import kotlinx.android.synthetic.main.activity_miradouro_detail.galleryView
import kotlinx.android.synthetic.main.activity_miradouro_detail.goBack
import kotlinx.android.synthetic.main.activity_miradouro_detail.titleMiradouro
import kotlinx.android.synthetic.main.activity_miradouro_detail.typeListView
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class ExperienciaDetail : AppCompatActivity(), GalleryListAdapter.OnActionListener, TypeGalleryListAdapter.OnActionListener {

    var experienciaItem : Experiencia? = null
    var currentGalleryList: ArrayList<ImageGallery>? = null
    var typeItemList = ArrayList<TypeGalleryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experiencia_detail)

        experienciaItem = intent.getParcelableExtra("experiencia")

        val bgImage = findViewById(R.id.bgImage) as ImageView

        Picasso.with(applicationContext).load(experienciaItem!!.mainImage).into(bgImage)
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

        val galleryAdapter = GalleryListAdapter(applicationContext, experienciaItem!!.listImages, this)
        galleryView.layoutManager = GridLayoutManager(applicationContext, 3)
        galleryView.adapter = galleryAdapter


        createExperiencia.setOnClickListener {
            val bottomSheetDialogFragment = CreateExperienceDialog()
            Log.d("PostID", experienciaItem!!.id.toString())
            bottomSheetDialogFragment.postID = experienciaItem!!.id
            bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
        }

        goBack.setOnClickListener {
            this.finish()
        }

    }

    override fun startActivity(context: Context, galleryItem: ImageGallery) {

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





}
