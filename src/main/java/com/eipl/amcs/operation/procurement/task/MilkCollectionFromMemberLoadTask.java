package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.dto.MilkCollectionPreReqDto;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
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

public class MilkCollectionFromMemberLoadTask extends Task<List<MilkCollection>> {
    private LocalDateTime date;
    private String code;

    public MilkCollectionFromMemberLoadTask(LocalDateTime date, String code) {
        this.date = date;
        this.code = code;
    }

    @Override
    protected List<MilkCollection> call() throws Exception {
        try {
            MilkCollectionService service=EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            List<MilkCollection> milkCollections=  service.findByMemberAndDate(date, code);
            if (milkCollections==null||milkCollections.isEmpty())
                return  null;
            return milkCollections;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION + "/member-data";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("date", date.toString())
//                    .queryParam("code", code);
//            ResponseEntity<MilkCollection[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, MilkCollection[].class);
//            if (response == null)
//                return null;
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
