package com.example.memorygame.model

enum class Boardsize (val numCard:Int){
    EASY(8),
    MEDIUM(18),
    HARD(24);
    fun getWIdth() :Int{
        return when(this){
            EASY -> 2
            MEDIUM -> 3
            HARD -> 4
        }
    }
    fun getHieght() :Int{
        return numCard/getWIdth()
    }
    fun getNumPairs():Int{
        return numCard/2
    }
}