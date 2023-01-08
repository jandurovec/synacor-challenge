import java.io.File
import java.util.*

fun main() {
    fun run(program: IntArray, input: String = "") {
        val mem = program.copyOf(32776)
        var programCounter = 0
        val stack: ArrayDeque<Int> = ArrayDeque()

        fun getValue(v: Int) = if (v <= 32767) v else if (v <= 32775) mem[v] else error("Invalid value $v")

        var inputBuffer = input
        var inputIndex = 0

        while (true) {
            when (mem[programCounter]) {
                0 -> break
                1 -> {
                    mem[mem[programCounter + 1]] = getValue(mem[programCounter + 2])
                    programCounter += 3
                }

                2 -> {
                    stack.push(getValue(mem[programCounter + 1]))
                    programCounter += 2
                }

                3 -> {
                    if (stack.isEmpty()) error("Empty stack")
                    mem[mem[programCounter + 1]] = stack.pop()
                    programCounter += 2
                }

                4 -> {
                    mem[mem[programCounter + 1]] =
                        if (getValue(mem[programCounter + 2]) == getValue(mem[programCounter + 3])) 1 else 0
                    programCounter += 4
                }

                5 -> {
                    mem[mem[programCounter + 1]] =
                        if (getValue(mem[programCounter + 2]) > getValue(mem[programCounter + 3])) 1 else 0
                    programCounter += 4
                }

                6 -> programCounter = getValue(mem[programCounter + 1])
                7 -> programCounter =
                    if (getValue(mem[programCounter + 1]) != 0) getValue(mem[programCounter + 2]) else programCounter + 3

                8 -> programCounter =
                    if (getValue(mem[programCounter + 1]) == 0) getValue(mem[programCounter + 2]) else programCounter + 3

                9 -> {
                    mem[mem[programCounter + 1]] =
                        (getValue(mem[programCounter + 2]) + getValue(mem[programCounter + 3])) % 32768
                    programCounter += 4
                }

                10 -> {
                    mem[mem[programCounter + 1]] =
                        (getValue(mem[programCounter + 2]) * getValue(mem[programCounter + 3])) % 32768
                    programCounter += 4
                }

                11 -> {
                    mem[mem[programCounter + 1]] =
                        getValue(mem[programCounter + 2]) % getValue(mem[programCounter + 3])
                    programCounter += 4
                }

                12 -> {
                    mem[mem[programCounter + 1]] =
                        getValue(mem[programCounter + 2]) and getValue(mem[programCounter + 3])
                    programCounter += 4
                }

                13 -> {
                    mem[mem[programCounter + 1]] =
                        getValue(mem[programCounter + 2]) or getValue(mem[programCounter + 3])
                    programCounter += 4
                }

                14 -> {
                    mem[mem[programCounter + 1]] = getValue(mem[programCounter + 2]).inv() and 0x7fff
                    programCounter += 3
                }

                15 -> {
                    mem[mem[programCounter + 1]] = mem[getValue(mem[programCounter + 2])]
                    programCounter += 3
                }

                16 -> {
                    mem[getValue(mem[programCounter + 1])] = getValue(mem[programCounter + 2])
                    programCounter += 3
                }

                17 -> {
                    stack.push(programCounter + 2)
                    programCounter = getValue(mem[programCounter + 1])
                }

                18 -> {
                    if (stack.isEmpty()) break
                    programCounter = stack.pop()
                }

                19 -> {
                    print(getValue(mem[programCounter + 1]).toChar())
                    programCounter += 2
                }

                20 -> {
                    if (inputIndex == inputBuffer.length) {
                        inputBuffer = readln() + '\n'
                        inputIndex = 0
                    }
                    mem[mem[programCounter + 1]] = inputBuffer[inputIndex++].code
                    programCounter += 2
                }

                21 -> programCounter++
                else -> error("${mem[programCounter]} not implemented yet")
            }
        }
    }

    val program = File("challenge.bin").readBytes()
        .asList().chunked(2).map {
            it[0].toUByte().toInt() + (it[1].toUByte().toInt() shl 8)
        }.toIntArray()

    val commands = buildString {
        appendLine("take tablet")
        appendLine("use tablet")
        appendLine("go doorway")
        appendLine("go north")
        appendLine("go north")
        appendLine("go bridge")
        appendLine("go continue")
        appendLine("go down")
        appendLine("go east")
        appendLine("take empty lantern")
        appendLine("go west")
        appendLine("go west")
        appendLine("go passage")
        appendLine("go ladder")
        appendLine("go west")
        appendLine("go north")
        appendLine("go south")
        appendLine("go north")
        appendLine("take can")
        appendLine("use can")
        appendLine("go west")
        appendLine("go ladder")
        appendLine("use lantern")
        appendLine("go darkness")
        appendLine("go continue")
        appendLine("go west")
        appendLine("go west")
        appendLine("go west")
        appendLine("go west")
        appendLine("go north")
        appendLine("look red coin")
        appendLine("take red coin")
        appendLine("go north")
        appendLine("go west")
        appendLine("look blue coin")
        appendLine("take blue coin")
        appendLine("go up")
        appendLine("look shiny coin")
        appendLine("take shiny coin")
        appendLine("go down")
        appendLine("go east")
        appendLine("go east")
        appendLine("look concave coin")
        appendLine("take concave coin")
        appendLine("go down")
        appendLine("look corroded coin")
        appendLine("take corroded coin")
        appendLine("go up")
        appendLine("go west")
        appendLine("use blue coin")
        appendLine("use red coin")
        appendLine("use shiny coin")
        appendLine("use concave coin")
        appendLine("use corroded coin")
        appendLine("go north")
        appendLine("take teleporter")
        appendLine("use teleporter")
    }

    run(program, commands)
}
