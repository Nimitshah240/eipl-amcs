package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.service.ShareDividendService;
import javafx.concurrent.Task;

import java.time.LocalDate;

public class ShareDividendAllDeleteTask extends Task<Boolean> {
    private final LocalDate fromDt;
    private final LocalDate toDt;

    public ShareDividendAllDeleteTask(LocalDate fromDt, LocalDate toDt) {
        this.fromDt = fromDt;
        this.toDt = toDt;
    }


    @Override
    protected Boolean call() throws Exception {
        try {
            ShareDividendService service = EmcsAppContext.getContext().getBean(ShareDividendService.class);
            service.deleteAll(fromDt, toDt);

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SHARE_DIVIDEND + "/all";
//
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("fromDate",fromDt.toString()).queryParam("toDate",toDt.toString());
//
//            ResponseEntity<ShareDividend[]> response = restTemplate.getForEntity(builder.toUriString(), ShareDividend[].class);
//
////            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class, uriVariables);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}