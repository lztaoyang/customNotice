package com.lazhu.bd.querylog.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.lazhu.core.base.BaseService;
import com.lazhu.bd.querylog.entity.QueryLog;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author taoyang
 * @since 2019-11-04
 */
@Service
@CacheConfig(cacheNames = "queryLog")
public class QueryLogService extends BaseService<QueryLog> {
	
}
