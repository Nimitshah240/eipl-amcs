package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class MemberTotalParametersLoadTask extends Task<Object> {
    private String code;
    private String no;
    private int milkType;

    public MemberTotalParametersLoadTask(String code, String no, int milktype) {
        this.code = code;
        this.no = no;
        this.milkType = milktype;
    }

    @Override
    protected Object call() throws Exception {
        try {

            MilkCollectionService service=EmcsAppContext.getContext().getBean(MilkCollectionService.class);
         Map<String, BigDecimal> fetchTotals =service.findTotals(code, no, milkType);
         if(fetchTotals==null||fetchTotals.isEmpty())
             return null;
         return fetchTotals;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION + "/total";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("code", code)
//                    .queryParam("no", no)
//                    .queryParam("milktype", milkType);
//            ResponseEntity<Object> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
//                    null, Object.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return response.getBody();
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
