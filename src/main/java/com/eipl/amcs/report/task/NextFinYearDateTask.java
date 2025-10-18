package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.FinancialYearRepository;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.List;

public class NextFinYearDateTask extends Task<List<LocalDate>> {

    private LocalDate currentDate;

    public NextFinYearDateTask(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public NextFinYearDateTask() {
    }

    @Override
    protected List<LocalDate> call() throws Exception {
        try {
            FinancialYearRepository financialYearRepository = EmcsAppContext.getContext().getBean(FinancialYearRepository.class);
            List<LocalDate> list = financialYearRepository.fetchByDate(currentDate);
            if (list == null || list.isEmpty())
                return null;
            return list;


//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.NEXT_FY_DATE;
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("date", currentDate.toString());
//            ResponseEntity<FinancialYear[]> response = restTemplate.getForEntity(builder.toUriString(), FinancialYear[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
