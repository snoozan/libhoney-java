package io.honeycomb;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;

public class LibHoneyTest {
    LibHoney libhoney;

    @Before
    public void setUp() throws Exception {
        libhoney = new LibHoney.Builder()
                .writeKey("499e56a6f613dc79e68afd742153750e")
                .dataSet("ds")
                .sampleRate(3)
                .maxConcurrentBranches(5)
                .blockOnSend(true)
                .blockOnResponse(true)
                .build();
    }

    @Test
    public void testConstructor() throws Exception {
        assertEquals("499e56a6f613dc79e68afd742153750e", libhoney.getWriteKey());
        assertEquals("ds", libhoney.getDataSet());
        assertEquals(3, libhoney.getSampleRate());
        assertEquals(5, libhoney.getMaxConcurrentBranches());
        assertEquals(true, libhoney.getBlockOnResponse());
    }

    @Test
    public void testClose() throws Exception {
        // Fields for FieldBuilder
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("foo", "bar");

        // Dynamic fields for FieldBuilder
        HashMap<String, Callable> dynFields = new HashMap<>();
        Callable randomUUID = () -> UUID.randomUUID().toString();
        dynFields.put("exampleDynField", randomUUID);

        // Create a FieldBuilder
        FieldBuilder fieldBuilder = libhoney.createFieldBuilder()
                .fields(fields)
                .dynFields(dynFields)
                .sampleRate(1)
                .build();

        // Send an event, verify running
        fieldBuilder.createEvent().send();
        assertEquals(false, libhoney.getTransmission().isShutdown());

        // Close, verify not running
        libhoney.close();
        assertEquals(true, libhoney.getTransmission().isShutdown());
    }
}