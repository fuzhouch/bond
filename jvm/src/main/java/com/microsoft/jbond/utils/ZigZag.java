// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.utils;

import com.microsoft.jbond.types.UnsignedByte;

/**
 * Contains util functions for decoding/encoding ZigZag format.
 */
public class ZigZag {
    // Doc REF: https://developers.google.com/protocol-buffers/docs/encoding
    // In other words, each value n is encoded using

    // (n << 1) ^ (n >> 31)
    // for sint32s, or
    // (n << 1) ^ (n >> 63)
    // for the 64-bit version.

    public static byte unsignedToSigned8(UnsignedByte uByte) {
        short value = uByte.getValue();
        return (byte)((value >> 1) ^ (~( value & 1 ) + 1));
    }

    public static UnsignedByte signedToUnsigned8(byte value) {
        return new UnsignedByte((short)((value << 1) ^ (value >> 15)));
    }
}
