// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond

import com.microsoft.jbond.protocols.TaggedProtocolReader
import com.microsoft.jbond.annotations.BondFieldId
import java.util.TreeMap

/**
 * Deserialize objects of type
 */
class Deserializer<T>(klass: Class<T>) {
    val cls = klass
    val fieldsMap = TreeMap<Int, String>()

    private fun generateDeserializer(): Unit {
        cls.declaredFields.forEach {
            val fieldId = it.getDeclaredAnnotation(BondFieldId::class.java).id
            // TODO
            fieldsMap.put(fieldId, "TODO")
        }
    }

    fun deserialize(taggedReader: TaggedProtocolReader): T {
        generateDeserializer()
        return cls.newInstance()
    }
}