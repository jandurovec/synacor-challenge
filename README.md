# Synacor Challenge

## Code 1: Architecture spec
The first code is listed in architecture spec document so no code is needed.

## Code 2: Boot
After implementing the first three instructions (0, 19, 21) as mentioned in the architecture spec, the VM yields the
second code.

## Code 3: Self-test
After implementing the remaining instructions the VM passes the self-test and yields the third code.

## Code 4: Tablet
Picking up and using tablet in the first location yields the fourth code.

## Code 5: Twisty passages
Exploring the map yields the fifth code

## Code 6: Ruins
After obtaining can of oil for the lantern, I've been able to navigate through the darkness. I've arrived to a strange
monument which read the following:

```
_ + _ * _^2 + _^3 - _ = 399
```

In addition to that, the adjacent rooms contained some coins. The coins have markings indicating their value (2,3,5,7,9)
and using them on a monument completes the equation. The trick is just to insert them in the correct order so that the
equation makes sense.

After the door is unlocked I find a teleporter device which yields the sixth code when used.

## Code 7: Teleporter
The new location contains a strange book that says that the teleporter confirmation mechanism needs to be disabled plus
the eight register needs to be set to a specific energy level so that the teleporter takes me to the correct place.

By enabling logging of `call` instruction we can see that the calculation calls function 6027 recursively. In addition
to that the first such call is from address 5489. This must be where the confirmation mechanism starts.

The code reveals the following

```
5489: 17    // call 6027
5490: 6027
5491: 4     // set <1> to 1 if <0> is equal to 6; set it to 0 otherwise
5492: 32769
5493: 32768
5494: 6
5495: 8     // if <1> is zero, jump to 5579
5496: 32769
5497: 5579
```

This means that in order to bypass the confirmation mechanism we just need to skip the instruction at 5489 and continue
with 5491 either by hardcoding that in the VM or by replacing 5489 and 5490 with `noop`. However, we still need to find
the correct value for the registries.

The function at address 6027 is as follows:

```
6027: 7     // if <0> is nonzero, jump to 6035
6028: 32768
6029: 6035
6030: 9     // assign into <0> the sum of <1> and 1
6031: 32768
6032: 32769
6033: 1
6034: 18    // return
6035: 7     // if <1> is nonzero, jump to 6048
6036: 32769
6037: 6048
6038: 9     // assign into <0> the sum of <0> and 32767
6039: 32768
6040: 32768
6041: 32767
6042: 1     // set register <1> to the value of <7>
6043: 32769
6044: 32775
6045: 17    // call 6027
6046: 6027
6047: 18    // return
6048: 2     // push <0> onto the stack
6049: 32768
6050: 9     // assign into <1> the sum of <1> and 32767
6051: 32769
6052: 32769
6053: 32767
6054: 17    // call 6027
6055: 6027
6056: 1     // set register <1> to the value of <0>
6057: 32769
6058: 32768
6059: 3     // remove the top element from the stack and write it into <0>
6060: 32768
6061: 9     // assign into <0> the sum of <0> and 32767
6062: 32768
6063: 32768
6064: 32767
6065: 17
6066: 6027  // call 6027
6067: 18    // return
```

When rewritten to actual code, it might look like this:

```kotlin
fun fun6027(r0 : Int, r1 : Int) {
    return if (r0 == 0) {
        (r1 + 1) % 32768
    } else if (r1 == 0) {
        fun6027((r0 + 32767) % 32768, teleporterEnergy)
    } else {
        fun6027(
            (r0 + 32767) % 32768,
            fun6027(r0, (r1 + 32767) % 32768)
        )
    }
}

check(fun6027(4, 1) == 6)
```

The code causes quite a deep recursion, but luckily not that deep that it would not fit into memory after extending JVM
stack size (20Mb was enough).

After finding the correct teleporter energy level and rewriting the `call` (and its argument) with `noop`, teleportation
yields the correct code.

## Code 8: Vault
The final puzzle is to figure out the shortest path through a maze with the orb, where transitions between rooms apply
mathematical operations to the orb (sequentially, no operator priorities). The orb weight must not be negative at any
point in time (the orb evaporates) and it needs to have a value of 30 on entering the last room before vault.

The maze is as follows:

```
[*][8][- ][1E]
[4][*][11][* ]
[+][4][- ][18]
[S][-][9 ][* ]
```

The orb starts in the lower left corner (room "S") with weight of 22 and visiting this room always resets the weight
(i.e. visiting it has the same effect as restarting the journey).

I've written the `OrbPathCalculator` to perform a simple BFS. I was thinking about adding some concept of "visited"
cache tracking if we've already been in a given room with the orb of a particular value to remove the loops, however,
the search algorithm finished fast and no such optimizations were needed.

The vault contained a mirror, which, when used, yielded the last code. However, given the fact that the code has been
seen in the mirror, it needed to be mirrored to get the real one (i.e. not only reversing the order but also mapping
p to q, etc.)
