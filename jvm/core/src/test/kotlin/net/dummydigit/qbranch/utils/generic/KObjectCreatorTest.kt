// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.utils.generic

import net.dummydigit.qbranch.exceptions.UnsupportedBondTypeException
import net.dummydigit.qbranch.ut.mocks.AllPrimitiveTypes
import net.dummydigit.qbranch.ut.mocks.StructWithGenericField
import org.junit.Assert
import org.junit.Test
import java.util.*

class KObjectCreatorTest {
    @Test(expected = UnsupportedBondTypeException::class)
    fun testThrowExceptionOnNonBondGenerated() {
        build(LinkedList::class)
    }

    @Test(expected = UnsupportedBondTypeException::class)
    fun testThrowExceptionWhenPassingGenericClassToNonGenericBuilder() {
        build(StructWithGenericField::class)
    }

    @Test(expected = UnsupportedBondTypeException::class)
    fun testThrowExceptionWhenPassingNonGenericClassToGenericBuilder() {
        build(AllPrimitiveTypes::class, toKCreators(listOf(KObjectCreatorTest::class)))
    }

    @Test(expected = UnsupportedBondTypeException::class)
    fun testThrowExceptionWhenPassingNonGenericClassToGenericBuilderArrayList() {
        build(AllPrimitiveTypes::class, toKCreators(arrayListOf(KObjectCreatorTest::class)))
    }

    @Test(expected = UnsupportedBondTypeException::class)
    fun testThrowExceptionWhenPassingNonGenericClassToGenericBuilderArray() {
        build(AllPrimitiveTypes::class, toKCreators(arrayOf(KObjectCreatorTest::class)))
    }

    @Test
    fun testCreateConcreteTypeCreators() {
        val creator = build(AllPrimitiveTypes::class.java)
        val obj = creator.newInstance()
        Assert.assertEquals(1, obj.fieldByte.toLong())
        Assert.assertEquals(2, obj.fieldShort.toLong())
        Assert.assertEquals(3, obj.fieldInt.toLong())
        Assert.assertEquals(4L, obj.fieldLong)
    }

    @Test
    fun testCreateGenericTypeCreators() {
        val creator = build(StructWithGenericField::class, toKCreatorsV(AllPrimitiveTypes::class))
        val genericObj : StructWithGenericField<AllPrimitiveTypes> = cast(creator.newInstance())

        val obj = genericObj.genericField
        Assert.assertEquals(1, obj.fieldByte.toLong())
        Assert.assertEquals(2, obj.fieldShort.toLong())
        Assert.assertEquals(3, obj.fieldInt.toLong())
        Assert.assertEquals(4L, obj.fieldLong)
    }
}