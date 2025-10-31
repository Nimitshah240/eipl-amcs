package com.eipl.amcs;

import com.eipl.amcs.auth.dto.IdentityDto;
import com.eipl.amcs.sync.model.NavigationBook;
import com.eipl.amcs.sync.repository.NavigationBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class EiplAmcsAppRunner implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(EiplAmcsAppRunner.class);
    public static Map<String, NavigationBook> books = new HashMap<>();
    public static IdentityDto identityDto = null;

    @Autowired
    private NavigationBookRepository navigationBookRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<NavigationBook> list = navigationBookRepository.findByNavForAndFlag((short) 1, (short) 1);
        if (list != null) {
            list.forEach(item -> {
                books.put(item.getTableName(), item);
            });
        }
    }

}
