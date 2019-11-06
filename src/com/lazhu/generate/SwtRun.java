package com.lazhu.generate;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.lazhu.generate.progress.CodeBuildRunProgress;
import com.lazhu.generate.progress.RefreshDataProgress;
import com.lazhu.generate.util.Resources;

/**
 * 图形化界面交互方式代码生成器
 * 
 * @author naxj
 */
public class SwtRun
{
//  dataBaseURL.setText("jdbc:oracle:thin:@hostname:1521:SID");
//	dataBaseURL.setText("jdbc:mysql://114.215.252.45/stock_crm_base?characterEncoding=utf8");
	
	//保险
	//private final String jdbcUrl = "jdbc:mysql://rm-bp1g5r765086nxgwxdo.mysql.rds.aliyuncs.com/insurance_crm?characterEncoding=utf8";
	//private final String jdbcUser = "crmroot";
	//private final String jdbcPassword ="insurance_CRM^_^@everyB0dy";
	
	
	
	//展业
	private final String jdbcUrl = "jdbc:mysql://rm-bp14p1zv95096lp12o.mysql.rds.aliyuncs.com:3306/extend?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=PRC&useSSL=false";
	private final String jdbcUser ="adminroot";
	private final String jdbcPassword ="Wts000ooo";
	
	 
     //passWord.setText("Cuishe111");
	//	dataBaseURL.setText("jdbc:mysql://rm-bp110dt5081manvc5o.mysql.rds.aliyuncs.com/wechat_collect?characterEncoding=utf8");
  			
	/*private final String jdbcUrl = "jdbc:mysql://rm-bp14p1zv95096lp12o.mysql.rds.aliyuncs.com:3306/extend?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=PRC&useSSL=false";
	private final String jdbcUser = "adminroot";
	private final String jdbcPassword = "Wts000ooo";*/
    Display display;
    
    Shell shell;
    
    // DataBase
    private final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    
    private final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    
    
    // 表名列表
    private Set<String> tableNameSet = new TreeSet<String>();
    
    // 界面组件
    private Combo dbTypeName;
    
    private Text dataBaseURL;
    
    private Text userName;
    
    private Text passWord;
    
    private Text filtertext;
    
    private List list;
    
    private Button refresh;
    
    private Button selectAll;
    
    private Button selectNone;
    
    private Button selectReverse;
    
    private Button overwrite;
    
    private Text packtext;
    
    private Text prefixtext;
    
    private Text projParentPath;
    
    private Button all;
    
    private Button html;
    
    private Text author;
    
    ResourceBundle copyright = ResourceBundle.getBundle("copyright");
    
    public SwtRun()
    {
        super();
        display = new Display();
        shell = new Shell(display, SWT.MIN | SWT.CLOSE);
        InputStream is = this.getClass().getResourceAsStream("/img/logo.png");
        if (is != null)
        {
            shell.setImage(new Image(display, is));
        }
        shell.setText(copyright.getString("title"));
        shell.setSize(540, 670);
        Rectangle screeRec = display.getBounds();
        Rectangle shellRec = shell.getBounds();
        if (shellRec.height > screeRec.height)
        {
            shellRec.height = screeRec.height;
        }
        if (shellRec.width > screeRec.width)
        {
            shellRec.width = screeRec.width;
        }
        shell.setLocation((screeRec.width - shellRec.width) / 2, (screeRec.height - shellRec.height) / 2);
        addMenu();
        setContent();
        shell.open();
        
        try
        {
            File file = new File("default.ini");
            InputStream fis;
            if (file.exists())
            {
                fis = new FileInputStream(file);
            }
            else
            {
                // 定位jar内资源
                fis = this.getClass().getResourceAsStream("/default.ini");
            }
            init(fis);
            MessageDialog.openInformation(shell, "确认", "加载默认配置文件成功！");
        }
        catch (Exception e)
        {
        }
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        display.dispose();
    }
    
