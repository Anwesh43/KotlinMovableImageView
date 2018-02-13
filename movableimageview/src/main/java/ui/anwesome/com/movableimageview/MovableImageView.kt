package ui.anwesome.com.movableimageview

/**
 * Created by anweshmishra on 13/02/18.
 */
import android.content.*
import android.graphics.*
import android.view.*
class MovableImageView(ctx:Context,var bitmap:Bitmap):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
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
    data class Animator(var view:View, var animated:Boolean = false) {
        fun animate(updatecb: () -> Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
    }
    data class MovableImage(var bitmap:Bitmap) {
        val state = MovableImageState()
        fun draw(canvas:Canvas, paint:Paint) {
            val w = bitmap.width.toFloat()
            val h = bitmap.height.toFloat()
            canvas.save()
            val path = Path()
            path.addRoundRect(RectF(0f,0f,w,h),Math.max(w,h)/5,Math.max(w,h)/5,Path.Direction.CW)
            canvas.save()
            canvas.translate(w/2, h/2)
            canvas.scale(1f + 0.5f * state.scale, 1f + 0.5f * state.scale)
            canvas.drawBitmap(bitmap, -w/2, -h/2, paint)
            canvas.restore()
            canvas.restore()
        }
        fun update(stopcb: (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb: () -> Unit) {
            state.startUpdating(startcb)
        }
    }
    data class Renderer(var view: MovableImageView, var time:Int = 0) {
        var movableImage:MovableImage ?= null
        val animator = Animator(view)
        fun render(canvas:Canvas, paint:Paint) {
            if(time == 0) {
                val w = canvas.width
                val h = canvas.height
                var bitmap = Bitmap.createScaledBitmap(view.bitmap, w, w, true)
                movableImage = MovableImage(bitmap)
            }
            canvas.drawColor(Color.parseColor("#212121"))
            movableImage?.draw(canvas,paint)
            time++
            animator.animate {
                movableImage?.update {
                    animator.stop()
                }
            }
        }
        fun handleTap () {
            movableImage?.startUpdating {
                animator.start()
            }
        }
    }
}