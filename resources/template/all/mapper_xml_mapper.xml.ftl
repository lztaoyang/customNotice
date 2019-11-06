<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${pknDAO}.${className}Mapper">

	<select id="selectIdPage" resultType="java.lang.Long">
		select id from ${tableName}
		<where>
		<#list columns as column>
		<#if column.javaType == "String">
			<if test="cm.${column.fieldName} != null and cm.${column.fieldName} != ''">
			    and ${column.name} like concat(concat('%',${r"#{"}cm.${column.fieldName}}),'%')
			</if>
		<#elseif column.javaType == "Boolean">
			<if test="cm.${column.fieldName} != null">
				and ${column.name} = ${r"#{"}cm.${column.fieldName}}
			</if>
		<#else>
			<if test="cm.${column.fieldName} != null and cm.${column.fieldName} != ''">
				and ${column.name} = ${r"#{"}cm.${column.fieldName}}
			</if>
		</#if>
		</#list>
		</where>
	</select>
</mapper>
