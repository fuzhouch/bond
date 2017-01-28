// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.bond.types

/**
 * Represent an unsigned byte value.
 *
 * @param value Given byte value.
 */
class UnsignedByte(val value : Byte = 0) {
    init {
        if (value < 0) {
            throw IllegalArgumentException("NegativeArgToUnsignedByte")
        }
    }
}

/**
 * Represent an unsigned short value.
 *
 * @param value Given byte value.
 */
class UnsignedShort(val value : Short = 0) {
    init {
        if (value < 0) {
            throw IllegalArgumentException("NegativeArgToUnsignedShort")
        }
    }
}

/**
 * Represent an unsigned integer value.
 *
 * @param value Given byte value.
 */
class UnsignedInt(val value : Int = 0) {
    init {
        if (value < 0) {
            throw IllegalArgumentException("NegativeArgToUnsignedInt")
        }
    }
}

/**
 * Represent an unsigned long value.
 *
 * @param value Given byte value.
 */
class UnsignedLong(val value : Long = 0) {
    init {
        if (value < 0) {
            throw IllegalArgumentException("NegativeArgToUnsignedLong")
        }
    }
}

