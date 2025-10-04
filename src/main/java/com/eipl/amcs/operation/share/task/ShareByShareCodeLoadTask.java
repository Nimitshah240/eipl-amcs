package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ShareByShareCodeLoadTask extends Task<List<Share>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShareByShareCodeLoadTask.class);

    private String memCode;

    public ShareByShareCodeLoadTask(String memCode) {
        this.memCode = memCode;
    }

    @Override
    protected List<Share> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SHARE + "/by_share_code";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("shareCode", memCode);
            ResponseEntity<Share[]> response = restTemplate.getForEntity(builder.toUriString(), Share[].class);
            if (response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }
}
