class OrbPathCalculator {
    companion object {
        const val START = 0
        const val VAULT = 7
    }

    data class Transition(val destination: Int, val path: String, val operation: (Int) -> Int)

    private val transitions = mapOf(
        0 to listOf(
            Transition(1, "NN") { it + 4 },
            Transition(2, "NE") { it + 4 },
            Transition(2, "EN") { it - 4 },
            Transition(3, "EE") { it - 9 }
        ),
        1 to listOf(
            Transition(4, "NE") { it * 8 },
            Transition(1, "NS") { it * 4 },
            Transition(5, "EE") { it * 11 },
            Transition(2, "ES") { it * 4 },
        ),
        2 to listOf(
            Transition(4, "NN") { it * 8 },
            Transition(5, "NE") { it * 11 },
            Transition(2, "NS") { it * 4 },
            Transition(1, "NW") { it * 4 },
            Transition(5, "EN") { it - 11 },
            Transition(6, "EE") { it - 18 },
            Transition(3, "ES") { it - 9 },
            Transition(2, "EW") { it - 4 },
            Transition(3, "SE") { it - 9 },
            Transition(1, "WN") { it + 4 },
            Transition(2, "WE") { it + 4 },
        ),
        3 to listOf(
            Transition(5, "NN") { it - 11 },
            Transition(6, "NE") { it - 18 },
            Transition(3, "NS") { it - 9 },
            Transition(2, "NW") { it - 4 },
            Transition(6, "EN") { it * 18 },
            Transition(3, "EW") { it * 9 },
            Transition(2, "WN") { it - 4 },
            Transition(3, "WE") { it - 9 },
        ),
        4 to listOf(
            Transition(VAULT, "EE") { it - 1 },
            Transition(5, "ES") { it - 11 },
            Transition(4, "EW") { it - 8 },
            Transition(4, "SN") { it * 8 },
            Transition(5, "SE") { it * 11 },
            Transition(2, "SS") { it * 4 },
            Transition(1, "SW") { it * 4 }
        ),
        5 to listOf(
            Transition(VAULT, "NE") { it - 1 },
            Transition(5, "NS") { it - 11 },
            Transition(4, "NW") { it - 8 },
            Transition(VAULT, "EN") { it },
            Transition(6, "ES") { it * 18 },
            Transition(5, "EW") { it * 11 },
            Transition(6, "SE") { it - 18 },
            Transition(3, "SS") { it - 9 },
            Transition(2, "SW") { it - 4 },
            Transition(4, "WN") { it * 8 },
            Transition(2, "WS") { it * 4 },
            Transition(1, "WW") { it * 4 }
        ),
        6 to listOf(
            Transition(VAULT, "NN") { it },
            Transition(6, "NS") { it * 18 },
            Transition(3, "SW") { it * 9 },
            Transition(5, "WN") { it - 11 },
            Transition(6, "WE") { it - 18 },
            Transition(3, "WS") { it - 9 },
            Transition(2, "WW") { it - 4 }
        ),
    )

    data class State(val location: Int, val orb: Int, val path: String)

    fun calculate(): String {
        val toExplore = ArrayDeque<State>()
        toExplore.addLast(State(START, 22, ""))
        while (toExplore.isNotEmpty()) {
            val cur = toExplore.removeFirst()
            transitions[cur.location]!!.forEach { t ->
                val newState = State(t.destination, t.operation.invoke(cur.orb), cur.path + t.path)
                if (newState.location == VAULT) {
                    if (newState.orb == 30) {
                        return buildString {
                            newState.path.forEach {
                                appendLine(
                                    "go " + when (it) {
                                        'N' -> "north"
                                        'E' -> "east"
                                        'W' -> "west"
                                        'S' -> "south"
                                        else -> error("Unexpected character: $it")
                                    }
                                )
                            }
                        }
                    }
                } else if (newState.orb >= 0) {
                    toExplore.addLast(newState)
                }
            }
        }
        return ""
    }
}
