package com.eipl.amcs.master.operation.bootcontroller;

import com.eipl.amcs.master.operation.model.MemberEkyc;
import com.eipl.amcs.master.operation.repository.MemberEkycRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member-ekyc")
public class MemberEkycController {

    @Autowired
    private  MemberEkycRepository memberEkycRepository;

    @GetMapping("/all")
    public List<MemberEkyc> getMemberEkyc() {
        return memberEkycRepository.findAllWithMembers();
    }

}
