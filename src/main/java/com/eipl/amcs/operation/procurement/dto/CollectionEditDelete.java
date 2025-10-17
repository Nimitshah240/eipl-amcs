package com.eipl.amcs.operation.procurement.dto;

import java.time.LocalDateTime;

public class CollectionEditDelete {
    private final LocalDateTime collectionDate;
    private final String operation;

    public CollectionEditDelete(LocalDateTime collectionDate, String operation) {
        this.collectionDate = collectionDate;
        this.operation = operation;
    }

    public LocalDateTime getCollectionDate() {
        return collectionDate;
    }

    public String getOperation() {
        return operation;
    }
}
