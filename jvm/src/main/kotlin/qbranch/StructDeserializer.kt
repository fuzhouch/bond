// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package qbranch

import bond.BondDataType
import qbranch.annotations.BondFieldId
import qbranch.exceptions.UnsupportedBondTypeException
import qbranch.protocols.TaggedProtocolReader
import qbranch.types.*
import qbranch.utils.DeserializerInfo
import qbranch.utils.isBondGeneratedStruct
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
        baseClassDeserializerChain.forEach {
            val baseClass = it.klass
            val baseDeserializer = it.deserializer
            baseDeserializer.deserializeDeclaredFields(deserializedObj, reader, true)
        }
        deserializeDeclaredFields(deserializedObj, reader, false)
        return deserializedObj
    }

    private fun deserializeDeclaredFields(obj : Any, reader: TaggedProtocolReader, isBase: Boolean) : Unit {
        val casted = cls.cast(obj)
        var fieldInfo = reader.parseNextField()
        val stopSign = if (isBase) { BondDataType.BT_STOP_BASE } else { BondDataType.BT_STOP }

        while (fieldInfo.typeId != stopSign) {
            fieldDeserializerMap[fieldInfo.fieldId]?.invoke(reader, casted)
            fieldInfo = reader.parseNextField()
        }
    }

    private fun buildDeserializerChain(klass : Class<*>, includeBase: Boolean) {
        buildDeclaredFieldDeserializer(klass)
        if (includeBase) {
            var parent = klass.superclass
            while (!parent.equals(java.lang.Object::class.java)) {
                val baseDeserializer = StructDeserializer(parent, charset, false)
                baseClassDeserializerChain.addFirst(DeserializerInfo(parent, baseDeserializer))
                parent = parent.superclass
            }
        }
    }

    private fun buildDeclaredFieldDeserializer(klass : Class<*>) : Unit {
        if (!klass.isBondGeneratedStruct()) {
            throw UnsupportedBondTypeException(klass)
        }

        // For each field, create its deserializer
        klass.declaredFields.forEach {
            val field = it
            val fieldId = it.getDeclaredAnnotation(BondFieldId::class.java).id
            val fieldClass = it.type
            field.isAccessible = true
            val fieldDeserializer = if (fieldClass.isBondGeneratedStruct()) {
                // TODO: Will it be a performance bottleneck?
                { reader, obj -> field.set(obj, StructDeserializer(fieldClass, charset, false).deserialize(reader)) }
            } else {
                createFieldSetter(field, charset)
            }
            fieldDeserializerMap[fieldId] = fieldDeserializer
        }
    }

    private fun createFieldSetter(field: Field, charset: Charset): (TaggedProtocolReader, Any) -> Unit {
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
            else -> throw UnsupportedBondTypeException(fieldType)
        }
    }
}