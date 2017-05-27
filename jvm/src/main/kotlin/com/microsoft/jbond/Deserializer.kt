// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond

import com.microsoft.jbond.protocols.TaggedProtocolReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Deserialize objects of given type.
 */
class Deserializer<T>(klass: Class<T>, charset: Charset) {
    constructor(klass: Class<T>) : this(klass, StandardCharsets.UTF_8)

    val cls = klass
    private val tagged = TaggedClassDeserializer<T>(cls, charset)
    // TODO Support untagged deserizlizer later.

    fun deserialize(reader: TaggedProtocolReader): T = tagged.deserialize(cls.newInstance(), reader)
}