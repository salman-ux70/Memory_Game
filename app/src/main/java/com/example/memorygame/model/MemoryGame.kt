package com.example.memorygame.model

import android.util.Log
import com.example.memorygame.utils.DEFAULT_ICONS

class MemoryGame(val boardSize:Boardsize){

    private var indexOfSeingleSelectedCard: Int? = null
    val cards:List<MemoryCard>
    var numPairsFound = 0
    private var numCardFlips = 0
    companion object{
       private val TAG = "MemoryGame"
    }
    init {
        val choosenImage = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizeImages = (choosenImage + choosenImage).shuffled()
         cards =  randomizeImages.map { MemoryCard(it) }
    }
    fun flipCard(position: Int):Boolean {
        numCardFlips++
      val card = cards[position]
        var foundMatch = false
        if (indexOfSeingleSelectedCard==null){
           restoreCards()
           indexOfSeingleSelectedCard = position
        }
        else {
             foundMatch = checkForMatch(indexOfSeingleSelectedCard!!,position)
            indexOfSeingleSelectedCard = null
        }
      card.isFaceUp = !card.isFaceUp
      return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
            if (cards[position1].identifier!=cards[position2].identifier){
              return false
            }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        Log.d(TAG,numPairsFound.toString())
        return true
    }

    private fun restoreCards() {
    for (card in cards){
        if (!card.isMatched){
            card.isFaceUp = false
        }
    }
    }

    fun haveWonGame(): Boolean {
      return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position:Int): Boolean {
     return cards[position].isFaceUp
    }

    fun getMoves(): Int {
        return numCardFlips /2
    }
}