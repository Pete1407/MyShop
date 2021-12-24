package com.example.myshop.util

import android.graphics.Canvas
import android.graphics.Color
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.activities.adapter.AddressAdapter
import android.graphics.drawable.ColorDrawable

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.myshop.R


class SwipeToDeleteCallback(val adapterParam : AddressAdapter) : ItemTouchHelper.SimpleCallback(0,
    ItemTouchHelper.LEFT) {

    private var icon: Drawable? = null
    private lateinit var background: ColorDrawable
    lateinit var adapter : AddressAdapter

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var position = viewHolder.adapterPosition
        Log.i("result","position --> ${viewHolder.adapterPosition}")
        adapter?.deleteItem(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dx: Float,
        dy: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dx, dy, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20
        adapter = adapterParam
        icon = ContextCompat.getDrawable(adapter!!.context, R.drawable.ic_baseline_delete_24)
        background = ColorDrawable(Color.BLACK)

        // compute icon
        var iconMargin = (itemView.height-icon!!.intrinsicHeight)/2
        var iconTop = itemView.top + (itemView.height - icon!!.intrinsicHeight)/2
        var iconBottom = iconTop + icon!!.intrinsicHeight

        // slide to right
//        if(dx > 0){
//            val leftSlide = itemView.left + icon!!.intrinsicHeight
//            val topSlide = itemView.top
//            val rightSlide = (itemView.right+dx.toInt()+backgroundCornerOffset)
//            val bottomSlide = itemView.bottom
//
//            val iconLeft = itemView.left + iconMargin + icon!!.intrinsicWidth;
//            val iconRight = itemView.left + iconMargin
//            background!!.setBounds(leftSlide,topSlide,rightSlide,bottomSlide)
//            icon!!.setBounds(iconLeft,iconTop,iconRight,iconBottom)
//        // slide to left
//        }else
        if(dx < 0){
//            Log.i("result","--------------------------------")
//            Log.i("result","left itemView --> ${itemView.left}")
//            Log.i("result","right itemView --> ${itemView.right}")
//            Log.i("result","top itemView --> ${itemView.top}")
//            Log.i("result","bottom itemView --> ${itemView.bottom}")
//            Log.i("result","##################-")

//            Log.i("result","left icon --> ${itemView.left}")
//            Log.i("result","right itemView --> ${itemView.right}")
//            Log.i("result","top itemView --> ${itemView.top}")
//            Log.i("result","bottom itemView --> ${itemView.bottom}")

            val leftSlide = ((itemView.right + dx.toInt())-backgroundCornerOffset)
            val topSlide = itemView.top
            val rightSlide = itemView.right
            val bottomSlide = itemView.bottom

            val iconLeft = itemView.right - iconMargin - icon!!.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            background!!.setBounds(leftSlide,topSlide, rightSlide, bottomSlide)
            icon!!.setBounds(iconLeft,iconTop,iconRight,iconBottom)
        }else{
            background!!.setBounds(0,0,0,0)
        }
        background!!.draw(c)
        icon!!.draw(c)

    }
}