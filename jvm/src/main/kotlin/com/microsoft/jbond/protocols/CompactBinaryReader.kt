// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.protocols

import com.microsoft.jbond.types.*
import java.io.InputStream

/** Reader to process CompactBinary protocols (both v1 and v2)
 */
class CompactBinaryReader(inputStream : InputStream) : TaggedProtocolReader {
    private val input = inputStream

    override fun readBool() : Boolean = input.read() == 0
    override fun readInt8() : Byte = input.read().toByte()
    override fun readUInt8() : UnsignedByte = UnsignedByte(input.read().toShort())

    override fun readInt16() : Short { return 0 }
    override fun readInt32() : Int { return 0 }
    override fun readInt64() : Long { return 0 }

    override fun readUInt16() : UnsignedShort { return UnsignedShort() }
    override fun readUInt32() : UnsignedInt { return UnsignedInt() }
    override fun readUInt64() : UnsignedLong { return UnsignedLong() }
    override fun readByteString() : ByteString { return ByteString() }
    override fun readUnicodeString() : String { return "" }
}