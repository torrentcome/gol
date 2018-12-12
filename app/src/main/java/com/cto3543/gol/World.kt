package com.cto3543.gol

import kotlin.random.Random

/*
* 1. Any live cell with fewer than two live neighbors dies, as if caused by underpopulation.
* 2. Any live cell with two or three live neighbors lives on to the next generation.
* 3. Any live cell with more than three live neighbors dies, as if by overpopulation.
* 4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
* */

class World(
    private var width: Int,
    private var height: Int,
    private val board: Array<Array<Cell>> = Array(width) { Array(height) { Cell(0, 0, false) } }
) {

    init {
        for (i in 0 until width) {
            for (j in 0 until height) {
                board[i][j] = Cell(i, j, Random.nextBoolean())
            }
        }
    }

    fun get(i: Int, j: Int): Cell {
        return board[i][j]
    }

    private fun nbNeighboursOf(i: Int, j: Int): Int {
        var nb = 0

        for (k in i - 1..i + 1) {
            for (l in j - 1..j + 1) {
                if ((k != i || l != j) && k >= 0 && k < width && l >= 0 && l < height) {
                    val cell = board[k][l]
                    if (cell.alive) {
                        nb++
                    }
                }
            }
        }
        return nb
    }


    fun nextGeneration() {
        val liveCells = ArrayList<Cell>()
        val deadCells = ArrayList<Cell>()

        for (i in 0 until width) {
            for (j in 0 until height) {
                val cell = board[i][j]
                val nbNeighbours = nbNeighboursOf(cell.x, cell.y)

                // rule 1 & rule 3
                if (cell.alive && (nbNeighbours < 2 || nbNeighbours > 3)) {
                    deadCells.add(cell)
                }

                // rule 2 & rule 4
                if (cell.alive && (nbNeighbours == 3 || nbNeighbours == 2) || !cell.alive && nbNeighbours == 3) {
                    liveCells.add(cell)
                }
            }
        }

        // update future live and dead cells
        for (cell in liveCells) {
            cell.reborn()
        }
        for (cell in deadCells) {
            cell.die()
        }
    }
}