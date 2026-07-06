package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.dto.StaticLookupItem;
import com.eipl.amcs.reportengine.model.RptLookup;
import com.eipl.amcs.reportengine.repository.RptLookupRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LookupServiceImpl implements LookupService {

    private final RptLookupRepository lookupRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<T> load(Long lookupId) {

        RptLookup lookup = lookupRepository.findById(lookupId)
                .orElseThrow(() ->
                        new RuntimeException("Lookup not found : " + lookupId));

        switch (lookup.getSourceType().toUpperCase()) {

            case "MODEL":
                return loadModel(lookup);

            case "STATIC":
                return loadStatic(lookup);

            case "ENUM":
                return loadEnum(lookup);

            case "SQL":
                return loadSql(lookup);

            case "SP":
                return loadSp(lookup);

            default:
                return Collections.emptyList();
        }
    }

    private List<T> loadModel(RptLookup lookup) {

        String entityName = lookup.getSourceValue();
        String jpql = "FROM " + entityName;
        if (lookup.getFilterClause() != null && !lookup.getFilterClause().isBlank()) {
            jpql += " WHERE " + lookup.getFilterClause();
        }

        if (lookup.getOrderBy() != null && !lookup.getOrderBy().isBlank()) {
            jpql += " ORDER BY " + lookup.getOrderBy();
        }
        return entityManager.createQuery(jpql).getResultList();
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> loadStatic(RptLookup lookup) {

        List<StaticLookupItem> items = new ArrayList<>();

        if (lookup.getSourceValue() == null || lookup.getSourceValue().isBlank()) {
            return (List<T>) items;
        }

        String[] values = lookup.getSourceValue().split(";");

        for (String value : values) {

            String[] pair = value.split(":");

            items.add(new StaticLookupItem(
                    pair[0].trim(),
                    pair.length > 1 ? pair[1].trim() : pair[0].trim()
            ));
        }

        return (List<T>) items;
    }

    private List<T> loadEnum(RptLookup lookup) {

        return new ArrayList<>();
    }

    private List<T> loadSql(RptLookup lookup) {

        return new ArrayList<>();
    }

    private List<T> loadSp(RptLookup lookup) {

        return new ArrayList<>();
    }
}