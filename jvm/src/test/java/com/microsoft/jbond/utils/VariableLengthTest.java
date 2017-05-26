// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

public class VariableLengthTest {
    final static short UINT16_FULL_LENGTH = 21845; // 0x5555 = b(0101010101010101)
    final static byte[] UINT16_FULL_LENGTH_BYTES = new byte[VariableLength.MAX_VAR_UINT16_BYTES];

    final static short UINT16_ZERO = 0;
    final static byte[] UINT16_ZERO_BYTES = new byte[1];
    @BeforeClass
    public static void createTestIntegers() {
        // Convert UInt16 to VarUInt16 bytes, in little endian:
        //   Raw value    = 0b 01010101 01010101
        //   Add flag bit -> 0b "0"01 "1"0101010 "1"1010101
        //                -> 0x99 0xc5 0x01
        UINT16_FULL_LENGTH_BYTES[0] = (byte)0xd5;
        UINT16_FULL_LENGTH_BYTES[1] = (byte)0xaa;
        UINT16_FULL_LENGTH_BYTES[2] = 0x1;

        UINT16_ZERO_BYTES[0] = 0;
    }

    @Test
    public void testValidDecodeVarUInt() {
        try {
            int decoded;
            decoded = VariableLength.decodeVarUInt16(new ByteArrayInputStream(UINT16_FULL_LENGTH_BYTES));
            Assert.assertEquals(decoded, UINT16_FULL_LENGTH);

            decoded = VariableLength.decodeVarUInt16(new ByteArrayInputStream(UINT16_ZERO_BYTES));
            Assert.assertEquals(decoded, UINT16_ZERO);
        } catch (IOException e) {
            Assert.fail("UnexpectedIOExceptionFromTest");
        }
    }
}
