// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package qbranch.protocols;

import qbranch.Deserializer;
import qbranch.mocks.InvalidBondGenerated;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;
import qbranch.exceptions.UnsupportedBondTypeException;
import qbranch.exceptions.UnsupportedVersionException;
import qbranch.ut.PrimitiveStruct;

import javax.xml.bind.DatatypeConverter;

public class CompactBinaryReaderTest {

    private byte[] getEncodedTestPayload(String txtInSource) {
        ClassLoader loader = getClass().getClassLoader();
        String base64EncodedContent = null;
        try {
            File testIdlEncodedContent = new File(loader.getResource(txtInSource).getFile());
            BufferedReader b = new BufferedReader(new FileReader(testIdlEncodedContent));
            base64EncodedContent = b.readLine();
            b.close();
        } catch (Exception e) {
            Assert.assertTrue("FileInResourcesNotFound", false);
        }
        Assert.assertTrue(base64EncodedContent != null);
        Assert.assertTrue(base64EncodedContent.length() > 0);
        return DatatypeConverter.parseBase64Binary(base64EncodedContent);
    }
    @Test
    public void testReaderWorkflow() {
        byte[] data = getEncodedTestPayload("primitive_values.txt");

        ByteArrayInputStream inputBuffer = new ByteArrayInputStream(data);
        CompactBinaryReader reader = new CompactBinaryReader(inputBuffer);
        Deserializer<PrimitiveStruct> deserializer = new Deserializer<>(PrimitiveStruct.class);
        PrimitiveStruct primitiveStruct = deserializer.deserialize(reader);

        Assert.assertEquals(0x01, primitiveStruct.getInt8value());
        Assert.assertEquals(0x0102, primitiveStruct.getInt16value());
        Assert.assertEquals(0x01020304, primitiveStruct.getInt32value());
        Assert.assertEquals(0x0102030405060708L, primitiveStruct.getInt64value());

        Assert.assertEquals((short)0x01, primitiveStruct.getUint8value().getValue());
        Assert.assertEquals(0x0201, primitiveStruct.getUint16value().getValue());
        Assert.assertEquals((long)0x04030201, primitiveStruct.getUint32value().getValue());
        Assert.assertEquals(new BigInteger("0807060504030201", 16), primitiveStruct.getUint64value().getValue());
        Assert.assertEquals("teststring", primitiveStruct.getStringvalue().getValue());
        Assert.assertEquals("testwstring", primitiveStruct.getWstringvalue());

        Assert.assertEquals(123.456, primitiveStruct.getFloatvalue(), 0.00001);
        Assert.assertEquals(654.321, primitiveStruct.getDoublevalue(), 0.00001);
    }

    @Test(expected=UnsupportedBondTypeException.class)
    public void testThrowExceptionOnBadClass() throws UnsupportedBondTypeException {
        Deserializer<InvalidBondGenerated> deserializer = new Deserializer<>(InvalidBondGenerated.class);
    }

    @Test(expected=UnsupportedVersionException.class)
    public void testThrowExceptionOnBadProtocolVersion() throws UnsupportedVersionException {
        byte[] data = DatatypeConverter.parseBase64Binary("TestData");
        ByteArrayInputStream inputBuffer = new ByteArrayInputStream(data);
        CompactBinaryReader reader = new CompactBinaryReader(inputBuffer, 2);
    }
}
