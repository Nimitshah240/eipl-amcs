package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.repository.MemberFamilyDetailRepository;
import javafx.concurrent.Task;

public class MemberFamilyDetailDeleteTask extends Task<Boolean> {
    private final String code;

    public MemberFamilyDetailDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MemberFamilyDetailRepository service = EmcsAppContext.getContext().getBean(MemberFamilyDetailRepository.class);
            service.deleteById(code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
