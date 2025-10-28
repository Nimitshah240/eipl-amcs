package com.eipl.amcs.serialize;

import com.eipl.amcs.master.account.model.VoucherType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class VoucherTypeSerialize extends JsonSerializer<VoucherType> {
    @Override
    public void serialize(VoucherType voucherType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(voucherType.getCode());
    }
}
