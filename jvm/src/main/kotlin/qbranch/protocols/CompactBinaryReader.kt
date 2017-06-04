// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package qbranch.protocols

import qbranch.exceptions.EndOfStreamException
import qbranch.exceptions.UnsupportedVersionException
import qbranch.types.*
import qbranch.utils.CompactBinaryFieldInfo
import qbranch.utils.VariableLength
import qbranch.utils.ZigZag
import java.io.InputStream
import java.nio.charset.Charset

/**
 * Reader to process CompactBinary protocols (v1 only)
 */
class CompactBinaryReader(inputStream : InputStream, version : Int) : TaggedProtocolReader {
    private val input = inputStream

    constructor(inputStream : InputStream) : this(inputStream, 1)

    init {
        if (version != 1) {
            throw UnsupportedVersionException("protocol=CompactBinary:version=$version")
        }
    }

    override fun readBool() : Boolean = input.read() == 0
    override fun readInt8() : Byte = input.read().toByte()
    override fun readUInt8() : UnsignedByte = UnsignedByte(input.read().toShort())
    override fun readInt16() : Short = ZigZag.unsignedToSigned16(readUInt16())
    override fun readUInt16(): UnsignedShort = VariableLength.decodeVarUInt16(input)
    override fun readUInt32(): UnsignedInt = VariableLength.decodeVarUInt32(input)
    override fun readInt32(): Int = ZigZag.unsignedToSigned32(readUInt32())
    override fun readInt64(): Long = ZigZag.unsignedToSigned64(readUInt64())
    override fun readUInt64(): UnsignedLong = VariableLength.decodeVarUInt64(input)
    override fun readByteString(charset: Charset): ByteString {
        val rawBytes = readRawStringBytes(1) ?: return ByteString("", charset)
        return ByteString(rawBytes, charset)
    }
    override fun readUTF16LEString(): String {
        // IMPORTANT: Following C#/Windows convention, we assume
        // we read UTF16 bytes. However, it may not be portable
        // on non-Windows platforms.
        val rawBytes = readRawStringBytes(2) ?: return ""
        return String(rawBytes, Charsets.UTF_16LE)
    }

    override fun readFloat(): Float {
        // TODO
        throw NotImplementedError("float")
    }

    override fun readDouble(): Double {
        // TODO
        throw NotImplementedError("double")
    }

    override fun skipField(): Unit {
        // TODO
        throw NotImplementedError("skipField")
    }

    private fun readRawStringBytes(charLen : Int) : ByteArray? {
        val stringLength = readUInt32().value.toInt()
        if (stringLength == 0) {
            return null
        }

        val rawBytes = ByteArray(stringLength * charLen)
        val readBytes = input.read(rawBytes)
        if (readBytes != stringLength) {
            throw EndOfStreamException(stringLength, readBytes)
        }
        return rawBytes
    }

    override fun parseNextField(): CompactBinaryFieldInfo {
        val fieldInfo = CompactBinaryFieldInfo(input)
        return fieldInfo
    }
}