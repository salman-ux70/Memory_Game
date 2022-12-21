package com.example.memorygame

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.model.Boardsize
import com.example.memorygame.model.MemoryCard
import kotlin.math.min

class CustomDataAdapter(
    private val context: Context,
    private val boardsize: Boardsize,
    private val cards: List<MemoryCard>,
    private val cardClickListner: CardClickListner
) : RecyclerView.Adapter<CustomDataAdapter.CustomViewHolder>() {
    companion object{
        private const val MARGIN = 10
        private const val TAG = "CustomDataAdapter"
    }
    interface CardClickListner {
     fun onCardClicked(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
         val view = LayoutInflater.from(context).inflate(R.layout.sample_layout,parent,false)
         val layoutPrams =  view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
         val cardWidth = parent.width /boardsize.getWIdth() -(2* MARGIN)
         val cardHieght = parent.height /boardsize.getHieght() - (2* MARGIN)
         val cardSideLength = min(cardWidth,cardHieght)
         layoutPrams.width = cardSideLength
         layoutPrams.height = cardSideLength
        layoutPrams.setMargins(MARGIN, MARGIN, MARGIN, MARGIN)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
       holder.bind(position)
    }

    override fun getItemCount() = boardsize.numCard
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val imageButton = itemView.findViewById<ImageButton>(R.id.image_button)
        fun bind(position: Int) {
            val memoryCard = cards[position]
            imageButton.setImageResource(if(memoryCard.isFaceUp) memoryCard.identifier else R.drawable.ic_launcher_background)
            imageButton.alpha = if (memoryCard.isMatched) .4f else 1.0f
/*
            val colorStateList =  if(memoryCard.isMatched) ContextCompat.getColorStateList(context,R.color.color_grey) else null
             ViewCompat.setBackgroundTintList(imageButton,colorStateList)
*/
             imageButton.setOnClickListener {
              Log.d(TAG,"clicked at position $position")
              cardClickListner.onCardClicked(position)
          }
        }




    }


}
