package com.cto3543.gol

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {
    private var gameOfLifeView: GOLView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameOfLifeView = findViewById(R.id.game_of_life)
    }

    override fun onResume() {
        super.onResume()
        gameOfLifeView?.start()
    }

    override fun onPause() {
        super.onPause()
        gameOfLifeView?.stop()
    }
}
