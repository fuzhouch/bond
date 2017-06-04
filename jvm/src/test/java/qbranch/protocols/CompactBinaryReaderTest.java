// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package qbranch.protocols;

import qbranch.Deserializer;
import qbranch.exceptions.*;
import qbranch.mocks.AllPrimitiveTypes;
import qbranch.mocks.InvalidBondGenerated;
import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Test;
import qbranch.Deserializer;
import qbranch.exceptions.UnsupportedBondTypeException;
import qbranch.exceptions.UnsupportedVersionException;
import qbranch.mocks.AllPrimitiveTypes;
import qbranch.mocks.InvalidBondGenerated;

import javax.xml.bind.DatatypeConverter;

public class CompactBinaryReaderTest {
    @Test
    public void testReaderWorkflow() {
        AllPrimitiveTypes testDef = new AllPrimitiveTypes();
        byte[] data = DatatypeConverter.parseBase64Binary("TestData");
        ByteArrayInputStream inputBuffer = new ByteArrayInputStream(data);
        CompactBinaryReader reader = new CompactBinaryReader(inputBuffer);
        Deserializer deserializer = new Deserializer<AllPrimitiveTypes>(AllPrimitiveTypes.class);
        try {
            deserializer.deserialize(reader);
        } catch (Exception e) {
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);
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
