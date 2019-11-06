package com.lazhu.generate.vo;

import java.util.List;

public class Column
{
    /********** JDBC **************/
    private String name;// 字段名
    
    private String type;// 字段类型
    
    private int size;// 长度
    
    private boolean nullable;// 是否允许空
    
    private String defaultValue;// 默认值
    
    private int digits;// 精度
    
    private String comment;// 备注
    
    /********** Java **************/
    private String javaType;// java type
    
    private String fieldName;// entity field名称
    
    private String setMethod;// set 方法名
    
    private String getMethod;// get 方法名
    
    public boolean useDicValue;//是否使用字典值
    
    private String dicType;//字典类型
    private List<DicValue> dicValues;//字典值
    
//    private boolean global = false;//是否公共字段
    
    private Table table;
    
    public int getDigits()
    {
        return digits;
    }
    
    public void setDigits(int digits)
    {
        this.digits = digits;
    }
    
    public String getDefaultValue()
    {
        return defaultValue;
    }
    
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }
    
    public int getSize()
    {
        return size;
    }
    
    public void setSize(int size)
    {
        this.size = size;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public boolean isNullable()
    {
        return nullable;
    }
    
    public void setNullable(boolean nullable)
    {
        this.nullable = nullable;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getComment()
    {
        return comment;
    }
    
    public void setComment(String comment)
    {
        this.comment = comment;
    }
    
    public String getJavaType()
    {
        return javaType;
    }
    
    public void setJavaType(String javaType)
    {
        this.javaType = javaType;
    }
    
    public String getFieldName()
    {
        return fieldName;
    }
    
    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }
    
    public Table getTable()
    {
        return table;
    }
    
    public void setTable(Table table)
    {
        this.table = table;
    }
    
    public String getSetMethod()
    {
        return setMethod;
    }
    
    public void setSetMethod(String setMethod)
    {
        this.setMethod = setMethod;
    }
    
    public String getGetMethod()
    {
        return getMethod;
    }
    
    public void setGetMethod(String getMethod)
    {
        this.getMethod = getMethod;
    }
    
    public List<DicValue> getDicValues() {
		return dicValues;
	}

	public void setDicValues(List<DicValue> dicValues) {
		this.dicValues = dicValues;
	}

	public boolean isUseDicValue() {
		return useDicValue;
	}

	public void setUseDicValue(boolean useDicValue) {
		this.useDicValue = useDicValue;
	}

	public String getDicType() {
		return dicType;
	}

	public void setDicType(String dicType) {
		this.dicType = dicType;
	}

	@Override
    public String toString()
    {
        return "Column [comment=" + comment + ", defaultValue=" + defaultValue + ", digits=" + digits + ", fieldName=" + fieldName + ", javaType=" + javaType
            + ", name=" + name + ", nullable=" + nullable + ", size=" + size + ", table=" + table + ", type=" + type + "]";
    }

	public boolean isGlobal() {
		boolean tmep = "createBy".equals(fieldName) ||
				"createTime".equals(fieldName) ||
				"updateBy".equals(fieldName) ||
				"updateTime".equals(fieldName) ||
				"enable".equals(fieldName) ||
				"remark".equals(fieldName) ||
				"id".equals(fieldName);
		return tmep;
//		return global;
	}
    
}
