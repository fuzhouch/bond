// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package qbranch.protocols;

import qbranch.Deserializer;
import qbranch.mocks.AllPrimitiveTypes;
import qbranch.mocks.InvalidBondGenerated;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.lang.instrument.Instrumentation;

import org.junit.Assert;
import org.junit.Test;
import qbranch.exceptions.UnsupportedBondTypeException;
import qbranch.exceptions.UnsupportedVersionException;
import qbranch.ut.IntStruct;

import javax.xml.bind.DatatypeConverter;

public class CompactBinaryReaderTest {
    @Test
    public void testReaderWorkflow() {
        ClassLoader loader = getClass().getClassLoader();
        String base64EncodedContent = null;
        try {
            File testIdlEncodedContent = new File(loader.getResource("intstruct_fullvalue.txt").getFile());
            BufferedReader b = new BufferedReader(new FileReader(testIdlEncodedContent));
            base64EncodedContent = b.readLine();
            b.close();
        } catch (Exception e) {
            Assert.assertTrue("FileInResourcesNotFound", false);
        }
        Assert.assertTrue(base64EncodedContent != null);
        Assert.assertTrue(base64EncodedContent.length() > 0);

        byte[] data = DatatypeConverter.parseBase64Binary(base64EncodedContent);
        ByteArrayInputStream inputBuffer = new ByteArrayInputStream(data);
        CompactBinaryReader reader = new CompactBinaryReader(inputBuffer);
        Deserializer<IntStruct> deserializer = new Deserializer<IntStruct>(IntStruct.class);
        IntStruct intStruct = deserializer.deserialize(reader);
        Assert.assertEquals(intStruct.getInt8value(), 0x01);
        Assert.assertEquals(intStruct.getInt16value(), 0x0101);
        Assert.assertEquals(intStruct.getInt32value(), 0x01010101);
        // Assert.assertEquals(intStruct.getInt64value(), 0x0101010101010101L);
    }

    @Test(expected=UnsupportedBondTypeException.class)
    public void testThrowExceptionOnBadClass() throws UnsupportedBondTypeException {
        Deserializer deserializer = new Deserializer<InvalidBondGenerated>(InvalidBondGenerated.class);
    }

    @Test(expected=UnsupportedVersionException.class)
    public void testThrowExceptionOnBadProtocolVersion() throws UnsupportedVersionException {
        byte[] data = DatatypeConverter.parseBase64Binary("TestData");
        ByteArrayInputStream inputBuffer = new ByteArrayInputStream(data);
        CompactBinaryReader reader = new CompactBinaryReader(inputBuffer, 2);
    }
}
