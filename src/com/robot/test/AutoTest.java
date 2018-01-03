package com.robot.test;



import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.HashMap;


public class AutoTest extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FactoryRecordSqlite database;
	private static final String dbName = "test.db";
	
	private enum Test_Type {
        NONE,USB_TEST, AUDIO_TEST, CAMERA_TEST, GYRO_TEST, ACCE_TEST, UART1_TEST, UART2_TEST}
	
	private enum Flash_Type {
		AUDIO, AUDIO_END_OK, AUDIO_END_FAIL, CAMERA, CAMERA_END_OK, CAMERA_END_FAIL, 
		GYRO, GYRO_END_OK, GYRO_END_FAIL, ACCE, ACCE_END_OK, ACCE_END_FAIL,
		UART1, UART1_END_OK, UART1_END_FAIL, UART2, UART2_END_OK, UART2_END_FAIL
	}
	
	
	
	private static final String audioCBName = "声音/喇叭";
	Checkbox cb_audio = new Checkbox(audioCBName);
	
	
	private static final String cameraCBName = "摄像头";
	Checkbox cb_camera =  new Checkbox(cameraCBName);
	
	
	private static final String gyroCBName = "角速度";
	Checkbox cb_gyro = new Checkbox(gyroCBName);
	
	
	private static final String acceCBName = "加速度";
	Checkbox cb_acce = new Checkbox(acceCBName);
	
	
	private static final String uart1CBName = "串口1";
	Checkbox cb_uart1 = new Checkbox(uart1CBName);
	
	
	private static final String uart2CBName = "串口2";
	Checkbox cb_uart2 = new Checkbox(uart2CBName);
		
	List<Test_Type> testItem = new LinkedList<Test_Type>();
	public HashMap<Test_Type, Integer> testResult = new HashMap<Test_Type, Integer>();
	
	
	JLabel autoTestTitle = new JLabel("自动化测试注意事项");
	JLabel autoTestNote1 = new JLabel("1 ： 请先勾选测试内容");
	JLabel autoTestNote2 = new JLabel("2 ： 设备开机，看到指示灯闪烁后再开始测试");
	
	JLabel passTitle = new JLabel("成功");
	JLabel failTitle = new JLabel("失败");
	
	JButton autoTestBt = new JButton("开始");
	JLabel autoTestInfo = new JLabel("");
	JLabel chipidInfo = new JLabel(""); 
	
	JButton audioIndication0 = new JButton("");
	JButton audioIndication1 = new JButton("");
	JButton audioIndication2 = new JButton("");
	JButton audioIndication3 = new JButton("");
	JButton audioIndication4 = new JButton("");
	JButton audioIndication5 = new JButton("");
	JButton audioIndication6 = new JButton("");
	JButton audioIndication7 = new JButton("");
	JButton audioIndication8 = new JButton("");
	JButton audioIndication9 = new JButton("");
	
	JButton cameraIndication0 = new JButton("");
	JButton cameraIndication1 = new JButton("");
	JButton cameraIndication2 = new JButton("");
	
	JButton gyroIndication0 = new JButton("");
	JButton gyroIndication1 = new JButton("");
	JButton gyroIndication2 = new JButton("");
	JButton gyroIndication3 = new JButton("");
	JButton gyroIndication4 = new JButton("");
	
	JLabel gyroTestResult0 = new JLabel(""); 
	JLabel gyroTestResult1 = new JLabel("");
	JLabel gyroTestResult2 = new JLabel("");
	
	JButton acceIndication0 = new JButton("");
	JButton acceIndication1 = new JButton("");
	JButton acceIndication2 = new JButton("");
	JButton acceIndication3 = new JButton("");
	JButton acceIndication4 = new JButton("");
	JLabel acceTestResult0 = new JLabel(""); 
	JLabel acceTestResult1 = new JLabel("");
	JLabel acceTestResult2 = new JLabel("");
	
	JButton uart1Indication0 = new JButton("");
	JButton uart1Indication1 = new JButton("");
	
	JButton uart2Indication0 = new JButton("");
	JButton uart2Indication1 = new JButton("");
	JButton uart2Indication2 = new JButton("");
	JButton uart2Indication3 = new JButton("");
	
	private LinkedBlockingQueue<Test_Type> queue = new LinkedBlockingQueue<Test_Type>();
	private LinkedBlockingQueue<Flash_Type> flashqueue = new LinkedBlockingQueue<Flash_Type>();
	
	int audioFlashCnt = 0;
	int cameraFlashCnt = 0;
	public static boolean testOnGoing = true;
	
	private DeviceControl device = new DeviceControl();
	
	MyPanel cameraArea = new MyPanel();
	
    public AutoTest() {
    	setSize(700, 700);
		setLocation(300, 300);
		setTitle("自动化测试");
		setLayout(null);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		System.out.println("autotest enter ..");
		
		
		database = new FactoryRecordSqlite();
		
		database.OpenRecord(dbName);
		
		
		testItem.add(Test_Type.USB_TEST);
		CbItemListener cbi = new CbItemListener();
		
		add(autoTestTitle);
		autoTestTitle.setBounds(400, 0, 300, 30);
		autoTestTitle.setFont(new Font("Dialog", 1, 30));
		autoTestTitle.setForeground(Color.red);
		
		add(autoTestNote1);
		autoTestNote1.setBounds(250, 35, 600, 30);
		autoTestNote1.setFont(new Font("Dialog", 1, 30));
		autoTestNote1.setForeground(Color.blue);
		
		add(autoTestNote2);
		autoTestNote2.setBounds(250, 70, 800, 30);
		autoTestNote2.setFont(new Font("Dialog", 1, 30));
		autoTestNote2.setForeground(Color.blue);
		
		add(cb_audio);
		
		cb_audio.setBounds(100, 150, 100, 25);
		cb_audio.setFont(new Font("Dialog", 1, 15));

		cb_audio.addItemListener(cbi);
		
		add(cb_camera);
		
		cb_camera.setBounds(210, 150, 100, 25);
		cb_camera.setFont(new Font("Dialog", 1, 15));

		cb_camera.addItemListener(cbi);
		
		add(cb_gyro);
		cb_gyro.setBounds(310, 150, 100, 25);
		cb_gyro.setFont(new Font("Dialog", 1, 15));

		cb_gyro.addItemListener(cbi);
		
		add(cb_acce);
		cb_acce.setBounds(410, 150, 100, 25);
		cb_acce.setFont(new Font("Dialog", 1, 15));

		cb_acce.addItemListener(cbi);
		
		add(cb_uart1);
		cb_uart1.setBounds(510, 150, 100, 25);
		cb_uart1.setFont(new Font("Dialog", 1, 15));

		cb_uart1.addItemListener(cbi);

		
		add(cb_uart2);
		cb_uart2.setBounds(610, 150, 100, 25);
		cb_uart2.setFont(new Font("Dialog", 1, 15));

		cb_uart2.addItemListener(cbi);

	    add(passTitle);
	    passTitle.setBounds(910, 150, 100, 30);
	    passTitle.setFont(new Font("Dialog", 1, 30));
        passTitle.setForeground(Color.green);		
		
        add(failTitle);
	    failTitle.setBounds(1110, 150, 100, 30);
	    failTitle.setFont(new Font("Dialog", 1, 30));
        failTitle.setForeground(Color.red);
		
        add(autoTestBt);
        autoTestBt.setBounds(410, 200, 100, 50);
        autoTestBt.setForeground(Color.blue);
        autoTestBt.setFont(new Font("Dialog", 1, 30));
        autoTestBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("auto test start");
				
				loadSelectedItem();
				
				
			}
		});
    	
        
        
        add(autoTestInfo);
        autoTestInfo.setBounds(550, 200, 200, 40);
        autoTestInfo.setForeground(Color.blue);
        autoTestInfo.setFont(new Font("Dialog", 1, 20));
        
        add(chipidInfo);
        chipidInfo.setBounds(300, 250, 500, 40);
        chipidInfo.setFont(new Font("Dialog", 1, 20));
        chipidInfo.setForeground(Color.red);
        
        
        add(audioIndication0);
        audioIndication0.setBounds(100, 300, 10, 30);
        audioIndication0.setBackground(Color.blue);
        audioIndication0.setBorderPainted(false);
        audioIndication0.setFocusPainted(false);
        audioIndication0.setEnabled(false);
        audioIndication0.setVisible(false);
        add(audioIndication1);
        audioIndication1.setBounds(110, 300, 10, 30);
        audioIndication1.setBackground(Color.blue);
        audioIndication1.setBorderPainted(false);
        audioIndication1.setFocusPainted(false);
        audioIndication1.setEnabled(false);
        audioIndication1.setVisible(false);
        add(audioIndication2);
        audioIndication2.setBounds(120, 300, 10, 30);
        audioIndication2.setBackground(Color.blue);
        audioIndication2.setBorderPainted(false);
        audioIndication2.setFocusPainted(false);
        audioIndication2.setEnabled(false);
        audioIndication2.setVisible(false);
        add(audioIndication3);
        audioIndication3.setBounds(130, 300, 10, 30);
        audioIndication3.setBackground(Color.blue);
        audioIndication3.setBorderPainted(false);
        audioIndication3.setFocusPainted(false);
        audioIndication3.setEnabled(false);
        audioIndication3.setVisible(false);
        add(audioIndication4);
        audioIndication4.setBounds(140, 300, 10, 30);
        audioIndication4.setBackground(Color.blue);
        audioIndication4.setBorderPainted(false);
        audioIndication4.setFocusPainted(false);
        audioIndication4.setEnabled(false);
        audioIndication4.setVisible(false);
        add(audioIndication5);
        audioIndication5.setBounds(150, 300, 10, 30);
        audioIndication5.setBackground(Color.blue);
        audioIndication5.setBorderPainted(false);
        audioIndication5.setFocusPainted(false);
        audioIndication5.setEnabled(false);
        audioIndication5.setVisible(false);
        add(audioIndication6);
        audioIndication6.setBounds(160, 300, 10, 30);
        audioIndication6.setBackground(Color.blue);
        audioIndication6.setBorderPainted(false);
        audioIndication6.setFocusPainted(false);
        audioIndication6.setEnabled(false);
        audioIndication6.setVisible(false);
        add(audioIndication7);
        audioIndication7.setBounds(170, 300, 10, 30);
        audioIndication7.setBackground(Color.blue);
        audioIndication7.setBorderPainted(false);
        audioIndication7.setFocusPainted(false);
        audioIndication7.setEnabled(false);
        audioIndication7.setVisible(false);
        add(audioIndication8);
        audioIndication8.setBounds(180, 300, 10, 30);
        audioIndication8.setBackground(Color.blue);
        audioIndication8.setBorderPainted(false);
        audioIndication8.setFocusPainted(false);
        audioIndication8.setEnabled(false);
        audioIndication8.setVisible(false);
        add(audioIndication9);
        audioIndication9.setBounds(190, 300, 10, 30);
        audioIndication9.setBackground(Color.blue);
        audioIndication9.setBorderPainted(false);
        audioIndication9.setFocusPainted(false);
        audioIndication9.setEnabled(false);
        audioIndication9.setVisible(false);
        
        add(cameraIndication0);
        cameraIndication0.setBounds(200, 300, 33, 30);
        cameraIndication0.setBackground(Color.blue);
        cameraIndication0.setBorderPainted(false);
        cameraIndication0.setFocusPainted(false);
        cameraIndication0.setEnabled(false);
        cameraIndication0.setVisible(false);
        add(cameraIndication1);
        cameraIndication1.setBounds(233, 300, 33, 30);
        cameraIndication1.setBackground(Color.blue);
        cameraIndication1.setBorderPainted(false);
        cameraIndication1.setFocusPainted(false);
        cameraIndication1.setEnabled(false);
        cameraIndication1.setVisible(false);
        add(cameraIndication2);
        cameraIndication2.setBounds(266, 300, 34, 30);
        cameraIndication2.setBackground(Color.blue);
        cameraIndication2.setBorderPainted(false);
        cameraIndication2.setFocusPainted(false);
        cameraIndication2.setEnabled(false);
        cameraIndication2.setVisible(false);
        add(cameraArea);
                
        cameraArea.setBounds(200, 500, 320, 240);
               
        deleteCameraFiles();
        
        
        add(gyroIndication0);
        gyroIndication0.setBounds(300, 300, 20, 30);
        gyroIndication0.setBackground(Color.blue);
        gyroIndication0.setBorderPainted(false);
        gyroIndication0.setFocusPainted(false);
        gyroIndication0.setEnabled(false);
        gyroIndication0.setVisible(false);
        add(gyroIndication1);
        gyroIndication1.setBounds(320, 300, 20, 30);
        gyroIndication1.setBackground(Color.blue);
        gyroIndication1.setBorderPainted(false);
        gyroIndication1.setFocusPainted(false);
        gyroIndication1.setEnabled(false);
        gyroIndication1.setVisible(false);
        add(gyroIndication2);
        gyroIndication2.setBounds(340, 300, 20, 30);
        gyroIndication2.setBackground(Color.blue);
        gyroIndication2.setBorderPainted(false);
        gyroIndication2.setFocusPainted(false);
        gyroIndication2.setEnabled(false);
        gyroIndication2.setVisible(false);
        add(gyroIndication3);
        gyroIndication3.setBounds(360, 300, 20, 30);
        gyroIndication3.setBackground(Color.blue);
        gyroIndication3.setBorderPainted(false);
        gyroIndication3.setFocusPainted(false);
        gyroIndication3.setEnabled(false);
        gyroIndication3.setVisible(false);
        add(gyroIndication4);
        gyroIndication4.setBounds(380, 300, 20, 30);
        gyroIndication4.setBackground(Color.blue);
        gyroIndication4.setBorderPainted(false);
        gyroIndication4.setFocusPainted(false);
        gyroIndication4.setEnabled(false);
        gyroIndication4.setVisible(false);
        
        add(gyroTestResult0);
        gyroTestResult0.setBounds(300, 430, 200, 25);
        gyroTestResult0.setFont(new Font("Dialog", 1, 10));
        
        add(gyroTestResult1);
        gyroTestResult1.setBounds(300, 460, 200, 25);
        gyroTestResult1.setFont(new Font("Dialog", 1, 10));
        add(gyroTestResult2);
        gyroTestResult2.setBounds(300, 490, 200, 25);
        gyroTestResult2.setFont(new Font("Dialog", 1, 10));
        
        
        add(acceIndication0);
        acceIndication0.setBounds(400, 300, 20, 30);
        acceIndication0.setBackground(Color.blue);
        acceIndication0.setBorderPainted(false);
        acceIndication0.setFocusPainted(false);
        acceIndication0.setEnabled(false);
        acceIndication0.setVisible(false);
        add(acceIndication1);
        acceIndication1.setBounds(420, 300, 20, 30);
        acceIndication1.setBackground(Color.blue);
        acceIndication1.setBorderPainted(false);
        acceIndication1.setFocusPainted(false);
        acceIndication1.setEnabled(false);
        acceIndication1.setVisible(false);
        add(acceIndication2);
        acceIndication2.setBounds(440, 300, 20, 30);
        acceIndication2.setBackground(Color.blue);
        acceIndication2.setBorderPainted(false);
        acceIndication2.setFocusPainted(false);
        acceIndication2.setEnabled(false);
        acceIndication2.setVisible(false);
        add(acceIndication3);
        acceIndication3.setBounds(460, 300, 20, 30);
        acceIndication3.setBackground(Color.blue);
        acceIndication3.setBorderPainted(false);
        acceIndication3.setFocusPainted(false);
        acceIndication3.setEnabled(false);
        acceIndication3.setVisible(false);
        add(acceIndication4);
        acceIndication4.setBounds(480, 300, 20, 30);
        acceIndication4.setBackground(Color.blue);
        acceIndication4.setBorderPainted(false);
        acceIndication4.setFocusPainted(false);
        acceIndication4.setEnabled(false);
        acceIndication4.setVisible(false);
        
        add(acceTestResult0);
        acceTestResult0.setBounds(400, 340, 100, 25);
        acceTestResult0.setFont(new Font("Dialog", 1, 10));
        add(acceTestResult1);
        acceTestResult1.setBounds(400, 365, 200, 25);
        acceTestResult1.setFont(new Font("Dialog", 1, 10));
        add(acceTestResult2);
        acceTestResult2.setBounds(400, 390, 200, 25);
        acceTestResult2.setFont(new Font("Dialog", 1, 10));
        
        add(uart1Indication0);
        uart1Indication0.setBounds(500, 300, 50, 30);
        uart1Indication0.setBackground(Color.blue);
        uart1Indication0.setBorderPainted(false);
        uart1Indication0.setFocusPainted(false);
        uart1Indication0.setEnabled(false);
        uart1Indication0.setVisible(false);
        add(uart1Indication1);
        uart1Indication1.setBounds(550, 300, 40, 30);
        uart1Indication1.setBackground(Color.blue);
        uart1Indication1.setBorderPainted(false);
        uart1Indication1.setFocusPainted(false);
        uart1Indication1.setEnabled(false);
        uart1Indication1.setVisible(false);
        
        
        add(uart2Indication0);
        uart2Indication0.setBounds(600, 300, 25, 30);
        uart2Indication0.setBackground(Color.blue);
        uart2Indication0.setBorderPainted(false);
        uart2Indication0.setFocusPainted(false);
        uart2Indication0.setEnabled(false);
        uart2Indication0.setVisible(false);
        add(uart2Indication1);
        uart2Indication1.setBounds(625, 300, 25, 30);
        uart2Indication1.setBackground(Color.blue);
        uart2Indication1.setBorderPainted(false);
        uart2Indication1.setFocusPainted(false);
        uart2Indication1.setEnabled(false);
        uart2Indication1.setVisible(false);
        add(uart2Indication2);
        uart2Indication2.setBounds(650, 300, 25, 30);
        uart2Indication2.setBackground(Color.blue);
        uart2Indication2.setBorderPainted(false);
        uart2Indication2.setFocusPainted(false);
        uart2Indication2.setEnabled(false);
        uart2Indication2.setVisible(false);
        add(uart2Indication3);
        uart2Indication3.setBounds(675, 300, 25, 30);
        uart2Indication3.setBackground(Color.blue);
        uart2Indication3.setBorderPainted(false);
        uart2Indication3.setFocusPainted(false);
        uart2Indication3.setEnabled(false);
        uart2Indication3.setVisible(false);
        
        
        testThread  tTh = new testThread();
		Thread thread1 = new Thread(tTh);
		thread1.start();
		
		flashThread  tThFlash = new flashThread();
		Thread thread2 = new Thread(tThFlash);
		thread2.start();
		
    }
    
    class CbItemListener implements ItemListener  
    {  
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			Checkbox cb=(Checkbox)e.getItemSelectable();
			String cbLabel = cb.getLabel();
		    boolean cbState = cb.getState();
			switch(cbLabel) {
			case audioCBName : {
				if (cbState == true) {
					if (testItem.contains(Test_Type.AUDIO_TEST)){
						System.out.println("already has audo test, why?");
					} else {
						testItem.add(Test_Type.AUDIO_TEST);
						System.out.println(audioCBName + " select");
					}
				} else {
					if (testItem.contains(Test_Type.AUDIO_TEST)){
						testItem.remove(Test_Type.AUDIO_TEST);
						System.out.println(audioCBName + " skip");
					} else {
						System.out.println("already has audo test, why?");
					}
				}
				break;
			}
			
			case cameraCBName : {
				if (cbState == true) {
					if (testItem.contains(Test_Type.CAMERA_TEST)){
						System.out.println("already has camera test, why add?");
					} else {
						testItem.add(Test_Type.CAMERA_TEST);
						System.out.println("camera test select");
					}
				} else {
					if (testItem.contains(Test_Type.CAMERA_TEST)){
						testItem.remove(Test_Type.CAMERA_TEST);
						System.out.println("camera test skip");
					} else {
						System.out.println("no camera test, why delele?");
					}
				}
				break;
			}
			
			case gyroCBName : {
				if (cbState == true) {
					if (testItem.contains(Test_Type.GYRO_TEST)){
						System.out.println("already has gyro test, why add?");
					} else {
						testItem.add(Test_Type.GYRO_TEST);
						System.out.println("gyro test select");
					}
				} else {
					if (testItem.contains(Test_Type.GYRO_TEST)){
						testItem.remove(Test_Type.GYRO_TEST);
						System.out.println("gyro test skip");
					} else {
						System.out.println("no gyro test, why delete?");
					}
				}
				
				break;
			}
			
			case acceCBName : {
				if (cbState == true) {
					if (testItem.contains(Test_Type.ACCE_TEST)){
						System.out.println("already has acce test, why add?");
					} else {
						testItem.add(Test_Type.ACCE_TEST);
						System.out.println("acce test select");
					}
				} else {
					if (testItem.contains(Test_Type.ACCE_TEST)){
						testItem.remove(Test_Type.ACCE_TEST);
						System.out.println("acce test skip");
					} else {
						System.out.println("already has acce test, why?");
					}
				}
				break;
			}
			
			case uart1CBName : {
				if (cbState == true) {
					if (testItem.contains(Test_Type.UART1_TEST)){
						System.out.println("already has uart1 test, why add?");
					} else {
						testItem.add(Test_Type.UART1_TEST);
						System.out.println("uart1 test select");
					}
				} else {
					if (testItem.contains(Test_Type.UART1_TEST)){
						testItem.remove(Test_Type.UART1_TEST);
						System.out.println("uart1 test skip");
					} else {
						System.out.println("no uart1 test, why delete?");
					}
				}
				
				break;
			}
			
			case uart2CBName : {
				if (cbState == true) {
					if (testItem.contains(Test_Type.UART2_TEST)){
						System.out.println("already has uart2 test, why add?");
					} else {
						testItem.add(Test_Type.UART2_TEST);
						System.out.println("uart2 test select");
					}
				} else {
					if (testItem.contains(Test_Type.UART2_TEST)){
						testItem.remove(Test_Type.UART2_TEST);
						System.out.println("uart2 test skip");
					} else {
						System.out.println("no uart2 test, why delete?");
					}
				}
			}
			
			}
			
		}
    }
    
    
    private void loadSelectedItem() {
    	
    	testResult.clear();
    	
    	try {
		    queue.put(Test_Type.USB_TEST);
		    
		    if(testItem.contains(Test_Type.AUDIO_TEST)) {
	    		displayAudioTest();
	    		queue.put(Test_Type.AUDIO_TEST);
	    	   
	    	} else {
	    		audioClear();
	    		testResult.put(Test_Type.AUDIO_TEST, -1);
	    	}
	    	
	    	if (testItem.contains(Test_Type.CAMERA_TEST)) {
	    		displayCameraTest();
	    		queue.put(Test_Type.CAMERA_TEST);
	    		
	    	} else {
	    		cameraClear();
	    		testResult.put(Test_Type.CAMERA_TEST, -1);
	    	}
	    	
	    	if (testItem.contains(Test_Type.GYRO_TEST)) {
	    		displayGyroTest();
	    		queue.put(Test_Type.GYRO_TEST);
	    		
	    	} else {
	    		gyroClear();
	    		testResult.put(Test_Type.GYRO_TEST, -1);
	    	}
	    	
	    	if (testItem.contains(Test_Type.ACCE_TEST)) {
	    		displayAcceTest();
	    		queue.put(Test_Type.ACCE_TEST);
	    		
	    	} else {
	    		acceClear();
	    		testResult.put(Test_Type.ACCE_TEST, -1);
	    	}
	    	
	    	if (testItem.contains(Test_Type.UART1_TEST)) {
	    		displayUart1Test();
	    		queue.put(Test_Type.UART1_TEST);
	    		
	    	} else {
	    		uart1Clear();
	    		testResult.put(Test_Type.UART1_TEST, -1);
	    	}
	    	
	    	if (testItem.contains(Test_Type.UART2_TEST)) {
	    		displayUart2Test();
	    		queue.put(Test_Type.UART2_TEST);
	    		
	    	} else {
	    		uart2Clear();
	    		testResult.put(Test_Type.UART2_TEST, -1);
	    	}
	    	
	    	
	    	queue.put(Test_Type.NONE);
	    	
	    } catch (InterruptedException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
	    }
    	
    	
    	
    	
    }
    
    
   
    private void displayAudioTest() {
    	
        audioIndication0.setVisible(true);
        audioIndication1.setVisible(true);
        audioIndication2.setVisible(true);
        audioIndication3.setVisible(true);
        audioIndication4.setVisible(true);
        audioIndication5.setVisible(true);
        audioIndication6.setVisible(true);
        audioIndication7.setVisible(true);
        audioIndication8.setVisible(true);
        audioIndication9.setVisible(true);
        audioIndication0.setBackground(Color.blue);
		audioIndication1.setBackground(Color.blue);
		audioIndication2.setBackground(Color.blue);
 		audioIndication3.setBackground(Color.blue);
 		audioIndication4.setBackground(Color.blue);
 		audioIndication5.setBackground(Color.blue);
 		audioIndication6.setBackground(Color.blue);
 		audioIndication7.setBackground(Color.blue);
		audioIndication8.setBackground(Color.blue);
		audioIndication9.setBackground(Color.blue); 	
		
    }
    
    private void displayCameraTest() {
    	cameraIndication0.setVisible(true);        
    	cameraIndication1.setVisible(true);
    	cameraIndication2.setVisible(true);
    	cameraIndication0.setBackground(Color.blue);
    	cameraIndication1.setBackground(Color.blue);
    	cameraIndication2.setBackground(Color.blue); 
    	deleteCameraFiles();
		showBmp();
    }
    
    void audioClear() {
		audioIndication0.setVisible(false);
		audioIndication1.setVisible(false);
		audioIndication2.setVisible(false);
 		audioIndication3.setVisible(false);
 		audioIndication4.setVisible(false);
 		audioIndication5.setVisible(false);
 		audioIndication6.setVisible(false);
 		audioIndication7.setVisible(false);
		audioIndication8.setVisible(false);
		audioIndication9.setVisible(false); 		
   }
    
    private void displayGyroTest() {
    	gyroIndication0.setVisible(true);        
    	gyroIndication1.setVisible(true);
    	gyroIndication2.setVisible(true);
    	gyroIndication3.setVisible(true);
    	gyroIndication4.setVisible(true);
    	
    	gyroTestResult0.setVisible(true);
    	gyroTestResult1.setVisible(true);
    	gyroTestResult2.setVisible(true);
    	
    	gyroIndication0.setBackground(Color.blue);
    	gyroIndication1.setBackground(Color.blue);
    	gyroIndication2.setBackground(Color.blue); 
    	gyroIndication3.setBackground(Color.blue);
    	gyroIndication4.setBackground(Color.blue); 
		
    }
    
    private void displayUart1Test() {
    	uart1Indication0.setVisible(true);
    	uart1Indication1.setVisible(true);
    	
    	uart1Indication0.setBackground(Color.blue);
    	uart1Indication1.setBackground(Color.blue);		
    }
    
    private void displayUart2Test() {
    	uart2Indication0.setVisible(true);
    	uart2Indication1.setVisible(true);
    	uart2Indication2.setVisible(true);
    	uart2Indication3.setVisible(true);
    	
    	uart2Indication0.setBackground(Color.blue);
    	uart2Indication1.setBackground(Color.blue);
    	uart2Indication2.setBackground(Color.blue);
    	uart2Indication3.setBackground(Color.blue);
    }
    
    void gyroClear() {
    	gyroIndication0.setVisible(false);
    	gyroIndication1.setVisible(false);
    	gyroIndication2.setVisible(false);
    	gyroIndication3.setVisible(false);
    	gyroIndication4.setVisible(false);
    	gyroTestResult0.setVisible(false);
    	gyroTestResult1.setVisible(false);
    	gyroTestResult2.setVisible(false);
				 		
   }
    
    void uart1Clear() {
    	uart1Indication0.setVisible(false);
    	uart1Indication1.setVisible(false);	 		
    }
    
    void uart2Clear() {
    	uart2Indication0.setVisible(false);
    	uart2Indication1.setVisible(false);
    	uart2Indication2.setVisible(false);
    	uart2Indication3.setVisible(false);
    }
    
    private void displayAcceTest() {
    	acceIndication0.setVisible(true);        
    	acceIndication1.setVisible(true);
    	acceIndication2.setVisible(true);
    	acceIndication3.setVisible(true);
    	acceIndication4.setVisible(true);
    	acceTestResult0.setVisible(true);
    	acceTestResult1.setVisible(true);
    	acceTestResult2.setVisible(true);
    	
    	acceIndication0.setBackground(Color.blue);
    	acceIndication1.setBackground(Color.blue);
    	acceIndication2.setBackground(Color.blue); 
    	acceIndication3.setBackground(Color.blue);
    	acceIndication4.setBackground(Color.blue); 
		
    }
    
    
    void acceClear() {
    	acceIndication0.setVisible(false);
    	acceIndication1.setVisible(false);
    	acceIndication2.setVisible(false);
    	acceIndication3.setVisible(false);
    	acceIndication4.setVisible(false);
    	acceTestResult0.setVisible(false);
    	acceTestResult1.setVisible(false);
    	acceTestResult2.setVisible(false);
				 		
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

						autoTestBt.setEnabled(false);
						autoTestInfo.setText("正在检测环境....");
						autoTestBt.setBackground(Color.gray);
						
						String deviceList = device.getAdbDevices();
						if(deviceList == null || deviceList.isEmpty() ){
						    chipidInfo.setText(" 失败，未找到设备");
						    break;
						} else {
							
						}
						
					    device.setAudioVolume(5);
													
						String id = device.getChipId();
						if(id == null || id.isEmpty()){
						    chipidInfo.setText("获取SN失败");
						} else {
							chipidInfo.setText("SN   :  " + id);
						}
								
						autoTestInfo.setText("环境通过");
						
					    break;
				    }
				    
				    case AUDIO_TEST :{

				    	boolean audioret = false;
						flashqueue.put(Flash_Type.AUDIO);
						audioFlashCnt = 0;
						String deviceList = device.getAdbDevices();
						
						
						if(deviceList == null || deviceList.isEmpty() ){
						    //audioTestResult.setText(audioTestCnt + " 失败，USB未连接");
						} else {
							//TestTimeTask.getInstance().setDuration(5000);
							//TestTimeTask.getInstance().start(true);
							String result = device.audioAdbTest();
							if(result.equalsIgnoreCase("pass") ) {
								//audioTestResult.setText(audioTestCnt + " 成功 ");
								audioret = true;
							} else {
								//audioTestResult.setText(audioTestCnt + " " + result);
							}
							
							//TestTimeTask.getInstance().destroyed();
						}
						if (audioret) {
						    flashqueue.put(Flash_Type.AUDIO_END_OK);
						    testResult.put(Test_Type.AUDIO_TEST, 1);
						} else {
							flashqueue.put(Flash_Type.AUDIO_END_FAIL);
							testResult.put(Test_Type.AUDIO_TEST, 0);
						}
												
						
					    break;
				    }
				    
				    case CAMERA_TEST : {

                        //cameraTestResult.setText("");
				    	
				    	boolean cameraret = false;
                        flashqueue.put(Flash_Type.CAMERA);
                        					
						
						String deviceList = device.getAdbDevices();
						if(deviceList == null || deviceList.isEmpty() ){
						    
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
						            
						        }
							}else {
								
							}
						}
						
						if(cameraret) {
						    flashqueue.put(Flash_Type.CAMERA_END_OK);
						    testResult.put(Test_Type.CAMERA_TEST, 1);
						}else {
							flashqueue.put(Flash_Type.CAMERA_END_FAIL);
							testResult.put(Test_Type.CAMERA_TEST, 0);
						}
						
					    break;
				    
				    }
				    
				case GYRO_TEST : {
					boolean gyroret = false;
					flashqueue.put(Flash_Type.GYRO);
					
					if(device.checkGyro()){
			    	    device.controlGyro(true);
			    	    showGyrodata();
			    	    device.controlGyro(false);
			    	    gyroret = true;
			    	}else {
			    		gyroTestResult0.setText("驱动未找到");
			    	}
					
					if (gyroret) {
						flashqueue.put(Flash_Type.GYRO_END_OK);
					    testResult.put(Test_Type.GYRO_TEST, 1);
					} else {
						flashqueue.put(Flash_Type.GYRO_END_FAIL);
					    testResult.put(Test_Type.GYRO_TEST, 0);
					}
					
					break;
				}
				
                case ACCE_TEST : {
                	boolean acceret = false;
					flashqueue.put(Flash_Type.ACCE);
					
                	if(device.checkAcce()){
			    	    device.controlAcce(true);
			    	    showAccedata();
			    	    device.controlAcce(false);
			    	}else {
			    		acceTestResult0.setText("驱动未找到");
			    	}
                	
                	if (acceret) {
						flashqueue.put(Flash_Type.ACCE_END_OK);
					    testResult.put(Test_Type.ACCE_TEST, 1);
					} else {
						flashqueue.put(Flash_Type.ACCE_END_FAIL);
					    testResult.put(Test_Type.ACCE_TEST, 0);
					}
                	
					break;
				}
				    
                case UART1_TEST : {
                	boolean uart1ret = false;
                	flashqueue.put(Flash_Type.UART1);
                	if(device.uart1test()){
			    		uart1ret = true;
			    	} else {
			    		
			    	}
                	
                	if (uart1ret) {
						flashqueue.put(Flash_Type.UART1_END_OK);
					    testResult.put(Test_Type.UART1_TEST, 1);
					} else {
						flashqueue.put(Flash_Type.UART1_END_FAIL);
					    testResult.put(Test_Type.UART1_TEST, 0);
					}
                	
                	
                	break;
                }
                
                case UART2_TEST : {
                	boolean uart2ret = false;
                	flashqueue.put(Flash_Type.UART2);
                	if(device.uart2test()){
			    		uart2ret = true;
			    	} else {
			    		
			    	}
                	
                	if (uart2ret) {
						flashqueue.put(Flash_Type.UART2_END_OK);
					    testResult.put(Test_Type.UART2_TEST, 1);
					} else {
						flashqueue.put(Flash_Type.UART2_END_FAIL);
					    testResult.put(Test_Type.UART2_TEST, 0);
					}
                	break;
                }
                
				case NONE : {
					autoTestBt.setEnabled(true);
					autoTestInfo.setText("结束");
					autoTestBt.setBackground(Color.green);
					break;
				}
				
				default:
					break;
				}
				
				
				
				
		        } catch (InterruptedException e) {
			        e.printStackTrace();
		        }
	
	        }
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
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null && num < 20) {
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
        //testState = Test_Type.NONE;
    	
    	return ret;
	}
	
	
	
	public boolean showAccedata() {
		boolean ret = false;
		String command = "adb shell getevent";
		int num = 0;
		//testState = Test_Type.GSENSOR_TEST;
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null && num < 20) {
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
        //testState = Test_Type.NONE;
    	
    	return ret;
	}
	
    class flashThread implements Runnable {
		@Override
		public void run () {
			System.out.println("flashThread start ...");
			while(testOnGoing){
			    try {
				    Flash_Type cmd  = flashqueue.take();
				
				   // System.out.println("flashThead receive :" + cmd);
				
				    switch(cmd) {
				    case AUDIO : {
				    	
				    	audioFlash(audioFlashCnt%10);
				    	try {
				    		Thread.sleep(1000);				    		
				    	} catch (InterruptedException e) {
				    		e.printStackTrace();
				    	}
				    	audioFlashCnt++;
				    	flashqueue.put(Flash_Type.AUDIO);
				    	break;
				    }
				    case AUDIO_END_OK: {
				    	audioFlash(8);
				    	audioFlash(9);
				    	flashqueue.removeIf(value -> value==Flash_Type.AUDIO);
				    	break;
				    }
				    
				    case AUDIO_END_FAIL: {
				    	audioFail();
				    	flashqueue.clear();
				    	break;
				    }
				    		    
				    
				    case CAMERA : {
				    	cameraFlash(cameraFlashCnt%2);
				    	try {
				    		Thread.sleep(900);				    		
				    	} catch (InterruptedException e) {
				    		e.printStackTrace();
				    	}
				    	cameraFlashCnt++;
				    	flashqueue.put(Flash_Type.CAMERA);
				    	break;
				    	
				    }
				    
				    case CAMERA_END_OK : {
				    	cameraFlash(1);
				    	cameraFlash(2);
				    	flashqueue.clear();
				    	break;
				    }
				    
				    case CAMERA_END_FAIL : {
				    	cameraFail();
				    	flashqueue.clear();
				    	break;
				    }
				    
				    default : {
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
    		audioIndication0.setBackground(Color.green);
    		break;
    	case 1:
    		audioIndication1.setBackground(Color.green);
    		break;
    	case 2:
    		audioIndication2.setBackground(Color.green);
    		break;
    	case 3:
    		audioIndication3.setBackground(Color.green);
    		break;
    	case 4:
    		audioIndication4.setBackground(Color.green);
    		break;
    	case 5:
    		audioIndication5.setBackground(Color.green);
    		break;
    	case 6:
    		audioIndication6.setBackground(Color.green);
    		break;
    	case 7:
    		audioIndication7.setBackground(Color.green);
    		break;
    	case 8:
    		audioIndication8.setBackground(Color.green);
    		break;
    	case 9:
    		audioIndication9.setBackground(Color.green);
    		break;
    	}
	}
    
    void audioFail() {
			audioIndication0.setBackground(Color.red);
    		audioIndication1.setBackground(Color.red);
    		audioIndication2.setBackground(Color.red);
     		audioIndication3.setBackground(Color.red);
     		audioIndication4.setBackground(Color.red);
     		audioIndication5.setBackground(Color.red);
     		audioIndication6.setBackground(Color.red);
     		audioIndication7.setBackground(Color.red);
    		audioIndication8.setBackground(Color.red);
    		audioIndication9.setBackground(Color.red); 		
	}
    
    
    
    void cameraFlash(int num) {
		switch(num) {
    	case 0:
    		cameraIndication0.setBackground(Color.green);
    		break;
    	case 1:
    		cameraIndication1.setBackground(Color.green);
    		break;
    	case 2:
    		cameraIndication2.setBackground(Color.green);
    		break;
    	}
	}
    
    void cameraFail() {
    		cameraIndication0.setBackground(Color.red);
    		
    		cameraIndication1.setBackground(Color.red);
    		
    		cameraIndication2.setBackground(Color.red);
	}
    
    void cameraClear() {
		cameraIndication0.setVisible(false);
		
		cameraIndication1.setVisible(false);
		
		cameraIndication2.setVisible(false);
    }
    
    void gyroFlash(int num) {
		switch(num) {
    	case 0:
    		gyroIndication0.setBackground(Color.green);
    		break;
    	case 1:
    		gyroIndication1.setBackground(Color.green);
    		break;
    	case 2:
    		gyroIndication2.setBackground(Color.green);
    		break;
    	case 3:
    		gyroIndication3.setBackground(Color.green);
    		break;
    	case 4:
    		gyroIndication4.setBackground(Color.green);
    		break;
    	}
	}
    
    void gyroFail() {
    	gyroIndication0.setBackground(Color.red);
    	gyroIndication1.setBackground(Color.red);
    	gyroIndication2.setBackground(Color.red);
    	gyroIndication3.setBackground(Color.red);
    	gyroIndication4.setBackground(Color.red);
    }
    
    void acceFlash(int num) {
		switch(num) {
    	case 0:
    		acceIndication0.setBackground(Color.green);
    		break;
    	case 1:
    		acceIndication1.setBackground(Color.green);
    		break;
    	case 2:
    		acceIndication2.setBackground(Color.green);
    		break;
    	case 3:
    		acceIndication3.setBackground(Color.green);
    		break;
    	case 4:
    		acceIndication4.setBackground(Color.green);
    		break;
    	}
	}
    
    void acceFail() {
    	acceIndication0.setBackground(Color.red);
    	acceIndication1.setBackground(Color.red);
    	acceIndication2.setBackground(Color.red);
    	acceIndication3.setBackground(Color.red);
    	acceIndication4.setBackground(Color.red);
    }
    
    
    
    public void close() {
    	System.out.println("AutoTest close ...");
    	database.Close();
    }

        

}
