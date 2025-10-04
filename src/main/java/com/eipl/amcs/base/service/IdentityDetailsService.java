package com.eipl.amcs.base.service;

import com.eipl.amcs.base.Identity;

import java.util.List;
import java.util.Optional;

public interface IdentityDetailsService {
    List<Identity> findAll();

    Identity save(Identity identity, String identityInfo);

    Identity update(Identity identity, String identityInfo);

    Optional<Identity> findById(String identityNo);

    void delete(String identityNo, String identityInfo);

    void delete(Identity identity, String identityInfo);


}
