package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.model.Route;

import java.util.List;

public interface RouteService {
	 List<Route> findAll();
     Route save(Route obj);

}
