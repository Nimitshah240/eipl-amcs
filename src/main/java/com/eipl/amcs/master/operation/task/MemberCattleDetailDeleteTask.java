package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.repository.MemberCattleDetailRepository;
import javafx.concurrent.Task;

public class MemberCattleDetailDeleteTask extends Task<Boolean> {
    private final String code;

    public MemberCattleDetailDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MemberCattleDetailRepository service = EmcsAppContext.getContext().getBean(MemberCattleDetailRepository.class);
            service.deleteById(code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
