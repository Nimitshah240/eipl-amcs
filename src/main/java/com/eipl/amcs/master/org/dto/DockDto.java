package com.eipl.amcs.master.org.dto;

import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Dock;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class DockDto implements Serializable {
    private Dock dock;
    private List<MilkType> milkTypes;

    public DockDto() {

    }

    public DockDto(Dock dock, List<MilkType> milkTypes) {
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

}
