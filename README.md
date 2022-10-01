# Archipelago-technical-test
My first technical test

### Copy paste problem

**Instructions**
* You must return your solution within 4 hours after you receive these instructions. Your solution is disqualified if it arrives late. Partial solutions will be considered, but a complete solution is expected.
* You can use any programming language you want. You must submit your code for review, zipped.
* Your program must read an ASCII text file as input and print its output to standard out.
* Include instructions for running your program. If you use a compiled language, you must include compilation instructions as well. We do not want your compiled program, we want the source code and compilation instructions, so that we can compile it ourselves.

</br>

**Problem**

</br>

Approximately 23 light-years from planet Earth there exists planet Parkimovil. Planet Parkimovil’s surface is covered by a great ocean dotted by many small islands.

</br>

For this question, Parkimovil’s surface will be modeled as a 2D plane. There exist N islands scattered on this 2D plane. The ith island is located at at (Xi, Yi). 

</br>

On this plane, line segments are defined in terms of islands. A line segment is bounded by two islands.

</br>

A Parkimovil archipelago consists of two distinct but equal-length line segments which have one shared island endpoint and two distinct island endpoints. Parkimovil archipelagos are considered distinct if they're not made up of the same three islands.

</br>

Your goal is to find out how many distinct Parkimovil archipelagos exist. 

</br>

**Input**

</br>

Input begins with an integer T, the number of test cases in the file. 

</br>

For each test case there is an integer N. The following N lines contain the space separated integers Xi and Yi.

</br>

**Output**

</br>

Print a line containing the number of Parkimovil archipelagos in the ocean.

</br>

**Constraints**

</br>

1 ≤ T ≤ 50 

</br>

1 ≤ N ≤ 2,000 

</br>

-10,000 ≤ Xi, Yi ≤ 10,000

</br>


### Solution

#### First step
##### Divide and conquer

#### Definition clases

We need a class for organize information

**Island**
</br>
Simple class for save coordinates for any island

```kotlin
data class Island(
  val x:Int,
  val y:Int
)

```
**Edge**
</br>
Class create by thow Island, this class is util for save the distance betheewn this islands

*distance*
</br>
<img style="background:white" src="https://user-images.githubusercontent.com/46488277/193378555-4640add9-e4f4-4da2-b08a-a77ac9ec1ec9.png">


``` kotlin
data class Edge(
    val islandX: Island,
    val islandY: Island
) {
    val distance: Float

    init {
        val sumDistance =(islandX.x - islandY.x).toFloat().pow(2) + (islandX.y - islandY.y).toFloat().pow(2)
        distance = sqrt(sumDistance)
    }
 }

```

**Archipelago**
</br>
Class create by thow Edges

``` kotlin

data class Archipelago(
    val edge1: Edge,
    val edge2: Edge
)

```

### First problem

The data class is very util, for override function as *toString* or *equals*, and remove elements using *sets*, but this problem is the **Edge** is same another , if has same Islands, but when create edges, maybe has the same order or not, but is the same

``` kotlin
  
  val island1 = Island(1,2)
  val island2 = Island(3,4)
  
  val edge1 = Edge(island1,island2)
  val edge2 = Edge(island1,island2)
  val edge3 = Edge(island2,island1)

  edge1 == edge2 // true
  edge2 == edge3 // false

```
So the need to override is equal and hashcode to manually

**Edge**
</br>

```kotlin

  override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            javaClass != other?.javaClass -> false
            else -> {
                other as Edge
                when {
                    (islandX == other.islandX) && (islandY == other.islandY) -> true
                    (islandX == other.islandY) && (islandY == other.islandX) -> true
                    else -> false
                }
            }
        }
    }

    override fun hashCode(): Int {
        var result = islandX.hashCode() + islandY.hashCode()
        result *= 31
        return result
    }

```

**Archepelago**
</br>

```kotlin

override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            javaClass != other?.javaClass -> return false
            else -> {
                other as Archipelago
                when {
                    edge1 == other.edge1 && edge2 == other.edge2 -> true
                    edge1 == other.edge2 && edge2 == other.edge1 -> true
                    else -> false
                }
            }
        }
    }

    override fun hashCode(): Int {
        var result = edge1.hashCode() + edge2.hashCode()
        result *= 31
        return result
    }

```

### Graph

The problem see as that

