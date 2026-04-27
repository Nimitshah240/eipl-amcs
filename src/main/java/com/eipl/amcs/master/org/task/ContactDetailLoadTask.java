package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.ContactDetails;
import com.eipl.amcs.master.org.repository.ContactDetailsRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ContactDetailLoadTask extends Task<List<ContactDetails>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactDetailLoadTask.class);

    private final String moduleCode;
    private final String moduleName;
    private final String department;

    public ContactDetailLoadTask(String moduleName, String moduleCode, String department) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.department = department;
    }

    @Override
    protected List<ContactDetails> call() throws Exception {
        try {
            ContactDetailsRepository service = EmcsAppContext.getContext().getBean(ContactDetailsRepository.class);
            List<ContactDetails> list = service.findByModuleNameIgnoreCaseAndModuleCodeAndDepartmentIgnoreCase(moduleName, moduleCode, department);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ContactDetails fetch", e);
        }
        return null;
    }
}
