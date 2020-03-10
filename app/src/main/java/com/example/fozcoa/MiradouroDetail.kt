package com.example.fozcoa

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fozcoa.adapters.GalleryListAdapter
import com.example.fozcoa.adapters.TypeGalleryListAdapter
import com.example.fozcoa.models.ImageGallery
import com.example.fozcoa.models.Miradouro
import com.example.fozcoa.models.TypeGalleryItem
import com.squareup.picasso.Picasso
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

        Picasso.with(applicationContext).load(miradouroItem!!.mainPicture).into(bgImage)
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

        val bottomSheetDialogFragment = ImageDialog()
        bottomSheetDialogFragment.url = galleryItem.url
        bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)

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
