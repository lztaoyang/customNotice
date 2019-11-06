<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${tableComment}管理</title>
    <!-- 引入样式 -->
    <link rel="stylesheet" href="/css/eui.1.3.0.css">
    <link rel="stylesheet" href="/css/bootstrap.3.3.7.css">
    <link href="https://cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <!--<link rel="stylesheet" href="css/docs.css">-->
    <!--<script src="js/jquery.min.1.12.4.js"></script>-->
    <script src="/js/lib/vue.2.3.2.js"></script>
    <script src="/js/lib/vuex.2.3.1.js"></script>
    <script src="/js/lib/vue-resource.1.3.1.js"></script>
    <script src="/js/lib/eui.1.3.0.js"></script>
    <script src="/js/lib/vue-html5-editor.js"></script>
    <script src="/js/lib/common.js"></script>
    <script src="/js/lib/vue-component-dic.js"></script>
</head>
<body>
<div id="app">
<!---------------------------页头导航 开始----------------------------------------------------------------------->
    <el-row style="margin:10px 10px 10px 10px;">
        <el-col :span="8">
            <strong>${tableComment}管理</strong>
        </el-col>
        <el-col :span="16">
            <el-breadcrumb separator="/" style="float:right">
                <el-breadcrumb-item>首页</el-breadcrumb-item>
                <el-breadcrumb-item>${tableComment}管理</el-breadcrumb-item>
            </el-breadcrumb>
        </el-col>
    </el-row>
<!---------------------------页头导航 结束----------------------------------------------------------------------->
<!---------------------------查询面板 开始----------------------------------------------------------------------->
    <el-collapse accordion>
        <el-collapse-item title="查询面板[点击展开]">
            <el-form :inline="true" :model="searchParams" label-position="right" style="margin-bottom: 0px" >
		<#list columns as column>
			<#if column.type=='DATE'>
				<el-form-item label="${column.comment}" style="font-size: 13px;" prop="${column.fieldName}">
		    		<el-date-picker type="date" size="small" placeholder="选择日期" v-model="searchParams.${column.fieldName}"></el-date-picker>
		    	</el-form-item>
	    	<#elseif column.type=='DATETIME'>
	    		<el-form-item label="${column.comment}" style="font-size: 13px;" prop="${column.fieldName}">
		    		<el-date-picker type="datetime" size="small" placeholder="选择日期时间" v-model="searchParams.${column.fieldName}"></el-date-picker>
		    	</el-form-item>
	    	<#elseif column.javaType=='Boolean'>
	    		<el-form-item label="${column.comment}" style="font-size: 13px;" prop="${column.fieldName}">
	    			<el-select v-model="searchParams.${column.fieldName}" clearable placeholder="请选择">
                        <el-option
                                v-for="item in [{label:'是',value:true},{label:'否',value:false}]"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
		    	</el-form-item>
	    	<#elseif column.useDicValue>
	    		<el-form-item label="${column.comment}" style="font-size: 13px;" prop="${column.fieldName}">
	    			<lz-dic-select dic-type="${column.name?upper_case}" v-model="searchParams.${column.fieldName}"></lz-dic-select>
	            </el-form-item>
	    	<#elseif column.type!='TEXT'>
	    		<el-form-item label="${column.comment}" style="font-size: 13px;" prop="${column.fieldName}">
		    		<el-input size="small" v-model="searchParams.${column.fieldName}" placeholder="${column.comment}" ></el-input>
		    	</el-form-item>
	        </#if>
		</#list>
                <el-form-item>
                    <el-button size="small" type="info" @click="readList">查询</el-button>
                </el-form-item>
            </el-form>
        </el-collapse-item>
    </el-collapse>
