package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
import com.eipl.amcs.operation.procurement.repository.AllowDcsManualCollectionRangeRepository;
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
import java.util.List;

import static com.eipl.amcs.utils.AppConstant.DATE_TIME_FMT;

public class AllowDcsManualCollectionDateShiftValidationLoadTask extends Task<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllowDcsManualCollectionDateShiftValidationLoadTask.class);

    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;
    private final Integer selectedIndex;
    private final Boolean weightManual;
    private final Boolean qualityManual;

    public AllowDcsManualCollectionDateShiftValidationLoadTask(LocalDateTime fromDate, LocalDateTime toDate, Integer selectedIndex, Boolean weightManual, Boolean qualityManual) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.selectedIndex = selectedIndex;
        this.weightManual = weightManual;
        this.qualityManual = qualityManual;
    }



    @Override
    protected Boolean call() throws Exception {
        try {
            AllowDcsManualCollectionRangeRepository repository=EmcsAppContext.getContext().getBean(AllowDcsManualCollectionRangeRepository.class);
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.ALLOWDCSMANUALCOLLECTIONRANGE + "/findByDateShiftValidation";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("fromDate", fromDate.toString())
//                    .queryParam("toDate", toDate.toString())
//                    .queryParam("weightManual", weightManual)
//                    .queryParam("qualityManual", qualityManual)
//                    .queryParam("type", selectedIndex);
//            ResponseEntity<Boolean> response = restTemplate.getForEntity(builder.toUriString(), Boolean.class);

            if (selectedIndex.toString().equalsIgnoreCase("0")) {
                 repository.findByFromDateLessThanEqualAndToDateGreaterThanEqualAndxCol1AndWeightManualAndQualityManualAndFromShiftAndToShiftAndStatus(fromDate, toDate, selectedIndex.toString(), weightManual, qualityManual, 2);
            } else {
                  repository.findByFromDateBetweenAndToDateBetweenAndxCol1AndWeightManualAndQualityManualAndStatus
                        (fromDate, toDate, selectedIndex.toString(), qualityManual, weightManual, 2);
            }
           return true;
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }
}
