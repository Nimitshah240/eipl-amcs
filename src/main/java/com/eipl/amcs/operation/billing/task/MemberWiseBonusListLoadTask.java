package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Array;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MemberWiseBonusListLoadTask extends Task<List<Map<String,Object>>> {
    private LocalDate fromDate;
    private LocalDate toDate;



    private static final Logger LOGGER = LoggerFactory.getLogger(MemberWiseBonusListLoadTask.class);

    public MemberWiseBonusListLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;

    }


    @Override
    protected List<Map<String,Object>>  call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BONUS + "/loadbonussummary";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("fromDate", fromDate.toString())
                    .queryParam("toDate", toDate.toString());
//                    .queryParam("member", member.getCode());
            ResponseEntity <Map[]> response = restTemplate.getForEntity(builder.toUriString(), Map[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Bonus fetch", e);
        }
        return null;
    }
}
