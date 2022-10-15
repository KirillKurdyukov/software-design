package ru.itmo.lrucache

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LRUCacheSimpleTest {

    @Test
    fun simpleCase() {
        val lruCache = LRUCache<Int, Int>(2)

        lruCache.put(1, 1)
        lruCache.put(2, 2)

        assertEquals(1, lruCache.get(1))
        lruCache.put(3, 3)

        assertEquals(null, lruCache.get(2))
        lruCache.put(4, 4)

        assertEquals(null, lruCache.get(1))

        assertEquals(3, lruCache.get(3))
        assertEquals(4, lruCache.get(4))
    }

    @Test
    fun lruCacheSizeOfOne() {
        val lruCache = LRUCache<Int, Int>(1)

        lruCache.put(1, 1)
        assertEquals(1, lruCache.get(1))

        lruCache.put(2, 2)
        assertEquals(2, lruCache.get(2))

        assertEquals(null, lruCache.get(1))
    }

    @Test
    fun emptyLruCache() {
        val lruCache = LRUCache<Int, Int>(0)

        lruCache.put(1, 1)
        assertEquals(null, lruCache.get(1))

        lruCache.put(2, 2)
        assertEquals(null, lruCache.get(2))
    }

    @Test
    fun testNoCorrectSizeLruCache() {
        assertThrows<IllegalStateException> { LRUCache<Int, Int>(-3) }
    }
}