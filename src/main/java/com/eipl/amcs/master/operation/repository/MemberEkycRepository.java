package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.master.operation.model.MemberEkyc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberEkycRepository extends JpaRepository<MemberEkyc, String> {
    @Query(value = "SELECT  me.code,CONCAT(me.first_name, ' ', COALESCE(me.middle_name, ''), ' ', me.last_name) AS name,me.mobile_no,md.aadhar_no,m.status, me.code AS member_code FROM members  me " +
            "LEFT JOIN member_ekyc m ON me.code = m.member_code " +
            "LEFT JOIN member_details md ON me.code = md.member_code " +
            "LIMIT 500",
            nativeQuery = true)
    List<MemberEkyc> findAllWithMembers();

}


