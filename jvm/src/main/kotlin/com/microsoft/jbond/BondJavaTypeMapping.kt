package com.microsoft.jbond

import bond.BondDataType
import com.microsoft.jbond.types.*
import com.microsoft.jbond.types.placeholders.*

/**
 * A mapping to allow
 */
object BondJavaTypeMapping {
    val mapping = hashMapOf(
            BondDataType.BT_STOP to StopTag::class.java,
            BondDataType.BT_STOP_BASE to StopBaseTag::class.java,
            BondDataType.BT_BOOL to Boolean::class.java,
            BondDataType.BT_UINT8 to UnsignedByte::class.java,
            BondDataType.BT_UINT16 to UnsignedShort::class.java,
            BondDataType.BT_UINT32 to UnsignedInt::class.java,
            BondDataType.BT_UINT64 to UnsignedLong::class.java,
            BondDataType.BT_FLOAT to Float::class.java,
            BondDataType.BT_DOUBLE to Double::class.java,
            BondDataType.BT_STRING to ByteString::class.java,
            BondDataType.BT_STRUCT to StructTag::class.java,
            BondDataType.BT_LIST to List::class.java,
            BondDataType.BT_SET to Set::class.java,
            BondDataType.BT_MAP to Map::class.java,
            BondDataType.BT_INT8 to Byte::class.java,
            BondDataType.BT_INT16 to Short::class.java,
            BondDataType.BT_INT32 to Int::class.java,
            BondDataType.BT_INT64 to Long::class.java,
            BondDataType.BT_WSTRING to String::class.java,
            BondDataType.BT_UNAVAILABLE to BadTag::class.java
    )
}
