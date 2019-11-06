package ${pknService};

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.lazhu.core.base.BaseService;
import ${pknEntity}.${className};

/**
 * <p>
 * ${tableComment} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date?date}
 */
@Service
@CacheConfig(cacheNames = "${instanceName}")
public class ${className}Service extends BaseService<${className}> {
	
}
