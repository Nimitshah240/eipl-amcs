package com.eipl.amcs.master.account.task;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.SubLedgerLedgerConfig;
import com.eipl.amcs.utils.AppConstant;

import javafx.concurrent.Task;

public class SubLedgerLedgerConfigBySubLedgerTypeLoadTask extends Task<List<SubLedgerLedgerConfig>> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(com.eipl.amcs.master.inventory.task.ProductPurchaseRateByProductTask.class);

	private final Integer code;

	public SubLedgerLedgerConfigBySubLedgerTypeLoadTask(Integer code) {
		this.code = code;
	}

	@Override
	protected List<SubLedgerLedgerConfig> call() throws Exception {
		try {
			RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
			String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null)
					+ AppConstant.UrlPath.SUB_LEDGER_LEDGER_CONFIG + "/by-subledger-type";
			UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url).queryParam("code",
					code);
			ResponseEntity<SubLedgerLedgerConfig[]> response = restTemplate.exchange(uriComponentsBuilder.toUriString(),
					HttpMethod.GET, null, SubLedgerLedgerConfig[].class);
			if (response == null || response.getStatusCode() != HttpStatus.OK)
				return null;
			LOGGER.info("Config fetched: {}", response.getBody());
			return response.getBody()!=null?Arrays.asList(response.getBody()):null;
		} catch (Exception e) {
			LOGGER.error("Config fetch", e);
		}
		return null;
	}
}
