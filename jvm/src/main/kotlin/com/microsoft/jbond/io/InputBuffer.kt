package com.microsoft.jbond.io

import com.microsoft.jbond.types.UnsignedByte
import com.microsoft.jbond.types.UnsignedInt
import com.microsoft.jbond.types.UnsignedLong
import com.microsoft.jbond.types.UnsignedShort
import java.nio.charset.Charset
import java.time.OffsetDateTime
import java.util.ArrayList

/**
 * Real input buffer as stream
 */
class InputBuffer(data: Array<Byte>, offset: Int, len: Int): InputStream {
    constructor(data: Array<Byte>) : this(data, 0, data.size) {}
    constructor(data: Array<Byte>, length: Int): this(data, 0, length) {}

    private val _data = data
    override val length : Int = len
    override var position: Int = offset

    val end : Int = if (data.size >= length) { length } else { data.size }

    // http://docs.oracle.com/javase/tutorial/java/nutsandbolts/op3.html
    // TODO: Negative byte can't be casted in Kotlin as there's no bitwise operations.
    override fun readUInt8(): UnsignedByte { return UnsignedByte() }

    override fun readUInt16(): UnsignedShort { return UnsignedShort() }
    override fun readUInt32(): UnsignedInt { return UnsignedInt() }
    override fun readUInt64(): UnsignedLong { return UnsignedLong() }
    override fun readFloat(): Float { return 0.0f }
    override fun readDouble(): Double { return 0.0 }
    override fun readBytes(count: Int) : List<Byte> { return ArrayList<Byte>() }

    override fun skipBytes(count: Int): Unit {
        position += count
    }

    override fun readVarUInt16(): UnsignedShort { return UnsignedShort() }
    override fun readVarUInt32(): UnsignedInt { return UnsignedInt() }
    override fun readvarUInt64(): UnsignedLong { return UnsignedLong() }
    override fun readString(charset: Charset, size: Int): String { return "" }
}