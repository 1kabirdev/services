package com.example.services

import org.junit.Test
import junit.framework.Assert.assertEquals

internal class MyServicesTest {
    private val testServices: MyServices = MyServices()

    @Test
    fun test_my_services() {
        assertEquals("Random number notification", testServices.CHANNEL_ID)
    }
}