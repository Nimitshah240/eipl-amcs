package com.eipl.amcs.base.service;

import com.eipl.amcs.base.Identity;
import com.eipl.amcs.base.repository.IdentityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class IdentityDetailsServiceImpl implements IdentityDetailsService {

    @Autowired
    private IdentityRepository identityRepository;


    private static final Logger log = LoggerFactory.getLogger(IdentityDetailsServiceImpl.class);

    @Override
    public List<Identity> findAll() {
        List<Identity> list = identityRepository.findAll();
        log.info("Identitys findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public Identity save(Identity identity, String identityInfo) {
        identity.setCode("1");
        return identityRepository.save(identity);
    }

    @Override
    public Identity update(Identity identity, String identityInfo) {
        identity.setCode("1");
        return identityRepository.save(identity);
    }

    @Override
    public Optional<Identity> findById(String identityNo) {
        return identityRepository.findById(identityNo);
    }

    @Override
    public void delete(String identityNo, String identityInfo) {
        identityRepository.deleteById(identityNo);
    }

    @Override
    public void delete(Identity identity, String identityInfo) {
        identityRepository.delete(identity);
    }


}