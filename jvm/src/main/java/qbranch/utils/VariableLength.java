// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package qbranch.utils;

import qbranch.types.UnsignedInt;
import qbranch.types.UnsignedLong;
import qbranch.types.UnsignedShort;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.BitSet;

// By May 2017 Kotlin does not support bit operations yet.
// We have to implement bit ops related functions in Java.

/**
 * Variable int encoder/decoder helper functions.
 */
public class VariableLength {

    public static final int MAX_VAR_UINT16_BYTES = 3;
    public static final int MAX_VAR_UINT32_BYTES = 5;
    public static final int MAX_VAR_UINT64_BYTES = 10;

    public static Boolean isHighBitZero(byte value) {
        return (value & 0x80) == 0;
    }

    /**
     * Decode uint16 from input stream, returned as UnsignedShort.
     * @param input Byte stream input.
     * @return An UnsignedShort object as return value.
     * @throws IOException
     */
    public static UnsignedShort decodeVarUInt16(InputStream input) throws IOException {
        int value = 0, shift = 0, readBytes = 0;
        byte oneByte;
        do {
            oneByte = (byte)input.read();
            value |= ((oneByte & 0x7F) << shift);
            shift += 7;
            readBytes += 1;
        } while (!isHighBitZero((oneByte)) && readBytes < MAX_VAR_UINT16_BYTES);
        return new UnsignedShort(value);
    }

    /**
     * Decode uint32 from intput stream, returned as UnsignedInt
     * @param input Byte stream input.
     * @return An UnsignedInt object as returned value.
     * @throws IOException
     */
    public static UnsignedInt decodeVarUInt32(InputStream input) throws IOException {
        long value = 0;
        int shift = 0, readBytes = 0;
        byte oneByte;
        do {
            oneByte = (byte)input.read();
            value |= ((oneByte & 0x7F) << shift);
            shift += 7;
            readBytes += 1;
        } while (!isHighBitZero((oneByte)) && readBytes < MAX_VAR_UINT32_BYTES);
        return new UnsignedInt(value);
    }

    /**
     * Decode uint64 from intput stream, returned as UnsignedLong
     * @param input Byte stream input.
     * @return An UnsignedLong object as returned value.
     * @throws IOException
     */
    public static UnsignedLong decodeVarUInt64(InputStream input) throws IOException {
        BitSet payload = new BitSet(MAX_VAR_UINT64_BYTES);
        byte[] payloadAsBitSet = decodeVarUInt64(input, payload).toByteArray();
        return new UnsignedLong(new BigInteger(payloadAsBitSet));
    }

    /**
     * Decode uint64 from input stream, returned as Bitset.
     * @param input Byte stream input.
     * @param payload A Bitset object as return value. Must call .clear() by caller.
     * @return Reference of passed payload parameter.
     * @throws IOException
     */
    public static BitSet decodeVarUInt64(InputStream input, BitSet payload) throws IOException {
        int shift = 0, readBytes = 0;
        byte oneByte;
        do {
            oneByte = (byte)input.read();
            int perBytePayload = oneByte & 0x7F;
            for (int i = 0; i < 7; ++i) {
                if (((perBytePayload >> i) & 0x01) == 1) {
                    payload.set(shift + i);
                }
            }
            shift += 7;
            readBytes += 1;
        } while (!isHighBitZero((oneByte)) && readBytes < MAX_VAR_UINT64_BYTES);
        return payload;
    }
}
