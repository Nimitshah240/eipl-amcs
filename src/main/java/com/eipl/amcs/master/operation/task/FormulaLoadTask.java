package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Formula;
import com.eipl.amcs.master.operation.repository.FormulaRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class acts as a load task to get formula.
 *
 * @author Nimit Shah
 * @createdOn 30-06-2025
 */
public class FormulaLoadTask extends Task<List<Formula>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FormulaLoadTask.class);

    /**
     * Method calls api with endpoint formula with method get
     * to get the list of formula.
     *
     * @return List<Forumla>
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    protected List<Formula> call() throws Exception {
        try {
            FormulaRepository repository = EmcsAppContext.getContext().getBean(FormulaRepository.class);
            return repository.findAll();

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.FORMULA;
//            ResponseEntity<Formula[]> response = restTemplate.getForEntity(url, Formula[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("Formula fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Formula fetch", e);
        }
        return null;
    }
}