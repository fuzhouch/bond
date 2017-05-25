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

internal data class FieldSetterInfo<T>(val field: Field, val setter: (T, Field, TaggedProtocolReader) -> Unit)

internal class TaggedClassDeserializer<T>(klass: Class<T>) {
    // TODO: Haven't supported inheritance yet.
    val cls = klass
    val fieldsMap = TreeMap<Int, FieldSetterInfo<T>>()

    init {
        if (!isBondGenerated(cls)) {
            throw UnsupportedBondTypeException(cls.name)
        }

        cls.declaredFields.forEach {
            val fieldId = it.getDeclaredAnnotation(BondFieldId::class.java).id
            val fieldType = it.type
            it.isAccessible = true
            fieldsMap.put(fieldId, FieldSetterInfo(it, createFieldSetter(fieldType)))
        }
        // TODO: Add support for cls.superclass.declaredFields later.
    }

    private fun createFieldSetter(fieldType: Class<*>): (T, Field, TaggedProtocolReader) -> Unit {
        return when (fieldType) {
            Boolean::class.java -> { obj, field, reader -> field.set(obj, reader.readBool()) }
            Byte::class.java -> { obj, field, reader -> field.set(obj, reader.readInt8()) }
            Short::class.java -> { obj, field, reader -> field.set(obj, reader.readInt16()) }
            Int::class.java -> { obj, field, reader -> field.set(obj, reader.readInt32()) }
            Long::class.java -> { obj, field, reader -> field.set(obj, reader.readInt64()) }
            UnsignedByte::class.java -> { obj, field, reader -> field.set(obj, reader.readUInt8()) }
            UnsignedShort::class.java -> { obj, field, reader -> field.set(obj, reader.readUInt16()) }
            UnsignedInt::class.java -> { obj, field, reader -> field.set(obj, reader.readUInt32()) }
            UnsignedLong::class.java -> { obj, field, reader -> field.set(obj, reader.readUInt64()) }
            ByteString::class.java -> { obj, field, reader -> field.set(obj, reader.readByteString()) }
            String::class.java -> { obj, field, reader -> field.set(obj, reader.readUnicodeString()) }
            else -> throw UnsupportedBondTypeException(fieldType.toString())
        }
    }

    fun deserialize(obj: T, reader: TaggedProtocolReader): T {
        fieldsMap.forEach { it.value.setter(obj, it.value.field, reader) }
        return obj
    }
}

/**
 * Deserialize objects of given type
 */
class Deserializer<T>(klass: Class<T>) {
    // TODO: Haven't supported inheritance yet.
    val cls = klass
    private val tagged = TaggedClassDeserializer<T>(cls)
    // TODO Support untagged deserizlizer later.

    fun deserialize(taggedReader: TaggedProtocolReader): T {
        return tagged.deserialize(cls.newInstance(), taggedReader)
    }
}