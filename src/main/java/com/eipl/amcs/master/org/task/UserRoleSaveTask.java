package com.eipl.amcs.master.org.task;

import com.eipl.amcs.auth.model.UserRole;
import com.eipl.amcs.auth.repository.UserRoleRepository;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;


public class UserRoleSaveTask extends Task<Boolean> {
    private final UserRole userRole;

    public UserRoleSaveTask(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    protected Boolean call() throws Exception {
        UserRoleRepository repository = EmcsAppContext.getContext().getBean(UserRoleRepository.class);
        return repository.save(userRole) != null;
    }
}
