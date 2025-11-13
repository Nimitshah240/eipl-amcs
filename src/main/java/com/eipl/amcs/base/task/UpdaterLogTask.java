package com.eipl.amcs.base.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.dto.JarUpdate;
import com.eipl.amcs.base.model.JarUpdateLog;
import com.eipl.amcs.base.repository.JarUpdateLogRepository;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class UpdaterLogTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdaterLogTask.class);

    private JarUpdate jarUpdate;

    public UpdaterLogTask(JarUpdate jarUpdate) {
        this.jarUpdate = jarUpdate;
    }

    @Override
    protected String call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            JarUpdateLogRepository jarUpdateLogRepository = EmcsAppContext.getContext().getBean(JarUpdateLogRepository.class);

            jarUpdate.setSocietyCode(MainApp.identityDto.getSociety().getCode());
            String url = AppConstant.UrlPath.DATA_PROCESSOR + AppConstant.UrlPath.APP_UPDATE + AppConstant.UrlPath.UPDATE_LOG;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("client_code", AppConstant.UrlPath.JAIPUR);

            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST,
                    new HttpEntity<>(jarUpdate), String.class);


            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;

            jarUpdateLogRepository.save(new JarUpdateLog(jarUpdate.getSocietyCode(), jarUpdate.getAppVersion()));
            return "Success";
        } catch (Exception e) {
            LOGGER.error("Updater Information Failed", e);
        }
        return null;
    }
}