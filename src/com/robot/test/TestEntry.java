package com.robot.test;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.robot.test.BMP;
import com.robot.test.NV21;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;


public class TestEntry extends JFrame{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final boolean testOnGoing = true;
	JLabel usbTestTitle =  new JLabel("USB ����");
	JButton usbTestBt = new JButton("��ʼ����");
	JLabel usbTestResult = new JLabel("");
	
	JLabel audioTestTitle =  new JLabel("����");
	JButton audioTestBt = new JButton("��ʼ����");
	JLabel audioTestResult = new JLabel("");
	
	JButton volume0Bt = new JButton("");
	JButton volume1Bt = new JButton("");
	JButton volume2Bt = new JButton("");
	JButton volume3Bt = new JButton("");
	JButton volume4Bt = new JButton("");
	
	JLabel audioVolumeTitle = new JLabel("��������");
	JButton volumeDownBt = new JButton("-");
	JButton volumeUpBt = new JButton("+");
	JLabel volumeResult = new JLabel("");
	
	JLabel cameraTestTitle = new JLabel("����ͷ");
	JButton cameraTestBt = new JButton("��ʼ����");
	JLabel cameraTestResult = new JLabel("");
	MyPanel cameraArea = new MyPanel();
	
	JLabel gyroTestTitle = new JLabel("���ٶ�");
	JButton gyroTestBt = new JButton("��ʼ����");
	JLabel gyroTestResult0 = new JLabel(""); 
	JLabel gyroTestResult1 = new JLabel("");
	JLabel gyroTestResult2 = new JLabel("");
	
	JLabel acceTestTitle = new JLabel("���ٶ�");
	JButton acceTestBt = new JButton("��ʼ����");
	JLabel acceTestResult0 = new JLabel(""); 
	JLabel acceTestResult1 = new JLabel("");
	JLabel acceTestResult2 = new JLabel("");
	
	boolean onlyOnce = false;
	int volumeGlobal = 0;
	
	static Object audioTestLock =  new Object();
	
	MyStack<String> stack = new MyStack<String>(); 
	private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	private LinkedBlockingQueue<String> flashqueue = new LinkedBlockingQueue<String>();
	int usbTestCnt = 0;	
	int audioTestCnt = 0;
	int cameraTestCnt = 0;
	int audioFlashCnt = 0;
	int cameraFlashCnt = 0;
	
	File logfile = null;
    private enum Test_Type {
        NONE,USB_TEST, AUDIO_TEST, CAMERA_TEST, GSENSOR_TEST, UART_EVENT, AUTO_EVENT}
    
    static Test_Type testState = Test_Type.NONE;
    
    FileOutputStream in;
    
