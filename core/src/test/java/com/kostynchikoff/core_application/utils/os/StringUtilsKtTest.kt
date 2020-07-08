package com.kostynchikoff.core_application.utils.os

import org.junit.Assert.*
import org.junit.Test

class StringUtilsKtTest{


    @Test
    fun `text format amount`(){
        assertEquals("70.00", formatWithZeroAmount("70"))
        assertEquals("70.00", formatWithZeroAmount("70.00"))
        assertEquals("0", formatWithZeroAmount("0"))
        assertEquals("", formatWithZeroAmount(""))

    }
}