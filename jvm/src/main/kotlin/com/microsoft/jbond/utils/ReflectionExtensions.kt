// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

@file:JvmName("ReflectionExtensions")

package com.microsoft.jbond.utils

import com.microsoft.jbond.annotations.BondGeneratedCode
import com.microsoft.jbond.annotations.BondStruct
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

/** A function to check if given class is a generated bond class.
 *  @return True if cls is Bond generated, and False if not.
 */
fun Class<*>.isBondGeneratedStruct() : Boolean {
    val isGenerated = this.getAnnotation(BondGeneratedCode::class.java) != null
    val isStruct = this.getAnnotation(BondStruct::class.java) != null
    return isGenerated && isStruct
}

/**
 * Tell whether a given field has a generic type.
 */
fun Field.isGenericType() : Boolean {
    val declaredType = this.genericType
    val realType = this.type
    return (declaredType !is ParameterizedType) && !declaredType.equals(realType)
}