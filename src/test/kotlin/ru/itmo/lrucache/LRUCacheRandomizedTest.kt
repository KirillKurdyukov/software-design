package ru.itmo.lrucache

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.LinkedHashMap

class LRUCacheRandomizedTest {

    @Test
    fun randomizedTest() {
        val random = Random(1)

        for (sizeLruCache in 5..30 step 5) {
            val lruCache = LRUCache<Int, Int>(sizeLruCache)
            val testLRUCache = LRUCacheTest(sizeLruCache)

            println("Testing with size lru cache: $sizeLruCache...")

            for (iteration in 0 until  100_000) {
                if (random.nextBoolean()) {
                    val key = random.nextInt(0, 10)
                    val value = random.nextInt(0, 10)

                    lruCache.put(key, value)
                    testLRUCache[key] = value
                } else {
                    val key = random.nextInt(0, 10)

                    assertEquals(testLRUCache[key], lruCache.get(key))
                }
            }
        }
    }

    private class LRUCacheTest(
        private val capacity: Int
    ) : LinkedHashMap<Int?, Int?>(
        capacity,
        0.75f,
        true
    ) {
        override fun removeEldestEntry(eldest: Map.Entry<Int?, Int?>): Boolean {
            return size > capacity
        }
    }
}