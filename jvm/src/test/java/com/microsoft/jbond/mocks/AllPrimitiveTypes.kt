// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.mocks

import com.microsoft.jbond.annotations.BondFieldId
import com.microsoft.jbond.annotations.BondGeneratedCode
import com.microsoft.jbond.annotations.BondSchema
import com.microsoft.jbond.types.*

// A mock class to test primitive types
@BondSchema
@BondGeneratedCode("gbc", "version.mock")
open class AllPrimitiveTypes {
    @BondFieldId(0) var fieldByte : Byte = 0
    @BondFieldId(1) var fieldShort : Short = 0
    @BondFieldId(2) var fieldInt : Int = 0
    @BondFieldId(3) var fieldLong : Long = 0L

    @BondFieldId(4) var fieldUnsignedByte : UnsignedByte = UnsignedByte()
    @BondFieldId(5) var fieldUnsignedShort : UnsignedShort = UnsignedShort()
    @BondFieldId(6) var fieldUnsignedInt : UnsignedInt = UnsignedInt()
    @BondFieldId(7) var fieldUnsignedLong : UnsignedLong = UnsignedLong()

    @BondFieldId(8) var fieldByteString : ByteString = ByteString()
    @BondFieldId(9) var fieldString : String = ""
}