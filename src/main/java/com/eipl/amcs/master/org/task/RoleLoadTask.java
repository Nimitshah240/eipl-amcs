package com.eipl.amcs.master.org.task;

import com.eipl.amcs.auth.model.Role;
import com.eipl.amcs.auth.repository.RoleRepository;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class RoleLoadTask extends Task<List<Role>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleLoadTask.class);


    @Override
    protected List<Role> call() throws Exception {

        RoleRepository repository =  EmcsAppContext.getContext().getBean(RoleRepository.class);
        return repository.findAll();
    }
}