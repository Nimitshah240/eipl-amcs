package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import com.eipl.amcs.master.inventory.service.ProductPurchaseRateService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProductPurchaseRateDeleteTask extends Task<Boolean> {
    private final String code;

    public ProductPurchaseRateDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            ProductPurchaseRateService service= EmcsAppContext.getContext().getBean(ProductPurchaseRateService.class);
            Optional<ProductPurchaseRate> productData = service.findById(code);
            if (productData == null || !productData.isPresent())
                return null;
            service.delete(productData.get(), CommonUtil.setIdentityHeader());
            return true;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_PURCHASE_RATE + "/{code}";
//            Map<String, Object> uriVariables = new HashMap<>();
//            uriVariables.put("code", code);
//
//            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class, uriVariables);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
