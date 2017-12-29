package com.robot.test;

import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;



public class TestEntry extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	JLabel autoTestTitle = new JLabel("自动化测试");
	JButton autoTestBt = new JButton("开始");
	JLabel manualTestTitle = new JLabel("单项测试");
	JButton manualTestBt = new JButton("开始");
	JLabel logInfo = new JLabel("日志存放位置");
	JLabel logpath = new JLabel("");
	
	File logfile = null;
        
    static FileOutputStream in;
    AutoTest autoWindow;
    JFrame manualWindow;
    static String jarPath;
    
	public TestEntry() {
		System.out.println("TestEntry..");
		
		Runtime run=Runtime.getRuntime();//当前 Java 应用程序相关的运行时对象。  
        run.addShutdownHook(new Thread(){ //注册新的虚拟机来关闭钩子  
            @Override  
            public void run() {  
                //程序结束时进行的操作  
                System.out.println("程序结束调用");  
                
                try {
                    in.close();
                }catch (IOException e) {
                	e.printStackTrace();
                }
            }  
        });  
              		
        String jarWholePath = TestEntry.class.getProtectionDomain().getCodeSource().getLocation().getFile();  
		try {  
		    jarWholePath = java.net.URLDecoder.decode(jarWholePath, "UTF-8");  
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.toString()); 
		}  
		
		jarPath = new File(jarWholePath).getParentFile().getAbsolutePath(); 
				
		
		
		setSize(700, 700);
		setLocation(300, 300);
		setTitle("工厂测试");
		setLayout(null);
		
		add(autoTestTitle);
		autoTestTitle.setBounds(300, 200, 120, 50);
		autoTestTitle.setFont(new Font("Dialog", 1, 20));
		autoTestTitle.setForeground(Color.blue);
		add(autoTestBt);
		autoTestBt.setBounds(430, 200, 100, 50);
		autoTestBt.setFont(new Font("DiaLog", 1, 20));
		autoTestBt.setBackground(Color.GREEN);
		autoTestBt.setForeground(Color.blue);
		
		autoTestBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("auto window");
				autoWindow = new AutoTest();
				//setVisible(false);
				dispose();
				autoWindow.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e){
						 System.out.println("auto window close");
						 autoWindow.close();
				         setVisible(true);
					}
				}); 
			
				
			  }
		});
		
		add(manualTestTitle);
		manualTestTitle.setBounds(300, 400, 120, 50);
		manualTestTitle.setFont(new Font("Dialog", 1, 20));
		manualTestTitle.setForeground(Color.blue);
		
		add(manualTestBt);
		manualTestBt.setBounds(430, 400, 100, 50);
		manualTestBt.setFont(new Font("DiaLog", 1, 20));
		manualTestBt.setBackground(Color.GREEN);
		manualTestBt.setForeground(Color.blue);
		
		manualTestBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("manual window");
				manualWindow = new ManualTest();
				setVisible(false);
				manualWindow.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e){
				         setVisible(true);
				         
					}
				}); 
			
				
			  }
		});
		
		add(logInfo);
		logInfo.setBounds(300, 500, 130, 50);
		logInfo.setFont(new Font("Dialog", 1, 20));
		logInfo.setForeground(Color.blue);
		add(logpath);
		logpath.setBounds(430, 500, 600, 50);
		logpath.setFont(new Font("Dialog", 1, 20));
		logpath.setForeground(Color.blue);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		createLogFile();
		
	}
			
	private void createLogFile()
	{
		logfile = new File(jarPath + "\\javalog.txt");
		
		logpath.setText(jarPath + "\\javalog.txt");
		try {  
        	
            logfile.createNewFile(); // 创建文件  
            
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();
                        
        } 
        
        try {
            in = new FileOutputStream(logfile);
        }catch (FileNotFoundException e) {  
           // TODO Auto-generated catch block  
           e.printStackTrace();  
        } 
	}
	
		
	public static void writeLog(String str) {
		
        
		
        long time = System.currentTimeMillis();
        
            
            try {
            	//in.write(timebt, 0, 8);
                //in.write(bt, 0, bt.length);  
                StringBuffer sb = new StringBuffer();
                sb.append(String.valueOf(time) + " : "  + str + System.getProperty("line.separator"));
                in.write(sb.toString().getBytes("UTF-8"));
                
                // boolean success=true;  
                // System.out.println("写入文件成功");  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
         
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        new TestEntry();   
	}

	
	
}
