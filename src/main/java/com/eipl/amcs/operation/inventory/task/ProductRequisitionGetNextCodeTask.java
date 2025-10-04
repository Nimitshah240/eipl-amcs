package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

public class ProductRequisitionGetNextCodeTask extends Task<String> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductRequisitionGetNextCodeTask.class);

//	private final String code;

//	public ProductReceiptGetNextCodeTask(String code) {
//		this.code = code;
//	}

	public ProductRequisitionGetNextCodeTask() {
	}

	@Override
	protected String call() throws Exception {
		try {
			String code = MainApp.identityDto.getSociety().getCode()+"/"+ LocalDate.now() +"/";
			RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
			String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_REQUISITION + "/fetchGrnNo";
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam(MainApp.identityDto.getSociety().getCode())
					.queryParam("code",code);
			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, String.class);
			if (response == null || response.getStatusCode() != HttpStatus.OK)
				return null;
			LOGGER.info("ProductReceipt fetched: {}", response.getBody());
			return response.getBody();
		} catch (Exception e) {
			LOGGER.error("ProductReceipt fetch", e);
		}
		return null;
	}

}
