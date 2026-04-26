package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.master.operation.model.MemberCattleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberCattleDetailRepository extends JpaRepository<MemberCattleDetail, String> {

    List<MemberCattleDetail> findByMember_Code(String code);
}