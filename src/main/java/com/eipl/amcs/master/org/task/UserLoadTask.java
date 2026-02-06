package com.eipl.amcs.master.org.task;

import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.repository.UserRepository;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class UserLoadTask extends Task<List<User>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoadTask.class);


    @Override
    protected List<User> call() throws Exception {

        UserRepository repository =  EmcsAppContext.getContext().getBean(UserRepository.class);
        return repository.findAll();
    }
}