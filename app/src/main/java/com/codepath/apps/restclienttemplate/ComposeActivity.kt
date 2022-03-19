package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var tvCount: TextView

    lateinit var client: TwitterClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        tvCount = findViewById(R.id.tvCount)

        client = TwitterApplication.getRestClient(this)

        etCompose.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                // Fires right as the text is being changed (even supplies the range of text)
                //var charactersLeft: Int = 280 - count
                //tvCount.text = charactersLeft.toString()
                var charactersRemaining: Int = 280 - s.length
                tvCount.setText("$charactersRemaining Characters")
                if (charactersRemaining < 0) {
                    tvCount.setTextColor(Color.RED)
                    btnTweet.isEnabled = false
                }else {
                    tvCount.setTextColor(Color.BLACK)
                    btnTweet.isEnabled = true
                }
            }
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Fires right before text is changing
            }
            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed
                //tvCount.setText("$s Characters")
                //tvCount.setText(s.toString())
            }
        })
        // Handling the user's click on the tweet button
        btnTweet.setOnClickListener {

            // Grab the content of edittext (etCompose)
            val tweetContent = etCompose.text.toString()
            // 1. Make sure the tweet isn't empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty tweets not allowed!", Toast.LENGTH_SHORT).show()
                // Look into displaying SnackBar message, looks prettier than toast
            } else
            // 2. Make sure the tweet is under character count
                if (tweetContent.length > 280) {
                    Toast.makeText(
                        this,
                        "Tweet is too long! Limit is 280 characters",
                        Toast.LENGTH_LONG
                    ).show()
                    tvCount.setTextColor(Color.RED)
                } else {
                    client.publishTweet(tweetContent, object : JsonHttpResponseHandler(){
                        override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                            Log.i(TAG,"Successfully published tweet!")
                            // Send the tweet back to TimelineActivity
                            val tweet = Tweet.fromJson((json.jsonObject))

                            val intent = Intent()
                            intent.putExtra("tweet", tweet)
                            setResult(RESULT_OK,intent)
                            finish()


                        }
                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            response: String?,
                            throwable: Throwable?
                        ) {
                            Log.e(TAG, "Failed to tweet", throwable)
                        }
                    })
                }
        }

    }
    companion object{
        val TAG = "ComposeActivity"
    }
}