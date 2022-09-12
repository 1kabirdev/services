package com.example.services

import junit.framework.Assert.assertEquals
import org.junit.Test

internal class SimpleTest {

    private val testSimple: Simple = Simple()

    @Test
    fun testSum() {
        val expected = 42
        assertEquals(expected, testSimple.sum(40, 2))
    }

    @Test
    fun testList() {
        val list = arrayListOf("Kabir", "Agamirzaev", "21")
        assertEquals(list, testSimple.getLists())
    }

    @Test
    fun testData() {
        val f = 5.25f
        assertEquals(f, testSimple.getData(10.5f, 2f))
    }
}