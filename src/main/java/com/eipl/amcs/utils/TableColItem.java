package com.eipl.amcs.utils;

import java.io.Serializable;

public class TableColItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String colName;
	private boolean visible;

	public TableColItem() {
		// TODO Auto-generated constructor stub
	}

	public TableColItem(String colName, boolean visible) {
		super();
		this.colName = colName;
		this.visible = visible;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