![image](https://user-images.githubusercontent.com/46488277/193413559-cf6ea5d6-5c8b-4bdb-98c1-645c8db0df3c.png)


So, take a one Island and calculate all Lines with the distance vs all points.In this case take a Point Initial A

![image](https://user-images.githubusercontent.com/46488277/193413575-b43af384-0d66-4483-87e9-46af4ec87b66.png)

Now agrup by all lines that has 2 o more distance equals, in this case has 2 groups

![image](https://user-images.githubusercontent.com/46488277/193413648-d29f0eb6-bf49-4f85-ac8e-4ee23605810a.png)
![image](https://user-images.githubusercontent.com/46488277/193413675-e37dffcf-1058-4ce2-8a0b-bc0c8a3a8dfa.png)


### First case 

The first case is simple, is has only 2 lines with the same length so, has only one Archepelago
![image](https://user-images.githubusercontent.com/46488277/193413989-05d23365-6858-44cf-9d25-01b914e9a139.png)

### Second Case

If has one group that has 3 or more so, need calculate all combinations
</br>
![image](https://user-images.githubusercontent.com/46488277/193414205-7f94e86a-8fb7-45d4-a0f3-910ccd7f980c.png)
![image](https://user-images.githubusercontent.com/46488277/193414267-9b685304-6ec7-4a48-b499-828662eb9e88.png)
![image](https://user-images.githubusercontent.com/46488277/193414305-b9414abb-88fd-459e-8895-18edbdffbb33.png)

In code see

```kotlin

fun calculateArchipelagos(
        island: Island,
        listIsland: List<Island>
    ): List<Archipelago> {
        val listArch = mutableSetOf<Archipelago>()
        listIsland.asSequence().mapNotNull {
            if (island != it) Edge(island, it) else null
        }.groupBy { it.distance }.asSequence().filter { it.value.size > 1 }.forEach { (_, listEdges) ->
            // * if only has 2 edges with same distance so, add new Archipelago
            if (listEdges.size == 2) {
                listArch.add(Archipelago(listEdges[0], listEdges[1]))
            } else {
                // * else calculate all combinations
                listEdges.forEach { edge1 ->
                    listEdges.forEach { edge2 ->
                        if (edge1 != edge2) {
                            listArch.add(Archipelago(edge1, edge2))
                        }
                    }
                }
            }
        }

        return listArch.toList()
    }

```

When calculate all Archipelagos only add in list, but as is for ech in all Island, has a repeat combinations, for that, use a **set**.

### Set

This set is a data structure that does not allow repeatable elements, this is useful when all the islands are created, the problem does not say that there are no repeated elements

```kotlin
 fun calculateListIslands(
        listCoordinates: List<String>
    ): List<Island> {
        return listCoordinates.map { line ->
            val coordinates = line.split(" ")
            Island(coordinates[0].toInt(), coordinates[1].toInt())
        }.toSet().toList()
    }

```

Or when add all archepelagos

``` kotlin
 fun initCalculateArchipelagos(
        listCoordinates: List<String>
    ): List<Archipelago> {
        // * calculate island and remove duplicates
        val listIslands = calculateListIslands(listCoordinates)

        val listArchipelago = mutableSetOf<Archipelago>()
        listIslands.forEach { island ->
            listArchipelago.addAll(calculateArchipelagos(island, listIslands))
        }


        return listArchipelago.toList()
    }

```

### And this is all

It took me a while to visualize the problem graphically, but this example can help,six equidistant points

![image](https://user-images.githubusercontent.com/46488277/193417082-fc644f24-30dd-4b0a-a21b-b2dae0983b19.png)

**first 5 five archipelagos**
</br>
![image](https://user-images.githubusercontent.com/46488277/193417128-be7562e3-0719-4091-b3f5-0d830f669b00.png)

</br>

**second 5 five archipelagos**
</br>
![image](https://user-images.githubusercontent.com/46488277/193417153-977403f3-01a5-4115-a594-558259dc1408.png)

</br>

**third 5 archipelagos**
</br>

![image](https://user-images.githubusercontent.com/46488277/193417197-de9fff71-40f8-4869-9cd6-5d171b6b3a5d.png)

</br>

**last 5 archipelagos**
</br>

![image](https://user-images.githubusercontent.com/46488277/193417239-ceea99e7-013b-41ef-a23a-67a34f60454b.png)

</br>

So has **20 archipelagos** in total

## App

Build a Android app for resolver this.Using Jetpack compose, MVVM, and coroutines for performance app

## Screenshots
### Splash
<p>
  <img src="https://i.imgur.com/JXZSC0v.png" alt="splash" width="200"/>
</p>

### Home
<p>
  <img src="https://i.imgur.com/iIyGD3v.png" alt="select file" width="200"/>
  <img src="https://i.imgur.com/CjcPhIm.png" alt="select file system" width="200"/>
  <img src="https://i.imgur.com/hd7Gv3s.png" alt="calculate" width="200"/>
  <img src="https://i.imgur.com/JMfsAbh.png" alt="rersult file" width="200"/>
</p>
