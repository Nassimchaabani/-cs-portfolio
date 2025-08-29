// HashTable.kt
// Simple hash table with separate chaining (buckets as mutable lists)

class HashTable<K, V>(private val capacity: Int = 16) {
    private data class Entry<K, V>(val key: K, var value: V)
    private val buckets: Array<MutableList<Entry<K, V>>> =
        Array(capacity) { mutableListOf<Entry<K, V>>() }

    private fun index(key: K) = (key.hashCode() and 0x7fffffff) % capacity

    fun put(key: K, value: V) {
        val b = buckets[index(key)]
        val e = b.firstOrNull { it.key == key }
        if (e == null) b.add(Entry(key, value)) else e.value = value
    }

    fun get(key: K): V? {
        val b = buckets[index(key)]
        return b.firstOrNull { it.key == key }?.value
    }

    fun remove(key: K): Boolean {
        val b = buckets[index(key)]
        val it = b.iterator()
        while (it.hasNext()) {
            if (it.next().key == key) { it.remove(); return true }
        }
        return false
    }

    fun size(): Int = buckets.sumOf { it.size }
    override fun toString(): String =
        buckets.withIndex().joinToString(separator = "\n") { (i, list) ->
            "$i -> " + list.joinToString { "[${it.key}:${it.value}]" }
        }
}

fun main() {
    val table = HashTable<String, Int>(8)
    table.put("apple", 3)
    table.put("banana", 5)
    table.put("orange", 2)
    println("apple -> ${table.get("apple")}")
    println("size = ${table.size()}")
    table.put("apple", 7)      // update
    println("apple -> ${table.get("apple")}")
    table.remove("banana")
    println("After remove banana, size = ${table.size()}")
    println(table)
}
