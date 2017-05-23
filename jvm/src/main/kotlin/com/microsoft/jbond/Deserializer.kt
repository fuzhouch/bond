// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond

import com.microsoft.jbond.protocols.TaggedProtocolReader
import java.lang.Class

/**
 * Deserialize objects of type
 */
class Deserializer<T>(klass: Class<T>) {
    val cls = klass
    fun deserialize(taggedReader: TaggedProtocolReader): T {
        return cls.newInstance()
    }
}