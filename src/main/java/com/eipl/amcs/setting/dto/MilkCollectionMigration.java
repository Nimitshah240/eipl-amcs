package com.eipl.amcs.setting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MilkCollectionMigration {
    private String month;
    private Long count;
}
