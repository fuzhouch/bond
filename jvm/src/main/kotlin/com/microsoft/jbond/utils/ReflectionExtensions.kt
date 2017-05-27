// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

@file:JvmName("ReflectionExtensions")

package com.microsoft.jbond.utils

import com.microsoft.jbond.annotations.BondGeneratedCode
import com.microsoft.jbond.exceptions.UnsupportedBondTypeException
import com.microsoft.jbond.protocols.TaggedProtocolReader
import com.microsoft.jbond.types.*
import java.lang.reflect.Field
import java.nio.charset.Charset

/** A function to check if given class is a generated bond class.
 *  @return True if cls is Bond generated, and False if not.
 */
fun Class<*>.isBondGenerated() : Boolean {
    return this.getAnnotation(BondGeneratedCode::class.java) != null
}

fun Field.createTaggedFieldReader(charset: Charset): (TaggedProtocolReader) -> Any {
    val fieldType = this.type
    return when (fieldType) {
        Boolean::class.java -> { reader -> reader.readBool() }
        Byte::class.java -> { reader -> reader.readInt8() }
        Short::class.java -> { reader -> reader.readInt16() }
        Int::class.java -> { reader -> reader.readInt32() }
        Long::class.java -> { reader -> reader.readInt64() }
        UnsignedByte::class.java -> { reader -> reader.readUInt8() }
        UnsignedShort::class.java -> { reader -> reader.readUInt16() }
        UnsignedInt::class.java -> { reader -> reader.readUInt32() }
        UnsignedLong::class.java -> { reader -> reader.readUInt64() }
        ByteString::class.java -> { reader -> reader.readByteString(charset) }
        String::class.java -> { reader -> reader.readUTF16String() }
        else -> throw UnsupportedBondTypeException(fieldType.toString())
    }
}