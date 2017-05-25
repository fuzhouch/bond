// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.protocols;

import com.microsoft.jbond.Deserializer;
import com.microsoft.jbond.exceptions.*;
import com.microsoft.jbond.mocks.AllPrimitiveTypes;
import com.microsoft.jbond.mocks.InvalidBondGenerated;
import java.io.ByteArrayInputStream;
import org.junit.Test;
import javax.xml.bind.DatatypeConverter;

public class CompactBinaryReaderTest {
    @Test
    public void testReaderWorkflow() {
        AllPrimitiveTypes testDef = new AllPrimitiveTypes();
        byte[] data = DatatypeConverter.parseBase64Binary("TestData");
        ByteArrayInputStream inputBuffer = new ByteArrayInputStream(data);
        CompactBinaryReader reader = new CompactBinaryReader(inputBuffer);
        Deserializer<AllPrimitiveTypes> deserializer = new Deserializer(AllPrimitiveTypes.class);
        deserializer.deserialize(reader);
    }

    @Test(expected=UnsupportedBondTypeException.class)
    public void testThrowExceptionOnBadClass() throws UnsupportedBondTypeException {
        Deserializer<InvalidBondGenerated> deserializer = new Deserializer(InvalidBondGenerated.class);
        byte[] data = DatatypeConverter.parseBase64Binary("TestData");
        ByteArrayInputStream inputBuffer = new ByteArrayInputStream(data);
        CompactBinaryReader reader = new CompactBinaryReader(inputBuffer);
        deserializer.deserialize(reader);
    }
}
