

object Search {
    fun linear(a: List<Int>, x: Int): Int =
        a.indexOf(x)

    fun binary(a: List<Int>, x: Int): Int {
        var l = 0; var r = a.lastIndex
        while (l <= r) {
            val m = (l + r) ushr 1
            when {
                a[m] == x -> return m
                a[m] < x  -> l = m + 1
                else      -> r = m - 1
            }
        }
        return -1
    }
}

object Sort {
    fun bubble(a: MutableList<Int>) {
        for (i in 0 until a.size)
            for (j in 0 until a.size - i - 1)
                if (a[j] > a[j + 1]) a[j] = a[j + 1].also { a[j + 1] = a[j] }
    }

    fun insertion(a: MutableList<Int>) {
        for (i in 1 until a.size) {
            val key = a[i]
            var j = i - 1
            while (j >= 0 && a[j] > key) { a[j + 1] = a[j]; j-- }
            a[j + 1] = key
        }
    }

    fun quick(a: MutableList<Int>, l: Int = 0, r: Int = a.lastIndex) {
        if (l >= r) return
        var i = l; var j = r
        val pivot = a[(l + r) ushr 1]
        while (i <= j) {
            while (a[i] < pivot) i++
            while (a[j] > pivot) j--
            if (i <= j) { val t = a[i]; a[i] = a[j]; a[j] = t; i++; j-- }
        }
        if (l < j) quick(a, l, j)
        if (i < r) quick(a, i, r)
    }
}

fun main() {
    val data = mutableListOf(7, 2, 9, 4, 1, 8, 3)
    println("Original: $data")
    val a1 = data.toMutableList(); Sort.bubble(a1); println("Bubble:   $a1")
    val a2 = data.toMutableList(); Sort.insertion(a2); println("Insertion:$a2")
    val a3 = data.toMutableList(); Sort.quick(a3); println("Quick:    $a3")

    val sorted = a3
    println("Linear search 4 -> ${Search.linear(sorted, 4)}")
    println("Binary search 4 -> ${Search.binary(sorted, 4)}")
}
