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


