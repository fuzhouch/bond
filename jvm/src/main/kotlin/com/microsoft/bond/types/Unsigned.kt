// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.bond.types

// NOTE
// As we know, JVM does not define unsigned types. We can't do
//
//
// The implementation tries to address the problem by two steps:
//   1. Apply a marker class to explicitly mark unsigned types.
//   2. Assign a `value' field with larger room to contain given input.

/**
 * Represent an unsigned byte value.
 *
 * @param value Given byte value.
 */
class UnsignedByte(val value : Short = 0) {
    init {
        if (value < 0 || value > 0xFF) {
            throw IllegalArgumentException("NegativeArgToUnsignedByte")
        }
    }
}

/**
 * Represent an unsigned short value.
 *
 * @param value Given byte value.
 */
class UnsignedShort(val value : Int = 0) {
    init {
        if (value < 0 || value > 0xFFFF) {
            throw IllegalArgumentException("NegativeArgToUnsignedShort")
        }
    }
}

/**
 * Represent an unsigned integer value.
 *
 * @param value Given byte value.
 */
class UnsignedInt(val value : Long = 0) {
    init {
        if (value < 0 || value > 0xFFFFFFFF) {
            throw IllegalArgumentException("NegativeArgToUnsignedInt")
        }
    }
}

/**
 * Represent an unsigned long value.
 *
 * @param value Given byte value.
 */
class UnsignedLong(val value : Double = 0.0) {
    init {
        if (value < 0 || value > 18446744073709551615.0 /* 0xFFFFFFFFFFFFFFFF */) {
            throw IllegalArgumentException("NegativeArgToUnsignedLong")
        }
    }
}

