package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.ProductGroupMappingDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingProductGroup;
import com.eipl.amcs.master.account.service.LedgerMappingProductGroupService;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.inventory.service.ProductGroupService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LedgerMappingProductGroupLoadTask extends Task<ProductGroupMappingDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerMappingProductGroupLoadTask.class);

    @Override
    protected ProductGroupMappingDto call() throws Exception {
        try {
            // product group
            ProductGroupService service = EmcsAppContext.getContext().getBean(ProductGroupService.class);
            List<ProductGroup> productGroupList = service.findAll();


            // ledger
            LedgerService ledgerService = EmcsAppContext.getContext().getBean(LedgerService.class);
            List<Ledger> ledgerList = ledgerService.findAllByIsActive();


            LedgerMappingProductGroupService ledgerMappingProductGroupService = EmcsAppContext.getContext().getBean(LedgerMappingProductGroupService.class);
            List<LedgerMappingProductGroup> mapping = ledgerMappingProductGroupService.findAll();


            List<LedgerMappingProductGroup> listMapping = new ArrayList<>(mapping);
            for (LedgerMappingProductGroup mp : listMapping) {
                productGroupList.removeIf(p -> p.getCode().toString().equalsIgnoreCase(mp.getProductGroup().getCode().toString()));
            }
            for (ProductGroup productGroup : productGroupList) {
                LedgerMappingProductGroup mp = new LedgerMappingProductGroup();
                mp.setProductGroup(productGroup);
                listMapping.add(mp);
            }
            List<Ledger> list = new ArrayList<>(ledgerList);
            list.add(0, new Ledger("None"));
            return new ProductGroupMappingDto(listMapping, list);

        } catch (Exception e) {
            LOGGER.error("LedgerMappingProductGroup fetch", e);
        }
        return null;
    }
}
