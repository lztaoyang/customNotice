package com.lazhu.bd.querylog.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.lazhu.core.base.BaseModel;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author taoyang
 * @since 2019-11-04
 */
@ApiModel(value = "",description="queryLog")
@TableName("bd_query_log")
public class QueryLog extends BaseModel {
    private static final long serialVersionUID = 1L;
    //query_key 搜索关键词 
    @ApiModelProperty(value = "搜索关键词", required = false)
    @TableField("query_key")
    private String queryKey;
    //num 搜索次数 
    @ApiModelProperty(value = "搜索次数", required = false)
    @TableField("num")
    private Integer num;

    public void setQueryKey(String queryKey)
    {
        this.queryKey = queryKey;
    }

    public String getQueryKey()
    {
        return this.queryKey;
    }

    public void setNum(Integer num)
    {
        this.num = num;
    }

    public Integer getNum()
    {
        return this.num;
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
    	sb.append(", queryKey=").append(getQueryKey());
    	sb.append(", num=").append(getNum());
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
		QueryLog other = (QueryLog) that;
		return (1==1
	    	&&this.getQueryKey() == null ? other.getQueryKey() == null : this.getQueryKey().equals(other.getQueryKey())
	    	&&this.getNum() == null ? other.getNum() == null : this.getNum().equals(other.getNum())
				);
	}

	/**
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
    	result = prime * result + ((getQueryKey() == null) ? 0 : getQueryKey().hashCode());
    	result = prime * result + ((getNum() == null) ? 0 : getNum().hashCode());
		return result;
	}
}
