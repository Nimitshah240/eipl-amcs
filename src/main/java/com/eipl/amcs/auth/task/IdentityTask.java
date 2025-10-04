package com.eipl.amcs.auth.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.dto.IdentityDto;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class IdentityTask extends Task<IdentityDto> {
    private String dockNumber;
    private String societyCode;
    private String unionCode;

    public IdentityTask(String dockNumber, String societyCode, String unionCode) {
        this.dockNumber = dockNumber;
        this.societyCode = societyCode;
        this.unionCode = unionCode;
    }

    @Override
    protected IdentityDto call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.IDENTITY + "/{dockNumber}/{societyCode}/{unionCode}";
            Map<String, Object> uriVariables = new HashMap<>();
            uriVariables.put("dockNumber", dockNumber);
            uriVariables.put("societyCode", societyCode);
            uriVariables.put("unionCode", unionCode);

            ResponseEntity<IdentityDto> response = restTemplate.exchange(url, HttpMethod.GET, null, IdentityDto.class, uriVariables);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
