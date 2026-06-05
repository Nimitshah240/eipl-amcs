package com.eipl.amcs.master.org.task;

import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.model.UserRole;
import com.eipl.amcs.auth.repository.UserRoleRepository;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class UserRoleLoadTask extends Task<List<UserRole>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleLoadTask.class);
    private User user;
    public UserRoleLoadTask() {
        this.user = null;
    }
    public UserRoleLoadTask(User user) {
        this.user = user;
    }

    @Override
    protected List<UserRole> call() throws Exception {
        UserRoleRepository repository = EmcsAppContext.getContext().getBean(UserRoleRepository.class);
        if (this.user != null) {
            return repository.findAllByUser(this.user);
        }
        return repository.findAll();
    }
}