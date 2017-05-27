// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.types

import kotlin.text.Charsets
import java.nio.charset.Charset

/**
 * Represent a byte string in Bond IDL.
 *
 * @param value Given string value.
 * @param encoding Parse encoding to convert string value to bytes.
 */
class ByteString(val stringValue : String = "",
                 val convertToCharset : Charset = Charsets.UTF_8) {
    constructor(bytes: ByteArray, convertToCharset: Charset = Charsets.UTF_8) : this(String(bytes), convertToCharset)

    val value = stringValue
    val charset = convertToCharset

    fun toByteArrary() : ByteArray {
        return value.toByteArray(charset)
    }
}