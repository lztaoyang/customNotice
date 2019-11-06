package com.lazhu.generate.core;

import com.lazhu.generate.db.Mysql;
import com.lazhu.generate.util.ConvertHandler;
import com.lazhu.generate.util.Resources;
import com.lazhu.generate.vo.Table;

public class GenerateFactory
{
    private Table table;
    
    private String tableName;
    
    public GenerateFactory()
    {
        this.tableName = Resources.TPL_TABLE_NAME;
    }
    
    /**
     * 
     */
    public GenerateFactory(String tableName)
    {
        this.tableName = tableName;
    }
    
    public GenerateFactory(Table table)
    {
        this.table = table;
    }
    
    private void init()
        throws Exception
    {
        Mysql db = new Mysql();
        table = db.getTable(tableName);
        ConvertHandler.tableHandle(table);
    }
    
    public void genJavaTemplate()
        throws Exception
    {
        if (table == null)
        {
            init();
        }
        new GenerateCode().generate(table);
    }
}
