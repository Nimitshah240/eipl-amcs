package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import javafx.concurrent.Task;

import java.time.LocalDateTime;
import java.util.List;

public class MilkCollectionFromMemberLoadTask extends Task<List<MilkCollection>> {
    private final LocalDateTime date;
    private final String code;

    public MilkCollectionFromMemberLoadTask(LocalDateTime date, String code) {
        this.date = date;
        this.code = code;
    }

    @Override
    protected List<MilkCollection> call() throws Exception {
        try {
            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            List<MilkCollection> milkCollections = service.findByMemberAndDate(date, code);
            if (milkCollections == null || milkCollections.isEmpty())
                return null;
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
