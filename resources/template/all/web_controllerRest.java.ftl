package ${pknController};

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONArray;
import com.lazhu.core.base.BaseController;
import ${pknEntity}.${className};

/**
 * <p>
 * ${tableComment} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date?date}
 */
@RestController
@Api(value = "${tableComment}", description = "${tableComment}")
@RequestMapping(value = "${appName}/${instanceName}")
public class ${className}Controller extends BaseController<${className}> {
	
	@ApiOperation(value = "查询${tableComment}")
	@PutMapping(value = "/read/list")
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		return super.query(modelMap, param);
	}

	@ApiOperation(value = "${tableComment}详情")
	@PutMapping(value = "/read/detail")
	public Object get(ModelMap modelMap, @RequestBody ${className} param) {
		Assert.notNull(param.getId(), "ID");
		return super.get(modelMap, param);
	}

	@PostMapping
	@ApiOperation(value = "修改${tableComment}")
	public Object update(ModelMap modelMap, @RequestBody ${className} param) {
		return super.update(modelMap, param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除${tableComment}")
	public Object delete(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		Assert.notNull(param.get("ids"), "IDS");
		List<Long> ids = null;
		if (param.get("ids") instanceof String){
			ids = new ArrayList<Long>();
			ids.add(Long.valueOf(param.get("ids").toString()));
		}else if(param.get("ids") instanceof JSONArray) {
			ids = ((JSONArray)param.get("ids")).toJavaList(Long.class);
		}
		return super.del(modelMap, ids);
	}
}
