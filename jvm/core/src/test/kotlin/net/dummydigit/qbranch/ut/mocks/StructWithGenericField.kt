// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.ut.mocks

import net.dummydigit.qbranch.annotations.BondFieldId
import net.dummydigit.qbranch.annotations.BondGeneratedCode
import net.dummydigit.qbranch.generic.ObjectCreatorAsAny
import net.dummydigit.qbranch.generic.cast

@BondGeneratedCode("gbc", "version.mock")
open class StructWithGenericField<T : Any>(typeArgs : Array<ObjectCreatorAsAny>) {
    @BondFieldId(0) var genericField : T = cast(typeArgs[0].newInstanceAsAny())
    @BondFieldId(1) var intField : Int = 0
    @BondFieldId(2) var containerField : List<T> = listOf()
}
