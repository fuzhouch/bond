// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package qbranch.utils;

import qbranch.mocks.StructWithGenericField;
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
            Assert.assertFalse(ReflectionExtensions.isGenericType(containerField));
        } catch(NoSuchFieldException e) {
            Assert.assertFalse("TestFieldNotFound", true);
        }
    }
}