<!---------------------------查询面板 结束----------------------------------------------------------------------->
<!---------------------------数据表格 开始----------------------------------------------------------------------->
    <el-row style="margin:10px 10px 0px 10px;">
        <el-table id="12324"
                  :data="pageData.data"
                  ref="entityTable"
                  stripe
                  max-height="450px"
                  row-key="id"
                  empty-text="-"
                  append="加载中..."
                  :row-style="{'font-size':'13px'}"
                  style="width: 100%"
                  @selection-change="selectionChange"
        >
            <el-table-column
                    type="selection"
                    prop="id"
            >
            </el-table-column>
            
        <#list columns as column>
        	<#if column.type!='TEXT'>
            <el-table-column
                    prop="${column.fieldName}"
                    min-width="100"
                    v-bind:show-overflow-tooltip="true"
                    label="${column.comment}"
                    >
                    <#if column.useDicValue>
	                    <template scope="scope">
		                    <lz-dic-txt dic-type="${column.name?upper_case}" :dic-code="scope.row.${column.fieldName}"></lz-dic-txt>
		                </template>
                    </#if>
                    <#if column.javaType=="Boolean">
	                    <template scope="scope">
		                    <span>{{convertBoolean(scope.row.${column.fieldName})}}</span>
		                </template>
                    </#if>
                </el-table-column>
            </#if>
            
        </#list>
            <el-table-column
                    fixed="right"
                    label="操作"
                    width="170">
                <template scope="scope">
                    <el-button-group size="small">
                        <el-button size="small" type="warning" @click="showEditDialog(scope.row.id)">编辑</el-button>
                        <el-button size="small" type="danger" @click="del(scope.row.id)">删除</el-button>
                        <el-button size="small" type="primary" @click="readDetail(scope.row.id)">详情</el-button>
                    </el-button-group>
                </template>
            </el-table-column>
        </el-table>
    </el-row>
    <!-- 表尾 开始-->
    <el-row style="margin:10px 10px 0px 10px;">
        <el-col :span="8">
            <el-button size="small" type="info" @click="showAddDialog">新增</el-button>
            <el-button size="small" type="danger" @click="delBatch">批量删除</el-button>
        </el-col>
        <el-col :span="16" class="text-right">
        	<!-- 分页 -->
            <el-pagination ref="entityTablePage"
                           :current-page="pageData.current"
                           :page-sizes="[10, 50, 100]"
                           :page-size="pageData.size"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="pageData.iTotalRecords"
                           @size-change="readList"
                           @current-change="readList"
            >
            </el-pagination>
        </el-col>
    </el-row>
    <!-- 表尾 结束-->
<!---------------------------数据表格 结束----------------------------------------------------------------------->
    <hr style="margin:0px ;"/>
<!---------------------------弹出编辑窗口 开始----------------------------------------------------------------------->
    <el-dialog :title="entity.id?'编辑':'新增'" :visible.sync="editFormVisible">
        <el-form ref="editForm" :model="entity" :rules="rules">
        <el-input size="small" v-model="entity.id" v-show="false" ></el-input>
    <#list columns as column>
    	<el-col :span="12">
        <el-form-item label="${column.comment}" label-width="100px" prop="${column.fieldName}">
        <#if column.useDicValue>
        	<lz-dic-select dic-type="${column.name?upper_case}" v-model="entity.${column.fieldName}"></lz-dic-select>
    	<#elseif column.type=='DATE'>
    		<el-date-picker type="date" size="small" placeholder="选择日期" v-model="entity.${column.fieldName}"></el-date-picker>
    	<#elseif column.type=='DATETIME'>
    		<el-date-picker type="datetime" size="small" placeholder="选择日期时间" v-model="entity.${column.fieldName}"></el-date-picker>
    	<#elseif column.javaType=='Boolean'>
    		<el-switch on-text="是" on-value="true" off-text="否" off-value="false" v-model="entity.${column.fieldName}"></el-switch>
    	<#elseif column.javaType=='Integer' || column.javaType=='Long' || column.javaType=='Double' || column.javaType=='Float' || column.javaType=='BigDecimal'>
    		<el-input type="number" size="small" v-model="entity.${column.fieldName}" placeholder="${column.comment}" ></el-input>
    	<#elseif column.type=='TEXT'>
			<vue-html5-editor :content="entity.${column.fieldName}" :height="300" show-module-name="true"
                                   ref="editor" @change="on${column.fieldName}Change"></vue-html5-editor>
        <#else>
            <#if column.size < 100 >
    			<el-input size="small" v-model="entity.${column.fieldName}" placeholder="${column.comment}" ></el-input>
    		<#else>
    			<el-input type="textarea" :rows="2" size="small" placeholder="${column.comment}" v-model="entity.${column.fieldName}"></el-input>
    		</#if>
        </#if>
        </el-form-item>
        </el-col>
    </#list>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="editFormVisible = false">取 消</el-button>
            <el-button size="small" type="info"  @click="save">保 存</el-button>
        </div>
    </el-dialog>
<!---------------------------弹出编辑窗口 结束----------------------------------------------------------------------->
<!---------------------------弹出详情窗口 开始----------------------------------------------------------------------->
    <el-dialog title="详情" :visible.sync="detailFormVisible">
        <el-form ref="detailForm" :model="entity" label-position="left">
		    <#list columns as column>
            <el-col :span="12">
		    	<el-form-item label="${column.comment}">
		    	<#if column.useDicValue>
		    		<lz-dic-txt dic-type="${column.name?upper_case}" :dic-code="entity.${column.fieldName}"></lz-dic-txt>
		    	<#elseif column.javaType=='Boolean'>
                    <span>{{ convertBoolean(entity.${column.fieldName}) }}</span>
		    	<#elseif column.type=='TEXT'>
                    <p v-html="entity.${column.fieldName}"></p>
                <#else>
                	<span>{{ entity.${column.fieldName} }}</span>
            	</#if>
            	</el-form-item>
        	</el-col>
		    </#list>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="detailFormVisible = false">关闭</el-button>
        </div>
    </el-dialog>
<!---------------------------弹出详情窗口 结束----------------------------------------------------------------------->
</div>
</body>
<script src="/js/model/${instanceName}_index.js"></script>
</html>