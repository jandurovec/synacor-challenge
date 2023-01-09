import java.util.ArrayDeque

class VirtualMachine(program: IntArray) {
    val mem = program.copyOf(32776)
    var programCounter = 0
    val stack: ArrayDeque<Int> = ArrayDeque()
    fun run(input: String = "", interactive: Boolean = false) {

        fun getValue(v: Int) = if (v <= 32767) v else if (v <= 32775) mem[v] else error("Invalid value $v")

        var inputBuffer = input
        var inputIndex = 0

        while (true) {
            if (programCounter == 5489) {
                // skip teleport validation
                mem[32768] = 6
                programCounter = 5491
            }
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
                    //println("Calling ${getValue(mem[programCounter + 1])} at ${programCounter}")
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
                        if (interactive) {
                            inputBuffer = readln() + '\n'
                            inputIndex = 0
                        } else {
                            break
                        }
                    }
                    mem[mem[programCounter + 1]] = inputBuffer[inputIndex++].code
                    programCounter += 2
                }

                21 -> programCounter++
                else -> error("${mem[programCounter]} not implemented yet")
            }
        }
    }
}
