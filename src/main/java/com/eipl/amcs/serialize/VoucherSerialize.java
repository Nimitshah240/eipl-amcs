package com.eipl.amcs.serialize;

import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.org.model.Bank;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class VoucherSerialize extends JsonSerializer<Voucher> {
    @Override
    public void serialize(Voucher voucher, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(voucher.getCode());
    }
}
