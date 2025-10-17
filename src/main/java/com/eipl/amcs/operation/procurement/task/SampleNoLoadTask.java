package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

public class SampleNoLoadTask extends Task<Number> {
    private LocalDateTime date;
    private Dock dock;
    private MilkType milkType;

    public SampleNoLoadTask(LocalDateTime date, Dock dock, MilkType milkType) {
        this.date = date;
        this.dock = dock;
        this.milkType = milkType;
    }

    @Override
    protected Number call() throws Exception {
        try {

            MilkCollectionService service=EmcsAppContext.getContext().getBean(MilkCollectionService.class);;
            return service.fetchNextSampleNo(date, dock.getDockNo());

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION + "/next-sampleno";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("date", date.toString())
//                    .queryParam("dockCode", dock.getDockNo())
//                    .queryParam("milkType", milkType != null ? milkType.getCode() : 0);
//            ResponseEntity<Number> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, Number.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
