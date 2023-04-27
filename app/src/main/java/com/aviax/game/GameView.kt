package com.aviax.game


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class GameView(val ctx: Context, val att: AttributeSet): SurfaceView(ctx,att) {

    var ball = BitmapFactory.decodeResource(ctx.resources,R.drawable.avia1)
    var bg = BitmapFactory.decodeResource(ctx.resources,R.drawable.bg)
    var fuel = BitmapFactory.decodeResource(ctx.resources,R.drawable.fuel)

    var music = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("music",true)
    var sounds = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("sounds",true)
    private var paintB: Paint = Paint(Paint.DITHER_FLAG)
    private var paintT = Paint().apply {
        color = Color.WHITE
        textSize = 130f
        style = Paint.Style.FILL
    }
    private var listener: EndListener? = null
    private val random = Random()
    private var millis = 0
    var player = MediaPlayer.create(ctx,R.raw.bg)
    var sound = MediaPlayer.create(ctx,R.raw.sound)

    init {
        player.setOnCompletionListener {
            it.start()
        }
        if(music) player.start()
         ball = Bitmap.createScaledBitmap(ball,ball.width/4,ball.height/4,true)
        bg = Bitmap.createScaledBitmap(bg,bg.width/2,bg.height/2,true)
        fuel = Bitmap.createScaledBitmap(fuel,fuel.width/4,fuel.height/4,true)
      holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder) {

            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                val canvas = holder.lockCanvas()
                if(canvas!=null) {
                    by = canvas.height/2
                    bx = (canvas.width/2f-ball.width/2f).toInt()
                    draw(canvas)
                    holder.unlockCanvasAndPost(canvas)
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                paused = true
                player.stop()
            }

        })
        val updateThread = Thread {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (!paused) {
                        update.run()
                        millis ++
                    }
                }
            }, 500, 16)
        }

        updateThread.start()
    }
    var code = -1f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action) {
            MotionEvent.ACTION_UP -> {
                code = -1f
            }
            MotionEvent.ACTION_DOWN -> {
                code = event.x
            }
        }
        postInvalidate()
        return true
    }
    var g = 0
    var paused = false
    var list = mutableListOf<Model>()
    var delta = 8
    var bx = 0
    var health = 3
    var score = 0
    var by = 0
    var progress = 50
    var tmp = 0

    val update = Runnable{
        var isEnd = false
        var sc = false
        if(paused) return@Runnable
        try {
            val canvas = holder.lockCanvas()
            if(code!=-1f) {
                if(abs(code-bx)>=delta) {
                    if(code>bx) bx+=delta
                    else bx-=delta
                }
            }
           // Log.d("TAG",tmp.toString())
            if(tmp>0) {
                by += canvas.height/500
                tmp--
            } else if(tmp<0) {
                by -= canvas.height/400
                tmp++
            }
            by = max(ball.height,by)
            if(millis>=80) {
                score += 5
                millis = 0
                tmp += 1
                progress--
                listener?.score(progress)
            }
            var i = 0
            while(i<list.size) {
                list[i].y+=5
                if(abs(list[i].x-bx)<=ball.width/2 && abs(list[i].y-(by))<=ball.height/2) {
                    score += 15
                    progress+=5
                    tmp -= 10
                    if(sounds) sound.start()
                    progress = min(progress,100)
                    listener?.score(progress)
                    list.removeAt(i)
                    break
                } else if(list[i].y>=canvas.height+fuel.height) {
                    list.removeAt(i)
                } else i++
            }
            while(list.size<4) {
                list.add(Model(random.nextInt(canvas.width).toFloat(),-fuel.height.toFloat(),0))
            }
            if(progress<=0) {
                isEnd = true
            }
            if(bx<=-ball.width) bx = canvas.width
            if(bx>=ball.width+canvas.width) bx = 0
            canvas.drawBitmap(bg,0f,0f,paintB)
            canvas.drawText(score.toString(),canvas.width/2f-(if(score<100) 50f else 100f),canvas.height/6f,paintT)
            for(i in list) {
                canvas.drawBitmap(fuel,i.x,i.y,paintB)
            }
            canvas.drawBitmap(ball,bx.toFloat(),by.toFloat(),paintB)
            holder.unlockCanvasAndPost(canvas)
            if(isEnd) {
                Log.d("TAG","END")
                togglePause()
                if(listener!=null) listener!!.end()
            }
            if(sc) {
                listener?.score(health)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setEndListener(list: EndListener) {
        this.listener = list
    }
    fun togglePause() {
        paused = !paused
    }
    companion object {
        interface EndListener {
            fun end();
            fun score(score: Int);
        }
        data class Model(var x: Float, var y: Float, var cur: Int)
    }
    val b = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("color",0)
}