package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "committee")
@Getter
@Setter
public class Committee implements JsonAndTableBuilder {

    @Id
    private String code;
    private String name;
    private String nameLocal;
    private LocalDate electionDate;
    private LocalDate formationDate;
    private String year;

    @OneToMany(mappedBy = "committee", fetch = FetchType.LAZY)
    private List<CommitteeMembers> members;

}