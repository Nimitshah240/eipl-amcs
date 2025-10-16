package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.service.ShareService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
            ShareService service=EmcsAppContext.getContext().getBean(ShareService.class);;

//            TODO - Taking fromDate and toDate for joke?

            List<Share> list=service.findAll();
            if (list==null||list.isEmpty())return null;
            return list;

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
