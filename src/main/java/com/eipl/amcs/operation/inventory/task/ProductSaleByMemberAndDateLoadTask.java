package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.service.ProductSaleService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class ProductSaleByMemberAndDateLoadTask extends Task<List<ProductSale>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductReceiptLoadTask.class);

    private final String memberCode;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    public ProductSaleByMemberAndDateLoadTask(String memberCode, LocalDate fromDate, LocalDate toDate) {
        this.memberCode = memberCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<ProductSale> call() throws Exception {
        try {
            ProductSaleService service = EmcsAppContext.getContext().getBean(ProductSaleService.class);
            List<ProductSale> list = service.findByMemberCodeAndDate(memberCode, fromDate, toDate);
            if (list == null || list.isEmpty())
                return Collections.emptyList();
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductSale fetch", e);
        }
        return  Collections.emptyList();
    }
}
