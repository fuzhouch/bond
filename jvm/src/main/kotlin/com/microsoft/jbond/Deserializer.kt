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

internal data class FieldSetterInfo<T>(val field: Field, val setter: (T, Field) -> Unit) {
    fun set(obj: T, value: Object) {
        field.isAccessible = true
        field.set(obj, value)
    }
}

internal class TaggedClassDeserializer<T>(klass: Class<T>, reader: TaggedProtocolReader) {
    // TODO: Haven't supported inheritance yet.
    val cls = klass
    val protocolReader = reader
    val fieldsMap = TreeMap<Int, FieldSetterInfo<T>>()

    init {
        if (!isBondGenerated(cls)) {
            throw UnsupportedBondTypeException(cls.name)
        }

        cls.declaredFields.forEach {
            val fieldId = it.getDeclaredAnnotation(BondFieldId::class.java).id
            val fieldType = it.type
            fieldsMap.put(fieldId, FieldSetterInfo(it, createFieldSetter(fieldType)))
        }
        // TODO: Add support for cls.superclass.declaredFields later.
    }

    private fun createFieldSetter(fieldType: Class<*>): (T, Field) -> Unit {
        return when (fieldType) {
            Boolean::class.java -> { obj, field -> field.set(obj, protocolReader.readBool()) }
            Byte::class.java -> { obj, field -> field.set(obj, protocolReader.readInt8()) }
            Short::class.java -> { obj, field -> field.set(obj, protocolReader.readInt16()) }
            Int::class.java -> { obj, field -> field.set(obj, protocolReader.readInt32()) }
            Long::class.java -> { obj, field -> field.set(obj, protocolReader.readInt64()) }
            UnsignedByte::class.java -> { obj, field -> field.set(obj, protocolReader.readUInt8()) }
            UnsignedShort::class.java -> { obj, field -> field.set(obj, protocolReader.readUInt16()) }
            UnsignedInt::class.java -> { obj, field -> field.set(obj, protocolReader.readUInt32()) }
            UnsignedLong::class.java -> { obj, field -> field.set(obj, protocolReader.readUInt64()) }
            ByteString::class.java -> { obj, field -> field.set(obj, protocolReader.readByteString()) }
            String::class.java -> { obj, field -> field.set(obj, protocolReader.readUnicodeString()) }
            else -> throw UnsupportedBondTypeException(fieldType.toString())
        }
    }

    fun deserialize(obj: T): T {
        fieldsMap.forEach {
            it.value.field.isAccessible = true
            it.value.setter(obj, it.value.field)
        }
        return obj
    }
}

/**
 * Deserialize objects of given type
 */
class Deserializer<T>(klass: Class<T>) {
    // TODO: Haven't supported inheritance yet.
    val cls = klass

    fun deserialize(taggedReader: TaggedProtocolReader): T {
        val deserializer = TaggedClassDeserializer<T>(cls, taggedReader)
        return deserializer.deserialize(cls.newInstance())
    }
}