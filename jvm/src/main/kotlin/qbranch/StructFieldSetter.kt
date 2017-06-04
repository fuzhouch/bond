// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package qbranch

import qbranch.protocols.TaggedProtocolReader
import java.lang.reflect.Field
import java.nio.charset.Charset

interface StructFieldSetter {
    fun set(obj: Any, reader: TaggedProtocolReader)
}

sealed class BondTypeFieldSetter(structField: Field) : StructFieldSetter {
    val field = structField

    class Bool(field: Field) : BondTypeFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readBool())
    }

    class Int8(field: Field) : BondTypeFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readInt8())
    }

    class Int16(field: Field) : BondTypeFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readInt16())
    }

    class Int32(field: Field) : BondTypeFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readInt32())
    }

    class Int64(field: Field) : BondTypeFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readInt64())
    }

    class UInt8(field: Field) : BondTypeFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readUInt8())
    }

    class UInt16(field: Field) : BondTypeFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readUInt16())
    }

    class UInt32(field: Field) : BondTypeFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readUInt32())
    }

    class UInt64(field: Field) : BondTypeFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readUInt64())
    }

    class ByteString(inputCharset: Charset, field: Field) : BondTypeFieldSetter(field) {
        val charset = inputCharset
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readByteString(charset))
    }

    class UTF16LEString(field: Field) : BondTypeFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readUTF16LEString())
    }

    class Float(field: Field) : BondTypeFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readFloat())
    }

    class Double(field: Field) : BondTypeFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readDouble())
    }
}