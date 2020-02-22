package com.example.fozcoa.models

class Miradouro() {

    lateinit var name : String
    lateinit var mainPicture: String
    lateinit var description: String
    lateinit var listImages: ArrayList<String>

    constructor(name: String){
        this.name = name
    }

}