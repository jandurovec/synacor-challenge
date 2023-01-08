import java.io.File

fun main() {
    fun run(program: IntArray) {
        val mem = program.copyOf(32776)
        var programCounter = 0

        fun getValue(v: Int) = if (v <= 32767) v else if (v <= 32775) mem[v] else error("Invalid value $v")

        while (true) {
            when (mem[programCounter]) {
                0 -> break
                19 -> {
                    print(getValue(mem[programCounter + 1]).toChar())
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

    run(program)
}
