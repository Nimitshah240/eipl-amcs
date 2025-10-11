package com.eipl.amcs.master.org.dto;

import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Dock;

import java.util.List;
import java.util.stream.Collectors;


public class DockMilkTypeDto {
    private Dock dock;
    private List<MilkType> milkTypes;

    public DockMilkTypeDto() {
    }

    public DockMilkTypeDto(Dock dock, List<MilkType> milkTypes) {
        super();
        this.dock = dock;
        this.milkTypes = milkTypes;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public List<MilkType> getMilkTypes() {
        return milkTypes;
    }

    public void setMilkTypes(List<MilkType> milkTypes) {
        this.milkTypes = milkTypes;
    }

    public String getMilkTypesAsString() {
        if (this.getMilkTypes() == null || this.getMilkTypes().isEmpty())
            return null;
        return this.getMilkTypes().stream().map(m -> m.getName()).collect(Collectors.joining(", "));
    }
}
