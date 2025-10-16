package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.model.MemberBillTransaction;
import com.eipl.amcs.operation.billing.service.MemberBillService;
import javafx.concurrent.Task;

import java.util.List;

public class MemberBillTransactionSaveTask extends Task<List<MemberBillTransaction>> {

    private final List<MemberBillTransaction> dto;

    public MemberBillTransactionSaveTask(List<MemberBillTransaction> dto) {
        this.dto = dto;
    }


    @Override
    protected List<MemberBillTransaction> call() throws Exception {
        try {
            MemberBillService service = EmcsAppContext.getContext().getBean(MemberBillService.class);
            List<MemberBillTransaction> memberBillTxnList = service.saveTrans(dto);

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_BILLING+"/savetrans";
//            ResponseEntity<MemberBillTransaction[]> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), MemberBillTransaction[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
//                return null;
//            return Arrays.asList(response.getBody());
            if (memberBillTxnList == null || memberBillTxnList.isEmpty()) return null;
            return memberBillTxnList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
