package ${pknEntity};

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lazhu.core.base.BaseModel;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * ${tableComment}
 * </p>
 *
 * @author ${author}
 * @since ${date?date}
 */
@ApiModel(value = "${tableComment}",description="${instanceName}")
@TableName("${tableName}")
public class ${className} extends BaseModel {
    private static final long serialVersionUID = 1L;
	<#list columns as column>
    //${column.name} ${column.comment} 
    <#if (column.nullable)>
    @ApiModelProperty(value = "${column.comment}", required = false)
    <#else>
    @ApiModelProperty(value = "${column.comment}", required = true)
    </#if>
    @TableField("${column.name}")
    private ${column.javaType} ${column.fieldName};
    </#list> 
	<#list columns as column>

    public void ${column.setMethod}(${column.javaType} ${column.fieldName})
    {
        this.${column.fieldName} = ${column.fieldName};
    }

    public ${column.javaType} ${column.getMethod}()
    {
        return this.${column.fieldName};
    }
</#list>

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
	<#list columns as column>
    	sb.append(", ${column.fieldName}=").append(${column.getMethod}());
    </#list>
		sb.append("]");
		return sb.toString();
	}

	/**
	 */
	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null) {
			return false;
		}
		if (getClass() != that.getClass()) {
			return false;
		}
		${className} other = (${className}) that;
		return (1==1
		<#list columns as column>
	    	&&this.${column.getMethod}() == null ? other.${column.getMethod}() == null : this.${column.getMethod}().equals(other.${column.getMethod}())
	    </#list>
				);
	}

	/**
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
	<#list columns as column>
    	result = prime * result + ((${column.getMethod}() == null) ? 0 : ${column.getMethod}().hashCode());
    </#list>
		return result;
	}
}
