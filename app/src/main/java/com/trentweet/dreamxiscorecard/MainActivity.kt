package com.trentweet.dreamxiscorecard

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var mSharedPreferences: AppSharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSharedPreferences = AppSharedPref(applicationContext)
        img_btn_add_team.setOnClickListener {
            showTeamDialog(null)
        }
        dream_image.setOnClickListener {
            saveFrameLayout(rv_teams_list, "/Pictures")
        }
        updateTeams()
    }

    private fun updateTeams() {
        val aa = AppSharedPref(this).getUserList()
        Collections.sort(aa, SortByItem())
        val adapter = UserListRecyclerView(this, aa)
        rv_teams_list.adapter = adapter
        rv_teams_list.layoutManager = LinearLayoutManager(applicationContext)
    }

    fun showTeamDialog(userProfileModel: UserProfileModel?) {
        val teamAddingDialog = Dialog(this)
        val inflater = LayoutInflater.from(applicationContext)
        val mView = inflater.inflate(R.layout.layout_add_update_team, null)
        teamAddingDialog.setContentView(mView)
        teamAddingDialog.show()

        val btn = mView.findViewById<Button>(R.id.btn_add_update_team)
        val btnDelete = mView.findViewById<Button>(R.id.btn_delete_update_team)
        val etTeamName = mView.findViewById<EditText>(R.id.et_team_name)
        val etTeamWins = mView.findViewById<EditText>(R.id.et_team_wins)
        val etTeamPoints = mView.findViewById<EditText>(R.id.et_team_points)

        if (userProfileModel != null) {
            etTeamName.setText(userProfileModel.teamName)
            etTeamWins.setText(userProfileModel.wins.toString())
            etTeamPoints.setText(userProfileModel.points.toString())
        }

        btnDelete.setOnClickListener {
            if (userProfileModel != null) {
                val userListModel = mSharedPreferences.getUserList()
                var topUpdatePosition = -1
                for (i in 0 until userListModel.size) {
                    if (userListModel[i].teamName == etTeamName.text.toString())
                        topUpdatePosition = i
                }
                userListModel.removeAt(topUpdatePosition)
                val jsonArray = JSONArray()
                var jsonObject: JSONObject
                for (i in 0 until userListModel.size) {
                    jsonObject = JSONObject()
                    jsonObject.put("teamName", userListModel[i].teamName)
                    jsonObject.put("wins", userListModel[i].wins)
                    jsonObject.put("points", userListModel[i].points)
                    jsonArray.put(jsonObject)
                }
                mSharedPreferences.saveUserList(jsonArray)
                teamAddingDialog.dismiss()
                updateTeams()
            }
        }

        btn.setOnClickListener {
            val userListModel = mSharedPreferences.getUserList()
            var topUpdatePosition = -1
            for (i in 0 until userListModel.size) {
                if (userListModel[i].teamName == etTeamName.text.toString())
                    topUpdatePosition = i
            }

            if (topUpdatePosition != -1)
                userListModel.removeAt(topUpdatePosition)
            userListModel.add(
                UserProfileModel(
                    etTeamName.text.toString(),
                    etTeamWins.text.toString().toInt(),
                    etTeamPoints.text.toString().toInt()
                )
            )
            val jsonArray = JSONArray()
            var jsonObject: JSONObject
            for (i in 0 until userListModel.size) {
                jsonObject = JSONObject()
                jsonObject.put("teamName", userListModel[i].teamName)
                jsonObject.put("wins", userListModel[i].wins)
                jsonObject.put("points", userListModel[i].points)
                jsonArray.put(jsonObject)
            }
            mSharedPreferences.saveUserList(jsonArray)
            teamAddingDialog.dismiss()
            updateTeams()
        }
    }

    fun saveFrameLayout(frameLayout: RecyclerView, path: String) {
        frameLayout.isDrawingCacheEnabled = true
        frameLayout.buildDrawingCache()
        val cache = frameLayout.drawingCache
        val mFolder = File(Environment.getExternalStorageDirectory().toString())
        val imgFile = File(mFolder.absolutePath + "/Download/someimage.png")
        try {
//            val mFolder = File("$filesDir/sample")
            if (!mFolder.exists()) {
                mFolder.mkdir()
            }
            if (!imgFile.exists()) {
                imgFile.createNewFile()
            }
            val fileOutputStream = FileOutputStream(imgFile)
            cache.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            Toast.makeText(applicationContext, "Done! (path: $imgFile", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            // TODO: handle exception
            Log.e("Done?", "no --> " + e.localizedMessage)
            Log.e("Done?", "no --> $imgFile")
        } finally {
            frameLayout.destroyDrawingCache()
        }
    }

    internal inner class SortByItem : Comparator<UserProfileModel> {
        override fun compare(a: UserProfileModel, b: UserProfileModel): Int {
            return if (b.wins == a.wins)
                b.points - a.points
            else
                b.wins - a.wins
        }
    }

}
