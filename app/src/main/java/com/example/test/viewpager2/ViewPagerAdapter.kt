package com.example.test.viewpager2

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R

/**
 * author: beitingsu
 * created on: 2022/1/13 1:00 下午
 */
class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ItemViewHolder>() {

    private var mDatas = mutableListOf<String>()

    fun setData(list: MutableList<String>) {
        mDatas = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = View.inflate(parent.context, R.layout.item_view, null)

        //必须添加不然会崩溃
        //https://stackoverflow.com/questions/58341487/pages-must-fill-the-whole-viewpager2-use-match-parent
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.textView.text = mDatas[position]
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById<TextView>(R.id.tv_text)
    }

}

