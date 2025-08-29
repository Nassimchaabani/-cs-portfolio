// BinaryTree.kt – BST with insert, search, delete, traversals

class Node(var value: Int) {
    var left: Node? = null
    var right: Node? = null
}

class BinaryTree {
    var root: Node? = null

    fun insert(x: Int) { root = insertRec(root, x) }
    private fun insertRec(n: Node?, x: Int): Node {
        n ?: return Node(x)
        if (x < n.value) n.left = insertRec(n.left, x)
        else if (x > n.value) n.right = insertRec(n.right, x)
        return n
    }

    fun search(x: Int): Boolean = searchRec(root, x)
    private fun searchRec(n: Node?, x: Int): Boolean =
        when {
            n == null -> false
            x == n.value -> true
            x < n.value -> searchRec(n.left, x)
            else -> searchRec(n.right, x)
        }

    fun delete(x: Int) { root = deleteRec(root, x) }
    private fun deleteRec(n: Node?, x: Int): Node? {
        n ?: return null
        when {
            x < n.value -> n.left = deleteRec(n.left, x)
            x > n.value -> n.right = deleteRec(n.right, x)
            else -> {
                if (n.left == null) return n.right
                if (n.right == null) return n.left
                val min = minNode(n.right!!)
                n.value = min.value
                n.right = deleteRec(n.right, min.value)
            }
        }
        return n
    }
    private fun minNode(n: Node): Node {
        var cur = n
        while (cur.left != null) cur = cur.left!!
        return cur
    }

    fun inorder()  { inorderRec(root);  println() }
    fun preorder() { preorderRec(root); println() }
    fun postorder(){ postorderRec(root);println() }

    private fun inorderRec(n: Node?) { if (n!=null){ inorderRec(n.left);  print("${n.value} "); inorderRec(n.right) } }
    private fun preorderRec(n: Node?){ if (n!=null){ print("${n.value} "); preorderRec(n.left); preorderRec(n.right) } }
    private fun postorderRec(n: Node?){ if (n!=null){ postorderRec(n.left); postorderRec(n.right); print("${n.value} ") } }
}

fun main() {
    val t = BinaryTree()
    listOf(50,30,70,20,40,60,80).forEach { t.insert(it) }
    println("Inorder:");  t.inorder()
    println("Search 40 -> ${t.search(40)}")
    t.delete(30)
    println("After delete 30 (inorder):")
    t.inorder()
}
