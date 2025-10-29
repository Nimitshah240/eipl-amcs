package com.eipl.amcs.auth.dto;

import com.eipl.amcs.auth.model.Permission;

import java.util.Comparator;

public class PermissionComparator implements Comparator<Permission> {
    @Override
    public int compare(Permission o1, Permission o2) {
        if (o1.getObject() == o2.getObject())
            return 0;
        else if (o1.getObject() > o2.getObject())
            return 1;
        else
            return -1;
    }
}
