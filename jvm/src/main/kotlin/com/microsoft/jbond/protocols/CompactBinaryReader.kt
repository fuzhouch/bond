package com.microsoft.jbond.protocols

import com.microsoft.jbond.types.*
import java.io.InputStream

/** Reader to process CompactBinary protocols (both v1 and v2)
 */
class CompactBinaryReader(input : InputStream) : TaggedProtocolReader {
    override fun readBool() : Boolean { return false }
    override fun readInt8() : Byte { return 0 }
    override fun readInt16() : Short { return 0 }
    override fun readInt32() : Int { return 0 }
    override fun readInt64() : Long { return 0 }
    override fun readUInt8() : UnsignedByte { return UnsignedByte() }
    override fun readUInt16() : UnsignedShort { return UnsignedShort() }
    override fun readUInt32() : UnsignedInt { return UnsignedInt() }
    override fun readUInt64() : UnsignedLong { return UnsignedLong() }
    override fun readByteString() : ByteString { return ByteString() }
    override fun readUnicodeString() : String { return "" }
}