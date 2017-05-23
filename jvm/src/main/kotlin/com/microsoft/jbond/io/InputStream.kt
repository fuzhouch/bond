package com.microsoft.jbond.io

import com.microsoft.jbond.types.UnsignedByte
import com.microsoft.jbond.types.UnsignedInt
import com.microsoft.jbond.types.UnsignedLong
import com.microsoft.jbond.types.UnsignedShort
import java.nio.charset.Charset

/**
 * Reads primitive data types as binary values in a specific encoding
 */
interface InputStream {
    val length: Int
    var position: Int

    fun readUInt8(): UnsignedByte
    fun readUInt16(): UnsignedShort
    fun readUInt32(): UnsignedInt
    fun readUInt64(): UnsignedLong
    fun readFloat(): Float
    fun readDouble(): Double
    fun readBytes(count: Int) : List<Byte>
    fun skipBytes(count: Int): Unit
    fun readVarUInt16(): UnsignedShort
    fun readVarUInt32(): UnsignedInt
    fun readvarUInt64(): UnsignedLong
    fun readString(charset: Charset, size: Int): String
}