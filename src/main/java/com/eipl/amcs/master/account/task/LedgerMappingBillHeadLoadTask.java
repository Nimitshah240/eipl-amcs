package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.BillHeadMappingDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingBillHead;
import com.eipl.amcs.master.account.service.LedgerMappingBillHeadService;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.service.BillHeadService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LedgerMappingBillHeadLoadTask extends Task<BillHeadMappingDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerMappingBillHeadLoadTask.class);

    @Override
    protected BillHeadMappingDto call() throws Exception {
        try {
            // Bill head
            BillHeadService service = EmcsAppContext.getContext().getBean(BillHeadService.class);
            List<BillHead> billHeadList = service.findAll();
            if (billHeadList == null || billHeadList.isEmpty())
                return null;
//            return billHeadList;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BILLHEAD;
//            ResponseEntity<BillHead[]> response = restTemplate.getForEntity(url, BillHead[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            List<BillHead> billHeadList = new ArrayList<>(Arrays.asList(response.getBody()));

            LedgerService service1 = EmcsAppContext.getContext().getBean(LedgerService.class);
            List<Ledger> ledgerList = service1.findAllByIsActive();
            if (ledgerList == null || ledgerList.isEmpty())
                return null;

            // ledger
//            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER;
//            ResponseEntity<Ledger[]> respLedger = restTemplate.getForEntity(url, Ledger[].class);
//            if (respLedger == null || respLedger.getStatusCode() != HttpStatus.OK)
//                return null;

            LedgerMappingBillHeadService service2 = EmcsAppContext.getContext().getBean(LedgerMappingBillHeadService.class);
            List<LedgerMappingBillHead> ledgerMappingBillHeadList = service2.findAll();
            if (ledgerMappingBillHeadList == null || ledgerMappingBillHeadList.isEmpty())
                return null;


//            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_MAPPING_BILL_HEAD;
//            ResponseEntity<LedgerMappingBillHead[]> respPgLedMap = restTemplate.getForEntity(url, LedgerMappingBillHead[].class);
//            if (respPgLedMap == null || respPgLedMap.getStatusCode() != HttpStatus.OK)
//                return null;
            List<LedgerMappingBillHead> listMapping = new ArrayList<>(ledgerMappingBillHeadList);
            for (LedgerMappingBillHead mp : listMapping) {
                billHeadList.removeIf(p -> p.getCode().equalsIgnoreCase(mp.getBillHead().getCode()));
            }
            for (BillHead billHead : billHeadList) {
                LedgerMappingBillHead mp = new LedgerMappingBillHead();
                mp.setBillHead(billHead);
                listMapping.add(mp);
            }
            ledgerList.add(0, new Ledger("None"));//"0",
            return new BillHeadMappingDto(listMapping, ledgerList);
        } catch (Exception e) {
            LOGGER.error("LedgerMappingBillHead fetch", e);
        }
        return null;
    }
}
