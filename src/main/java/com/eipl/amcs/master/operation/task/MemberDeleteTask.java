package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.service.MemberService;
import com.eipl.amcs.util.CommonUtil;
import javafx.concurrent.Task;

public class MemberDeleteTask extends Task<Boolean> {
    private final String code;

    public MemberDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MemberService service = EmcsAppContext.getContext().getBean(MemberService.class);
            service.delete(code, CommonUtil.setIdentityHeader());
            return true;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER + "/{code}";
//            Map<String, Object> uriVariables = new HashMap<>();
//            uriVariables.put("code", code);
//
//            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class, uriVariables);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
