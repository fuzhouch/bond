// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package qbranch

import bond.BondDataType
import qbranch.annotations.BondFieldId
import qbranch.exceptions.UnsupportedBondTypeException
import qbranch.protocols.TaggedProtocolReader
import qbranch.utils.isBondGeneratedStruct
import java.lang.reflect.Field
import java.nio.charset.Charset
import java.util.*

/**
 * Implementation that return deserialized struct as Object.
 */
internal class StructDeserializer(klass : Class<*>, stringCharset : Charset) {
    var baseDeserializer : StructDeserializer? = null
    val declaredFieldDeserializerMap = TreeMap<Int /* fieldId */, StructFieldSetter>()
    val cls = klass
    val charset = stringCharset

    init {
        var parent = klass.superclass
        if (!parent.equals(java.lang.Object::class.java)) {
            baseDeserializer = StructDeserializer(parent, charset)
        }
        buildDeclaredFieldDeserializer(klass)
    }

    fun deserialize(preCreatedObj: Any, reader: TaggedProtocolReader, isBase: Boolean): Any {
        val obj = cls.cast(preCreatedObj)
        baseDeserializer?.deserialize(obj, reader, true)
        deserializeDeclaredFields(obj, reader, isBase)
        return obj
    }

    private fun deserializeDeclaredFields(obj : Any, reader: TaggedProtocolReader, isBase: Boolean) : Unit {
        val casted = cls.cast(obj)
        var fieldInfo = reader.parseNextField()
        val stopSign = if (isBase) { BondDataType.BT_STOP_BASE } else { BondDataType.BT_STOP }

        while (fieldInfo.typeId != stopSign) {
            val fieldSetter = declaredFieldDeserializerMap[fieldInfo.fieldId]
            if (fieldSetter != null) {
                fieldSetter.set(casted, reader)
            } else {
                // A field appears in encoded binary but unknown
                // to deserializer. There can be two cases:
                //
                // 1. The binary represents a struct with new version.
                // 2. This is an invalid buffer.
                //
                // We surely won't be able to handle #1
                reader.skipField()
            }
            fieldInfo = reader.parseNextField()
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
            field.isAccessible = true
            val bondTag = BondJavaTypeMapping.builtInTypeToBondTag[field.genericType]
            if (bondTag != null) {
                declaredFieldDeserializerMap[fieldId] = createFieldSetterByBondTag(bondTag, field)
            } else {
                throw UnsupportedBondTypeException(field.type)
            }
        }
    }

    private fun createFieldSetterByBondTag(bondTag: BondDataType, field: Field) : StructFieldSetter {
        return when (bondTag) {
            BondDataType.BT_BOOL -> BondTypeFieldSetter.Bool(field)
            BondDataType.BT_UINT8 -> BondTypeFieldSetter.UInt8(field)
            BondDataType.BT_UINT16 -> BondTypeFieldSetter.UInt16(field)
            BondDataType.BT_UINT32 -> BondTypeFieldSetter.UInt32(field)
            BondDataType.BT_UINT64 -> BondTypeFieldSetter.UInt64(field)
            BondDataType.BT_FLOAT -> BondTypeFieldSetter.Float(field)
            BondDataType.BT_DOUBLE -> BondTypeFieldSetter.Double(field)
            BondDataType.BT_STRING -> BondTypeFieldSetter.ByteString(charset, field)
            BondDataType.BT_INT8 -> BondTypeFieldSetter.UInt8(field)
            BondDataType.BT_INT16 -> BondTypeFieldSetter.UInt16(field)
            BondDataType.BT_INT32 -> BondTypeFieldSetter.UInt32(field)
            BondDataType.BT_INT64 -> BondTypeFieldSetter.UInt64(field)
            BondDataType.BT_WSTRING -> BondTypeFieldSetter.UTF16LEString(field)
            // TODO: Container is more complicated than primitive types.
            // BondDataType.BT_LIST
            // BondDataType.BT_SET,
            // BondDataType.BT_MAP,
            else -> throw UnsupportedBondTypeException(field.type)
        }
    }
}