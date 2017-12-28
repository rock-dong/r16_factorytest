package com.robot.test;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


//import com.robot.test.TestEntry.Test_Type;

public class ManualTest extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final boolean testOnGoing = true;
	JLabel usbTestTitle =  new JLabel("USB 连接");
	JButton usbTestBt = new JButton("开始测试");
	JLabel usbTestResult = new JLabel("");
	
	JLabel audioTestTitle =  new JLabel("声音");
	JButton audioTestBt = new JButton("开始测试");
	JLabel audioTestResult = new JLabel("");
	
	JButton volume0Bt = new JButton("");
	JButton volume1Bt = new JButton("");
	JButton volume2Bt = new JButton("");
	JButton volume3Bt = new JButton("");
	JButton volume4Bt = new JButton("");
	
	JLabel audioVolumeTitle = new JLabel("音量调节");
	JButton volumeDownBt = new JButton("-");
	JButton volumeUpBt = new JButton("+");
	JLabel volumeResult = new JLabel("");
	
	JLabel cameraTestTitle = new JLabel("摄像头");
	JButton cameraTestBt = new JButton("开始测试");
	JLabel cameraTestResult = new JLabel("");
	MyPanel cameraArea = new MyPanel();
	
	JLabel gyroTestTitle = new JLabel("角速度");
	JButton gyroTestBt = new JButton("开始测试");
	JLabel gyroTestResult0 = new JLabel(""); 
	JLabel gyroTestResult1 = new JLabel("");
	JLabel gyroTestResult2 = new JLabel("");
	
	JLabel acceTestTitle = new JLabel("加速度");
	JButton acceTestBt = new JButton("开始测试");
	JLabel acceTestResult0 = new JLabel(""); 
	JLabel acceTestResult1 = new JLabel("");
	JLabel acceTestResult2 = new JLabel("");
	
	JLabel logline0 = new JLabel("");
	JLabel logline1 = new JLabel("");
	JLabel logline2 = new JLabel("");
	JLabel logline3 = new JLabel("");
	JLabel logline4 = new JLabel("");
	JLabel logline5 = new JLabel("");
	
	JLabel uart1TestTitle = new JLabel("串口 1");
	JButton uart1TestBt = new JButton("开始测试");
	JLabel uart1TestResult = new JLabel();
	
	JLabel uart2TestTitle = new JLabel("串口 2");
	JButton uart2TestBt = new JButton("开始测试");
	JLabel uart2TestResult = new JLabel();
	
	boolean onlyOnce = false;
	int volumeGlobal = 0;
	
	static Object audioTestLock =  new Object();
	
	//MyStack<String> stack = new MyStack<String>(); 
	private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	private LinkedBlockingQueue<String> flashqueue = new LinkedBlockingQueue<String>();
	int usbTestCnt = 0;	
	int audioTestCnt = 0;
	int cameraTestCnt = 0;
	int uart1TestCnt = 0;
	int uart2TestCnt = 0;
	
	int audioFlashCnt = 0;
	int cameraFlashCnt = 0;
	
	private enum Test_Type {
        NONE,USB_TEST, AUDIO_TEST, CAMERA_TEST, GSENSOR_TEST, UART_EVENT, AUTO_EVENT}
    
    static Test_Type testState = Test_Type.NONE;
    
    
    
	public ManualTest() {
				  		
			deleteCameraFiles();
			
			setSize(700, 700);
			setLocation(300, 300);
			setTitle("单项测试");
			setLayout(null);
			
			add(usbTestTitle);
			usbTestTitle.setBounds(30, 30, 80, 25);
			add(usbTestBt);
			usbTestBt.setBounds(115, 30, 100, 25);
			usbTestBt.setBackground(Color.green);
			usbTestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("usb test start");
					if(testState != Test_Type.NONE) {
						usbTestResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put("usb test");
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
			add(usbTestResult);
			usbTestResult.setBounds(220, 30, 120, 25);
			
			add(logline0);
			logline0.setBounds(350, 30, 600, 25);
			add(logline1);
			logline1.setBounds(350, 60, 600, 25);
			add(logline2);
			logline2.setBounds(350, 90, 600, 25);
			add(logline3);
			logline3.setBounds(350, 120, 600, 25);
			add(logline4);
			logline4.setBounds(350, 150, 600, 25);
			
			add(audioTestTitle);
			audioTestTitle.setBounds(30, 60, 80, 25);
			add(audioTestBt);
			audioTestBt.setBounds(115, 60, 100, 25);
			audioTestBt.setBackground(Color.green);
			audioTestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("audio test start");
					if(testState != Test_Type.NONE) {
						audioTestResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put("audio test");
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
			add(audioTestResult);
			audioTestResult.setBounds(220, 60, 120, 25);
			
			
			add(volume0Bt);
			volume0Bt.setBounds(115, 90, 20, 25);
	        volume0Bt.setBackground(Color.blue);
			volume0Bt.setBorderPainted(false);
			volume0Bt.setFocusPainted(false);
			volume0Bt.setEnabled(false);
			
			add(volume1Bt);
			volume1Bt.setBounds(135, 90, 20, 25);
			volume1Bt.setBackground(Color.blue);
			volume1Bt.setBorderPainted(false);
			volume1Bt.setFocusPainted(false);
			volume1Bt.setEnabled(false);
			
			add(volume2Bt);
			volume2Bt.setBounds(155, 90, 20, 25);
			volume2Bt.setBackground(Color.blue);
			volume2Bt.setBorderPainted(false);
			volume2Bt.setFocusPainted(false);
			volume2Bt.setEnabled(false);
			
			add(volume3Bt);
			volume3Bt.setBounds(175, 90, 20, 25);
			volume3Bt.setBackground(Color.white);
			volume3Bt.setBorderPainted(false);
			volume3Bt.setFocusPainted(false);
			volume3Bt.setEnabled(false);
			
			add(volume4Bt);
			volume4Bt.setBounds(195, 90, 20, 25);
			volume4Bt.setBackground(Color.white);
			volume4Bt.setBorderPainted(false);
			volume4Bt.setFocusPainted(false);
			volume4Bt.setEnabled(false);
			
			
			add(audioVolumeTitle);
			audioVolumeTitle.setBounds(30, 120, 80, 25);
			
			add(volumeDownBt);
			volumeDownBt.setBounds(115, 120, 45, 25);
			volumeDownBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("volume down");
					if(testState != Test_Type.NONE) {
						volumeResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put("volume down");
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
			
			add(volumeUpBt);
			volumeUpBt.setBounds(170, 120, 45, 25);
			volumeUpBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("volume up");
					if(testState != Test_Type.NONE) {
						volumeResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put("volume up");
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
			
			add(volumeResult);
			volumeResult.setBounds(220, 120, 120, 25);
			
			
			add(cameraTestTitle);
			cameraTestTitle.setBounds(30, 150, 80, 25);
			add(cameraTestBt);
			cameraTestBt.setBounds(115, 150, 100, 25);
			cameraTestBt.setBackground(Color.green);
			cameraTestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("camera capture");
					if(testState != Test_Type.NONE) {
						cameraTestResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put("camera capture");
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
			
			add(cameraArea);
	        
	        cameraArea.setBounds(220, 150, 320, 240);
	        add(cameraTestResult);
	        cameraTestResult.setBounds(115, 180, 100, 25);
			//add(cameraShow);
			//cameraShow.setBounds(220, 150, 320, 240);
			
			add(gyroTestTitle);
			gyroTestTitle.setBounds(30, 400, 80, 25);
	        add(gyroTestBt);
	        gyroTestBt.setBounds(115, 400, 100, 25);
	        gyroTestBt.setBackground(Color.green);
	        gyroTestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("camera capture");
					if(testState != Test_Type.NONE) {
						gyroTestResult0.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put("gyro test");
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
	        
	        add(gyroTestResult0);
	        gyroTestResult0.setBounds(220, 400, 100, 25);
	        add(gyroTestResult1);
	        gyroTestResult1.setBounds(220, 430, 100, 25);
	        add(gyroTestResult2);
	        gyroTestResult2.setBounds(220, 460, 100, 25);
	        
	        
	        add(acceTestTitle);
			acceTestTitle.setBounds(30, 500, 80, 25);
	        add(acceTestBt);
	        acceTestBt.setBounds(115, 500, 100, 25);
	        acceTestBt.setBackground(Color.green);
	        acceTestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("acce test");
					if(testState != Test_Type.NONE) {
						acceTestResult0.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put("acce test");
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
	        
	        add(acceTestResult0);
	        acceTestResult0.setBounds(220, 500, 100, 25);
	        add(acceTestResult1);
	        acceTestResult1.setBounds(220, 530, 100, 25);
	        add(acceTestResult2);
	        acceTestResult2.setBounds(220, 560, 100, 25);
	        
	        add(uart1TestTitle);
	        uart1TestTitle.setBounds(30, 590, 80, 25);
	        add(uart1TestBt);
	        uart1TestBt.setBounds(115, 590, 100, 25);
	        uart1TestBt.setBackground(Color.green);
	        uart1TestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("uart1 test");
					if(testState != Test_Type.NONE) {
						uart1TestResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put("uart1 test");
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
	        add(uart1TestResult);
	        uart1TestResult.setBounds(220, 590, 100, 25);
	        
	        add(uart2TestTitle);
	        uart2TestTitle.setBounds(30, 620, 80, 25);
	        add(uart2TestBt);
	        uart2TestBt.setBounds(115, 620, 100, 25);
	        uart2TestBt.setBackground(Color.green);
	        uart2TestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("uart2 test");
					if(testState != Test_Type.NONE) {
						uart1TestResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put("uart2 test");
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
	        
	        add(uart2TestResult);
	        uart2TestResult.setBounds(220, 620, 100, 25);
	        
	        
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
			
			testThread  tTh = new testThread();
			Thread thread1 = new Thread(tTh);
			thread1.start();
			
			flashThread  tThFlash = new flashThread();
			Thread thread2 = new Thread(tThFlash);
			thread2.start();
			
	}
	
	public String getAdbDevices() {
    	//String command = "adb shell cameratest 640 480 photo 1 /tmp NV21";
    	String command = "adb devices";
    	System.out.println(command);
    	String deviceName = null;
    	//ArrayList<String> devices = new ArrayList<String>();
        testState = Test_Type.USB_TEST;
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	    String line = bufferedReader.readLine();
    	    while(line != null) {
    	        System.out.println(line);
    	        if(line.endsWith("device")){
    	            deviceName = line.substring(0, line.length() - "device".length()).trim();
    	            break;
    	        }
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
    	testState = Test_Type.NONE;
    	return deviceName;
    }
	
	public String audioAdbTest() {
    	String command = "adb shell tinyplayer /media/boot.mp3";
    	
    	testState = Test_Type.AUDIO_TEST;
    	
    	System.out.println(command);
    	
    	String result = "失败";
    	
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	    String line = bufferedReader.readLine();
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.endsWith("(quit_flag:0)")){
    	        	result = "pass";
    	        	break;
    	        } else if(line.endsWith("directory")) {
    	        	result = "音乐文件未找到";
    	        	break;
    	        } else if(line.endsWith("found")){
    	        	result = "音乐软件未找到";
    	            break;
    	        }
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
    	
    	testState = Test_Type.NONE;
    	
    	return result;
    }
	
	
	public int getAudioVolume() {
    	String command = "adb shell amixer get 'speaker volume control'";
    	int volume = 0;
    	testState = Test_Type.AUDIO_TEST;
    	
    	System.out.println(command);
    	    	   	
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.endsWith("%]")){
    	        	
    	        	int startIndex = line.indexOf(":");
    	        	int endIndex = line.indexOf("[");
    	            String volumeNum = line.substring(startIndex+1, endIndex).trim();
    	        	   	        	
    	        	System.out.println( "current volume is " + volumeNum);
    	        	
    	        	volume = Integer.parseInt(volumeNum);
    	        	
    	        	if(0 == (volume %12)) {
    	        		 volume = volume / 12;
    	        	} else {
    	        	     volume = volume / 12 + 1;
    	        	}
    	        	
    	        	volume = Math.min(volume, 5);
    	        	
    	        	System.out.println( "cover 0-5 volume is " + volume);
    	        	
    	        	break;
    	        } 
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
    	
    	testState = Test_Type.NONE;
    	
    	return volume;
    }
	
	
	public void showVolume(int value) {
		
		
		switch(value) {
		    case 0: {
		    	volume0Bt.setBackground(Color.white);
				volume1Bt.setBackground(Color.white);
				volume2Bt.setBackground(Color.white);
				volume3Bt.setBackground(Color.white);
				volume4Bt.setBackground(Color.white);
				
		        break;	
		    }
		    case 1: {
		    	volume0Bt.setBackground(Color.blue);
				volume1Bt.setBackground(Color.white);
				volume2Bt.setBackground(Color.white);
				volume3Bt.setBackground(Color.white);
				volume4Bt.setBackground(Color.white);
				
		    	break;
		    }
		    case 2 : {
		     	volume0Bt.setBackground(Color.blue);
				volume1Bt.setBackground(Color.blue);
				volume2Bt.setBackground(Color.white);
				volume3Bt.setBackground(Color.white);
				volume4Bt.setBackground(Color.white);
		    	break;
		    }
		    case 3 :{
		     	volume0Bt.setBackground(Color.blue);
				volume1Bt.setBackground(Color.blue);
				volume2Bt.setBackground(Color.blue);
				volume3Bt.setBackground(Color.white);
				volume4Bt.setBackground(Color.white);
		    	break;
		    }
		    case 4: {
		     	volume0Bt.setBackground(Color.blue);
				volume1Bt.setBackground(Color.blue);
				volume2Bt.setBackground(Color.blue);
				volume3Bt.setBackground(Color.blue);
				volume4Bt.setBackground(Color.white);
		    	break;
		    }
		    case 5:{
		     	volume0Bt.setBackground(Color.blue);
				volume1Bt.setBackground(Color.blue);
				volume2Bt.setBackground(Color.blue);
				volume3Bt.setBackground(Color.blue);
				volume4Bt.setBackground(Color.blue);
				break;
		    }
		}
	}
	
	public boolean setAudioVolume(int value) {
    	String command = "adb shell amixer set 'speaker volume control' ";
    	
    	testState = Test_Type.AUDIO_TEST;
    	
    	
    	System.out.println("volume <=== " + value);
    	
    	command  = command + Integer.toString(value * 12);
    	
    	System.out.println(command);
    	
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.endsWith("%]")){
    	        	
    	        	int startIndex = line.indexOf(":");
    	        	int endIndex = line.indexOf("[");
    	            String volumeNum = line.substring(startIndex+1, endIndex).trim();
    	        	   	        	
    	        	System.out.println( "current volume is " + volumeNum);
    	        	    	        	  	        	
    	        	break;
    	        } 
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
    	
    	testState = Test_Type.NONE;
    	
    	return true;
    }
	
	
	public void removeYuvInDevice() {
        String command = "adb shell rm /tmp/source_data1.yuv ";
    	
    	testState = Test_Type.CAMERA_TEST;
    	    	
    	System.out.println(command);
    	
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.endsWith("finish!")){
    	        	    	        	  	        	
    	        	break;
    	        } 
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
    	
    	testState = Test_Type.NONE;
    	return;
	}
	
	
	public boolean checkCameraDriver() {
		boolean ret = false;
		String command = "adb shell ls /dev/video0 ";
		testState = Test_Type.CAMERA_TEST;
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.contains("video0")){
    	        	System.out.println("find camera driver");
    	        	TestEntry.writeLog("find camera driver");
    	        	ret = true;    	        	                    	        	
    	        	break;
    	        } 
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
        testState = Test_Type.NONE;
    	
    	return ret;
	}
	
	
	public boolean startCameraCapture() {
    	boolean ret = false;
		String command = "adb shell cameratest 640 480 photo 1 /tmp NV21 ";
    	
    	testState = Test_Type.CAMERA_TEST;
    	    	
    	System.out.println(command);
    	
    	TestEntry.writeLog("startCameraCapture ....");
    	
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.endsWith("finish!")){
    	        	ret = true;   
    	        	TestEntry.writeLog("cameratest cmd ok");
    	        	break;
    	        } 
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	    TestEntry.writeLog(e.toString());
    	}
    	
    	try{
    		Thread.sleep(500);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    		TestEntry.writeLog(e.toString());
    	}
    	
    	if( !ret ){
    		testState = Test_Type.NONE;
    		System.out.println("camera capture fail");
    		TestEntry.writeLog("cameratest fail");
    		return ret;
    	}
    	
    	//ret = false;
    	TestEntry.writeLog("ignore adb result check");
    	
    	command = "adb pull /tmp/source_data1.yuv " + TestEntry.jarPath;
    	System.out.println(command);
    	
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.endsWith("s)")){  	
    	        	ret = true;
    	        	//writeLog("adb pull yuv ok");
    	        	break;
    	        } 
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	    TestEntry.writeLog(e.toString());
    	}
    	
    	testState = Test_Type.NONE;
    	
    	return ret;
    }
	
	private void deleteCameraFiles() {
		System.out.println("deleteCameraFiles ...");
		//writeLog("deleteCameraFiles ...");
		File file = new File(TestEntry.jarPath + "\\source_data1.yuv"); // The input NV21 file
		if (file.exists()){
		    delete(file);
		}
		
		file = new File(TestEntry.jarPath + "\\nv21.bmp");
		if (file.exists()){
		    delete(file);
		}
		
		file = new File(TestEntry.jarPath + "\\javalog.txt");
		if (file.exists()){
		    delete(file);
		}
	}
	
	public boolean convertYuvToBmp() {
		TestEntry.writeLog("covertYuvToBmp +++");
		File file = new File(TestEntry.jarPath + "\\source_data1.yuv"); // The input NV21 file
		if (!file.exists()){
			TestEntry.writeLog("covertYuvToBmp not found---");
			return false;
		}

		// BMP file info
		//int width = 2048, height = 1536;
		int width = 640, height = 480;
		short pixelBits = 32;

		try {
			// Read all bytes
			byte[] bytes = Files.readAllBytes(file.toPath());

			int[] data = NV21.yuv2rgb(bytes, width, height);
			BMP bmp = new BMP(width, height, pixelBits, data);
			bmp.saveBMP(TestEntry.jarPath + "\\nv21.bmp"); // The output BMP file

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TestEntry.writeLog(e.toString());
			TestEntry.writeLog("covertYuvToBmp io exception");
			return false;
		}

		TestEntry.writeLog("covertYuvToBmp  is done");
		
		System.out.println("Conversion is done.");
		return true;
	}
	
	public void showBmp(){

		System.out.println("shouwBmp ...");
		TestEntry.writeLog("showBmp ...");
/*
		MyPanel mp =new MyPanel();

        add(mp);
        System.out.println("add bmp ...");
        //this.setSize(640, 480);
        //mp.setBounds(0, 0, 640, 480);
        mp.setBounds(220, 150, 640, 480);
        
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         * cameraArea.setVisible(true);
*/
        cameraArea.repaint();
        
        System.out.println("showBmp ...");
    }
	
	
	
	
	
	
	private class MyPanel extends JPanel{

	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Image image=null;
     
        @Override
	    public void paint(Graphics g){
        	System.out.println("paint ...");
	        try {

	        	File bmpfile = new File(TestEntry.jarPath + "\\nv21.bmp");
	        	if(bmpfile.exists()){
	                image=ImageIO.read(bmpfile);

	                //g.drawImage(image, 220, 150, 640, 480, null);
                    if(image != null) {
	                    g.drawImage(image, 0, 0, 320, 240, null);
                    }else {
                        g.clearRect(0, 0, 320, 240);
                    }
	        	}else {
	        		g.clearRect(0, 0, 320, 240);
	        	}

	        } catch (Exception e) {

	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }

	}
	
	
	public static void delete(File file){
        if(file.isFile()){
            file.delete();
        }

        if(file.isDirectory()){
            File[] childFiles = file.listFiles();
            if(childFiles == null || childFiles.length == 0){
                file.delete();
                return;
            }

            for(int i = 0; i < childFiles.length; i++){
                delete(childFiles[i]);
            }
            file.delete();
        }

    }
	
	
	public boolean checkGyro() {
		boolean ret = false;
		String command = "adb shell cat /sys/class/input/input4/name";
		testState = Test_Type.GSENSOR_TEST;
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.contains("bmg160")){
    	        	System.out.println("find gyro driver");
    	        	ret = true;    	        	                    	        	
    	        	break;
    	        } 
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
        testState = Test_Type.NONE;
    	
    	return ret;
	}
	
	public boolean controlGyro(boolean onoff) {
		boolean ret = false;
		String command;
		if(onoff)
		    command = "adb shell \"echo 1 > /sys/class/input/input4/enable\"";
		else
			command = "adb shell \"echo 0 > /sys/class/input/input4/enable\"";
		
		testState = Test_Type.GSENSOR_TEST;
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
        testState = Test_Type.NONE;
    	
    	return ret;
	}
	
	public boolean showGyrodata() {
		boolean ret = false;
		String command = "adb shell getevent";
		int num = 0;
		testState = Test_Type.GSENSOR_TEST;
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null && num < 30) {
    	        System.out.println(line);  
    	        
    	        if(line.contains("dev/input/event4")){
    	        	if(line.contains("0003 0000")){
    	        		int startIndex = line.indexOf("0000");
    	        		String content = line.substring(startIndex, line.length());
    	        		gyroTestResult0.setText(content);
    	        	}
    	        	if(line.contains("0003 0001")){
    	        		int startIndex = line.indexOf("0001");
    	        		String content = line.substring(startIndex, line.length());
    	        		gyroTestResult1.setText(content);
    	        	}
    	        	if(line.contains("0003 0002")){
    	        		int startIndex = line.indexOf("0002");
    	        		String content = line.substring(startIndex, line.length());
    	        		gyroTestResult2.setText(content);
    	        	}
    	        	num ++;
    	        }
    	        
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
        testState = Test_Type.NONE;
    	
    	return ret;
	}
	
	public boolean controlAcce(boolean onoff) {
		boolean ret = false;
		String command;
		if(onoff)
		    command = "adb shell \"echo 1 > /sys/class/input/input2/enable\"";
		else
			command = "adb shell \"echo 0 > /sys/class/input/input2/enable\"";
		
		testState = Test_Type.GSENSOR_TEST;
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
        testState = Test_Type.NONE;
    	
    	return ret;
	}
	
	public boolean showAccedata() {
		boolean ret = false;
		String command = "adb shell getevent";
		int num = 0;
		testState = Test_Type.GSENSOR_TEST;
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null && num < 30) {
    	        System.out.println(line);  
    	        
    	        if(line.contains("dev/input/event2")){
    	        	if(line.contains("0003 0000")){
    	        		int startIndex = line.indexOf("0000");
    	        		String content = line.substring(startIndex, line.length());
    	        		acceTestResult0.setText(content);
    	        	}
    	        	if(line.contains("0003 0001")){
    	        		int startIndex = line.indexOf("0001");
    	        		String content = line.substring(startIndex, line.length());
    	        		acceTestResult1.setText(content);
    	        	}
    	        	if(line.contains("0003 0002")){
    	        		int startIndex = line.indexOf("0002");
    	        		String content = line.substring(startIndex, line.length());
    	        		acceTestResult2.setText(content);
    	        	}
    	        	num ++;
    	        }
    	        
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
        testState = Test_Type.NONE;
    	
    	return ret;
	}
	
	
	public boolean checkAcce() {
		boolean ret = false;
		String command = "adb shell cat /sys/class/input/input2/name";
		testState = Test_Type.GSENSOR_TEST;
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.contains("bma2x2")){
    	        	System.out.println("find acce driver");
    	        	ret = true;    	        	                    	        	
    	        	break;
    	        } 
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
        testState = Test_Type.NONE;
    	
    	return ret;
	}
	
	
	public boolean uart1test() {
		boolean ret = false;
		String command = "adb shell mincomtest";
		testState = Test_Type.GSENSOR_TEST;
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.contains("uart 1 test success")){
    	        	System.out.println("uart 1 test pass");
    	        	ret = true;    	        	                    	        	
    	        	break;
    	        } else if (line.contains("uart 1 test fail")) {
    	        	System.out.println("uart 1 test fail");
    	        	ret = false;    	        	                    	        	
    	        	break;
    	        }
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
    	
        testState = Test_Type.NONE;
    	
    	return ret;
	}
	
	public boolean uart2test() {
		boolean ret = false;
		String command = "adb shell mincomtest";
		testState = Test_Type.GSENSOR_TEST;
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.contains("uart 2 test success")){
    	        	System.out.println("uart 1 test pass");
    	        	ret = true;    	        	                    	        	
    	        	break;
    	        } else if (line.contains("uart 2 test fail")) {
    	        	System.out.println("uart 2 test fail");
    	        	ret = false;    	        	                    	        	
    	        	break;
    	        }
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
    	
        testState = Test_Type.NONE;
    	
    	return ret;
	}
	
	/*
	public static class TestTimeTask extends TimerTask{  
	      
	    private Timer timer = null;  
	    private static boolean flag = false;  
	    private long time = 3000;  
	    private static TestTimeTask tstTimeTask = null;  
	      
	    private TestTimeTask(){  
	          
	    } 
	    
	    public static TestTimeTask getInstance(){  
	        if (tstTimeTask == null || flag ) {  
	            //当flag == true时，为了解决，timer.cancel()后，重新创建一个timer  
	        	tstTimeTask = new TestTimeTask();    
	            if (flag){  
	                flag = false;  
	            }  
	         
	        }
	        return tstTimeTask;  
	    }
	        
	    public void start(boolean flg) {  
	            //毫秒  
	        long duration = this.getStartTime();  
	              
	        if (timer == null){  
	            timer = new Timer();  
	        } else {  
	            //从此计时器的任务队列中移除所有已取消的任务。  
	            timer.purge();  
	        }  
	          
	        timer.schedule(this, duration);
	            System.out.println("Timer start...");  
	        }  
	      
	        public void run() {  
	        	System.out.println("Timer fire...");  
	        	
	        }  
	          
	        public void destroyed(){  
	            System.out.println("Timer destroy...");  
	            //终止此计时器，丢弃所有当前已安排的任务。(不但结束当前schedule，连整个Timer的线程(即当前的定时任务)都会结束掉)  
	            timer.cancel();  
	            flag = true;  
	        }  
	        
	        public void setDuration(long timeInMs) {
	        	time = timeInMs;
	        }
	        private long getStartTime() {  
	            //毫秒                
	            return time;  
	        }    
	}
	    */
	    
	class testThread implements Runnable {
		@Override
		public void run () {
			System.out.println("testThread start ...");
			while(testOnGoing){
			try {
				String cmd  = queue.take();
				
				System.out.println("testThead receive :" + cmd);
				
				switch(cmd) {
				    case "usb test" :{

						usbTestBt.setEnabled(false);
						usbTestResult.setText("开始测试....");
						usbTestBt.setBackground(Color.blue);
						
						String deviceList = getAdbDevices();
						if(!onlyOnce) {
							volumeGlobal = getAudioVolume();
							
							showVolume(volumeGlobal);
							onlyOnce = true;
						} else {
							
						}
						
						usbTestBt.setEnabled(true);
						usbTestBt.setBackground(Color.green);
						
						usbTestCnt ++;
						if(deviceList == null || deviceList.isEmpty() ){
						    usbTestResult.setText(usbTestCnt + " 失败，未找到设备");
						} else {
							usbTestResult.setText(usbTestCnt + " 成功");
						}
					
					    break;
				    }
				    
				    case "audio test" :{

						audioTestBt.setEnabled(false);
						//audioTestResult.setText("开始测试...");
						audioTestBt.setBackground(Color.blue);
						flashqueue.put("audio flash");
						audioFlashCnt = 0;
						String deviceList = getAdbDevices();
						audioTestCnt++;
						
						if(deviceList == null || deviceList.isEmpty() ){
						    audioTestResult.setText(audioTestCnt + " 失败，USB未连接");
						} else {
							//TestTimeTask.getInstance().setDuration(5000);
							//TestTimeTask.getInstance().start(true);
							String result = audioAdbTest();
							if(result.equalsIgnoreCase("pass") ) {
								audioTestResult.setText(audioTestCnt + " 成功 ");
							} else {
								audioTestResult.setText(audioTestCnt + " " + result);
							}
							
							//TestTimeTask.getInstance().destroyed();
						}
						flashqueue.put("stop");
						audioTestBt.setEnabled(true);
						audioTestBt.setBackground(Color.green);
						
						
					    break;
				    }
				    
				    case "volume up" : {
                        volumeResult.setText("");
						int value = getAudioVolume();
						System.out.println("volume up ===> " + value);
						if(value == 5) {
							showVolume(0);
							try { 
								Thread.sleep(100); 
							} catch (InterruptedException e) { 
								e.printStackTrace(); 
							}
							showVolume(5);
							try { 
								Thread.sleep(100); 
							} catch (InterruptedException e) { 
								e.printStackTrace(); 
							}
							showVolume(0);
							try { 
								Thread.sleep(100); 
							} catch (InterruptedException e) { 
								e.printStackTrace(); 
							}
							showVolume(5);
							try { 
								Thread.sleep(100); 
							} catch (InterruptedException e) { 
								e.printStackTrace(); 
							}
						} else {
							showVolume(value +1);
							setAudioVolume(value +1);
						}
						
					    break;
				    }
				    
				    case "volume down" : {
                        volumeResult.setText("");
						int value = getAudioVolume();
						System.out.println("volume down ===> " + value);
						if(value == 0) {
							showVolume(1);
							try { 
								Thread.sleep(100); 
							} catch (InterruptedException e) { 
								e.printStackTrace(); 
							}
							showVolume(0);
							try { 
								Thread.sleep(100); 
							} catch (InterruptedException e) { 
								e.printStackTrace(); 
							}
							showVolume(1);
							try { 
								Thread.sleep(100); 
							} catch (InterruptedException e) { 
								e.printStackTrace(); 
							}
							showVolume(0);
							try { 
								Thread.sleep(100); 
							} catch (InterruptedException e) { 
								e.printStackTrace(); 
							}
						} else {
							showVolume(value - 1);
							setAudioVolume(value - 1);
						}
						
					
				    	break;
				    }
				    
				    case "camera capture" : {
                        //cameraTestResult.setText("");
				    	TestEntry.writeLog("run camera capture");
				    	boolean cameraret = false;
                        flashqueue.put("camera flash");
                        cameraFlashCnt = 0;
						cameraTestBt.setEnabled(false);
						cameraTestBt.setBackground(Color.blue);
						deleteCameraFiles();
						showBmp();
						String deviceList = getAdbDevices();
						if(deviceList == null || deviceList.isEmpty() ){
						    cameraTestResult.setText(audioTestCnt + " 失败，USB未连接");
						    TestEntry.writeLog("usb connect fail");
						} else {
							if(checkCameraDriver()) {
								TestEntry.writeLog("find video0 driver");
						        removeYuvInDevice();
						        if(startCameraCapture()){
						        	TestEntry.writeLog("capture ok");
                                    if(convertYuvToBmp()){
                                	    showBmp();
                                	    cameraret = true;
						            } else {
						            
						            }
						        }else {
						            TestEntry.writeLog("fail to capture");
						        }
							}else {
								TestEntry.writeLog("fail to find camera video0 driver");
							}
						}
						flashqueue.put("stop");
						try {
				    		Thread.sleep(200);				    		
				    	} catch (InterruptedException e) {
				    		e.printStackTrace();
				    	}
						cameraTestCnt ++;
						if(cameraret)
						    cameraTestResult.setText(cameraTestCnt + "成功");
						else
							cameraTestResult.setText(cameraTestCnt + "失败");
						
						cameraTestBt.setEnabled(true);
						cameraTestBt.setBackground(Color.green);
					    break;
				    }
					
				    case "gyro test" : {
				    	gyroTestBt.setEnabled(false);
				    	gyroTestBt.setBackground(Color.blue);
				    	
				    	if(checkGyro()){
				    	    controlGyro(true);
				    	    showGyrodata();
				    	    controlGyro(false);
				    	    
				    	}else {
				    		gyroTestResult0.setText("驱动未找到");
				    	}
				    	gyroTestBt.setEnabled(true);
				    	gyroTestBt.setBackground(Color.green);
				    	
				    	
				    	break;
				    }
				    
				    case "acce test" : {
				    	acceTestBt.setEnabled(false);
				    	acceTestBt.setBackground(Color.blue);
				    	
				    	if(checkAcce()){
				    	    controlAcce(true);
				    	    showAccedata();
				    	    controlAcce(false);
				    	}else {
				    		acceTestResult0.setText("驱动未找到");
				    	}
				    	acceTestBt.setEnabled(true);
				    	acceTestBt.setBackground(Color.green);
				    	
				    	
				    	break;
				    }
				    
				    case "uart1 test" : {
				    	uart1TestBt.setEnabled(false);
				    	uart1TestBt.setBackground(Color.blue);
				    	uart1TestCnt++;
				    	
				    	if(uart1test()){
				    		uart1TestResult.setText(uart1TestCnt + " 成功 ");
				    	} else {
				    		uart1TestResult.setText(uart1TestCnt + " 失败");
				    	}
				    	
				    	uart1TestBt.setEnabled(true);
				    	uart1TestBt.setBackground(Color.green);
				    	
				    	break;
				    }
				    
				    case "uart2 test" : {
				    	uart2TestBt.setEnabled(false);
				    	uart2TestBt.setBackground(Color.blue);
				    	uart2TestCnt++;
				    	
				    	if(uart2test()){
				    		uart2TestResult.setText(uart2TestCnt + " 成功 ");
				    	} else {
				    		uart2TestResult.setText(uart2TestCnt + " 失败");
				    	}
				    	
				    	uart2TestBt.setEnabled(true);
				    	uart2TestBt.setBackground(Color.green);
				    	
				    	break;
				    }
				}
				
	
				
			    } catch (InterruptedException e) {
				    e.printStackTrace();
			    }
		
		    }
		}
		
	}
	
	
	class flashThread implements Runnable {
		@Override
		public void run () {
			System.out.println("flashThread start ...");
			while(testOnGoing){
			    try {
				    String cmd  = flashqueue.take();
				
				   // System.out.println("flashThead receive :" + cmd);
				
				    switch(cmd) {
				    case "audio flash" : {
				    	
				    	audioFlash(audioFlashCnt%10);
				    	try {
				    		Thread.sleep(300);				    		
				    	} catch (InterruptedException e) {
				    		e.printStackTrace();
				    	}
				    	audioFlashCnt++;
				    	flashqueue.put("audio flash");
				    	break;
				    }
				    case "camera flash" : {
				    	cameraFlash(cameraFlashCnt);
				    	try {
				    		Thread.sleep(200);				    		
				    	} catch (InterruptedException e) {
				    		e.printStackTrace();
				    	}
				    	cameraFlashCnt++;
				    	flashqueue.put("camera flash");
				    	break;
				    }
				    
				    case "stop" :{
				    	System.out.println("flash stop !!!");
				    	flashqueue.clear();
				    	break;
				    }
				    }
			    } catch (InterruptedException e) {
			        e.printStackTrace();
		        }
			
		    }
		}
	}
			
	
	void audioFlash(int num) {
		switch(num) {
    	case 0:
    		audioTestResult.setText("^    ");
    		break;
    	case 1:
    		audioTestResult.setText("^ _   ");
    		break;
    	case 2:
    		audioTestResult.setText("^ _ ^  ");
    		break;
    	case 3:
    		audioTestResult.setText("^ _ ^ _ ");
    		break;
    	case 4:
    		audioTestResult.setText("^ _ ^ _ ^");
    		break;
    	case 5:
    		audioTestResult.setText("^ _ ^ _");
    		break;
    	case 6:
    		audioTestResult.setText("^ _ ^");
    		break;
    	case 7:
    		audioTestResult.setText("^ _");
    		break;
    	case 8:
    		audioTestResult.setText("^");
    		break;
    	case 9:
    		audioTestResult.setText("");
    		break;
    	}
	}
	
	void cameraFlash(int num) {
		switch(num) {
    	case 0:
    		cameraTestResult.setText("><--------");
    		break;
    	case 1:
    		cameraTestResult.setText("==><------");
    		break;
    	case 2:
    		cameraTestResult.setText("====><----");
    		break;
    	case 3:
    		cameraTestResult.setText("======><--");
    		break;
    	case 4:
    		cameraTestResult.setText("======><--");
    		break;
    	case 5:
    		cameraTestResult.setText("========><");
    		break;
    	case 6:
    		cameraTestResult.setText("======><++");
    		break;
    	case 7:
    		cameraTestResult.setText("====><++++");
    		break;
    	case 8:
    		cameraTestResult.setText("==><++++++");
    		break;
    	case 9:
    		cameraTestResult.setText("><++++++++");
    		break;
    	}
	}

}
