package com.eipl.amcs.master.org.task;

import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.repository.UserRepository;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;


public class UserSaveTask extends Task<User> {

    private final User user;

    public UserSaveTask(User user) {
        this.user = user;

    }

    @Override
    protected User call() throws Exception {
        UserRepository repository =  EmcsAppContext.getContext().getBean(UserRepository.class);

        return repository.save(user);
    }
}