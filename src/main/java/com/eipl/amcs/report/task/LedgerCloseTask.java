package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.report.dto.LedgerClose;
import javafx.concurrent.Task;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LedgerCloseTask extends Task<List<LedgerClose>> {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String locale;
    private String societyCode;


    public LedgerCloseTask(String societyCode, LocalDate fromDate, LocalDate toDate, String locale) {
        this.societyCode = societyCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.locale = locale;
    }

    public LedgerCloseTask() {

    }

    @Override
    protected List<LedgerClose> call() throws Exception {
        try {
            LedgerRepository ledgerRepository = EmcsAppContext.getContext().getBean(LedgerRepository.class);
            List<LedgerClose> listResp = new ArrayList<>();
            Date fromDt = Date.valueOf(fromDate);
            Date toDt = Date.valueOf(toDate);
            List<Object[]> list = ledgerRepository.fetchLedgerClosing(societyCode, fromDt, toDt, locale);
            if (list != null && !list.isEmpty()) {
                for (Object[] arr : list) {
                    LedgerClose bal = new LedgerClose((String) arr[0], (String) arr[1], (((BigDecimal) arr[2]).doubleValue() < 0 ? false : true), ((BigDecimal) arr[2]).doubleValue());
                    listResp.add(bal);
                }
            }
            return listResp;


//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_CLOSE;
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("societyCode", societyCode)
//                    .queryParam("fromDate", fromDate.toString())
//                    .queryParam("toDate", toDate.toString())
//                    .queryParam("locale", MainApp.locale);
//            ResponseEntity<LedgerClose[]> response = restTemplate.getForEntity(builder.toUriString(), LedgerClose[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return Arrays.asList(response.getBody());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
