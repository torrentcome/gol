package com.cto3543.gol

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.WindowManager


const val DEFAULT_SIZE = 30

class GOLView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    SurfaceView(context, attrs, defStyleAttr), Runnable {

    private var defaultLiveColor : Int = Color.parseColor("#718792")
    private var defaultDeadColor : Int = Color.parseColor("#000a12")

    private lateinit var thread: Thread
    private var isRunning = false
    private var columnWidth = 1
    private var rowHeight = 1
    private var nbColumns = 1
    private var nbRows = 1
    private lateinit var world: World
    private val r = Rect()
    private val p = Paint()

    init {
        initWorld()
    }

    private fun initWorld() {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val point = Point()
        display.getSize(point)
        nbColumns = point.x / DEFAULT_SIZE
        nbRows = point.y / DEFAULT_SIZE
        columnWidth = point.x / nbColumns
        rowHeight = point.y / nbRows
        world = World(nbColumns, nbRows)
    }

    override fun run() {
        while (isRunning) {
            if (!holder.surface.isValid)
                continue
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
            }
            val canvas = holder.lockCanvas()
            world.nextGeneration()
            drawCells(canvas)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun start() {
        isRunning = true
        thread = Thread(this)
        thread.start()
    }

    fun stop() {
        isRunning = false
        while (true) {
            try {
                thread.join()
            } catch (ignored: InterruptedException) {
            }
            break
        }
    }

    private fun drawCells(canvas: Canvas) {
        for (i in 0 until nbColumns) {
            for (j in 0 until nbRows) {
                val cell = world.get(i, j)
                r.set(
                    cell.x * columnWidth - 1, cell.y * rowHeight - 1,
                    cell.x * columnWidth + columnWidth - 1,
                    cell.y * rowHeight + rowHeight - 1
                )
                p.color = if (cell.alive) defaultLiveColor else defaultDeadColor
                canvas.drawRect(r, p)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val i = (event.x / columnWidth).toInt()
            val j = (event.y / rowHeight).toInt()
            val cell = world.get(i, j)
            cell.invert()
            invalidate()
        }
        return super.onTouchEvent(event)
    }
}