	public TestEntry() {
		
		Runtime run=Runtime.getRuntime();//��ǰ Java Ӧ�ó�����ص�����ʱ����  
        run.addShutdownHook(new Thread(){ //ע���µ���������رչ���  
            @Override  
            public void run() {  
                //�������ʱ���еĲ���  
                System.out.println("�����������");  
                /*
                try {
                in.close();
                }catch (IOException e) {
                	e.printStackTrace();
                }
                */
            }  
        }); 
        
		deleteCameraFiles();
		
		createLogFile();
		
		setSize(1200, 1000);
		setLocation(300, 300);
		setTitle("��������");
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
					usbTestResult.setText("�����У����Ժ�");
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
					audioTestResult.setText("�����У����Ժ�");
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
					volumeResult.setText("�����У����Ժ�");
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
					volumeResult.setText("�����У����Ժ�");
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
					cameraTestResult.setText("�����У����Ժ�");
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
        
        cameraArea.setBounds(220, 150, 640, 480);
        add(cameraTestResult);
        cameraTestResult.setBounds(115, 180, 100, 25);
		//add(cameraShow);
		//cameraShow.setBounds(220, 150, 320, 240);
		
		add(gyroTestTitle);
		gyroTestTitle.setBounds(30, 650, 80, 25);
        add(gyroTestBt);
        gyroTestBt.setBounds(115, 650, 100, 25);
        gyroTestBt.setBackground(Color.green);
        gyroTestBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("camera capture");
				if(testState != Test_Type.NONE) {
					gyroTestResult0.setText("�����У����Ժ�");
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
        gyroTestResult0.setBounds(220, 650, 100, 25);
        add(gyroTestResult1);
        gyroTestResult1.setBounds(220, 680, 100, 25);
        add(gyroTestResult2);
        gyroTestResult2.setBounds(220, 710, 100, 25);
        
        
        add(acceTestTitle);
		acceTestTitle.setBounds(30, 750, 80, 25);
        add(acceTestBt);
        acceTestBt.setBounds(115, 750, 100, 25);
        acceTestBt.setBackground(Color.green);
        acceTestBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("camera capture");
				if(testState != Test_Type.NONE) {
					acceTestResult0.setText("�����У����Ժ�");
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
        acceTestResult0.setBounds(220, 750, 100, 25);
        add(acceTestResult1);
        acceTestResult1.setBounds(220, 780, 100, 25);
        add(acceTestResult2);
        acceTestResult2.setBounds(220, 810, 100, 25);
        
        
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		testThread  tTh = new testThread();
		Thread thread1 = new Thread(tTh);
		thread1.start();
		
		flashThread  tThFlash = new flashThread();
		Thread thread2 = new Thread(tThFlash);
		thread2.start();
	}
	
	public class MyStack<T> {  
	    private LinkedList<T> storage = new LinkedList<T>();  
	  
	    public synchronized void push(T e) {//��Ҫ����ͬ��  
	        storage.addFirst(e);  
	    }  
	  
	    public T peek() {  
	        return storage.getFirst();  
	    }  
	  
	    public void pop() {  
	        storage.removeFirst();  
	    }  
	  
	    public boolean empty() {  
	        return storage.isEmpty();  
	    }  
	  
	    @Override  
	    public String toString() {  
	        return storage.toString();  
	    }  
	  
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
    	
    	String result = "ʧ��";
    	
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
    	        	result = "�����ļ�δ�ҵ�";
    	        	break;
    	        } else if(line.endsWith("found")){
    	        	result = "�������δ�ҵ�";
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
    	
    	
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.endsWith("finish!")){
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
    	
    	try{
    		Thread.sleep(500);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    	
    	if( !ret ){
    		testState = Test_Type.NONE;
    		System.out.println("camera capture fail");
    		return ret;
    	}
    	
    	ret = false;
    	
    	command = "adb pull /tmp/source_data1.yuv";
    	System.out.println(command);
    	
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.endsWith("s)")){  	
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
	
	private void deleteCameraFiles() {
		System.out.println("deleteCameraFiles ...");
		File file = new File("source_data1.yuv"); // The input NV21 file
		if (file.exists()){
		    delete(file);
		}
		
		file = new File("nv21.bmp");
		if (file.exists()){
		    delete(file);
		}
		
		file = new File("log.txt");
		if (file.exists()){
		    delete(file);
		}
	}
	
	private void createLogFile()
	{
		/*logfile = new File("log.txt");  
        try {  
            logfile.createNewFile(); // �����ļ�  
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
        */
	}
	
		
	private void writeLog(String str) {
		
        
		/*
        long time = System.currentTimeMillis();
        
            
            try {
            	//in.write(timebt, 0, 8);
                //in.write(bt, 0, bt.length);  
                StringBuffer sb = new StringBuffer();
                sb.append(String.valueOf(time) + " : "  + str + System.getProperty("line.separator"));
                in.write(sb.toString().getBytes("UTF-8"));
                
                // boolean success=true;  
                // System.out.println("д���ļ��ɹ�");  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        */ 
	}
	
	
	public boolean convertYuvToBmp() {
		writeLog("covertYuvToBmp +++");
		File file = new File("source_data1.yuv"); // The input NV21 file
		if (!file.exists()){
			writeLog("covertYuvToBmp not found---");
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
			bmp.saveBMP("nv21.bmp"); // The output BMP file

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			writeLog("covertYuvToBmp io exception");
			return false;
		}

		writeLog("covertYuvToBmp  is done");
		
		System.out.println("Conversion is done.");
		return true;
	}
	
	public void showBmp(){

		System.out.println("shouwBmp ...");
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

	        	File bmpfile = new File("nv21.bmp");
	        	if(bmpfile.exists()){
	                image=ImageIO.read(bmpfile);

	                //g.drawImage(image, 220, 150, 640, 480, null);
                    if(image != null) {
	                    g.drawImage(image, 0, 0, 640, 480, null);
                    }else {
                        g.clearRect(0, 0, 640, 480);
                    }
	        	}else {
	        		g.clearRect(0, 0, 640, 480);
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
	            //��flag == trueʱ��Ϊ�˽����timer.cancel()�����´���һ��timer  
	        	tstTimeTask = new TestTimeTask();    
	            if (flag){  
	                flag = false;  
	            }  
	         
	        }
	        return tstTimeTask;  
	    }
	        
	    public void start(boolean flg) {  
	            //����  
	        long duration = this.getStartTime();  
	              
	        if (timer == null){  
	            timer = new Timer();  
	        } else {  
	            //�Ӵ˼�ʱ��������������Ƴ�������ȡ��������  
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
	            //��ֹ�˼�ʱ�����������е�ǰ�Ѱ��ŵ�����(����������ǰschedule��������Timer���߳�(����ǰ�Ķ�ʱ����)���������)  
	            timer.cancel();  
	            flag = true;  
	        }  
	        
	        public void setDuration(long timeInMs) {
	        	time = timeInMs;
	        }
	        private long getStartTime() {  
	            //����                
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
						usbTestResult.setText("��ʼ����....");
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
						    usbTestResult.setText(usbTestCnt + " ʧ�ܣ�δ�ҵ��豸");
						} else {
							usbTestResult.setText(usbTestCnt + " �ɹ�");
						}
					
					    break;
				    }
				    
				    case "audio test" :{

						audioTestBt.setEnabled(false);
						//audioTestResult.setText("��ʼ����...");
						audioTestBt.setBackground(Color.blue);
						flashqueue.put("audio flash");
						audioFlashCnt = 0;
						String deviceList = getAdbDevices();
						audioTestCnt++;
						
						if(deviceList == null || deviceList.isEmpty() ){
						    audioTestResult.setText(audioTestCnt + " ʧ�ܣ�USBδ����");
						} else {
							//TestTimeTask.getInstance().setDuration(5000);
							//TestTimeTask.getInstance().start(true);
							String result = audioAdbTest();
							if(result.equalsIgnoreCase("pass") ) {
								audioTestResult.setText(audioTestCnt + " �ɹ� ");
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
				    	writeLog("run camera capture");
				    	boolean cameraret = false;
                        flashqueue.put("camera flash");
                        cameraFlashCnt = 0;
						cameraTestBt.setEnabled(false);
						cameraTestBt.setBackground(Color.blue);
						deleteCameraFiles();
						showBmp();
						String deviceList = getAdbDevices();
						if(deviceList == null || deviceList.isEmpty() ){
						    cameraTestResult.setText(audioTestCnt + " ʧ�ܣ�USBδ����");
						} else {
							if(checkCameraDriver()) {
						        removeYuvInDevice();
						        if(startCameraCapture()){
						        	writeLog("capture ok");
                                    if(convertYuvToBmp()){
                                	    showBmp();
                                	    cameraret = true;
						            } else {
						            
						            }
						        }else {
						    
						        }
							}else {
								
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
						    cameraTestResult.setText(cameraTestCnt + "�ɹ�");
						else
							cameraTestResult.setText(cameraTestCnt + "ʧ��");
						
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
				    		gyroTestResult0.setText("����δ�ҵ�");
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
				    		acceTestResult0.setText("����δ�ҵ�");
				    	}
				    	acceTestBt.setEnabled(true);
				    	acceTestBt.setBackground(Color.green);
				    	
				    	
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        new TestEntry();   
	}

	
	
}
