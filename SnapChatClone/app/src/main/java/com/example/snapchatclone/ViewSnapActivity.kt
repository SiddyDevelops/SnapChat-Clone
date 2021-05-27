package com.example.snapchatclone

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutionException

class ViewSnapActivity : AppCompatActivity() {

    var messageTextView: TextView? = null
    var snapImageView: ImageView? = null
    val mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)

        messageTextView = findViewById(R.id.messageTextView)
        snapImageView = findViewById(R.id.snapImageView)

        messageTextView?.text = intent.getStringExtra("message")

        val task = ImageDownloader()
        val myImage: Bitmap
        try {
            myImage =
                task.execute(intent.getStringExtra("imageURL"))
                    .get()!!
            snapImageView?.setImageBitmap(myImage)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    class ImageDownloader : AsyncTask<String?, Void?, Bitmap?>() {
        protected override fun doInBackground(vararg urls: String?): Bitmap? {
            return try {
                val url = URL(urls[0])
                val connection =
                    url.openConnection() as HttpURLConnection
                connection.connect()
                val `in` = connection.inputStream
                BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid.toString()).child("snaps").child(intent.getStringExtra("snapKey").toString()).removeValue()

        FirebaseStorage.getInstance().getReference().child("images").child(intent.getStringExtra("imageName").toString()).delete()

    }

}