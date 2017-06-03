package com.microsoft.jbond.utils

import org.junit.Assert
import org.junit.Test
import java.util.HashMap

class ReflectionUtilsInlineFunctionTest {
    @Test
    fun testExtractGenericTypeArguments() {
        val args = extractGenericTypeArguments<HashMap<String, Int>>()
        Assert.assertTrue(args.size == 2)
        Assert.assertEquals(args[0], String::class.java)
        Assert.assertEquals(args[1], Integer::class.java)
    }

    @Test(expected=UnsupportedOperationException::class)
    fun testThrowExceptionOnNonGenericType() {
        val args = extractGenericTypeArguments<String>()
    }
}