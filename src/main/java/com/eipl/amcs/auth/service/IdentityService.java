package com.eipl.amcs.auth.service;

import com.eipl.amcs.auth.dto.IdentityDto;

public interface IdentityService {

    IdentityDto fetchIdentity(String dockNumber, String societyCode, String unionCode);

}
