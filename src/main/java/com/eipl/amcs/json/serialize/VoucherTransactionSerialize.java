package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class VoucherTransactionSerialize extends JsonSerializer<VoucherTransaction> {
    @Override
    public void serialize(VoucherTransaction voucherTransaction, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(voucherTransaction.getCode());
    }
}
