package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.ContactDetails;
import com.eipl.amcs.master.org.repository.ContactDetailsRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ContactDetailSaveTask extends Task<List<ContactDetails>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactDetailSaveTask.class);

    private List<ContactDetails> contactDetailsList;

    public ContactDetailSaveTask(List<ContactDetails> contactDetailsList) {
        this.contactDetailsList = contactDetailsList;
    }

    @Override
    protected List<ContactDetails> call() throws Exception {
        try {
            ContactDetailsRepository service = EmcsAppContext.getContext().getBean(ContactDetailsRepository.class);
            return service.saveAll(contactDetailsList);
        } catch (Exception e) {
            LOGGER.error("ContactDetails fetch", e);
        }
        return null;
    }
}
