// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond

import com.microsoft.jbond.annotations.BondFieldId
import com.microsoft.jbond.exceptions.UnsupportedBondTypeException
import com.microsoft.jbond.protocols.TaggedProtocolReader
import com.microsoft.jbond.utils.TaggedStructField
import com.microsoft.jbond.utils.createTaggedFieldReader
import com.microsoft.jbond.utils.isBondGenerated
import java.util.*

internal class TaggedClassDeserializer<T>(klass: Class<T>) {
    // TODO: Haven't supported inheritance yet.
    val cls = klass
    private val fieldsMap = TreeMap<Int, TaggedStructField<T>>()

    init {
        if (!cls.isBondGenerated()) {
            throw UnsupportedBondTypeException(cls.name)
        }

        cls.declaredFields.forEach {
            val fieldId = it.getDeclaredAnnotation(BondFieldId::class.java).id
            it.isAccessible = true
            fieldsMap.put(fieldId, TaggedStructField(it, it.createTaggedFieldReader()))
        }
        // TODO: Add support for cls.superclass.declaredFields later.
    }

    fun deserialize(obj: T, reader: TaggedProtocolReader): T {
        fieldsMap.forEach { it.value.setFieldValue(obj, reader) }
        return obj
    }
}