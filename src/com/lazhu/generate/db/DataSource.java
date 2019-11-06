package com.lazhu.generate.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lazhu.generate.util.Resources;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * 数据库表信息工具
 * 
 * @author naxj
 */
public class DataSource
{
    Logger log = LoggerFactory.getLogger(getClass());
    
    private static MysqlDataSource dataSource = new MysqlDataSource();
    
    // 使用ThreadLocal存储当前线程中的Connection对象
    private ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
    
    /**
     * 获取数据库连接
     * 
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @see [类、类#方法、类#成员]
     */
    protected Connection getConnection()
    {
        dataSource.setUrl(Resources.JDBC_URL);
        dataSource.setUser(Resources.JDBC_USERNAME);
        dataSource.setPassword(Resources.JDBC_PASSWORD);
        Connection connection = threadLocal.get();
        try
        {
            if (connection == null)
            {
                // 把 connection绑定到当前线程上
                connection = dataSource.getConnection();
                threadLocal.set(connection);
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to get Mysql connection");
        }
        return connection;
    }
    
   
    
    /**
     * 释放数据库连接
     * 
     * @see [类、类#方法、类#成员]
     */
    public void freeConnection()
    {
        log.info("------释放数据库连接-------");
        try
        {
            Connection conn = threadLocal.get();
            if (conn != null)
            {
                conn.close();
                threadLocal.remove(); // 解除当前线程上绑定conn
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 测试
     * 
     * @param args
     * @throws SQLException
     * @see [类、类#方法、类#成员]
     */
    public static void main(String[] args)
        throws SQLException
    {
        DataSource util = new DataSource();

    }
}
