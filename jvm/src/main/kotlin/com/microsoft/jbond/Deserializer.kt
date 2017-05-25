// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond

import com.microsoft.jbond.protocols.TaggedProtocolReader
import com.microsoft.jbond.annotations.BondFieldId
import com.microsoft.jbond.exceptions.UnsupportedBondTypeException
import com.microsoft.jbond.types.*
import java.lang.reflect.Field
import java.util.TreeMap

internal data class FieldSetterInfo<T>(val field: Field, val readValueOp: (TaggedProtocolReader) -> Any) {
    fun setFieldValue(obj: T, reader: TaggedProtocolReader) = field.set(obj, readValueOp(reader))
}

internal class TaggedClassDeserializer<T>(klass: Class<T>) {
    // TODO: Haven't supported inheritance yet.
    val cls = klass
    private val fieldsMap = TreeMap<Int, FieldSetterInfo<T>>()

    init {
        if (!isBondGenerated(cls)) {
            throw UnsupportedBondTypeException(cls.name)
        }

        cls.declaredFields.forEach {
            val fieldId = it.getDeclaredAnnotation(BondFieldId::class.java).id
            val fieldType = it.type
            it.isAccessible = true
            fieldsMap.put(fieldId, FieldSetterInfo(it, createFieldGetter(fieldType)))
        }
        // TODO: Add support for cls.superclass.declaredFields later.
    }

    fun deserialize(obj: T, reader: TaggedProtocolReader): T {
        fieldsMap.forEach { it.value.setFieldValue(obj, reader) }
        return obj
    }

    private fun createFieldGetter(fieldType: Class<*>): (TaggedProtocolReader) -> Any {
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
            ByteString::class.java -> { reader -> reader.readByteString() }
            String::class.java -> { reader -> reader.readUnicodeString() }
            else -> throw UnsupportedBondTypeException(fieldType.toString())
        }
    }
}

/**
 * Deserialize objects of given type.
 */
class Deserializer<T>(klass: Class<T>) {
    val cls = klass
    private val tagged = TaggedClassDeserializer<T>(cls)
    // TODO Support untagged deserizlizer later.

    fun deserialize(reader: TaggedProtocolReader): T = tagged.deserialize(cls.newInstance(), reader)
}