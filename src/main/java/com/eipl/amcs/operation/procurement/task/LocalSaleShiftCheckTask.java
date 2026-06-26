package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.repository.LocalMilkSaleRepository;
import javafx.concurrent.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.eipl.amcs.utils.AppConstant.DATE_FORMATTER_LOCALE;

/**
 * Task to verify that every shift in a given range has at least one local milk sale.
 */
public class LocalSaleShiftCheckTask extends Task<List<String>> {

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final LocalMilkSaleRepository localMilkSaleRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public LocalSaleShiftCheckTask(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.localMilkSaleRepository = EmcsAppContext.getContext().getBean(LocalMilkSaleRepository.class);
    }

    @Override
    protected List<String> call() throws Exception {
        List<String> missingShifts = new ArrayList<>();
        LocalDateTime current = startDateTime;

        while (!current.isAfter(endDateTime)) {
            java.time.LocalDate date = current.toLocalDate();
            int shiftCode = current.getHour() < 12 ? 1 : 2;
            String shiftName = shiftCode == 1 ? MainApp.getBundle().getString("Morning") :  MainApp.getBundle().getString("Evening");
            LocalDateTime shiftStart = current.withHour(shiftCode == 1 ? 0 : 12).withMinute(0).withSecond(0);
            LocalDateTime shiftEnd= current.withHour(shiftCode == 1 ? 11 : 23).withMinute(59).withSecond(59);

            long count = localMilkSaleRepository.countSalesByShiftDetails(
                    MainApp.identityDto.getSociety(),
                    shiftStart,
                    shiftEnd,
                    shiftCode
            );

            if (count == 0) {
                missingShifts.add(date.format(DATE_FORMATTER_LOCALE) + " (" + shiftName + ")");
            }

            // Move to the next shift
            current = getNextShift(current);
        }

        return missingShifts;
    }

    private LocalDateTime getNextShift(LocalDateTime current) {
        if (current.getHour() < 12) {
            // Move from Morning to Evening of same day
            return current.withHour(12).withMinute(0);
        } else {
            // Move from Evening to Morning of next day
            return current.plusDays(1).withHour(0).withMinute(0);
        }
    }
}