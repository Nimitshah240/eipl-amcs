package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.report.dto.MemberRegister;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class MemberRegisterReportLoadTask extends Task<List<MemberRegister>> {
    private final String societyCode;
    private final LocalDate fromDate;
    private final Integer memberType;

    public MemberRegisterReportLoadTask(String societyCode, Integer memberType, LocalDate fromDate) {
        this.societyCode = societyCode;
        this.fromDate = fromDate;
        this.memberType = memberType;
    }

    @Override
    protected List<MemberRegister> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_REGISTER;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", societyCode)
                    .queryParam("fromDate", fromDate.toString())
                    .queryParam("memberType", memberType);
            ResponseEntity<MemberRegister[]> response = restTemplate.getForEntity(builder.toUriString(), MemberRegister[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
