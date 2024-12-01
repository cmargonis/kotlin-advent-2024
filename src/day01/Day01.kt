package day01

import println
import readInput
import kotlin.math.abs

fun main() {
    fun calculateDistances(left: List<Int>, right: List<Int>): Int {
        val distances: Int = left.zip(right).fold(0) { acc, par ->
            val distance = abs(par.first - par.second)
            val next = acc + distance
            next
        }
        return distances
    }

    fun parseLists(
        input: List<String>,
        left: MutableList<Int>,
        right: MutableList<Int>
    ) {
        input.forEach { pair ->
            val (a, b) = pair.split("""\s+""".toRegex())
            left.add(a.toInt())
            right.add(b.toInt())
        }
    }

    fun part1(input: List<String>): Int {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()
        parseLists(input, left, right)
        left.sort()
        right.sort()

        val distances: Int = calculateDistances(left, right)
        return distances
    }

    fun getSimilarityScore(left: List<Int>, right: List<Int>): Int =
        left.sumOf { l ->
            val occurrences = right.count { l == it }
            l * occurrences
        }

    fun part2(input: List<String>): Int {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()
        parseLists(input, left, right)
        return getSimilarityScore(left, right)
    }

    val testInput = readInput("day01/Day01_test")
    val input = readInput("day01/Day01")

    check(part1(testInput) == 11)
    part1(input).println()

    check(part2(testInput) == 31)
    part2(input).println()
}
