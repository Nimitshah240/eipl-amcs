package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.model.RptLookup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LookupLoader {

    private final ApplicationContext applicationContext;
    @PersistenceContext
    private EntityManager entityManager;

    public ObservableList<?> load(RptLookup lookup) {

        switch (lookup.getSourceType()) {

            case "MODEL":
                return loadModel(lookup);

            case "STATIC":
                return loadStatic(lookup);

            case "ENUM":
//                return loadEnum(lookup);

            case "SQL":
//                return loadSql(lookup);

            default:
                return FXCollections.observableArrayList();
        }
    }

    public ObservableList<?> loadModel(RptLookup lookup) {
        try {
            String entityName = lookup.getSourceValue();
            List<?> list = entityManager.createQuery("FROM " + entityName).getResultList();
            return FXCollections.observableArrayList(list);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load model : " + lookup.getSourceValue(), e);
        }
    }


    private ObservableList<?> loadStatic(RptLookup lookup) {
        return FXCollections.observableArrayList(
                lookup.getSourceValue().split(",")
        );
    }
}