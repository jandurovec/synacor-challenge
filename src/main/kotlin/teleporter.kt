fun main() {
    fun confirmation(
        r0: Int,
        r1: Int,
        energy: Int,
        cache: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
    ): Int {
        val key = r0 to r1
        return if (cache.containsKey(key)) {
            cache[key]!!
        } else if (r0 == 0) {
            ((r1 + 1) % 32768).also { cache[key] = it }
        } else if (r1 == 0) {
            confirmation((r0 + 32767) % 32768, energy, energy, cache).also { cache[key] = it }
        } else {
            val nr1 = confirmation(r0, (r1 + 32767) % 32768, energy, cache)
            confirmation((r0 + 32767) % 32768, nr1, energy, cache).also { cache[key] = it }
        }
    }

    for (energy in 32767 downTo 1) {
        // progress bar
        if (energy % 1000 == 0) {
            print((energy/1000).toString(33))
        }
        if (confirmation(4, 1, energy) == 6) {
            println()
            println("Teleporter energy: $energy")
            break
        }
    }
}
