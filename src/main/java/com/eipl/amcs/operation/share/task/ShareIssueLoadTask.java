package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.service.ShareService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ShareIssueLoadTask extends Task<List<Share>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShareIssueLoadTask.class);

    private LocalDate fromDate, toDate;

    public ShareIssueLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public ShareIssueLoadTask() {

    }

    @Override
    protected List<Share> call() throws Exception {
        try {
            ShareService service = EmcsAppContext.getContext().getBean(ShareService.class);
            LocalDate fromDt = fromDate;
            LocalDate toDt = toDate;
            if (fromDate == null || toDate == null) {
                return service.findAll();
            }
            return service.findAllData(fromDt, toDt);

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SHARE;
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
//            if (fromDate != null && toDate != null) {
//                builder.queryParam("fromDate", fromDate.toString());
//                builder.queryParam("toDate", toDate.toString());
//            }
//            ResponseEntity<Share[]> response = restTemplate.getForEntity(builder.toUriString(), Share[].class);
//            if (response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }
}
