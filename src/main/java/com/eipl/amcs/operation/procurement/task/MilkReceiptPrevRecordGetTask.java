package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.repository.MilkReceiptRepository;
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

public class MilkReceiptPrevRecordGetTask extends Task<MilkReceipt> {
    private LocalDateTime fromDate;

    public MilkReceiptPrevRecordGetTask(LocalDateTime fromDate) {
        this.fromDate = fromDate;

    }

    @Override
    protected MilkReceipt call() throws Exception {
        try {

            MilkReceiptRepository repository=EmcsAppContext.getContext().getBean(MilkReceiptRepository.class);;
           MilkReceipt milkReceipt= repository.findPreviousRecordOfGoodMilkType(fromDate).get();
           if (milkReceipt==null||milkReceipt.getCode().isEmpty())return null;
           return milkReceipt;


//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_RECEIPT + "/prev-record";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("fromDate", fromDate.toString());
//            ResponseEntity<MilkReceipt> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, MilkReceipt.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
