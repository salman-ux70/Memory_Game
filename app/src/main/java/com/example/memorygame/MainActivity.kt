package com.example.memorygame

import android.animation.ArgbEvaluator
import android.content.DialogInterface
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.model.Boardsize
import com.example.memorygame.model.MemoryGame
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var rootLayout:ConstraintLayout
    private lateinit var recyclerView:RecyclerView
    private lateinit var tvNumMoves:TextView
    private lateinit var tvNumPairs:TextView
    private lateinit var adapter :CustomDataAdapter
    private lateinit var memoryGame: MemoryGame
    private var boardsize:Boardsize = Boardsize.EASY
    companion object{
      private const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rootLayout = findViewById(R.id.root_layout)
        recyclerView = findViewById(R.id.recyclerview)
        tvNumMoves = findViewById(R.id.tv_tries)
        tvNumPairs = findViewById(R.id.tv_pairs)
       setUpBoard()
    }

    private fun setUpBoard() {
        when(boardsize){
            Boardsize.EASY -> {
                tvNumMoves.text = "Easy: 4 * 2"
                tvNumPairs.text = "0 / 4"
            }
            Boardsize.MEDIUM -> {
                tvNumMoves.text = "Easy: 6 * 3"
                tvNumPairs.text = "0 / 9"
            }
            Boardsize.HARD -> {
                tvNumMoves.text = "Easy: 6 * 4"
                tvNumPairs.text = "0 / 12"
            }
        }
        tvNumPairs.setTextColor(ContextCompat.getColor(this,R.color.progress_none))
        memoryGame = MemoryGame(boardsize)
        adapter = CustomDataAdapter(this,boardsize,memoryGame.cards,object :CustomDataAdapter.CardClickListner{
            override fun onCardClicked(position: Int) {
                Log.d(TAG,"clicked at position $position")
                updateGameFlip(position)
            }

        })
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this,boardsize.getWIdth())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
      when(item.itemId){
          R.id.menu_refresh -> {
              if (memoryGame.getMoves() > 0 && !memoryGame.haveWonGame()){
                  showAlertDialog("Quit your Game",null,View.OnClickListener {
                      setUpBoard()
                  })
              }
              else{
                  setUpBoard()
              }
            return true
          }
          R.id.menu_add_new_game -> {
              chooseNewSize()
              return true
          }
      }
        return super.onOptionsItemSelected(item)
    }

    private fun chooseNewSize() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size,null)
        var radioGroup = boardSizeView.findViewById<RadioGroup>(R.id.radio_group)
        when(boardsize){
            Boardsize.EASY -> radioGroup.check(R.id.rb_easy)
            Boardsize.MEDIUM -> radioGroup.check(R.id.rb_medium)
            Boardsize.HARD -> radioGroup.check(R.id.rb_hard)
        }
     showAlertDialog("Choose new size",boardSizeView, View.OnClickListener {
       boardsize =   when(radioGroup.checkedRadioButtonId){
           R.id.rb_easy -> Boardsize.EASY
           R.id.rb_medium -> Boardsize.MEDIUM
           else ->  Boardsize.HARD
         }
         setUpBoard()
     })
    }

    private fun showAlertDialog(title:String,view:View?,positiveClickListner:View.OnClickListener) {
     AlertDialog.Builder(this)
         .setView(view)
         .setTitle(title)
         .setNegativeButton("Cancel",null)
         .setPositiveButton("OK"){_,_ ->
           positiveClickListner.onClick(null)
         }.show()

    }

    private fun updateGameFlip(position: Int) {
        if(memoryGame.haveWonGame()){
            Snackbar.make(rootLayout,"You have already won!",Snackbar.LENGTH_LONG).show()
            return
        }
        if (memoryGame.isCardFaceUp(position)){
            Snackbar.make(rootLayout,"Invalid move",Snackbar.LENGTH_SHORT).show()
            return
        }
      if (memoryGame.flipCard(position)){
          val color = ArgbEvaluator().evaluate(memoryGame.numPairsFound.toFloat() / boardsize.getNumPairs(),
          ContextCompat.getColor(this,R.color.progress_none),
          ContextCompat.getColor(this,R.color.progress_full)) as Int
          tvNumPairs.setTextColor(color)
          tvNumPairs.text = "Pairs: ${memoryGame.numPairsFound} / ${boardsize.getNumPairs()}"
          if (memoryGame.haveWonGame()){
              Snackbar.make(rootLayout,"You won!Congratulations.",Snackbar.LENGTH_LONG).show()
          }
      }
        tvNumMoves.text = "Moves: ${memoryGame.getMoves()}"
      adapter.notifyDataSetChanged()
    }


}