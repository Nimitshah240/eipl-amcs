package com.eipl.amcs.auth.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.dto.LoginDto;
import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.service.UserService;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

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
            UserService service = EmcsAppContext.getContext().getBean(UserService.class);
            LoginDto dto = new LoginDto(username, password, MainApp.identityDto.getSociety());
            User user = service.authenticate(dto);
            if (user != null)
                return user;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
