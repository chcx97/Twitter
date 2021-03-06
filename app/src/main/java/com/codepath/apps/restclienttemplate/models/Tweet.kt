package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import com.codepath.apps.restclienttemplate.TimeFormatter
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject
@Parcelize
class Tweet (var body: String = "",
             var createdAt: String = "",
             var user: User? = null ):
    Parcelable {

    // getFormattedTimestamp(): String {
     //   val tweet = Tweet()
       // return TimeFormatter.getTimeDifference(tweet.createdAt)
    //}
    companion object {

        //returns one json object tweet
        fun fromJson(jsonObject: JSONObject): Tweet {
            val tweet = Tweet()

            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))

            return tweet
        }

        //returns a json array into a list of tweets
        fun fromJsonArray(jsonArray: JSONArray): List<Tweet> {
            val tweets = ArrayList<Tweet>()

            for (i in 0 until jsonArray.length()) {
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }

            return tweets
        }
    }
}
