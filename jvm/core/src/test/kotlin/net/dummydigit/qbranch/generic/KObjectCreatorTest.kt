// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

import net.dummydigit.qbranch.exceptions.UnsupportedBondTypeException
import net.dummydigit.qbranch.ut.mocks.AllPrimitiveTypes
import net.dummydigit.qbranch.ut.mocks.InvalidBondGenerated
import net.dummydigit.qbranch.ut.mocks.StructWithGenericField
import org.junit.Assert
import org.junit.Test

class KObjectCreatorTest {
    @Test(expected = UnsupportedBondTypeException::class)
    fun testThrowExceptionOnNonBondGenerated() {
        mkCreator(InvalidBondGenerated::class)
    }

    @Test(expected = UnsupportedBondTypeException::class)
    fun testThrowExceptionWhenPassingGenericClassToNonGenericBuilder() {
        mkCreator(StructWithGenericField::class)
    }

    @Test(expected = UnsupportedBondTypeException::class)
    fun testThrowExceptionWhenPassingNonGenericClassToGenericBuilder() {
        mkCreator(AllPrimitiveTypes::class, toKTypeArgs(listOf(KObjectCreatorTest::class)))
    }

    @Test(expected = UnsupportedBondTypeException::class)
    fun testThrowExceptionWhenPassingNonGenericClassToGenericBuilderArrayList() {
        mkCreator(AllPrimitiveTypes::class, toKTypeArgs(arrayListOf(KObjectCreatorTest::class)))
    }

    @Test(expected = UnsupportedBondTypeException::class)
    fun testThrowExceptionWhenPassingNonGenericClassToGenericBuilderArray() {
        mkCreator(AllPrimitiveTypes::class, toKTypeArgs(arrayOf(KObjectCreatorTest::class)))
    }

    @Test
    fun testCreateConcreteTypeCreators() {
        val creator = mkCreator(AllPrimitiveTypes::class.java)
        val obj = creator.newInstance()
        Assert.assertEquals(1, obj.fieldByte.toLong())
        Assert.assertEquals(2, obj.fieldShort.toLong())
        Assert.assertEquals(3, obj.fieldInt.toLong())
        Assert.assertEquals(4L, obj.fieldLong)
    }

    @Test
    fun testCreateGenericTypeCreators() {
        val creator = mkCreator(StructWithGenericField::class, toKTypeArgsV(AllPrimitiveTypes::class))
        val genericObj : StructWithGenericField<AllPrimitiveTypes> = cast(creator.newInstance())

        val obj = genericObj.genericField
        Assert.assertEquals(1, obj.fieldByte.toLong())
        Assert.assertEquals(2, obj.fieldShort.toLong())
        Assert.assertEquals(3, obj.fieldInt.toLong())
        Assert.assertEquals(4L, obj.fieldLong)
    }
}