package com.robot.test;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
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

	public static boolean testOnGoing = true;
	JLabel usbTestTitle =  new JLabel("USB 连接");
	JButton usbTestBt = new JButton("测试");
	JLabel usbTestResult = new JLabel("");
	
	JLabel audioTestTitle =  new JLabel("声音");
	JButton audioTestBt = new JButton("测试");
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
	JButton cameraTestBt = new JButton("测试");
	JLabel cameraTestResult = new JLabel("");
	MyPanel cameraArea = new MyPanel();
	
	JLabel gyroTestTitle = new JLabel("角速度");
	JButton gyroTestBt = new JButton("测试");
	JLabel gyroTestResult0 = new JLabel(""); 
	JLabel gyroTestResult1 = new JLabel("");
	JLabel gyroTestResult2 = new JLabel("");
	
	JLabel gyroXMinTitle = new JLabel(""); 
	JLabel gyroXMaxTitle = new JLabel("");
	JLabel gyroYMinTitle = new JLabel(""); 
	JLabel gyroYMaxTitle = new JLabel("");
	JLabel gyroZMinTitle = new JLabel(""); 
	JLabel gyroZMaxTitle = new JLabel("");
	
	
	JLabel acceTestTitle = new JLabel("加速度");
	JButton acceTestBt = new JButton("测试");
	JLabel acceTestResult0 = new JLabel(""); 
	JLabel acceTestResult1 = new JLabel("");
	JLabel acceTestResult2 = new JLabel("");
	
	
	JLabel acceXMinTitle = new JLabel(""); 
	JLabel acceXMaxTitle = new JLabel("");
	JLabel acceYMinTitle = new JLabel(""); 
	JLabel acceYMaxTitle = new JLabel("");
	JLabel acceZMinTitle = new JLabel(""); 
	JLabel acceZMaxTitle = new JLabel("");
	
	JLabel logline0 = new JLabel("");
	JLabel logline1 = new JLabel("");
	JLabel logline2 = new JLabel("");
	JLabel logline3 = new JLabel("");
	JLabel logline4 = new JLabel("");
	JLabel logline5 = new JLabel("");
	
	JLabel uart1TestTitle = new JLabel("串口 1");
	JButton uart1TestBt = new JButton("测试");
	JLabel uart1TestResult = new JLabel();
	
	JLabel uart2TestTitle = new JLabel("串口 2");
	JButton uart2TestBt = new JButton("测试");
	JLabel uart2TestResult = new JLabel();
	
	JLabel chipIdTitle = new JLabel("SN");
	JLabel chipidInfo = new JLabel("");
	
	boolean onlyOnce = false;
	int volumeGlobal = 0;
	
	static Object audioTestLock =  new Object();
	
	//MyStack<String> stack = new MyStack<String>(); 
	
	private LinkedBlockingQueue<String> flashqueue = new LinkedBlockingQueue<String>();
	int usbTestCnt = 0;	
	int audioTestCnt = 0;
	int cameraTestCnt = 0;
	int uart1TestCnt = 0;
	int uart2TestCnt = 0;
	
	int audioFlashCnt = 0;
	int cameraFlashCnt = 0;
	
	int gyroXMinKey = 0;
	int gyroXMaxKey = 0;
	int gyroYMinKey = 0;
	int gyroYMaxKey = 0;
	int gyroZMinKey = 0;
	int gyroZMaxKey = 0;
	
	int acceXMinKey = 0;
	int acceXMaxKey = 0;
	int acceYMinKey = 0;
	int acceYMaxKey = 0;
	int acceZMinKey = 0;
	int acceZMaxKey = 0;
	
	private enum Test_Type {
        NONE,USB_TEST, AUDIO_TEST, VOLUME_UP, VOLUME_DOWN, CAMERA_TEST, GYRO_TEST, ACCE_TEST, UART1_TEST, UART2_TEST}
	
	//private static final String[] Test_Cmd = {"stop", "usb test", "audio test", "volume up", "volume down", "camera capture",
	//		                      "gyro test", "acce test", "uart1 test", "uart2 test"};
	
	private LinkedBlockingQueue<Test_Type> queue = new LinkedBlockingQueue<Test_Type>();
	
    static Test_Type testState = Test_Type.NONE;
    
    private DeviceControl device;
    
	public ManualTest() {
				  		
			deleteCameraFiles();
			
			setSize(700, 700);
			setLocation(300, 300);
			setTitle("单项测试");
			setLayout(null);
			
			add(chipIdTitle);
	        chipIdTitle.setBounds(30, 0, 80, 50);
	        chipIdTitle.setFont(new Font("Dialog", 1, 20));
	        chipIdTitle.setForeground(Color.red);
	        
	        add(chipidInfo);
	        chipidInfo.setBounds(115, 0, 400, 50);
	        chipidInfo.setFont(new Font("Dialog", 1, 20));
	        chipidInfo.setForeground(Color.red);
	        
			add(usbTestTitle);
			usbTestTitle.setBounds(30, 60, 80, 25);
			usbTestTitle.setFont(new Font("Dialog", 1, 15));
			
	        
			add(usbTestBt);
			usbTestBt.setBounds(115, 60, 100, 25);
			usbTestBt.setBackground(Color.green);
			usbTestBt.setFont(new Font("Dialog", 1, 15));
			usbTestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("usb test start");
					if(testState != Test_Type.NONE) {
						usbTestResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put(Test_Type.USB_TEST);
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
			add(usbTestResult);
			usbTestResult.setBounds(220, 60, 120, 25);
			usbTestResult.setFont(new Font("Dialog", 1, 15));
			
			add(logline0);
			logline0.setBounds(350, 60, 600, 25);
			add(logline1);
			logline1.setBounds(350, 90, 600, 25);
			add(logline2);
			logline2.setBounds(350, 120, 600, 25);
			add(logline3);
			logline3.setBounds(350, 150, 600, 25);
			add(logline4);
			logline4.setBounds(350, 180, 600, 25);
			
			add(audioTestTitle);
			audioTestTitle.setBounds(30, 90, 80, 25);
			audioTestTitle.setFont(new Font("Dialog", 1, 15));
			add(audioTestBt);
			audioTestBt.setBounds(115, 90, 100, 25);
			audioTestBt.setBackground(Color.green);
			audioTestBt.setFont(new Font("Dialog", 1, 15));
			audioTestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("audio test start");
					if(testState != Test_Type.NONE) {
						audioTestResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put(Test_Type.AUDIO_TEST);
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
			add(audioTestResult);
			audioTestResult.setBounds(220, 90, 120, 25);
			audioTestResult.setFont(new Font("Dialog", 1, 15));
			
			add(volume0Bt);
			volume0Bt.setBounds(115, 120, 20, 25);
	        volume0Bt.setBackground(Color.blue);
			volume0Bt.setBorderPainted(false);
			volume0Bt.setFocusPainted(false);
			volume0Bt.setEnabled(false);
			
			
			add(volume1Bt);
			volume1Bt.setBounds(135, 120, 20, 25);
			volume1Bt.setBackground(Color.blue);
			volume1Bt.setBorderPainted(false);
			volume1Bt.setFocusPainted(false);
			volume1Bt.setEnabled(false);
			
			add(volume2Bt);
			volume2Bt.setBounds(155, 120, 20, 25);
			volume2Bt.setBackground(Color.blue);
			volume2Bt.setBorderPainted(false);
			volume2Bt.setFocusPainted(false);
			volume2Bt.setEnabled(false);
			
			add(volume3Bt);
			volume3Bt.setBounds(175, 120, 20, 25);
			volume3Bt.setBackground(Color.white);
			volume3Bt.setBorderPainted(false);
			volume3Bt.setFocusPainted(false);
			volume3Bt.setEnabled(false);
			
			add(volume4Bt);
			volume4Bt.setBounds(195, 120, 20, 25);
			volume4Bt.setBackground(Color.white);
			volume4Bt.setBorderPainted(false);
			volume4Bt.setFocusPainted(false);
			volume4Bt.setEnabled(false);
			
			
			add(audioVolumeTitle);
			audioVolumeTitle.setBounds(30, 150, 80, 25);
			audioVolumeTitle.setFont(new Font("Dialog", 1, 15));
			
			add(volumeDownBt);
			volumeDownBt.setBounds(115, 150, 45, 25);
			volumeDownBt.setFont(new Font("Dialog", 1, 15));
			volumeDownBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("volume down");
					if(testState != Test_Type.NONE) {
						volumeResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put(Test_Type.VOLUME_DOWN);
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
			
			add(volumeUpBt);
			volumeUpBt.setBounds(170, 150, 45, 25);
			volumeUpBt.setFont(new Font("Dialog", 1, 15));
			volumeUpBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("volume up");
					if(testState != Test_Type.NONE) {
						volumeResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put(Test_Type.VOLUME_UP);
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
			
			add(volumeResult);
			volumeResult.setBounds(220, 150, 120, 25);
			volumeResult.setFont(new Font("Dialog", 1, 15));
			
			add(cameraTestTitle);
			cameraTestTitle.setBounds(30, 180, 80, 25);
			cameraTestTitle.setFont(new Font("Dialog", 1, 15));
			
			add(cameraTestBt);
			cameraTestBt.setBounds(115, 180, 100, 25);
			cameraTestBt.setFont(new Font("Dialog", 1, 15));
			cameraTestBt.setBackground(Color.green);
			cameraTestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("camera capture");
					if(testState != Test_Type.NONE) {
						cameraTestResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put(Test_Type.CAMERA_TEST);
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
			
			add(cameraArea);
	        
	        cameraArea.setBounds(220, 180, 320, 240);
	        add(cameraTestResult);
	        cameraTestResult.setBounds(115, 210, 100, 25);
	        cameraTestResult.setFont(new Font("Dialog", 1, 15));
			//add(cameraShow);
			//cameraShow.setBounds(220, 150, 320, 240);
			
			add(gyroTestTitle);
			gyroTestTitle.setBounds(30, 430, 80, 25);
			gyroTestTitle.setFont(new Font("Dialog", 1, 15));
			
	        add(gyroTestBt);
	        gyroTestBt.setBounds(115, 430, 100, 25);
	        gyroTestBt.setFont(new Font("Dialog", 1, 15));
	        gyroTestBt.setBackground(Color.green);
	        gyroTestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("camera capture");
					if(testState != Test_Type.NONE) {
						gyroTestResult0.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put(Test_Type.GYRO_TEST);
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
	        
	        add(gyroTestResult0);
	        gyroTestResult0.setBounds(220, 430, 200, 25);
	        gyroTestResult0.setFont(new Font("Dialog", 1, 15));
	        add(gyroTestResult1);
	        gyroTestResult1.setBounds(220, 460, 200, 25);
	        gyroTestResult1.setFont(new Font("Dialog", 1, 15));
	        add(gyroTestResult2);
	        gyroTestResult2.setBounds(220, 490, 200, 25);
	        gyroTestResult2.setFont(new Font("Dialog", 1, 15));
	        
	        add(gyroXMinTitle);
	        gyroXMinTitle.setBounds(430, 430, 150, 25);
	        gyroXMinTitle.setFont(new Font("Dialog", 1, 15));
	        add(gyroXMaxTitle);
	        gyroXMaxTitle.setBounds(590, 430, 150, 25);
	        gyroXMaxTitle.setFont(new Font("Dialog", 1, 15));
	       
	        add(gyroYMinTitle);
	        gyroYMinTitle.setBounds(430, 460, 150, 25);
	        gyroYMinTitle.setFont(new Font("Dialog", 1, 15));
	        add(gyroYMaxTitle);
	        gyroYMaxTitle.setBounds(590, 460, 150, 25);
	        gyroYMaxTitle.setFont(new Font("Dialog", 1, 15));
	        
	        add(gyroZMinTitle);
	        gyroZMinTitle.setBounds(430, 490, 150, 25);
	        gyroZMinTitle.setFont(new Font("Dialog", 1, 15));
	        add(gyroZMaxTitle);
	        gyroZMaxTitle.setBounds(590, 490, 150, 25);
	        gyroZMaxTitle.setFont(new Font("Dialog", 1, 15));
	        
	        
	        add(acceTestTitle);
			acceTestTitle.setBounds(30, 530, 80, 25);
			acceTestTitle.setFont(new Font("Dialog", 1, 15));
			
	        add(acceTestBt);
	        acceTestBt.setBounds(115, 530, 100, 25);
	        acceTestBt.setFont(new Font("Dialog", 1, 15));
	        
	        acceTestBt.setBackground(Color.green);
	        acceTestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("acce test");
					if(testState != Test_Type.NONE) {
						acceTestResult0.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put(Test_Type.ACCE_TEST);
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
	        
	        add(acceTestResult0);
	        acceTestResult0.setBounds(220, 530, 200, 25);
	        acceTestResult0.setFont(new Font("Dialog", 1, 15));
	        add(acceTestResult1);
	        acceTestResult1.setBounds(220, 560, 200, 25);
	        acceTestResult1.setFont(new Font("Dialog", 1, 15));
	        add(acceTestResult2);
	        acceTestResult2.setBounds(220, 590, 200, 25);
	        acceTestResult2.setFont(new Font("Dialog", 1, 15));
	        
	        
	        add(acceXMinTitle);
	        acceXMinTitle.setBounds(430, 530, 150, 25);
	        acceXMinTitle.setFont(new Font("Dialog", 1, 15));
	        add(acceXMaxTitle);
	        acceXMaxTitle.setBounds(590, 530, 150, 25);
	        acceXMaxTitle.setFont(new Font("Dialog", 1, 15));
	       
	        add(acceYMinTitle);
	        acceYMinTitle.setBounds(430, 560, 150, 25);
	        acceYMinTitle.setFont(new Font("Dialog", 1, 15));
	        add(acceYMaxTitle);
	        acceYMaxTitle.setBounds(590, 560, 150, 25);
	        acceYMaxTitle.setFont(new Font("Dialog", 1, 15));
	        
	        add(acceZMinTitle);
	        acceZMinTitle.setBounds(430, 590, 150, 25);
	        acceZMinTitle.setFont(new Font("Dialog", 1, 15));
	        add(acceZMaxTitle);
	        acceZMaxTitle.setBounds(590, 590, 150, 25);
	        acceZMaxTitle.setFont(new Font("Dialog", 1, 15));
	        
	        add(uart1TestTitle);
	        uart1TestTitle.setBounds(30, 620, 80, 25);
	        uart1TestTitle.setFont(new Font("Dialog", 1, 15));
	        add(uart1TestBt);
	        uart1TestBt.setBounds(115, 620, 100, 25);
	        uart1TestBt.setFont(new Font("Dialog", 1, 15));
	        uart1TestBt.setBackground(Color.green);
	        uart1TestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("uart1 test");
					if(testState != Test_Type.NONE) {
						uart1TestResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put(Test_Type.UART1_TEST);
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
	        add(uart1TestResult);
	        uart1TestResult.setBounds(220, 620, 100, 25);
	        uart1TestResult.setFont(new Font("Dialog", 1, 15));
	        
	        add(uart2TestTitle);
	        uart2TestTitle.setBounds(30, 650, 80, 25);
	        uart2TestTitle.setFont(new Font("Dialog", 1, 15));
	        
	        add(uart2TestBt);
	        uart2TestBt.setBounds(115, 650, 100, 25);
	        uart2TestBt.setFont(new Font("Dialog", 1, 15));
	        uart2TestBt.setBackground(Color.green);
	        uart2TestBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("uart2 test");
					if(testState != Test_Type.NONE) {
						uart2TestResult.setText("测试中，请稍后");
					}else {
					    try {
						    queue.put(Test_Type.UART2_TEST);
					    } catch (InterruptedException e1) {
						    // TODO Auto-generated catch block
						    e1.printStackTrace();
					    }
					}
				}
			});
	        
	        add(uart2TestResult);
	        uart2TestResult.setBounds(220, 650, 100, 25);
	        uart2TestResult.setFont(new Font("Dialog", 1, 15));
	               
	        
			//setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
			
			device = new DeviceControl();
			
			testThread  tTh = new testThread();
			Thread thread1 = new Thread(tTh);
			thread1.start();
			
			flashThread  tThFlash = new flashThread();
			Thread thread2 = new Thread(tThFlash);
			thread2.start();
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
	
	public boolean showGyrodata() {
		boolean ret = false;
		String command = "adb shell getevent";
		int num = 0;
		//testState = Test_Type.GSENSOR_TEST;
		BigInteger xKey;
		gyroXMinKey = 0;
		gyroXMaxKey = 0;
		gyroYMinKey = 0;
		gyroYMaxKey = 0;
		gyroZMinKey = 0;
		gyroZMaxKey = 0;
		
		gyroXMinTitle.setText("");
	    gyroXMaxTitle.setText("");
	    gyroYMinTitle.setText("");
	    gyroYMaxTitle.setText("");
	    gyroZMinTitle.setText("");
	    gyroZMaxTitle.setText("");
	    
		
		
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null && num < 50) {
    	        System.out.println(line);  
    	        
    	        if(line.contains("dev/input/event4")){
    	        	if(line.contains("0003 0000")){
    	        		int startIndex = line.indexOf("0000");
    	        		
    	        		String content = line.substring(startIndex, line.length());
    	        		String keyValue = line.substring(startIndex+4, line.length()).trim();
    	        		gyroTestResult0.setText(content);
    	        		xKey = new BigInteger(keyValue, 16);
    	        		if(xKey.intValue() > gyroXMaxKey) {
    	        			gyroXMaxKey = xKey.intValue();
    	        		}
    	        		if(xKey.intValue() < gyroXMinKey) {
    	        			gyroXMinKey = xKey.intValue();
    	        		}
    	        		
    	        		
    	        	}
    	        	if(line.contains("0003 0001")){
    	        		int startIndex = line.indexOf("0001");
    	        		String content = line.substring(startIndex, line.length());
    	        		String keyValue = line.substring(startIndex+4, line.length()).trim();
    	        		gyroTestResult1.setText(content);
    	        		xKey = new BigInteger(keyValue, 16);
    	        		if(xKey.intValue() > gyroYMaxKey) {
    	        			gyroYMaxKey = xKey.intValue();
    	        		}
    	        		if(xKey.intValue() < gyroYMinKey) {
    	        			gyroYMinKey = xKey.intValue();
    	        		}
    	        	}
    	        	if(line.contains("0003 0002")){
    	        		int startIndex = line.indexOf("0002");
    	        		String content = line.substring(startIndex, line.length());
    	        		String keyValue = line.substring(startIndex+4, line.length()).trim();
    	        		gyroTestResult2.setText(content);
    	        		xKey = new BigInteger(keyValue, 16);
    	        		if(xKey.intValue() > gyroZMaxKey) {
    	        			gyroZMaxKey = xKey.intValue();
    	        		}
    	        		if(xKey.intValue() < gyroZMinKey) {
    	        			gyroZMinKey = xKey.intValue();
    	        		}
    	        	}
    	        	num ++;
    	        }
    	        
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	    
    	    gyroXMinTitle.setText(Integer.toString(gyroXMinKey));
    	    gyroXMaxTitle.setText(Integer.toString(gyroXMaxKey));
    	    gyroYMinTitle.setText(Integer.toString(gyroYMinKey));
    	    gyroYMaxTitle.setText(Integer.toString(gyroYMaxKey));
    	    gyroZMinTitle.setText(Integer.toString(gyroZMinKey));
    	    gyroZMaxTitle.setText(Integer.toString(gyroZMaxKey));
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
        //testState = Test_Type.NONE;
    	
    	return ret;
	}
	
	
	
	public boolean showAccedata() {
		boolean ret = false;
		String command = "adb shell getevent";
		int num = 0;
		//testState = Test_Type.GSENSOR_TEST;
    	
		BigInteger xKey;
		acceXMinKey = 0;
		acceXMaxKey = 0;
		acceYMinKey = 0;
		acceYMaxKey = 0;
		acceZMinKey = 0;
		acceZMaxKey = 0;
		
		acceXMinTitle.setText("");
	    acceXMaxTitle.setText("");
	    acceYMinTitle.setText("");
	    acceYMaxTitle.setText("");
	    acceZMinTitle.setText("");
	    acceZMaxTitle.setText("");
	    
	    
	    boolean firstXKey = false;
	    boolean firstYKey = false;
	    boolean firstZKey = false;
	    
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
    	        		
    	        		
    	        		String keyValue = line.substring(startIndex+4, line.length()).trim();
    	        		
    	        		xKey = new BigInteger(keyValue, 16);
    	        		
    	        		if(!firstXKey) {
    	        			acceXMinKey = xKey.intValue();
    	        			firstXKey = true;
    	        		}
    	        		if(xKey.intValue() > acceXMaxKey) {
    	        			acceXMaxKey = xKey.intValue();
    	        		}
    	        		if(xKey.intValue() < acceXMinKey) {
    	        			acceXMinKey = xKey.intValue();
    	        		}
    	        		
    	        	}
    	        	if(line.contains("0003 0001")){
    	        		int startIndex = line.indexOf("0001");
    	        		String content = line.substring(startIndex, line.length());
    	        		acceTestResult1.setText(content);
                        String keyValue = line.substring(startIndex+4, line.length()).trim();
    	        		
    	        		xKey = new BigInteger(keyValue, 16);
    	        		if(!firstYKey) {
    	        			acceYMinKey = xKey.intValue();
    	        			firstYKey = true;
    	        		}
    	        		
    	        		if(xKey.intValue() > acceYMaxKey) {
    	        			acceYMaxKey = xKey.intValue();
    	        		}
    	        		if(xKey.intValue() < acceYMinKey) {
    	        			acceYMinKey = xKey.intValue();
    	        		}
    	        	}
    	        	if(line.contains("0003 0002")){
    	        		int startIndex = line.indexOf("0002");
    	        		String content = line.substring(startIndex, line.length());
    	        		acceTestResult2.setText(content);
    	        		
                        String keyValue = line.substring(startIndex+4, line.length()).trim();
    	        		
    	        		xKey = new BigInteger(keyValue, 16);
    	        		
    	        		if(!firstZKey) {
    	        			acceZMinKey = xKey.intValue();
    	        			firstZKey = true;
    	        		}
    	        		
    	        		if(xKey.intValue() > acceZMaxKey) {
    	        			acceZMaxKey = xKey.intValue();
    	        		}
    	        		if(xKey.intValue() < acceZMinKey) {
    	        			acceZMinKey = xKey.intValue();
    	        		}
    	        		
    	        	}
    	        	num ++;
    	        }
    	        
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    acceXMinTitle.setText(Integer.toString(acceXMinKey));
    	    acceXMaxTitle.setText(Integer.toString(acceXMaxKey));
    	    acceYMinTitle.setText(Integer.toString(acceYMinKey));
    	    acceYMaxTitle.setText(Integer.toString(acceYMaxKey));
    	    acceZMinTitle.setText(Integer.toString(acceZMinKey));
    	    acceZMaxTitle.setText(Integer.toString(acceZMaxKey));
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
        //testState = Test_Type.NONE;
    	
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
				Test_Type cmd  = queue.take();
				
				System.out.println("testThead receive :" + cmd);
				
				switch(cmd) {
				    case USB_TEST :{

						usbTestBt.setEnabled(false);
						usbTestResult.setText("开始测试....");
						usbTestBt.setBackground(Color.blue);
						
						String deviceList = device.getAdbDevices();
						if(!onlyOnce) {
							volumeGlobal = device.getAudioVolume();
							
							showVolume(volumeGlobal);
							
							String id = device.getChipId();
							if(id == null || id.isEmpty()){
								chipidInfo.setText("获取SN失败");
							} else {
								chipidInfo.setText(id);
							}
							
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
				    
				    case AUDIO_TEST :{

						audioTestBt.setEnabled(false);
						//audioTestResult.setText("开始测试...");
						audioTestBt.setBackground(Color.blue);
						flashqueue.put("audio flash");
						audioFlashCnt = 0;
						String deviceList = device.getAdbDevices();
						audioTestCnt++;
						
						if(deviceList == null || deviceList.isEmpty() ){
						    audioTestResult.setText(audioTestCnt + " 失败，USB未连接");
						} else {
							//TestTimeTask.getInstance().setDuration(5000);
							//TestTimeTask.getInstance().start(true);
							String result = device.audioAdbTest();
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
				    
				    case VOLUME_UP : {
                        volumeResult.setText("");
						int value = device.getAudioVolume();
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
							device.setAudioVolume(value +1);
						}
						
					    break;
				    }
				    
				    case VOLUME_DOWN : {
                        volumeResult.setText("");
						int value = device.getAudioVolume();
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
							device.setAudioVolume(value - 1);
						}
						
					
				    	break;
				    }
				    
				    case CAMERA_TEST : {
                        //cameraTestResult.setText("");
				    	TestEntry.writeLog("run camera capture");
				    	boolean cameraret = false;
                        flashqueue.put("camera flash");
                        cameraFlashCnt = 0;
						cameraTestBt.setEnabled(false);
						cameraTestBt.setBackground(Color.blue);
						deleteCameraFiles();
						showBmp();
						String deviceList = device.getAdbDevices();
						if(deviceList == null || deviceList.isEmpty() ){
						    cameraTestResult.setText(audioTestCnt + " 失败，USB未连接");
						    TestEntry.writeLog("usb connect fail");
						} else {
							if(device.checkCameraDriver()) {
								TestEntry.writeLog("find video0 driver");
						        device.removeYuvInDevice();
						        if(device.startCameraCapture()){
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
					
				    case GYRO_TEST : {
				    	gyroTestBt.setEnabled(false);
				    	gyroTestBt.setBackground(Color.blue);
				    	
				    	if(device.checkGyro()){
				    	    device.controlGyro(true);
				    	    showGyrodata();
				    	    device.controlGyro(false);
				    	    
				    	}else {
				    		gyroTestResult0.setText("驱动未找到");
				    	}
				    	gyroTestBt.setEnabled(true);
				    	gyroTestBt.setBackground(Color.green);
				    	
				    	
				    	break;
				    }
				    
				    case ACCE_TEST : {
				    	acceTestBt.setEnabled(false);
				    	acceTestBt.setBackground(Color.blue);
				    	
				    	if(device.checkAcce()){
				    	    device.controlAcce(true);
				    	    showAccedata();
				    	    device.controlAcce(false);
				    	}else {
				    		acceTestResult0.setText("驱动未找到");
				    	}
				    	acceTestBt.setEnabled(true);
				    	acceTestBt.setBackground(Color.green);
				    	
				    	
				    	break;
				    }
				    
				    case UART1_TEST : {
				    	uart1TestBt.setEnabled(false);
				    	uart1TestBt.setBackground(Color.blue);
				    	uart1TestCnt++;
				    	
				    	if(device.uart1test()){
				    		uart1TestResult.setText(uart1TestCnt + " 成功 ");
				    	} else {
				    		uart1TestResult.setText(uart1TestCnt + " 失败");
				    	}
				    	
				    	uart1TestBt.setEnabled(true);
				    	uart1TestBt.setBackground(Color.green);
				    	
				    	break;
				    }
				    
				    case UART2_TEST: {
				    	uart2TestBt.setEnabled(false);
				    	uart2TestBt.setBackground(Color.blue);
				    	uart2TestCnt++;
				    	
				    	if(device.uart2test()){
				    		uart2TestResult.setText(uart2TestCnt + " 成功 ");
				    	} else {
				    		uart2TestResult.setText(uart2TestCnt + " 失败");
				    	}
				    	
				    	uart2TestBt.setEnabled(true);
				    	uart2TestBt.setBackground(Color.green);
				    	
				    	break;
				    }
				    
				    case NONE : {
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
