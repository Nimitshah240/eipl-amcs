package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.model.ShareDividend;
import com.eipl.amcs.operation.share.service.ShareDividendService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.List;

public class ShareDividendListSaveTask extends Task<String> {
    private final List<ShareDividend> shareList;
    private final short update;

    public ShareDividendListSaveTask(List<ShareDividend> shareList, short update) {
        this.shareList = shareList;
        this.update = update;
    }


    @Override
    protected String call() throws Exception {
        try {

            ShareDividendService service = EmcsAppContext.getContext().getBean(ShareDividendService.class);

            ShareDividend sharedividend;
            if (this.update == 0) {
                return service.save(shareList, CommonUtils.setIdentityHeader());
            } else {
                sharedividend = service.update(shareList.get(0), CommonUtils.setIdentityHeader());
            }

            return sharedividend.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
