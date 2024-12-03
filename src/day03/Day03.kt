package day03

import day03.Command.Multiply
import readInput

private const val DIRECTORY = "day03/Day03"

fun main() {
    val mulInstruction = """mul\(\d{1,3},\d{1,3}\)""".toRegex()
    val mulWithDoOrDoNotInstruction = """(mul\(\d{1,3},\d{1,3}\))|(don't\(\))|(do\(\))""".toRegex()

    fun parseMulCommands(it: String) = mulInstruction.findAll(it).map { result: MatchResult -> result.value }

    fun accumulateSimpleValues(commandList: Sequence<String>) =
        commandList.fold(0) { acc: Int, cmnd -> Multiply.fromString(cmnd).result + acc }

    fun part1(input: List<String>): Int {
        return input
            .map { parseMulCommands(it) }
            .sumOf { commandList -> accumulateSimpleValues(commandList) }
    }

    fun parseAdvancedCommands(it: String) =
        mulWithDoOrDoNotInstruction.findAll(it).map { result: MatchResult -> result.value.toCommand() }

    fun accumulateAdvancedValue(command: Command, accumulated: Int, shouldProcess: Boolean) = when {
        command is Command.ControlFlow.Do -> accumulated to true
        command is Command.ControlFlow.DoNot -> accumulated to false
        shouldProcess -> (accumulated + (command as Multiply).result) to true
        else -> accumulated to false
    }

    fun part2(input: List<String>): Int {
        return input.map { parseAdvancedCommands(it) }.flatMap { it }
            .fold(0 to true) { (accumulated, shouldProcess), command ->
                accumulateAdvancedValue(command, accumulated, shouldProcess)
            }.first
    }

    val testInput = readInput("${DIRECTORY}_test")
    val input = readInput(DIRECTORY)

    val test1Result = part1(testInput)
    println("Test input 1 results: $test1Result")
    check(test1Result == 161)
    println("Part 1 result: ${part1(input)}")

    val test2Result = part2(testInput)
    println("Test input 2 results: $test2Result")
    check(test2Result == 48)
    val part2Result = part2(input)
    println("Part 2 result: $part2Result")
}

private sealed interface Command {

    data class Multiply(val x: Int, val y: Int) : Command {
        val result: Int = x * y

        companion object {
            fun fromString(input: String): Multiply {
                val numbers = """\d{1,3}""".toRegex().findAll(input).map { it.value }.toList().take(2)
                return Multiply(numbers[0].toInt(), numbers[1].toInt())
            }
        }
    }

    sealed class ControlFlow : Command {
        data object Do : ControlFlow()
        data object DoNot : ControlFlow()
    }
}

private fun String.toCommand(): Command = when (this) {
    "do()" -> Command.ControlFlow.Do
    "don't()" -> Command.ControlFlow.DoNot
    else -> Multiply.fromString(this)
}
