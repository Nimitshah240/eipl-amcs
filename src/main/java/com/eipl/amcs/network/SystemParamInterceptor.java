package com.eipl.amcs.network;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.utils.AppConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SystemParamInterceptor implements ClientHttpRequestInterceptor {

    final static Logger log = LoggerFactory.getLogger(SystemParamInterceptor.class);

    final List<String> urlToBypass = new ArrayList<String>() {{
        add("eipl-amcs/identity");
        add("eipl-amcs/home");
        add("amcs-desktop/register");
        add("realtime-services/purchase-rate");
    }};

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        addSystemParamsInHeader(request);
        ClientHttpResponse response = execution.execute(request, body);
        return response;
    }

    private void addSystemParamsInHeader(HttpRequest request) {
        try {
            for (String p : urlToBypass) {
                if (request.getURI().toURL().toString().contains(p))
                    return;
            }
        } catch (MalformedURLException e) {
        }
        String sb = "SOCIETY" +
                "#" +
                MainApp.identityDto.getSociety().getCode() +
                "#" +
                MainApp.systemId +
                "#" +
                MainApp.getProperty(AppConstant.Props.VERSION, "1.0") +
                "#" +
                MainApp.locale;
        String headerVal = new String(Base64.getEncoder().encode(sb.getBytes()));
        request.getHeaders().add("identity", headerVal);
    }

}
