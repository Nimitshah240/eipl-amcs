package com.eipl.amcs.setting.dto;

public class MilkCollectionMigration {
    private String month;
    private Long count;

    public MilkCollectionMigration() {
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
