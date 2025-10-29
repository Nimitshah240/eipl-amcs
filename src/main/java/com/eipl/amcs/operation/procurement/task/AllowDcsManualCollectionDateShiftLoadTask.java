package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
import com.eipl.amcs.operation.procurement.repository.AllowDcsManualCollectionRangeRepository;
import javafx.concurrent.Task;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AllowDcsManualCollectionDateShiftLoadTask extends Task<List<AllowDcsManualCollectionRange>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllowDcsManualCollectionDateShiftLoadTask.class);

    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;
    private final Integer selectedIndex;

    public AllowDcsManualCollectionDateShiftLoadTask(LocalDateTime fromDate, LocalDateTime toDate, Integer selectedIndex) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.selectedIndex = selectedIndex;
    }


    @Override
    protected List<AllowDcsManualCollectionRange> call() throws Exception {
        try {
            AllowDcsManualCollectionRangeRepository repository = EmcsAppContext.getContext().getBean(AllowDcsManualCollectionRangeRepository.class);
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.ALLOWDCSMANUALCOLLECTIONRANGE + "/findByDateShift";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("fromDate", fromDate.toString())
//                    .queryParam("toDate", toDate.toString())
//                    .queryParam("type", selectedIndex);
//            ResponseEntity<AllowDcsManualCollectionRange[]> response = restTemplate.getForEntity(builder.toUriString(), AllowDcsManualCollectionRange[].class);


            List<AllowDcsManualCollectionRange> list = new ArrayList<>();
            if (selectedIndex == null || selectedIndex.toString().equalsIgnoreCase("")) {
                list = repository.findByFromDateLessThanEqualAndToDateGreaterThanEqual(fromDate, toDate);
            } else {
                list = repository.findByFromDateLessThanEqualAndToDateGreaterThanEqualAndxCol1(fromDate, toDate, selectedIndex.toString());
            }
            for (AllowDcsManualCollectionRange range : list) {
                range.setSociety(Hibernate.unproxy(range.getSociety(), Society.class));
                range.setFromShift(Hibernate.unproxy(range.getFromShift(), Shift.class));
                range.setToShift(Hibernate.unproxy(range.getToShift(), Shift.class));
            }
            if
            (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }
}
