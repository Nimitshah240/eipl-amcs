package com.eipl.amcs.operation.administartion.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;

import java.time.LocalDate;

public class MomAction extends BaseModelTxn {
    private String code;
    private String action_taken;
    private LocalDate date;
    private short meetingType;
    private Mom mom;
    private MeetingAgenda meetingAgenda;
    private Society society;
    private Union union;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAction_taken() {
        return action_taken;
    }

    public void setAction_taken(String action_taken) {
        this.action_taken = action_taken;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public short getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(short meetingType) {
        this.meetingType = meetingType;
    }

    public Mom getMom() {
        return mom;
    }

    public void setMom(Mom mom) {
        this.mom = mom;
    }

    public MeetingAgenda getMeetingAgenda() {
        return meetingAgenda;
    }

    public void setMeetingAgenda(MeetingAgenda meetingAgenda) {
        this.meetingAgenda = meetingAgenda;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }
}
