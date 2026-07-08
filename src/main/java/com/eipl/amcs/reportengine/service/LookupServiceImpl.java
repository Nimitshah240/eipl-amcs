package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.dto.StaticLookupItem;
import com.eipl.amcs.reportengine.model.RptLookup;
import com.eipl.amcs.reportengine.repository.RptLookupRepository;
import lombok.RequiredArgsConstructor;
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
    @SuppressWarnings("unchecked")
    public <T> List<T> load(String lookupCode) {

        RptLookup lookup = lookupRepository.findById(lookupCode)
                .orElseThrow(() ->
                        new RuntimeException("Lookup not found : " + lookupCode));

        switch (lookup.getSourceType().toUpperCase()) {

            case "MODEL":
                return (List<T>) loadModel(lookup);

            case "STATIC":
                return loadStatic(lookup);

            case "ENUM":
                return (List<T>) loadEnum(lookup);

            case "SQL":
                return (List<T>) loadSql(lookup);

            case "SP":
                return (List<T>) loadSp(lookup);

            default:
                return Collections.emptyList();
        }
    }

    private List<?> loadModel(RptLookup lookup) {

        String jpql = "FROM " + lookup.getSourceValue();

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

    private List<?> loadEnum(RptLookup lookup) {
        return new ArrayList<>();
    }

    private List<?> loadSql(RptLookup lookup) {
        return new ArrayList<>();
    }

    private List<?> loadSp(RptLookup lookup) {
        return new ArrayList<>();
    }
}