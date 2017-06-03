// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

@file:JvmName("ReflectionExtensions")

package com.microsoft.jbond.utils

import com.microsoft.jbond.annotations.BondGeneratedCode
import com.microsoft.jbond.annotations.BondStruct
import com.microsoft.jbond.exceptions.UnsupportedBondTypeException
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

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


/**
 * A helper class to retrieve type arguments of given type.
 */
abstract class TypeReference<T> : Comparable<TypeReference<T>> {
    val type: Type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
    override fun compareTo(other: TypeReference<T>) = 0
}

/**
 * Extract type arguments of generic types. Callable from Kotlin code only.
 * @return An array of type, which represents the list of type arguments.
 */
inline fun <reified T: Any> extractGenericTypeArguments() : Array<Class<*>> {
    // Make use of generic type token to allow we
    val type = object : TypeReference<T>() {}.type
    if (type is ParameterizedType) {
        return type.actualTypeArguments.map { it as Class<*> }.toTypedArray()
    } else {
        throw UnsupportedOperationException("NonParameterizedType:type=${type}")
    }
}