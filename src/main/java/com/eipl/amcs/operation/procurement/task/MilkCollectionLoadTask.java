package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MilkCollectionLoadTask extends Task<List<MilkCollection>> {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String code;
    private String dockNo;
    private int sync;

    public MilkCollectionLoadTask(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public MilkCollectionLoadTask(LocalDateTime fromDate, LocalDateTime toDate, int sync) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.sync = sync;
    }

    public MilkCollectionLoadTask(LocalDateTime fromDate, LocalDateTime toDate, String dockNo, int sync) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.dockNo = dockNo;
        this.sync = sync;
    }

    @Override
    protected List<MilkCollection> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION;
            UriComponentsBuilder builder = null;
            if (fromDate != null)
                builder = UriComponentsBuilder.fromUriString(url)
                        .queryParam("fromDate", fromDate.toString());
            if (toDate != null)
                builder.queryParam("toDate", toDate.toString());
            if (code != null && !code.isEmpty())
                builder.queryParam("code", code);
            if (dockNo != null && !dockNo.isEmpty() && !"All".equalsIgnoreCase(dockNo))
                builder.queryParam("dockNo", dockNo);
            if (builder != null) {
                builder.queryParam("sync", sync);
                ResponseEntity<MilkCollection[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, MilkCollection[].class);
                if (response == null || response.getStatusCode() != HttpStatus.OK)
                    return null;
                return Arrays.asList(response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
