package qbranch.utils.generic;

import java.sql.Struct;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Test;
import qbranch.exceptions.UnsupportedBondTypeException;
import qbranch.mocks.AllPrimitiveTypes;
import qbranch.mocks.StructWithGenericField;

public class JObjectCreatorTest {
    @Test(expected=UnsupportedBondTypeException.class)
    public void testThrowExceptionOnNonBondGenerated() {
        ObjectCreatorAsConcreteType<LinkedList> creator = ObjectCreator.build(LinkedList.class);
    }

    @Test(expected=UnsupportedBondTypeException.class)
    public void testThrowExceptionWhenPassingGenericClassToNonGenericBuilder() {
        ObjectCreatorAsConcreteType<StructWithGenericField> creator =
                ObjectCreator.build(StructWithGenericField.class);
    }

    @Test(expected=UnsupportedBondTypeException.class)
    public void testThrowExceptionWhenPassingNonGenericClassToGenericBuilder() {
        ObjectCreatorAsConcreteType<AllPrimitiveTypes> creator =
                ObjectCreator.build(AllPrimitiveTypes.class,
                        ObjectCreator.toJCreators(new Class[] { JObjectCreatorTest.class }));
    }

    @Test
    public void testCreateConcreteTypeCreators() {
        ObjectCreatorAsConcreteType<AllPrimitiveTypes> creator = ObjectCreator.build(AllPrimitiveTypes.class);
        AllPrimitiveTypes obj = creator.newInstance();
        Assert.assertEquals(1, obj.getFieldByte());
        Assert.assertEquals(2, obj.getFieldShort());
        Assert.assertEquals(3, obj.getFieldInt());
        Assert.assertEquals(4L, obj.getFieldLong());
    }

    @Test
    public void testCreateGenericTypeCreators() {
        ObjectCreatorAsConcreteType<StructWithGenericField> creator =
                ObjectCreator.build(StructWithGenericField.class,
                        ObjectCreator.toJCreatorsV(AllPrimitiveTypes.class));
        StructWithGenericField<AllPrimitiveTypes> genericObj = creator.newInstance();

        AllPrimitiveTypes obj = genericObj.getGenericField();
        Assert.assertEquals(1, obj.getFieldByte());
        Assert.assertEquals(2, obj.getFieldShort());
        Assert.assertEquals(3, obj.getFieldInt());
        Assert.assertEquals(4L, obj.getFieldLong());
    }
}
