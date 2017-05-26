// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.utils

import com.microsoft.jbond.protocols.TaggedProtocolReader
import java.lang.reflect.Field

/**
 * Access field values
 */
internal data class TaggedStructField<T>(val field: Field, val readValueOp: (TaggedProtocolReader) -> Any) {
    fun setFieldValue(obj: T, reader: TaggedProtocolReader) = field.set(obj, readValueOp(reader))
}