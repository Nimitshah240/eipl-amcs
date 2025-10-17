package com.eipl.amcs.operation.administartion.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;

import java.time.LocalDate;

public class MeetingAgenda extends BaseModelTxn {
    private String code;
    private String detailedAgenda;
    private String meetingTime;
    private String subjectLine;
    private LocalDate date;
    private LocalDate meetingDate;
    private Union union;
    private short meetingType;
    private Society society;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public short getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(short meetingType) {
        this.meetingType = meetingType;
    }

    public String getDetailedAgenda() {
        return detailedAgenda;
    }

    public void setDetailedAgenda(String detailedAgenda) {
        this.detailedAgenda = detailedAgenda;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getSubjectLine() {
        return subjectLine;
    }

    public void setSubjectLine(String subjectLine) {
        this.subjectLine = subjectLine;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }
}

