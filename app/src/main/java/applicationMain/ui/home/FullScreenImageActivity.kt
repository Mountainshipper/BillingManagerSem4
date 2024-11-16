package applicationMain.ui.home

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.semester4.R


class FullScreenImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_full_screen_image)

        // Receive the imageUrl passed from the previous activity
        val imageUrl = intent.getStringExtra("imageUrl")

        // Find the ImageView from the layout
        val imageView = findViewById<ImageView>(R.id.fullScreenImageView)

        // Load the image from Firebase Storage and display it in the ImageView
        if (imageUrl != null) {
            //Toast.makeText(this, "Image URL: $imageUrl", Toast.LENGTH_LONG).show()
            //val storageReference = Firebase.storage.getReferenceFromUrl(imageUrl)
            Glide.with(this)
                .load(imageUrl)
                .into(imageView)
        }

        // Close the activity when the image is tapped
        /*
        imageView.setOnClickListener {
            finish()  // Beendet die Vollbildansicht und kehrt zurück
        }*/
    }
}
