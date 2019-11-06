package com.lazhu.generate.progress;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.dbutils.DbUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.lazhu.generate.util.Resources;
import com.lazhu.generate.vo.DicValue;

/**
 * 
 * 刷新数据库表进度条线程
 * 
 * @author naxj
 */
public class RefreshDataProgress implements IRunnableWithProgress
{
    // DataBase
    final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    
    final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    
    private String url;
    
    private String userName;
    
    private String passWord;
    
    private String driver;
    
    // 表名列表
    private Set<String> tableNameSet = new TreeSet<String>();
    
    public RefreshDataProgress(String driver, String url, String userName, String passWord, Set<String> tableNameSet)
    {
        super();
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.passWord = passWord;
        this.tableNameSet = tableNameSet;
    }
    
    @Override
    public void run(IProgressMonitor monitor)
        throws InvocationTargetException, InterruptedException
    {
        monitor.beginTask("刷新数据", IProgressMonitor.UNKNOWN);
        monitor.subTask("获取数据库连接中......");
        Connection connection = null;
        try
        {
            Class.forName(driver);
            passWord= "".equals(passWord)?null:passWord;
            connection = DriverManager.getConnection(url, userName, passWord);

            monitor.subTask("获取数据表名列表中......");
            getAllTableName(driver, connection);
            monitor.done();
        }
        catch (ClassNotFoundException e)
        {
            throw new InvocationTargetException(e.getCause(), "加载数据库驱动失败!");
        }
        catch (SQLException e)
        {
            throw new InvocationTargetException(e.getCause(), "获取连接失败,请检查URL,用户名和密码,并确认网络状况!");
        }
        finally
        {
            DbUtils.closeQuietly(connection);
        }
    }
    public static Map<String,String> TableComments = new HashMap<String,String>();
    private void getAllTableName(String driver, Connection con)
        throws SQLException
    {
        // 清空原来保存的表名信息
        tableNameSet.clear();
        String sql = (ORACLE_DRIVER.equals(driver) ? "select table_name from user_tables order by table_name" : "show table status");
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next())
        {
        	tableNameSet.add(rs.getString("Name"));
        	TableComments.put(rs.getString("Name"), rs.getString("Comment"));
        }
        sql = "select id,type_,code_,code_text,sort_no from sys_dic where enable_ = 1 order by type_,sort_no";
        PreparedStatement pstmt2 = con.prepareStatement(sql);
        ResultSet rs2 = pstmt2.executeQuery();
        while (rs2.next())
        {
        	DicValue dicValue = new DicValue();
        	dicValue.setId(rs2.getLong("id"));
        	dicValue.setType(rs2.getString("type_"));
        	dicValue.setCode(rs2.getString("code_"));
        	dicValue.setCodeText(rs2.getString("code_text"));
        	dicValue.setSortNO(rs2.getInt("sort_no"));
        	List<DicValue> values = Resources.DIC_MAP.get(dicValue.getType());
        	if (values == null){
        		values = new ArrayList<DicValue>();
        		Resources.DIC_MAP.put(dicValue.getType(), values);
        	}
        	values.add(dicValue);
        }
        DbUtils.closeQuietly(pstmt2);
        DbUtils.closeQuietly(pstmt);
    }
}