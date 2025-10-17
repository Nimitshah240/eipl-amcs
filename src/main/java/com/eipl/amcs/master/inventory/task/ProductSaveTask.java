package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.service.ProductService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class ProductSaveTask extends Task<Object> {

    private final Product dto;
    private final short update;

    public ProductSaveTask(Product dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            ProductService service = EmcsAppContext.getContext().getBean(ProductService.class);
            if (this.update == 0) {
                service.save(dto, CommonUtil.setIdentityHeader());
            } else {
                service.update(dto, CommonUtil.setIdentityHeader());
            }
            return true;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT;
//
//            ResponseEntity<Product> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), Product.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), Product.class);
//
//            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
//                return null;
//            return response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
