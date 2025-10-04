package com.eipl.amcs.master.operation.convertor;

import com.eipl.amcs.master.operation.model.Member;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class MemberReportConvertor extends StringConverter<Member> {

    private final ComboBox<Member> cbox;

    public MemberReportConvertor(ComboBox<Member> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Member member) {
        if (member == null)
            return null;
        return member.toMemberNameWithExCode();
    }

    @Override
    public Member fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toMemberNameWithExCode())).findAny().orElse(null);
    }
}
