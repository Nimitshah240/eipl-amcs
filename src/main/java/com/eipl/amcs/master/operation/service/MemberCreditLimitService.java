package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.master.operation.model.MemberCreditLimit;

import java.util.Optional;

public interface MemberCreditLimitService {
    Optional<MemberCreditLimit> findByConsumerCodeAndConsumerType(String consumerCode, Short consumerType);
}
