package com.trentweet.dreamxiscorecard

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray


class AppSharedPref(context: Context) {

    private val teamList = "TEAM_LIST"
    private val preference = "DreamXIScoreBoard"
    private val mContext = context

    private var preferences: SharedPreferences = mContext.getSharedPreferences(preference, 0)

    fun saveUserList(teamListJson: JSONArray) {
        preferences.edit().putString(teamList, teamListJson.toString()).apply()
    }

    fun getUserList(): ArrayList<UserProfileModel> {
        val userObject = JSONArray(preferences.getString(teamList, "[]"))
        val model = ArrayList<UserProfileModel>()
        for (i in 0 until userObject.length())
            model.add(
                UserProfileModel(
                    userObject.optJSONObject(i).optString("teamName"),
                    userObject.optJSONObject(i).optInt("wins"),
                    userObject.optJSONObject(i).optInt("points")
                )
            )
        return model
    }

    fun clearAppSharedPref() {
        preferences.edit().clear().apply()
    }

}