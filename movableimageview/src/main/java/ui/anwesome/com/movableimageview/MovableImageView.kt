package ui.anwesome.com.movableimageview

/**
 * Created by anweshmishra on 13/02/18.
 */
import android.content.*
import android.graphics.*
import android.view.*
class MovableImageView(ctx:Context,var bitmap:Bitmap):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class MovableImageState(var scale:Float = 0f, var dir:Float = 0f, var prevScale:Float = 0f) {
        fun update(stopcb: (Float) ->  Unit) {
            scale += 0.1f * dir
            if(Math.abs(scale - prevScale) > 1) {
                this.scale = this.prevScale + this.dir
                dir = 0f
                this.prevScale = this.scale
                stopcb(this.prevScale)
            }
        }
        fun startUpdating(startcb: () -> Unit) {
            if(dir == 0f) {
                dir = 1f - 2*scale
                startcb()
            }
        }
    }
}