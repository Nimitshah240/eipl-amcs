package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.report.dto.MemberCollection;
import com.eipl.amcs.report.dto.MemberCollection;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MemberCollectionTask extends Task<List<MemberCollection>> {
    private String societyCode;
    private LocalDateTime from_date;
    private LocalDateTime to_date;
    private String qty_mode;

    public MemberCollectionTask(String societyCode, LocalDateTime fromDate, LocalDateTime toDate, String qtyMode) {
        this.societyCode = societyCode;
        this.from_date = fromDate;
        this.to_date = toDate;
        this.qty_mode = qtyMode;
    }

    @Override
    protected List<MemberCollection> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.REPORT_MEMBER_COLLECTION;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", societyCode)
                    .queryParam("fromDate", from_date.toString())
                    .queryParam("toDate", to_date.toString())
                    .queryParam("qtyMode", qty_mode);

            ResponseEntity<MemberCollection[]> response = restTemplate.getForEntity(builder.toUriString(), MemberCollection[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
