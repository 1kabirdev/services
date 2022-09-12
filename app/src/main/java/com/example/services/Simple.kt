package com.example.services

class Simple {

    fun sum(a: Int, b: Int): Int {
        return a + b
    }

    fun getLists(): ArrayList<String> {
        val list: ArrayList<String> = arrayListOf()
        list.add("Kabir")
        list.add("Agamirzaev")
        list.add("21")
        return list
    }

    fun getData(a: Float, b: Float): Float {
        return a / b
    }

}