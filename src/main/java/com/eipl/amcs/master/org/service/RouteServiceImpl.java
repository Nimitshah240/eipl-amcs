package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.model.Route;
import com.eipl.amcs.master.org.repository.RouteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {

    private static final Logger log = LoggerFactory.getLogger(RouteServiceImpl.class);
    @Autowired
    private RouteRepository routeRepository;

    @Override
    public List<Route> findAll() {
        List<Route> list = routeRepository.findAll(Sort.by("name"));
        log.info("Routes findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public Route save(Route obj) {
        routeRepository.save(obj);
        return null;
    }

}
