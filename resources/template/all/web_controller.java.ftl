package ${package.Controller};

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

#if(${superControllerClassPackage})
import ${superControllerClassPackage};
#end
import ${package.Entity}.${entity};

/**
 * <p>
 * ${table.comment} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date?date}
 */
@Controller
@Api(value = "${table.comment}", description = "${table.comment}")
@RequestMapping(value = "${package.ModuleName}")
#if(${superControllerClass})
public class ${table.controllerName} extends ${superControllerClass} {
#else
public class ${table.controllerName} {
#end
	@RequestMapping("/test")
	@ApiOperation(value = "测试", httpMethod = "GET")
	public String test(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		return "/${package.ModuleName}/test";
	}
}
