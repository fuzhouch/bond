package com.microsoft.jbond

import com.microsoft.jbond.annotations.BondFieldId
import com.microsoft.jbond.exceptions.UnsupportedBondTypeException
import com.microsoft.jbond.protocols.TaggedProtocolReader
import com.microsoft.jbond.types.*
import com.microsoft.jbond.utils.DeserializerInfo
import com.microsoft.jbond.utils.isBondGeneratedStruct
import java.lang.reflect.Field
import java.nio.charset.Charset
import java.util.*

/**
 * Implementation that return deserialized struct as Object.
 */
internal class StructDeserializer(klass : Class<*>, stringCharset : Charset, includeBase : Boolean) {
    val baseClassDeserializerChain = LinkedList<DeserializerInfo>()
    val fieldDeserializerMap = TreeMap<Int, (TaggedProtocolReader, Any) -> Unit>()
    val cls = klass
    val charset = stringCharset

    init {
        buildDeserializerChain(klass, includeBase)
    }

    fun deserialize(reader: TaggedProtocolReader): Any {
        val deserializedObj = cls.newInstance()
        // TODO
        return deserializedObj
    }

    private fun buildDeserializerChain(klass : Class<*>, includeBase: Boolean) {
        buildFieldDeserializer(klass)
        if (includeBase) {
            var parent = klass.superclass
            while (!parent.equals(java.lang.Object::class.java)) {
                val baseDeserializer = StructDeserializer(parent, charset, false)
                baseClassDeserializerChain.addFirst(DeserializerInfo(parent, baseDeserializer))
                parent = parent.superclass
            }
        }
    }

    private fun buildFieldDeserializer(klass : Class<*>) : Unit {
        if (!klass.isBondGeneratedStruct()) {
            throw UnsupportedBondTypeException(klass.name)
        }

        // For each field, create its deserializer
        klass.declaredFields.forEach {
            val field = it
            val fieldId = it.getDeclaredAnnotation(BondFieldId::class.java).id
            val fieldClass = it.type
            field.isAccessible = true
            val fieldDeserializer = if (fieldClass.isBondGeneratedStruct()) {
                { reader, obj -> field.set(obj, StructDeserializer(fieldClass, charset, false).deserialize(reader)) }
            } else {
                createFieldSetter(field, charset)
            }
            fieldDeserializerMap[fieldId] = fieldDeserializer
        }
    }

    fun createFieldSetter(field: Field, charset: Charset): (TaggedProtocolReader, Any) -> Unit {
        val fieldType = field.type
        return when (fieldType) {
            Boolean::class.java -> { reader, obj -> field.set(obj, reader.readBool()) }
            Byte::class.java -> { reader, obj -> field.set(obj, reader.readInt8()) }
            Short::class.java -> { reader, obj -> field.set(obj, reader.readInt16()) }
            Int::class.java -> { reader, obj -> field.set(obj, reader.readInt32()) }
            Long::class.java -> { reader, obj -> field.set(obj, reader.readInt64()) }
            UnsignedByte::class.java -> { reader, obj -> field.set(obj, reader.readUInt8()) }
            UnsignedShort::class.java -> { reader, obj -> field.set(obj, reader.readUInt16()) }
            UnsignedInt::class.java -> { reader, obj -> field.set(obj, reader.readUInt32()) }
            UnsignedLong::class.java -> { reader, obj -> field.set(obj, reader.readUInt64()) }
            ByteString::class.java -> { reader, obj -> field.set(obj, reader.readByteString(charset)) }
            String::class.java -> { reader, obj -> field.set(obj, reader.readUTF16LEString()) }
            // TODO: Container support comes later.
            else -> throw UnsupportedBondTypeException(fieldType.toString())
        }
    }
}