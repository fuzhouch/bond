// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.mocks

import com.microsoft.jbond.annotations.BondFieldId
import com.microsoft.jbond.annotations.BondGeneratedCode
import com.microsoft.jbond.annotations.BondStruct
import com.microsoft.jbond.types.*

@BondStruct
@BondGeneratedCode("gbc", "version.mock")
open class StructWithGenericField<T : Any> {
    @BondFieldId(0) var genericField : T? = null
    @BondFieldId(1) var intField : Int = 0
    @BondFieldId(2) var containerField : List<T> = listOf()
}