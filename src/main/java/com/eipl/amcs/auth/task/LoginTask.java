package com.eipl.amcs.auth.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.dto.LoginDto;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class LoginTask extends Task<Object> {
    private String username;
    private String password;

    public LoginTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected Object call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.AUTH;

            LoginDto dto = new LoginDto(username, password, MainApp.identityDto.getSociety());
            ResponseEntity<User> resp = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), User.class);
            if (resp != null && resp.getStatusCode() == HttpStatus.OK)
                return resp.getBody();
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
