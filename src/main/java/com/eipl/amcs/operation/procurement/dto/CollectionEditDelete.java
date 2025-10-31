package com.eipl.amcs.operation.procurement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CollectionEditDelete {
    private final LocalDateTime collectionDate;
    private final String operation;
}
