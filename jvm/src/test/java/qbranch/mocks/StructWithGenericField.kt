// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package qbranch.mocks

import qbranch.annotations.BondFieldId
import qbranch.annotations.BondGeneratedCode
import qbranch.annotations.BondStruct
import qbranch.types.*

@BondStruct
@BondGeneratedCode("gbc", "version.mock")
open class StructWithGenericField<T : Any> {
    @BondFieldId(0) var genericField : T? = null
    @BondFieldId(1) var intField : Int = 0
    @BondFieldId(2) var containerField : List<T> = listOf()
}