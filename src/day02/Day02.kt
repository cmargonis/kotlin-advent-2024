package day02

import readInput
import kotlin.math.abs

private const val MINIMUM_DIFFERENCE = 1
private const val MAXIMUM_DIFFERENCE = 3

fun main() {

    fun isOrdered(levels: List<Int>): Boolean = with(levels) {
        zipWithNext { a, b -> a <= b }.all { it } || zipWithNext { a, b -> a >= b }.all { it }
    }

    fun parseReportsToLists(reportsToParse: String): List<Int> =
        reportsToParse.split("""\s+""".toRegex()).map { it.toInt() }

    fun isPairChangeValid(a: Int, b: Int): Boolean {
        val difference = abs(b - a)
        return difference in MINIMUM_DIFFERENCE..MAXIMUM_DIFFERENCE
    }

    fun isReportSafe(levels: List<Int>) = with(levels) {
        isOrdered(this) && zipWithNext { a, b -> isPairChangeValid(a, b) }.all { differences -> differences }
    }

    fun part1(input: List<String>): Int = input
        .map { reportsToParse -> parseReportsToLists(reportsToParse) }
        .map { levels -> isReportSafe(levels) }
        .count { safeReports -> safeReports }

    fun checkForSafetyWithLevelRemoval(levels: List<Int>): Boolean {
        var isReportSafe = isReportSafe(levels)
        if (!isReportSafe) {
            // Attempt to find if removing a level results to a safe report
            for (i in levels.indices) {
                val mutableLevels = levels.toMutableList()
                mutableLevels.removeAt(i)
                isReportSafe = isReportSafe(mutableLevels)
                if (isReportSafe) break
            }
        }
        return isReportSafe
    }

    fun part2(input: List<String>): Int = input
        .map { reportsToParse -> parseReportsToLists(reportsToParse) }
        .map { levels -> checkForSafetyWithLevelRemoval(levels) }
        .count { safeReports -> safeReports }

    val testInput = readInput("day02/Day02_test")
    val input = readInput("day02/Day02")

    val test1Result = part1(testInput)
    println("Test input 1 results: $test1Result")
    check(test1Result == 2)
    println("Part 1 result: ${part1(input)}")

    val test2Result = part2(testInput)
    println("Test input 2 results: $test2Result")
    check(test2Result == 4)
    println("Part 2 result: ${part2(input)}")
}
