package com.eipl.amcs.utils.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.AppConstant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BroadcastedGroupDataTask extends Task<Map<String, Integer>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastedGroupDataTask.class);
    private final Map<String, Integer> groupedCount = new HashMap<>();

    @Override
    protected Map<String, Integer> call() {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String baseUrl = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.IDENTITY_CALL;
            String urlGroup = baseUrl + "/group-by-table";

            ResponseEntity<String> response = restTemplate.getForEntity(urlGroup, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                updateMessage("Fetching grouped data failed: " + response.getStatusCode());
                return null;
            }

            String json = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Parse as Map<String, List<Object>> to avoid Broadcasted model mapping issues
            Map<String, List<Object>> raw = mapper.readValue(json, new TypeReference<>() {
            });
            raw.forEach((key, list) -> groupedCount.put(key, list.size()));

            updateMessage("Success!");
            return groupedCount;

        } catch (Exception e) {
            LOGGER.error("Group fetch task failed", e);
            updateMessage("Error: " + e.getMessage());
            return null;
        }
    }
}