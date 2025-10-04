package com.eipl.amcs.operation.procurement.dto;

import java.time.LocalDateTime;

public class CollectionEditDelete {
    private LocalDateTime collectionDate;
    private String operation;

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
