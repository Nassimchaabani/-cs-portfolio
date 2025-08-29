fun mergeSort(a: MutableList<Int>): MutableList<Int> {
    if (a.size <= 1) return a
    val mid = a.size / 2
    val left = mergeSort(a.subList(0, mid).toMutableList())
    val right = mergeSort(a.subList(mid, a.size).toMutableList())
    return merge(left, right)
}

private fun merge(left: MutableList<Int>, right: MutableList<Int>): MutableList<Int> {
    val res = mutableListOf<Int>()
    var i = 0; var j = 0
    while (i < left.size && j < right.size) {
        if (left[i] <= right[j]) { res.add(left[i]); i++ } else { res.add(right[j]); j++ }
    }
    while (i < left.size) { res.add(left[i]); i++ }
    while (j < right.size) { res.add(right[j]); j++ }
    return res
}

fun main() {
    val a = mutableListOf(38, 27, 43, 3, 9, 82, 10)
    println("Before: $a")
    val sorted = mergeSort(a)
    println("After:  $sorted")
}
