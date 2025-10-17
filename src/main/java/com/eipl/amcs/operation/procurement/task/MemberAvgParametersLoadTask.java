package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class MemberAvgParametersLoadTask extends Task<Object> {
    private String code;
    private String no;
    private String milktype;
    private LocalDate date;
    private int shiftCode;

    public MemberAvgParametersLoadTask(String code, String no, String milktype, LocalDate date,int shiftCode) {
        this.code = code;
        this.no = no;
        this.milktype = milktype;
        this.date = date;
        this.shiftCode = shiftCode;
    }

    @Override
    protected Object call() throws Exception {
        try {
            MilkCollectionService service=EmcsAppContext.getContext().getBean(MilkCollectionService.class);

//            LocalDate d = LocalDate.parse(date);
            Map<String, BigDecimal> list= service.findAvgFatAndSnf(code, Integer.parseInt(no), milktype, date, shiftCode);
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION + "/avg";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("code", code)
//                    .queryParam("no", no)
//                    .queryParam("milktype", milktype)
//                    .queryParam("shiftCode", shiftCode)
//                    .queryParam("date", date.toString());
//            ResponseEntity<Object> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
//                    null, Object.class);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
