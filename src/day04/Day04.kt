package day04

import readInput

private const val DIRECTORY = "day04/Day04"
fun main() {
    val xmasRegex = """(?=XMAS)|(?=SAMX)""".toRegex()

    fun getDiagonalMatches(input: List<String>): Int {
        val mapDiagonalIndices: HashMap<Int, MutableList<Char>> = HashMap(input.size * input.first().length)
        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, c ->
                mapDiagonalIndices[rowIndex + columnIndex]?.add(c)
                    ?: mapDiagonalIndices.put(rowIndex + columnIndex, mutableListOf(c))
            }
        }
        val diagonalTexts = mapDiagonalIndices.map { (_, value) -> value.joinToString("") }
        val diagonalMatches = diagonalTexts.sumOf { xmasRegex.findAll(it).count() }
        return diagonalMatches
    }

    fun getReverseDiagonalMatches(input: List<String>): Int {
        val mapReverseDiagonalIndices: HashMap<Int, MutableList<Char>> = HashMap()
        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, c ->
                mapReverseDiagonalIndices[rowIndex - columnIndex]?.add(c)
                    ?: mapReverseDiagonalIndices.put(rowIndex - columnIndex, mutableListOf(c))
            }
        }
        val reverseDiagonalTexts = mapReverseDiagonalIndices.map { (_, value) -> value.joinToString("") }
        val reverseDiagonalMatches = reverseDiagonalTexts.sumOf { xmasRegex.findAll(it).count() }
        return reverseDiagonalMatches
    }

    fun part1(input: List<String>): Int {
        val horizontalMatches = input.sumOf { xmasRegex.findAll(it).count() }
        val depth = input.first().length
        var verticalMatches = 0
        for (i in 0 until depth) {
            val column = buildString { input.forEach { append(it[i]) } }
            verticalMatches += xmasRegex.findAll(column).count()
        }

        val diagonalMatches = getDiagonalMatches(input)
        val reverseDiagonalMatches = getReverseDiagonalMatches(input)

        return verticalMatches + horizontalMatches + diagonalMatches + reverseDiagonalMatches
    }

    fun part2(input: List<String>): Int {
        var numberOfXmas = 0
        for (i in 1 until input.size - 1) {
            for (j in 1 until input.first().length - 1) {
                val letter = input[i][j]
                if (letter == 'A') {
                    // Check for diagonal MAS or SAM
                    val diagonal = buildString {
                        append(input[i - 1][j - 1])
                        append(letter)
                        append(input[i + 1][j + 1])
                    }
                    val reverseDiagonal = buildString {
                        append(input[i - 1][j + 1])
                        append(letter)
                        append(input[i + 1][j - 1])
                    }
                    if ((diagonal == "MAS" || diagonal == "SAM") && (reverseDiagonal == "MAS" || reverseDiagonal == "SAM")) {
                        numberOfXmas++
                    }
                }
            }
        }
        return numberOfXmas
    }

    val testInput = readInput("${DIRECTORY}_test")
    val input = readInput(DIRECTORY)

    val test1Result = part1(testInput)
    println("Test input 1 results: $test1Result")
    check(test1Result == 18)
    println("Part 1 result: ${part1(input)}")

    val test2Result = part2(testInput)
    println("Test input 2 results: $test2Result")
    check(test2Result == 9)
    val part2Result = part2(input)
    println("Part 2 result: $part2Result")
}
