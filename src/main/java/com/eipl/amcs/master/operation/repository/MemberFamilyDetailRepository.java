package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.master.operation.model.MemberFamilyDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberFamilyDetailRepository extends JpaRepository<MemberFamilyDetail, String> {

    @EntityGraph(attributePaths = {"relationship", "gender"})
    List<MemberFamilyDetail> findByMember_Code(String code);

}