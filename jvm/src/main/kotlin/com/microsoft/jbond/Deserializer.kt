// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond

import com.microsoft.jbond.protocols.TaggedProtocolReader

/**
 * Deserialize objects of given type.
 */
class Deserializer<T>(klass: Class<T>) {
    val cls = klass
    private val tagged = TaggedClassDeserializer<T>(cls)
    // TODO Support untagged deserizlizer later.

    fun deserialize(reader: TaggedProtocolReader): T = tagged.deserialize(cls.newInstance(), reader)
}