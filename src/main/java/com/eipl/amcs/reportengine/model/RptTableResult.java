package com.eipl.amcs.reportengine.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RptTableResult implements Serializable {
    private Long id;
    private Long reportCode;
    private String respFieldName;
    private String respDispName;
    private Integer respSeq;
    private Integer dispSeq;
    private Boolean visible;

    public RptTableResult() {
        this.selected = new SimpleBooleanProperty(false);
    }

    public RptTableResult(Long id, Long reportCode, String respFieldName, String respDispName, Integer respSeq, Integer dispSeq, Boolean visible) {
        this.id = id;
        this.reportCode = reportCode;
        this.respFieldName = respFieldName;
        this.respDispName = respDispName;
        this.respSeq = respSeq;
        this.dispSeq = dispSeq;
        this.visible = visible;
        this.selected = new SimpleBooleanProperty(this.visible);
    }

    @Transient
    private transient BooleanProperty selected;

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public static List<RptTableResult> buildForMilkCollectionReport(Long reportCode) {
        File file = new File("resources/report_" + reportCode + ".ser");
        if (!file.exists()) {
            return prepareDefault(reportCode);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<RptTableResult> rptTableResults = (List<RptTableResult>) ois.readObject();
            for (RptTableResult rptTableResult : rptTableResults) {
                rptTableResult.selected = new SimpleBooleanProperty(rptTableResult.getVisible());
            }
            return rptTableResults;
        } catch (IOException | ClassNotFoundException e) {
            return prepareDefault(reportCode);
        }
    }

    private static List<RptTableResult> prepareDefault(Long reportCode) {
        List<RptTableResult> resultList = new ArrayList<>();
        resultList.add(new RptTableResult(1L, reportCode, "society_code", "Society Code", 0, 0, false));
        resultList.add(new RptTableResult(2L, reportCode, "society_name", "Society Name", 1, 0, false));
        resultList.add(new RptTableResult(3L, reportCode, "member_code", "Member Code", 2, 1, true));
        resultList.add(new RptTableResult(4L, reportCode, "member_name", "Member Name", 3, 2, true));
        resultList.add(new RptTableResult(5L, reportCode, "period", "Period", 4, 4, false));
        resultList.add(new RptTableResult(6L, reportCode, "cow_qty", "Cow Qty", 5, 5, false));
        resultList.add(new RptTableResult(7L, reportCode, "cow_fat", "Cow FAT", 6, 6, false));
        resultList.add(new RptTableResult(8L, reportCode, "cow_snf", "Cow SNF", 7, 7, false));
        resultList.add(new RptTableResult(9L, reportCode, "cow_amount", "Code Amt", 8, 8, false));
        resultList.add(new RptTableResult(9L, reportCode, "buffalo_qty", "Buff Qty", 9, 9, false));
        resultList.add(new RptTableResult(9L, reportCode, "buffalo_fat", "Buff FAT", 10, 10, false));
        resultList.add(new RptTableResult(9L, reportCode, "buffalo_snf", "Buff SNF", 11, 11, false));
        resultList.add(new RptTableResult(9L, reportCode, "buffalo_amount", "Buff Amt", 12, 12, false));
        resultList.add(new RptTableResult(9L, reportCode, "milk_qty", "Qty", 13, 13, true));
        resultList.add(new RptTableResult(9L, reportCode, "milk_fat", "FAT", 14, 14, true));
        resultList.add(new RptTableResult(9L, reportCode, "milk_snf", "SNF", 15, 15, true));
        resultList.add(new RptTableResult(9L, reportCode, "milk_amount", "Milk Amount", 16, 16, true));
        resultList.add(new RptTableResult(9L, reportCode, "addition_amount", "Add Amount", 17, 17, true));
        resultList.add(new RptTableResult(9L, reportCode, "deduction_amount", "Ded Amount", 18, 18, true));
        resultList.add(new RptTableResult(9L, reportCode, "net_amount", "Net Amount", 19, 18, true));
        resultList.add(new RptTableResult(9L, reportCode, "bank_name", "Bank Name", 20, 20, true));
        resultList.add(new RptTableResult(9L, reportCode, "branch_name", "Branch Name", 21, 21, true));
        resultList.add(new RptTableResult(9L, reportCode, "ifsc", "IFSC", 22, 22, true));
        resultList.add(new RptTableResult(9L, reportCode, "bank_acno", "Bank Ac. No.", 23, 23, true));
        resultList.add(new RptTableResult(9L, reportCode, "mobile_no", "Mobile No", 24, 3, true));
        return resultList;
    }
}
