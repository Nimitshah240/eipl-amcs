package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.service.LocalMilkSaleService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class LocalMilkSaleLoadTask extends Task<List<LocalMilkSale>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalMilkSaleLoadTask.class);

    private LocalDate fromDate, toDate;

    public LocalMilkSaleLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public LocalMilkSaleLoadTask() {

    }

    @Override
    protected List<LocalMilkSale> call() throws Exception {
        try {
            LocalMilkSaleService service = EmcsAppContext.getContext().getBean(LocalMilkSaleService.class);
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LOCAL_MILK_SALE;
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("fromDate", fromDate.toString())
//                    .queryParam("toDate", toDate.toString());
//            ResponseEntity<LocalMilkSale[]> response = restTemplate.getForEntity(builder.toUriString(), LocalMilkSale[].class);
            LocalDateTime fromDt = LocalDateTime.of((fromDate), LocalTime.MIN);
            LocalDateTime toDt = LocalDateTime.of((toDate), LocalTime.MAX);
            List<LocalMilkSale> list = service.findAll(fromDt, toDt);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }
}
