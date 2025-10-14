package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.operation.billing.service.BonusService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MemberWiseBonusLoadTask extends Task<Map<String,Object>> {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String memberCode;


    private static final Logger LOGGER = LoggerFactory.getLogger(MemberWiseBonusLoadTask.class);

    public MemberWiseBonusLoadTask(LocalDate fromDate, LocalDate toDate, String memberCode) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.memberCode=memberCode;
    }


    @Override
    protected Map<String,Object> call() throws Exception {
        try {
            BonusService service =  EmcsAppContext.getContext().getBean(BonusService.class);
            Map<String,Object> loadDataBonusResult = service.loadDataBonus( fromDate, toDate,memberCode);

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BONUS + "/loadbonus";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("fromDate", fromDate.toString())
//                    .queryParam("toDate", toDate.toString())
//                    .queryParam("memberCode", memberCode);
//            ResponseEntity<Map> response = restTemplate.getForEntity(builder.toUriString(), Map.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return response.getBody();
            return loadDataBonusResult;
        } catch (Exception e) {
            LOGGER.error("Bonus fetch", e);
        }
        return null;
    }
}
