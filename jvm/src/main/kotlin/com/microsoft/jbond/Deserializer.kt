// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond

import com.microsoft.jbond.exceptions.UnsupportedBondTypeException
import com.microsoft.jbond.protocols.TaggedProtocolReader
import com.microsoft.jbond.types.isBondGenerated
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Deserialize objects of given type.
 */
class Deserializer<T>(klass: Class<T>, charset: Charset) {
    constructor(klass: Class<T>) : this(klass, StandardCharsets.UTF_8)

    val cls = klass
    private val deserializerImpl = StructDeserializer(klass, charset, true)

    fun deserialize(reader: TaggedProtocolReader): T {
        return cls.cast(deserializerImpl.deserialize(reader))
    }
}