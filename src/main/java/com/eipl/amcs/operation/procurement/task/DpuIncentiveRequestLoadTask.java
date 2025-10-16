package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
import com.eipl.amcs.operation.procurement.model.DpuIncentiveRequest;
import com.eipl.amcs.operation.procurement.repository.DpuIncentiveRequestRepository;
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

public class DpuIncentiveRequestLoadTask extends Task<DpuIncentiveRequest> {

    private LocalDate fromDate, toDate;
    private static final Logger LOGGER = LoggerFactory.getLogger(DpuIncentiveRequestLoadTask.class);

    public DpuIncentiveRequestLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected DpuIncentiveRequest call() throws Exception {
        try {
             DpuIncentiveRequestRepository dpuIncentiveRequestRepository =  EmcsAppContext.getContext().getBean(DpuIncentiveRequestRepository.class);

            if(dpuIncentiveRequestRepository.findTop1ByOrderByCreatedAtDesc().isPresent()) {
              return  dpuIncentiveRequestRepository.findTop1ByOrderByCreatedAtDesc().get();
            }
            else
                return null;
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION+"/findByDate";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("fromDate", fromDate.toString())
//                    .queryParam("toDate", toDate.toString());
//            ResponseEntity<DpuIncentiveRequest> response = restTemplate.getForEntity(builder.toUriString(), DpuIncentiveRequest.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("ManualRequest fetch", e);
        }
        return null;
    }
}
