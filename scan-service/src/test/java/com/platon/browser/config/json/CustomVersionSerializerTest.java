package com.platon.browser.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.platon.browser.config.json.CustomVersionSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomVersionSerializerTest {

    @Mock
    private JsonGenerator generator;
    @Mock
    private SerializerProvider serializers;
    @Spy
    private CustomVersionSerializer target;

    @Test
    public void test() throws IOException {
        target.serialize("888",generator,serializers);
        target.serialize(null,generator,serializers);
        verify(target, times(2)).serialize(any(),any(),any());
    }
}
