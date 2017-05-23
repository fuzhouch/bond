package com.microsoft.jbond.protocols;

import bond.TypeDef;
import com.microsoft.jbond.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import org.junit.Test;
import javax.xml.bind.DatatypeConverter;

public class CompactBinaryReaderTest {

    @Test
    public void testReaderWorkflow() {
        TypeDef testDef = new TypeDef();
        testDef.setBonded_type(false);
        byte[] data = DatatypeConverter.parseBase64Binary("TestData");
        ByteArrayInputStream inputBuffer = new ByteArrayInputStream(data);
        CompactBinaryReader reader = new CompactBinaryReader(inputBuffer);
        Deserializer<TypeDef> deserializer = new Deserializer(TypeDef.class);
        deserializer.deserialize(reader);
    }
}
