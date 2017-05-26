// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.utils;

import com.microsoft.jbond.types.UnsignedByte;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by fuzhouch on 5/26/2017.
 */
public class ZigZagTest {
    public static final byte[] INT8_VALUES = new byte[11];
    @BeforeClass
    public static void createTestIntegers() {
        INT8_VALUES[0] = 0;
        INT8_VALUES[1] = -1;
        INT8_VALUES[2] = 1;
        INT8_VALUES[3] = -2;
        INT8_VALUES[4] = 2;
        INT8_VALUES[5] = -3;
        INT8_VALUES[6] = 3;
        INT8_VALUES[7] = -4;
        INT8_VALUES[8] = 4;
        INT8_VALUES[9] = -5;
        INT8_VALUES[10] = 5;
    }

    @Test
    public void testConvertInt8() {
        for (int i = 0; i < INT8_VALUES.length; ++i) {
            byte v = INT8_VALUES[i];
            UnsignedByte u8 = ZigZag.signedToUnsigned8(v);
            byte i8 = ZigZag.unsignedToSigned8(u8);
            Assert.assertEquals(i, u8.getValue());
            Assert.assertEquals(v, i8);
        }
    }
}
