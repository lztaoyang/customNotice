package com.lazhu.bd.commission.entity;

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
 * @since 2019-03-27
 */
@ApiModel(value = "",description="commission")
@TableName("bd_commission")
public class Commission extends BaseModel {
    private static final long serialVersionUID = 1L;
    //name 案件审核类型 
    @ApiModelProperty(value = "案件审核类型", required = false)
    @TableField("name")
    private String name;
    //type 审核类型 
    @ApiModelProperty(value = "审核类型", required = false)
    @TableField("type")
    private Integer type;
    //price 单笔金额 
    @ApiModelProperty(value = "单笔金额", required = false)
    @TableField("price")
    private BigDecimal price;

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return this.type;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return this.price;
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
    	sb.append(", name=").append(getName());
    	sb.append(", type=").append(getType());
    	sb.append(", price=").append(getPrice());
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
		Commission other = (Commission) that;
		return (1==1
	    	&&this.getName() == null ? other.getName() == null : this.getName().equals(other.getName())
	    	&&this.getType() == null ? other.getType() == null : this.getType().equals(other.getType())
	    	&&this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice())
				);
	}

	/**
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
    	result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
    	result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
    	result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
		return result;
	}
}
