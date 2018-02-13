package ui.anwesome.com.kotlinmovableimageview

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.movableimageview.MovableImageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MovableImageView.create(this, BitmapFactory.decodeResource(resources, R.drawable.nature_more))
    }
}
