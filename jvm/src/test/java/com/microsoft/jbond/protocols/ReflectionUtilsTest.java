// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.protocols;

import com.microsoft.jbond.mocks.StructWithGenericField;
import com.microsoft.jbond.utils.ReflectionExtensions;
import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;

public class ReflectionUtilsTest {
    @Test
    public void testFieldGeneric() {
        StructWithGenericField<String> testObj = new StructWithGenericField<String>();
        try {
            Field genericField = testObj.getClass().getDeclaredField("genericField");
            Field intField = testObj.getClass().getDeclaredField("intField");
            Field containerField = testObj.getClass().getDeclaredField("containerField");
            Assert.assertTrue(ReflectionExtensions.isGenericType(genericField));
            Assert.assertFalse(ReflectionExtensions.isGenericType(intField));
            Assert.assertTrue(ReflectionExtensions.isGenericType(containerField));
        } catch(NoSuchFieldException e) {
            Assert.assertFalse("TestFieldNotFound", true);
        }
    }
}
