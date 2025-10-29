package com.eipl.amcs.json.serialize;

import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class BonusSummarySerialize extends JsonSerializer<BonusSummary> {
    @Override
    public void serialize(BonusSummary bonusSummary, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(bonusSummary.getCode());
    }
}
