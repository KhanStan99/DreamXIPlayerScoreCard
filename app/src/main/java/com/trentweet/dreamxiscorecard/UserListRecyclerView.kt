package com.trentweet.dreamxiscorecard

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView

class UserListRecyclerView(
    private val mainActivity: MainActivity,
    private val arrayList: ArrayList<UserProfileModel>
) :
    RecyclerView.Adapter<UserListRecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mainActivity)
                .inflate(R.layout.layout_user_list, p0, false)
        )
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val constraintLayoutHeader: TableRow = view.findViewById(R.id.cl_header)
        val constraintLayoutIteam: TableRow = view.findViewById(R.id.cl_team_item)
        val txtRank: TextView = view.findViewById(R.id.txt_rank)
        val txtTeamName: TextView = view.findViewById(R.id.txt_team_name)
        val txtTeamWins: TextView = view.findViewById(R.id.txt_wins)
        val txtTeamPoints: TextView = view.findViewById(R.id.txt_points)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position % 2 == 1) {
            holder.constraintLayoutIteam.setBackgroundColor(mainActivity.getColor(R.color.colorGray))
        } else {
            holder.constraintLayoutIteam.setBackgroundColor(mainActivity.getColor(R.color.colorWhite))
        }

        when (position) {
            0 -> {
                holder.txtRank.text = ""
                holder.txtRank.background = mainActivity.getDrawable(R.drawable.img_1)
                holder.constraintLayoutHeader.visibility = View.VISIBLE
            }
            1 -> {
                holder.txtRank.text = ""
                holder.txtRank.background = mainActivity.getDrawable(R.drawable.img_2)
                holder.constraintLayoutHeader.visibility = View.GONE
            }
            2 -> {
                holder.txtRank.text = ""
                holder.txtRank.background = mainActivity.getDrawable(R.drawable.img_3)
                holder.constraintLayoutHeader.visibility = View.GONE
            }
            else -> {
                holder.txtRank.text = (position.inc()).toString()
                holder.txtRank.background = null
                holder.constraintLayoutHeader.visibility = View.GONE
            }
        }

        holder.txtTeamName.text = arrayList[position].teamName
        holder.txtTeamWins.text = arrayList[position].wins.toString()
        holder.txtTeamPoints.text = arrayList[position].points.toString()

        holder.constraintLayoutIteam.setOnClickListener {
            mainActivity.showTeamDialog(arrayList[position])
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}