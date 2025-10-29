package com.eipl.amcs.json.serialize;

import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ProductReceiptTransactionSerialize extends JsonSerializer<ProductReceiptTransaction> {
    @Override
    public void serialize(ProductReceiptTransaction productReceiptTransaction, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(productReceiptTransaction.getGrnTxnNo());
    }
}
