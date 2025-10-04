package com.eipl.amcs.sync.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "navigation_book")
public class NavigationBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nav_for")
    private short navFor;

    @Column(name = "flag")
    private short flag;
    private short destinations; // 1-Single, 2-Both, 0-None
    @Column(name = "org_type")
    private String orgType;
    @Column(name = "org_code")
    private String orgCode;
    @Column(name = "source_type")
    private short sourceType; //1-SELF, 2-PARENT, 3-CHILD
    @Column(name = "table_name")
    private String tableName;
    @Column(name = "to_parent")
    private short toParent; //1-To Parent, 0-NA
    @Column(name = "to_child")
    private short toChild; //1- To Child, 0-NA
}
