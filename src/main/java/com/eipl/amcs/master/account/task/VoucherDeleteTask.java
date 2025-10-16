package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.repository.VoucherRepository;
import com.eipl.amcs.master.account.service.VoucherService;
import com.eipl.amcs.util.CommonUtil;
import javafx.concurrent.Task;

import java.util.Optional;

public class VoucherDeleteTask extends Task<Boolean> {
    private final String code;

    public VoucherDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            VoucherService service = EmcsAppContext.getContext().getBean(VoucherService.class);
            VoucherRepository repository = EmcsAppContext.getContext().getBean(VoucherRepository.class);
            Optional<Voucher> voucher = repository.findById(code);
            service.delete(voucher.get(), CommonUtil.setIdentityHeader());
            return true;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.VOUCHER;
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("code", code);
//            restTemplate.delete(builder.toUriString(), Void.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
