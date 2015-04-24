package com.gary.dao.mybatis;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.caipiao.commons.utils.DataSourceHolder;

/**
 * 动态数据源，用于动态指定数据源
 *@author sky
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceHolder.getDataSourceName();
	}

}
