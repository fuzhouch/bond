// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

@file:JvmName("ReflectionExtensions")

package com.microsoft.jbond.utils

import com.microsoft.jbond.annotations.BondGeneratedCode
import com.microsoft.jbond.annotations.BondStruct
import com.microsoft.jbond.exceptions.UnsupportedBondTypeException
import com.microsoft.jbond.protocols.TaggedProtocolReader
import com.microsoft.jbond.types.*
import java.lang.reflect.Field
import java.nio.charset.Charset

/** A function to check if given class is a generated bond class.
 *  @return True if cls is Bond generated, and False if not.
 */
fun Class<*>.isBondGenerated() : Boolean {
    val isGenerated = this.getAnnotation(BondGeneratedCode::class.java) != null
    val isStruct = this.getAnnotation(BondStruct::class.java) != null
    return isGenerated && isStruct
}