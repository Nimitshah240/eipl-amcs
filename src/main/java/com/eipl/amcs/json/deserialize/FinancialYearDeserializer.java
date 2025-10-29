package com.eipl.amcs.json.deserialize;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.repository.FinancialYearRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class FinancialYearDeserializer extends JsonDeserializer<FinancialYear> {
    private FinancialYearRepository Repository;

    public FinancialYearDeserializer() {
        Repository = EmcsAppContext.getContext().getBean(FinancialYearRepository.class);
    }

    @Override
    public FinancialYear deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        return Repository.findById(String.valueOf(parser.getValueAsInt())).get();
    }

}
