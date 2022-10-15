package ru.itmo.lrucache

class LRUCache<K : Any, V : Any>(
    private val capacity: Int,
) {
    init {
        check(capacity >= 0) { "Not correct initial capacity." }
    }

    private var size = 0

    private var tail: Node<K, V>
    private var head: Node<K, V>

    init {
        val dummy = Node<K, V>()
        tail= dummy
        head = dummy
    }

    private val linkOnNode: HashMap<K, Node<K, V>> = HashMap()

    fun get(key: K): V? = linkOnNode[key]
        ?.also { moveToTail(it) }
        ?.value

    fun put(key: K, value: V) {
        if (linkOnNode.containsKey(key)) {
            val node = linkOnNode[key]!!
            node.value = value

            moveToTail(node)
        } else {
            add(key, value)
        }

        if (size > capacity) {
            remove()
        }

        assert(size >= 0) { "Correct size balance." }
    }

    private fun moveToTail(node: Node<K, V>) {
        val prev = node.prev
        val next = node.next

        prev?.next = next ?: return
        next.prev = prev

        node.next = null

        addNode(node)
    }

    private fun add(key: K, value: V) {
        val node = Node(key, value)
        linkOnNode[key] = node

        addNode(node)

        size++
    }

    private fun addNode(node: Node<K, V>) {
        tail.next = node
        node.prev = tail

        assert(node.next == null) { "Not null as the node is added to the end of the linked list." }
        tail = node
    }

    private fun remove() {
        assert(head.next != null && head != tail) { "Can't be deleted without adding to the element." }
        val curHead = head.next!!

        linkOnNode.remove(curHead.key)

        head = curHead
        curHead.prev = null

        size--
    }

    private open class Node<K, V>(
        val key: K? = null,
        var value: V? = null,
        var prev: Node<K, V>? = null,
        var next: Node<K, V>? = null,
    )
}
