package com.example.chuckapp.modules.home.view.bottomNavigation.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.chuckapp.R
import com.example.chuckapp.model.requestModels.Home.Entry
import kotlinx.android.synthetic.main.inactivelog_recycler_row.view.*

class InactiveLogRecyclerAdapter : RecyclerView.Adapter<InactiveLogRecyclerAdapter.InactiveLogViewHolder>() {

    var loglist : List<Entry> = listOf()

    class InactiveLogViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InactiveLogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.inactivelog_recycler_row,parent,false)
        return InactiveLogViewHolder(view)
    }

    override fun getItemCount(): Int {
       return loglist.size
    }

    override fun onBindViewHolder(holder: InactiveLogViewHolder, position: Int) {

        holder.itemView.inactiveDeviceName.text = loglist[position].deviceInfo
        holder.itemView.inactiveIpAddress.text = loglist[position].ipAdress
        holder.itemView.inactiveDate.text = "1 Jan 2018, Friday"
        if (loglist[position].plaformName == "WEB")
            holder.itemView.inactivePlatformIcon.setCompoundDrawablesWithIntrinsicBounds(null,null,null,ContextCompat.getDrawable(holder.itemView.context,R.drawable.ic_baseline_computer_24))
        else if (loglist[position].plaformName == "MOBILE")
            holder.itemView.inactivePlatformIcon.setCompoundDrawablesWithIntrinsicBounds(null,null,null,ContextCompat.getDrawable(holder.itemView.context,R.drawable.ic_baseline_phone_iphone_24))
        if (position == 0)
            holder.itemView.inactiveLineer.background = null

    }
    fun setInactiveListItems(loglist : List<Entry>){
        this.loglist = loglist
        notifyDataSetChanged()
    }

    fun updateInactiveListItems(loglist: List<Entry>){
        this.loglist = this.loglist + loglist
        notifyItemRangeInserted(this.loglist.size,loglist.size)
    }
}