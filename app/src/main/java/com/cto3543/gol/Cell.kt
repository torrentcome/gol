package com.cto3543.gol

class Cell (var x: Int, var y: Int, var alive: Boolean){

    fun die() {
        alive = false
    }

    fun reborn() {
        alive = true
    }

    fun invert() {
        alive = !alive
    }
}