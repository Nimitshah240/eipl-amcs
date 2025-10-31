package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.account.model.TaxDetail;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class TaxDetailSerialize extends JsonSerializer<TaxDetail> {
    @Override
    public void serialize(TaxDetail taxDetail, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(taxDetail.getCode());
    }
}
