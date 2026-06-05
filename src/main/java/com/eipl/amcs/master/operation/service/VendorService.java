package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.master.operation.model.Vendor;

import java.util.List;

public interface VendorService {

    List<Vendor> findAllBySociety(String societyCode);
}
