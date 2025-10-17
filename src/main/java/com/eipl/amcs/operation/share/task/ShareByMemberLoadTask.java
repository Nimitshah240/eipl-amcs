package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.service.ShareService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ShareByMemberLoadTask extends Task<List<Share>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShareByMemberLoadTask.class);

    private String memCode;

    public ShareByMemberLoadTask(String memCode) {
        this.memCode = memCode;
    }

    @Override
    protected List<Share> call() throws Exception {
        try {

            ShareService service=EmcsAppContext.getContext().getBean(ShareService.class);
            MemberRepository memberRepository=EmcsAppContext.getContext().getBean(MemberRepository.class);
            Member member = memberRepository.findByCode(memCode);

            List<Share>list=  service.findByMember(member);

            if (list==null||list.isEmpty())return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SHARE + "/by_member";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("memCode", memCode);
//            ResponseEntity<Share[]> response = restTemplate.getForEntity(builder.toUriString(), Share[].class);
//            if (response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }
}
