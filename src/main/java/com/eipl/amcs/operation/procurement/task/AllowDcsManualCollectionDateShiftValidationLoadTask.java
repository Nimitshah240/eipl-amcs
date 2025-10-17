package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
import com.eipl.amcs.operation.procurement.repository.AllowDcsManualCollectionRangeRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

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
            AllowDcsManualCollectionRangeRepository repository = EmcsAppContext.getContext().getBean(AllowDcsManualCollectionRangeRepository.class);
            List<AllowDcsManualCollectionRange> existingData;
            if (selectedIndex.toString().equalsIgnoreCase("0")) {
                existingData = repository.findByFromDateLessThanEqualAndToDateGreaterThanEqualAndxCol1AndWeightManualAndQualityManualAndFromShiftAndToShiftAndStatus(fromDate, toDate, String.valueOf(selectedIndex), weightManual, qualityManual, 2);
            } else {
                existingData = repository.findByFromDateBetweenAndToDateBetweenAndxCol1AndWeightManualAndQualityManualAndStatus
                        (fromDate, toDate, String.valueOf(selectedIndex), qualityManual, weightManual, 2);
            }
            return existingData.isEmpty();
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.ALLOWDCSMANUALCOLLECTIONRANGE + "/findByDateShiftValidation";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("fromDate", fromDate.toString())
//                    .queryParam("toDate", toDate.toString())
//                    .queryParam("weightManual", weightManual)
//                    .queryParam("qualityManual", qualityManual)
//                    .queryParam("type", selectedIndex);
//            ResponseEntity<Boolean> response = restTemplate.getForEntity(builder.toUriString(), Boolean.class);

        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }
}
