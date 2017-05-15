package com.microsoft.bond.reflections;

import bond.TypeDef;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RuntimeDecoderBuilderTest {
    @Before
    public void setUp() {
    }

    @Test
    public void testCreateBuilder() {
        RuntimeDecoderBuilder<TypeDef> builder = new RuntimeDecoderBuilder<TypeDef>(TypeDef.class);
        builder.createDecoder();
    }

    @Test(expected=Exception.class)
    public void testCreateBuildWithNonBondType() {
        RuntimeDecoderBuilder<RuntimeDecoderBuilderTest> builder =
                new RuntimeDecoderBuilder<RuntimeDecoderBuilderTest>(RuntimeDecoderBuilderTest.class);
        builder.createDecoder();
    }
}
