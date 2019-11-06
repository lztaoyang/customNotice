package com.lazhu.bd.commission.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.lazhu.core.base.BaseService;
import com.lazhu.bd.commission.entity.Commission;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author taoyang
 * @since 2019-03-27
 */
@Service
@CacheConfig(cacheNames = "commission")
public class CommissionService extends BaseService<Commission> {
	
}
