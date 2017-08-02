// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.ut.mocks

import net.dummydigit.qbranch.annotations.BondFieldId
import net.dummydigit.qbranch.annotations.BondGeneratedCode
import net.dummydigit.qbranch.types.*

// A mock class to test primitive types
@BondGeneratedCode("gbc", "version.mock")
open class AllPrimitiveTypes {
    @BondFieldId(0) var fieldByte : Byte = 1
    @BondFieldId(1) var fieldShort : Short = 2
    @BondFieldId(2) var fieldInt : Int = 3
    @BondFieldId(3) var fieldLong : Long = 4L

    @BondFieldId(4) var fieldUnsignedByte : UnsignedByte = UnsignedByte()
    @BondFieldId(5) var fieldUnsignedShort : UnsignedShort = UnsignedShort()
    @BondFieldId(6) var fieldUnsignedInt : UnsignedInt = UnsignedInt()
    @BondFieldId(7) var fieldUnsignedLong : UnsignedLong = UnsignedLong()

    @BondFieldId(8) var fieldByteString : ByteString = ByteString()
    @BondFieldId(9) var fieldString : String = ""
}
