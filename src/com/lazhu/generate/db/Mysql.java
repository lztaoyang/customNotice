package com.lazhu.generate.db;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lazhu.generate.progress.RefreshDataProgress;
import com.lazhu.generate.util.Resources;
import com.lazhu.generate.vo.Column;
import com.lazhu.generate.vo.DicValue;
import com.lazhu.generate.vo.Table;

/**
 * Mysql Metadata读取
 */
public class Mysql extends DataSource
{
    public Table getTable(String tableName)
        throws SQLException
    {
        Table t = new Table(tableName);
        t.setTableComment(RefreshDataProgress.TableComments.get(tableName));
        ResultSet rs = null;
        t.setColumns(new ArrayList<Column>());
        try
        {
            DatabaseMetaData dmd = getConnection().getMetaData();// 获取数据库的MataData信息
            rs = dmd.getColumns(null, "", tableName, "");
            getColumns(rs, t);
            rs = dmd.getPrimaryKeys(null, null, tableName);
            t.setPk(getPk(rs));
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            freeConnection();
        }
        return t;
    }
    
    /**
     * 获取所有列
     * 
     * @param rs
     * @param t
     * @throws SQLException
     */
    private void getColumns(ResultSet rs, Table t)
        throws SQLException
    {
        while (rs.next())
        {
            // 这里没有提供获取当前列是否主键/外键的信息提示
            Column col = new Column();
            String name = rs.getString("COLUMN_NAME");
            if (",create_by,create_time,update_by,update_time,enable_,remark_,id,".indexOf(","+name.toLowerCase()+",")>-1){
            	continue;
            }
            col.setName(name);
            col.setType(rs.getString("TYPE_NAME"));
            col.setSize(rs.getInt("COLUMN_SIZE"));
            col.setNullable(rs.getBoolean("NULLABLE"));
            col.setDigits(rs.getInt("DECIMAL_DIGITS"));
            col.setDefaultValue(rs.getString("COLUMN_DEF"));
            col.setComment(rs.getString("REMARKS"));
            
            List<DicValue> dicValues = Resources.DIC_MAP.get(name);
            if (dicValues!=null){
            	col.setDicValues(dicValues);
            	col.setUseDicValue(true);
            }
            t.getColumns().add(col);
        }
    }
    
    /**
     * 获取主键
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    private Column getPk(ResultSet rs)
        throws SQLException
    {
        Column pk = new Column();
        // ResultSetMetaData rsmd = rs.getMetaData();
        // int columnsCount = rsmd.getColumnCount();
        while (rs.next())
        {
            pk.setName(rs.getString("COLUMN_NAME"));
            // System.out.println(rs.getString("COLUMN_NAME"));
        }
        return pk;
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<Table> getTables()
        throws SQLException
    {
        List<Table> tables = new ArrayList();
        ResultSet rs = null;
        try
        {
            DatabaseMetaData dmd = getConnection().getMetaData();
            rs = dmd.getTables("", "", "%", null);
            while (rs.next())
            {
                Table t = new Table();
                t.setTableName(rs.getString("TABLE_NAME"));
                tables.add(t);
            }
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            freeConnection();
        }
        return tables;
    }
}