    private void init(InputStream is)
        throws Exception
    {
        Properties config = new Properties();
        config.load(is);
        String driver = config.getProperty("driver");
        if (MYSQL_DRIVER.equals(driver))
        {
            dbTypeName.select(1);
        }
        else
        {
            dbTypeName.select(0);
        }
        dataBaseURL.setText(config.getProperty("url"));
        userName.setText(config.getProperty("username"));
        passWord.setText(config.getProperty("password"));
        packtext.setText(config.getProperty("packtext"));
        String protext = config.getProperty("protext", "");
        if (StringUtils.isEmpty(protext) || !new File(protext).exists())
        {
            projParentPath.setText(new File(" ").getAbsolutePath().trim());
        }
        else
        {
            projParentPath.setText(protext);
        }
        prefixtext.setText(config.getProperty("prefixtext", ""));
        overwrite.setSelection("true".equals(config.getProperty("overwrite", "false")));
        filtertext.setText("");
        tableNameSet.clear();
        list.removeAll();
        IOUtils.closeQuietly(is);
    }
    
    private void addMenu()
    {
        Menu m = new Menu(shell, SWT.BAR);
        // create a file menu and add an exit item
        MenuItem file = new MenuItem(m, SWT.CASCADE);
        file.setText("配置文件(&F)");
        file.setAccelerator(SWT.CTRL + 'F');
        Menu filemenu = new Menu(shell, SWT.DROP_DOWN);
        file.setMenu(filemenu);
        MenuItem openMenuItem = new MenuItem(filemenu, SWT.CASCADE);
        openMenuItem.setText("加载(&O)");
        openMenuItem.setAccelerator(SWT.CTRL + 'O');
        openMenuItem.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
                fileDialog.setText("请选择配置文件");
                fileDialog.setFilterExtensions(new String[] {"*.ini"});
                String filePath = fileDialog.open();
                if (filePath == null)
                {
                    return;
                }
                try
                {
                    InputStream is = new FileInputStream(filePath);
                    init(is);
                }
                catch (Exception e1)
                {
                    MessageDialog.openError(shell, "警告", "加载配置文件失败！");
                    return;
                }
            }
        });
        
        MenuItem saveMenuItem = new MenuItem(filemenu, SWT.CASCADE);
        saveMenuItem.setText("保存(&S)");
        saveMenuItem.setAccelerator(SWT.CTRL + 'S');
        saveMenuItem.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
                fileDialog.setText("选择文件");
                fileDialog.setFilterPath(new File(" ").getAbsolutePath().trim());
                fileDialog.setFileName("default.ini");
                fileDialog.setFilterExtensions(new String[] {"*.ini"});
                String fileName = fileDialog.open();
                if (fileName == null)
                {
                    return;
                }
                BufferedWriter writer = null;
                try
                {
                    String driver = (dbTypeName.getSelectionIndex() == 0 ? ORACLE_DRIVER : MYSQL_DRIVER);
                    String dburl = dataBaseURL.getText().trim();
                    String username = userName.getText().trim();
                    String password = passWord.getText().trim();
                    String packText = packtext.getText().trim();
                    String prefixText = prefixtext.getText().trim();
                    String proText = projParentPath.getText().trim();
                    boolean overWrite = overwrite.getSelection();
                    
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName))));
                    writer.newLine();
                    writer.write("driver=" + driver);
                    writer.newLine();
                    writer.write("url=" + dburl);
                    writer.newLine();
                    writer.write("username=" + username);
                    writer.newLine();
                    writer.write("password=" + password);
                    writer.newLine();
                    writer.write("packtext=" + packText);
                    writer.newLine();
                    writer.write("prefixtext=" + prefixText);
                    writer.newLine();
                    writer.write("overwrite=" + overWrite);
                    writer.newLine();
                    writer.write("protext=" + proText.replace("\\", "\\\\"));
                    writer.newLine();
                    writer.flush();
                    MessageDialog.openInformation(shell, "确认", "保存配置文件成功！ \n文件位置：" + fileName);
                    return;
                }
                catch (Exception e1)
                {
                    MessageDialog.openError(shell, "警告", "保存配置文件失败！");
                    return;
                }
                finally
                {
                    IOUtils.closeQuietly(writer);
                }
            }
        });
        
        new MenuItem(filemenu, SWT.SEPARATOR);
        MenuItem exitMenuItem = new MenuItem(filemenu, SWT.PUSH);
        exitMenuItem.setText("退出(&X)");
        exitMenuItem.setAccelerator(SWT.CTRL + 'X');
        exitMenuItem.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                System.exit(0);
            }
        });
        
        MenuItem help = new MenuItem(m, SWT.CASCADE);
        help.setText("帮助(&H)");
        help.setAccelerator(SWT.CTRL + 'H');
        Menu helpmenu = new Menu(shell, SWT.DROP_DOWN);
        help.setMenu(helpmenu);
        
        MenuItem useMenuItem = new MenuItem(helpmenu, SWT.PUSH);
        useMenuItem.setText("使用指南(&U)");
        useMenuItem.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent event)
            {
                MessageDialog.openInformation(shell, "使用指南", copyright.getString("instruction"));
            }
        });
        
        new MenuItem(helpmenu, SWT.SEPARATOR);
        MenuItem aboutMenuItem = new MenuItem(helpmenu, SWT.PUSH);
        aboutMenuItem.setText("关于本工具(&A)");
        aboutMenuItem.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent event)
            {
                MessageDialog.openInformation(shell, "关于本工具", copyright.getString("about"));
            }
        });
        shell.setMenuBar(m);
    }
    
    private void setContent()
    {
    	InetAddress addr = null;
        String hostName = "taoyang";
        /*try {
            addr = InetAddress.getLocalHost();//新建一个InetAddress类
            hostName = addr.getHostName().toString();// 获得本机名称
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        Group group1 = new Group(shell, SWT.NONE);
        group1.setText("数据库设置");
        group1.setBounds(10, 10, 510, 140);
        Label dataBaseLabel = new Label(group1, SWT.NONE);
        dataBaseLabel.setText("author注释:");
        dataBaseLabel.setBounds(20, 30, 60, 20);
        author = new Text(group1, SWT.BORDER);
        author.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        author.setBounds(80, 30, 100, 20);
        author.setText(hostName);
//        dbTypeName = new Combo(group1, SWT.DROP_DOWN | SWT.READ_ONLY);
//        dbTypeName.setBounds(80, 30, 100, 65);
//        String items[] = {" Oracle", " MySql"};
//        dbTypeName.setItems(items);
//        dbTypeName.select(0);
//        dbTypeName.addModifyListener(new ModifyListener()
//        {
//            @Override
//            public void modifyText(ModifyEvent modifyevent)
//            {
//                int index = dbTypeName.getSelectionIndex();
//                switch (index)
//                {
//                    case 0:
//                        dataBaseURL.setText("jdbc:oracle:thin:@hostname:1521:SID");
//                        break;
//                    case 1:
//                        dataBaseURL.setText("jdbc:mysql://127.0.0.1:3306/dbname");
//                        break;
//                }
//                tableNameSet.clear();
//                list.removeAll();
//            }
//        });
        
        Label sourceLabel = new Label(group1, SWT.NONE);
        sourceLabel.setText("   URL:");
        sourceLabel.setBounds(20, 70, 60, 20);
        dataBaseURL = new Text(group1, SWT.BORDER);
        dataBaseURL.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        dataBaseURL.setBounds(80, 70, 350, 20);
        dataBaseURL.setText(jdbcUrl);;
					  
        Label user = new Label(group1, SWT.NONE);
        user.setText("用户名:");
        user.setBounds(20, 100, 60, 20);
        userName = new Text(group1, SWT.BORDER);
        userName.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        userName.setBounds(80, 100, 100, 20);
        userName.setText(jdbcUser);
        
        Label pass = new Label(group1, SWT.NONE);
        pass.setText("密  码:");
        pass.setBounds(200, 100, 50, 20);
        passWord = new Text(group1, SWT.BORDER);
        passWord.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        //passWord.setEchoChar('●');
        passWord.setBounds(250, 100, 100, 20);
        passWord.setText(jdbcPassword);;
        
        Button test = new Button(group1, SWT.PUSH);
        test.setText(" 测 试 ");
        test.setBounds(370, 100, 60, 20);
        test.addSelectionListener(new DataListener());
        
        Group group2 = new Group(shell, SWT.NONE);
        group2.setText("数据表选择");
        group2.setBounds(10, 160, 510, 260);
        
        Label filter = new Label(group2, SWT.NONE);
        filter.setText("选择器:");
        filter.setBounds(20, 30, 60, 20);
        filtertext = new Text(group2, SWT.BORDER);
        filtertext.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        filtertext.setBounds(80, 30, 260, 20);
        filtertext.setToolTipText("输入空格表示条件 或 ");
        filtertext.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent modifyevent)
            {
                String nameParam = filtertext.getText().toLowerCase().trim();
                if ("".equals(nameParam))
                {
                    return;
                }
                while (nameParam.contains("  "))
                {
                    nameParam = nameParam.replace("  ", " ");
                }
                list.removeAll();
                String[] params = nameParam.split(" ");
                
                // Arr->List->Set
                Set<String> set = new HashSet<String>(Arrays.asList(params));
                for (String tableName : tableNameSet)
                {
                    for (String param : set)
                    {
                        if (tableName.contains(param))
                        {
                            list.add(tableName);
                            break;
                        }
                    }
                }
            }
        });
        
        Button clear = new Button(group2, SWT.PUSH);
        clear.setText(" 清  除 ");
        clear.setBounds(350, 30, 60, 20);
        clear.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent selectionevent)
            {
                filtertext.setText("");
                java.util.List<String> l = new ArrayList<String>(tableNameSet);
                list.setItems(l.toArray(new String[0]));
            }
        });
        
        Label tab = new Label(group2, SWT.NONE);
        tab.setText("表  名:");
        tab.setBounds(20, 130, 60, 20);
        list = new List(group2, SWT.BORDER | SWT.V_SCROLL | SWT.SIMPLE | SWT.MULTI);
        list.setBounds(80, 60, 330, 190);
        list.setToolTipText("选择需要生成代码的数据库表,支持多选!");
        list.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String[] ses = ((List)arg0.getSource()).getSelection();
				if (ses[ses.length-1].indexOf("_")>0){
					String qianZhui = StringUtils.substringBefore(ses[ses.length-1], "_");
					if (qianZhui!=null){
						packtext.setText("com.lazhu."+qianZhui);
						prefixtext.setText(qianZhui+"_");
						
					}
				}
				
			}
        	
        });
        
        refresh = new Button(group2, SWT.PUSH);
        refresh.setText("刷  新");
        refresh.setBounds(430, 80, 60, 30);
        refresh.addSelectionListener(new DataListener());
        
        selectAll = new Button(group2, SWT.PUSH);
        selectAll.setText("全  选");
        selectAll.setBounds(430, 120, 60, 30);
        selectAll.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent event)
            {
                list.selectAll();
            }
        });
        selectNone = new Button(group2, SWT.PUSH);
        selectNone.setText("不  选");
        selectNone.setBounds(430, 160, 60, 30);
        selectNone.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent event)
            {
                list.deselectAll();
            }
        });
        
        selectReverse = new Button(group2, SWT.PUSH);
        selectReverse.setText("反  选");
        selectReverse.setBounds(430, 200, 60, 30);
        selectReverse.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent event)
            {
                int count = list.getItemCount();
                for (int i = 0; i < count; i++)
                {
                    if (list.isSelected(i))
                    {
                        list.deselect(i);
                    }
                    else
                    {
                        list.select(i);
                    }
                }
            }
        });
        
        Group group3 = new Group(shell, SWT.NONE);
        group3.setBounds(10, 430, 510, 180);
        group3.setText("工程设置");
        
        Label pack = new Label(group3, SWT.NONE);
        pack.setText("源码包名:");
        pack.setBounds(20, 30, 60, 20);
        packtext = new Text(group3, SWT.BORDER);
        packtext.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        packtext.setBounds(80, 30, 160, 20);
        packtext.setText("com.lazhu.xxx");
        packtext.setToolTipText("dao、map、model 文件存放的源码路径， 类似于 com.lazhu.demo");
        packtext.addFocusListener(new FocusAdapter()
        {
            public void focusLost(FocusEvent focusevent)
            {
                String text = packtext.getText().toLowerCase().trim();
                packtext.setText(text);
            }
        });
        Label prefix = new Label(group3, SWT.NONE);
        prefix.setText("去除表名前缀:");
        prefix.setBounds(250, 30, 75, 20);
        prefixtext = new Text(group3, SWT.BORDER);
        prefixtext.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        prefixtext.setBounds(340, 30, 70, 20);
        
        overwrite = new Button(group3, SWT.CHECK);
        overwrite.setText("覆盖代码");
        overwrite.setSelection(true);
        overwrite.setBounds(430, 30, 67, 20);
        overwrite.setToolTipText("选中后，若原工程目录存在代码则覆盖，不会对原来的代码做备份操作");
        
        Label pro = new Label(group3, SWT.NONE);
        pro.setText("工程位置:");
        pro.setBounds(20, 60, 60, 20);
        projParentPath = new Text(group3, SWT.BORDER);
        projParentPath.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        projParentPath.setBounds(80, 60, 330, 20);
        projParentPath.setText(new File(" ").getAbsolutePath().trim());
        projParentPath.setEditable(false);
        projParentPath.setToolTipText("工程位置文件存放的路径，一般选为Java工程目录，默认值为当前目录");
        Button bakBrowse = new Button(group3, SWT.PUSH);
        bakBrowse.setText("选  择");
        bakBrowse.setBounds(430, 60, 60, 20);
        bakBrowse.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent event)
            {
                DirectoryDialog dialog = new DirectoryDialog(shell);
                dialog.setFilterPath(projParentPath.getText());
                String fileName = dialog.open();
                if (fileName != null)
                {
                    if (fileName.endsWith("\\"))
                    {
                        projParentPath.setText(fileName);
                    }
                    else
                    {
                        projParentPath.setText(fileName + "\\");
                    }
                }
            }
        });
        
        Label codeTyLabel = new Label(group3, SWT.NONE);
        codeTyLabel.setText("工程类型:");
        codeTyLabel.setBounds(20, 90, 60, 20);
        all = new Button(group3, SWT.RADIO);
        all.setText("全部代码");
        all.setBounds(80, 90, 90, 20);
        all.setSelection(true);
        html = new Button(group3, SWT.RADIO);
        html.setText("仅HTML代码");
        html.setBounds(200, 90, 120, 20);
        //spring.setSelection(true);
        
        Button run = new Button(group3, SWT.PUSH);
        run.setText("生 成 代 码");
        run.setBounds(200, 128, 140, 40);
        run.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent event)
            {
                String dburl = dataBaseURL.getText().trim();
                if (dburl.length() <= 0)
                {
                    MessageDialog.openError(shell, "警告", "数据库地址不可为空!");
                    dataBaseURL.setFocus();
                    return;
                }
                String username = userName.getText().trim();
                if (username.length() <= 0)
                {
                    MessageDialog.openError(shell, "警告", "用户名不可为空!");
                    userName.setFocus();
                    return;
                }
                String password = passWord.getText().trim();
//                if (password.length() <= 0)
//                {
//                    MessageDialog.openError(shell, "警告", "密码不可为空!");
//                    passWord.setFocus();
//                    return;
//                }
                
                int count = list.getSelectionCount();
                if (count < 1)
                {
                    MessageDialog.openError(shell, "警告", "请选择数据库表!");
                    list.setFocus();
                    return;
                }
                
                String packName = packtext.getText().toLowerCase().trim();
                if (packName.length() <= 0)
                {
                    MessageDialog.openError(shell, "警告", "源码包名不可为空!");
                    packtext.setFocus();
                    return;
                }
                
                if (!Pattern.matches("\\w+(\\.\\w+)+", packName))
                {
                    MessageDialog.openError(shell, "警告", "源码包名不符合规范,请重新填写!");
                    packtext.setFocus();
                    return;
                }
                
                String projectPath = projParentPath.getText().trim();
                if (projectPath.length() <= 0)
                {
                    MessageDialog.openError(shell, "警告", "工程位置不可为空!");
                    projParentPath.setFocus();
                    return;
                }
                Resources.AUTHOR = author.getText();
                String driver;
//                if (0 == dbTypeName.getSelectionIndex())
//                {
//                    driver = ORACLE_DRIVER;
//                }
//                else
//                {
                    driver = MYSQL_DRIVER;
//                }
                
                String projDir = projectPath + "project"+File.separator;
                File projDirFile = new File(projDir);
                
                if (!overwrite.getSelection())
                {
                    int i = 1;
                    while (projDirFile.exists())
                    {
                        projDir = projectPath + "project_" + (i++) + "\\";
                        projDirFile = new File(projDir);
                    }
                }
                else
                {
                    try
                    {
                        // 清空目录下的文件
                        if (projDirFile.exists())
                        {
                            FileUtils.cleanDirectory(projDirFile);
                        }
                    }
                    catch (IOException e2)
                    {
                    }
                }
                String prefix = prefixtext.getText().trim();
                String codeType = (all.getSelection() ? "all" : "html");
                try
                {
                    // 在当前目录，创建并运行脚本
                    IRunnableWithProgress runProgress =
                        new CodeBuildRunProgress(driver, dburl, username, password, packName, projDir, list.getSelection(), prefix, codeType);
                    new ProgressMonitorDialog(shell).run(true, false, runProgress);
                }
                catch (InvocationTargetException e)
                {
                    MessageDialog.openError(shell, "警告", "生成代码失败!" + e.getMessage());
                    return;
                }
                catch (InterruptedException e)
                {
                    MessageDialog.openInformation(shell, "确认", "生成代码被用户取消!");
                    return;
                }
                StringBuilder desc = new StringBuilder("\n生成java源码的数据库表共").append(list.getSelection().length).append(" 张！");
                MessageDialog.openInformation(shell, "确认", "生成代码成功！ \n文件位置：" + projDir + desc);
                if ("spring".equals(codeType) && MessageDialog.openConfirm(shell, "确认", "是否生成项目运行所需jar ！"))
                {
                    try
                    {
                        creatRunLib(projDir);
                    }
                    catch (IOException e)
                    {
                        MessageDialog.openError(shell, "错误", e.getMessage());
                        return;
                    }
                }
                if (MessageDialog.openConfirm(shell, "查看项目代码", "处理完成，是否直接查看生成的代码?"))
                {
                    try
                    {
                        Desktop.getDesktop().open(new File(projDir));
                    }
                    catch (IOException e)
                    {
                    }
                }
            }
            
            private void creatRunLib(String projectPath)
                throws IOException
            {
                String libPath = projectPath + "\\WebRoot\\WEB-INF\\lib\\";
                new File(libPath).mkdirs();
                
                URL url = SwtRun.class.getProtectionDomain().getCodeSource().getLocation();
                if (url.getPath().endsWith(".jar"))
                {
                    JarFile jarFile = new JarFile(url.getFile());
                    Enumeration<JarEntry> entrys = jarFile.entries();
                    while (entrys.hasMoreElements())
                    {
                        JarEntry jar = entrys.nextElement();
                        String name = jar.getName();
                        if (name.startsWith("lib/") && name.endsWith(".jar"))
                        {
                            InputStream in = SwtRun.class.getClassLoader().getResource(name).openStream();
                            OutputStream out = new FileOutputStream(libPath + new File(name).getName());
                            IOUtils.copy(in, out);
                            IOUtils.closeQuietly(in);
                            IOUtils.closeQuietly(out);
                        }
                    }
                    jarFile.close();
                }
                else
                {
                    FileUtils.copyDirectory(new File(url.getFile() + "/lib"), new File(libPath), false);
                }
            }
        });
        
    }
    
    class DataListener extends SelectionAdapter
    {
        public void widgetSelected(SelectionEvent event)
        {
            String dburl = dataBaseURL.getText().trim();
            if (dburl.length() <= 0)
            {
                MessageDialog.openError(shell, "警告", "数据库地址不可为空!");
                dataBaseURL.setFocus();
                return;
            }
            String username = userName.getText().trim();
            if (username.length() <= 0)
            {
                MessageDialog.openError(shell, "警告", "用户名不可为空!");
                userName.setFocus();
                return;
            }
            String password = passWord.getText().trim();
//            if (password.length() <= 0)
//            {
//                MessageDialog.openError(shell, "警告", "密码不可为空!");
//                passWord.setFocus();
//                return;
//            }
            String driver = MYSQL_DRIVER;
            try
            {
                IRunnableWithProgress runnable = new RefreshDataProgress(driver, dburl, username, password, tableNameSet);
                new ProgressMonitorDialog(shell).run(true, false, runnable);
                filtertext.setText("");
                list.removeAll();
                // set->List->Arr
                java.util.List<String> l = new ArrayList<String>(tableNameSet);
                list.setItems(l.toArray(new String[0]));
            }
            catch (InvocationTargetException e)
            {
                MessageDialog.openError(shell, "警告", e.getMessage());
            }
            catch (InterruptedException e)
            {
                MessageDialog.openInformation(shell, "Cancelled", "刷新操作被用户取消！");
            }
        };
    }
    
    /**
     * main
     * 
     * @param args
     * @see [类、类#方法、类#成员]
     */
    public static void main(String[] args)
    {
        new SwtRun();
    }
}
