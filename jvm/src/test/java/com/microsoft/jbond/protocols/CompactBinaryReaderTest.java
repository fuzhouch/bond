// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.protocols;

import com.microsoft.jbond.Deserializer;
import com.microsoft.jbond.exceptions.*;
import com.microsoft.jbond.mocks.AllPrimitiveTypes;
import com.microsoft.jbond.mocks.InvalidBondGenerated;
import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Test;
import javax.xml.bind.DatatypeConverter;

public class CompactBinaryReaderTest {
    @Test
    public void testReaderWorkflow() {
        AllPrimitiveTypes testDef = new AllPrimitiveTypes();
        byte[] data = DatatypeConverter.parseBase64Binary("TestData");
        ByteArrayInputStream inputBuffer = new ByteArrayInputStream(data);
        CompactBinaryReader reader = new CompactBinaryReader(inputBuffer);
        Deserializer deserializer = new Deserializer<AllPrimitiveTypes>(AllPrimitiveTypes.class);
        deserializer.deserialize(reader);
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
