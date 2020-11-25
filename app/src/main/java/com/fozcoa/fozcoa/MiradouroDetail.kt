package com.fozcoa.fozcoa

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fozcoa.fozcoa.adapters.GalleryListAdapter
import com.fozcoa.fozcoa.adapters.TypeGalleryListAdapter
import com.fozcoa.fozcoa.models.ImageGallery
import com.fozcoa.fozcoa.models.Miradouro
import com.fozcoa.fozcoa.models.TypeGalleryItem
import kotlinx.android.synthetic.main.activity_miradouro_detail.*


class MiradouroDetail : AppCompatActivity(), GalleryListAdapter.OnActionListener, TypeGalleryListAdapter.OnActionListener {

    var miradouroItem : Miradouro? = null
    var currentGalleryList: ArrayList<ImageGallery>? = null
    var typeItemList = ArrayList<TypeGalleryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_miradouro_detail)

        miradouroItem = intent.getParcelableExtra("miradouro")

        val bgImage = findViewById(R.id.bgImage) as ImageView

        Glide
            .with(applicationContext)
            .load(miradouroItem!!.mainPicture)
            .centerCrop()
            .placeholder(R.drawable.bg_miradouro_1)
            .into(bgImage);

        titleMiradouro.text = miradouroItem!!.name
        description.text = miradouroItem!!.description

        typeItemList.add(TypeGalleryItem("todos"))

        for(i in 0 until miradouroItem!!.listImages.size){
            if(!checkType(miradouroItem!!.listImages.get(i).type)){
                typeItemList.add(TypeGalleryItem(miradouroItem!!.listImages.get(i).type))
            }
        }

        if(typeItemList.size == 2){
            typeItemList.removeAt(0)
        }


        val typeAdapter = TypeGalleryListAdapter(applicationContext, typeItemList, 0, this)
        typeListView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        typeListView.adapter = typeAdapter

        val galleryAdapter = GalleryListAdapter(applicationContext, miradouroItem!!.listImages, this)
        galleryView.layoutManager = GridLayoutManager(applicationContext, 3)
        galleryView.adapter = galleryAdapter

        goBack.setOnClickListener {
            this.finish()
        }

    }

    override fun startActivity(context: Context, galleryItem: ImageGallery) {
        if(galleryItem.type == "360º"){
            val intent = Intent(context, MiradouroViewer::class.java)
            intent.putExtra("miradouro", galleryItem.url)
            startActivity(intent)
        }else if(galleryItem.type == "image"){
            val detailPhotoDialogFragment = DetailPhotoDialog()
            detailPhotoDialogFragment.galleryItem = galleryItem
            detailPhotoDialogFragment.miradouroDetail = miradouroItem
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

        for(i in 0 until miradouroItem!!.listImages.size){

            if(miradouroItem!!.listImages.get(i).type == typeItem.name || typeItem.name == "todos"){
               currentGalleryList!!.add(miradouroItem!!.listImages.get(i))
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
