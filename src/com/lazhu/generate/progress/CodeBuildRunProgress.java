package com.lazhu.generate.progress;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.lazhu.generate.core.GenerateFactory;
import com.lazhu.generate.util.Resources;

/**
 * 
 * 创建代码进度条线程
 * 
 * @author naxj
 */
public class CodeBuildRunProgress implements IRunnableWithProgress
{
    private String driver;
    
    private String dburl;
    
    private String username;
    
    private String password;
    
    private String packName;
    
    private String projectDir;
    
    private String projectSrcDir;
    
    private String[] tabName;
    
    private String prefix;
    
    private String codeType;
    
    // FreeMarker model
    Map<String, Object> model = new HashMap<String, Object>();
    
    public CodeBuildRunProgress(String driver, String dburl, String username, String password, String packName, String projectDir, String[] tabName,
        String prefix, String codeType)
    {
        super();
        this.driver = driver;
        this.dburl = dburl;
        this.username = username;
        this.password = password;
        this.packName = packName;
        this.projectDir = projectDir;
        this.projectSrcDir = projectDir + "src"+File.separator;
        this.tabName = tabName;
        this.prefix = prefix;
        this.codeType = codeType;
    }
    
    @Override
    public void run(IProgressMonitor monitor)
        throws InvocationTargetException, InterruptedException
    {
        // 在当前目录，创建并运行脚本
        try
        {
            model.put("driver", driver);
            model.put("dburl", dburl);
            model.put("username", username);
            model.put("password", password);
            model.put("packName", packName);
            model.put("projectSrcDir", projectSrcDir);
            model.put("tables", tabName);
            model.put("date", new Date());
            monitor.beginTask("生成代码", IProgressMonitor.UNKNOWN);
            monitor.subTask("自动生成model、dao、service 代码中......");
            creatAndRun(driver, dburl, username, password, packName, projectSrcDir, prefix, tabName);
            monitor.done();
        }
        catch (Exception e)
        {
            throw new InvocationTargetException(e.getCause(), e.getMessage());
        }
    }
    
    // 运行代码创建程序
    private void creatAndRun(String driver, String dburl, String username, String password, String packName, String projectSrcDir, String prefix,
        String[] tabNames)
        throws Exception
    {
    	prefix = prefix.replaceAll("_", "");
        for (String tableName : tabNames)
        {
            String className = camelCase(tableName, true);
            if (className.toLowerCase().startsWith(prefix.toLowerCase()))
            {
                className = className.substring(prefix.length());
            }
            if ("all".equals(codeType))
            {
                Resources.init(driver, dburl, username, password, tableName, packName, projectSrcDir, className, "all");
            }
            else if ("html".equals(codeType))
            {
                Resources.init(driver, dburl, username, password, tableName, packName, projectSrcDir, className, "html");
            }
            GenerateFactory factory = new GenerateFactory();
            factory.genJavaTemplate();
        }
        

    }
    
    /**
     * 根据模板代码生成文件
     * 
     * @param projectDir
     * @param packName
     * @param tabNames
     * @param model
     * @see [类、类#方法、类#成员]
     */
    private void creatTemplateCode1(String projectDir, String packName, String[] tabNames, Map<String, Object> model)
    {
        // 生成spring web相关配置文件
        Set<String> cfg = getFtlPath();
        for (String path : cfg)
        {
            try
            {
                String fileName = new File(path).getName();
                String newPath = projectDir + fileName.replace("_", "\\").replace(".ftl", "");
                File file = new File(newPath);
                if (!file.exists())
                {
                    String data = FreeMarkers.renderTemplate(model, fileName);
                    FileUtils.writeStringToFile(file, data, "UTF-8");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 获取模板路径信息
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    private Set<String> getFtlPath()
    {
        Set<String> cfg = new HashSet<String>();
        URL url = CodeBuildRunProgress.class.getProtectionDomain().getCodeSource().getLocation();
        if (url.getPath().endsWith(".jar"))
        {
            try
            {
                JarFile jarFile = new JarFile(url.getFile());
                Enumeration<JarEntry> entrys = jarFile.entries();
                while (entrys.hasMoreElements())
                {
                    JarEntry jar = entrys.nextElement();
                    String name = jar.getName();
                    if (StringUtils.countMatches(name, "/") == 1 && name.endsWith(".ftl"))
                    {
                        cfg.add(name);
                    }
                }
                jarFile.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            File tFile = new File(url.getFile() + "/template");
            for (File file : tFile.listFiles())
            {
                if (file.isFile())
                {
                    cfg.add(file.getAbsolutePath());
                }
            }
        }
        return cfg;
    }
    
    /**
     * 驼峰命名
     * 
     * @param input
     * @param firstCharacterUppercase
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static String camelCase(String input, boolean firstCharacterUppercase)
    {
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < input.length(); i++)
        {
            char c = input.charAt(i);
            switch (c)
            {
                case '_':
                case '-':
                case '@':
                case '$':
                case '#':
                case ' ':
                    if (sb.length() > 0)
                    {
                        nextUpperCase = true;
                    }
                    break;
                default:
                    if (nextUpperCase)
                    {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    }
                    else
                    {
                        sb.append(Character.toLowerCase(c));
                    }
                    break;
            }
        }
        if (firstCharacterUppercase)
        {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
        return sb.toString();
    }
    
}