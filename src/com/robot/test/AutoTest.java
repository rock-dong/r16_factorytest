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
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


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
	JLabel passNumberTitle = new JLabel(); 
	JLabel failTitle = new JLabel("失败");
	JLabel failNumberTitle = new JLabel();
	
	JButton autoTestBt = new JButton("开始");
	JLabel autoTestInfo = new JLabel("");
	JLabel chipidInfo = new JLabel("");
	String chipid;
	String barcode;
	boolean mUsbCheckFail = true;
	JLabel barcodeInfo = new JLabel("");
	
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
	
	
	
	private static final String audioResult0Name = "有";
	JLabel cbAudioTitle = new JLabel("");

	JCheckBox cbjAudio0 = new JCheckBox(audioResult0Name, false);
	private static final String audioResult1Name = "没有";
    
    JCheckBox cbjAudio1 = new JCheckBox(audioResult1Name, false);
    
	JButton cameraIndication0 = new JButton("");
	JButton cameraIndication1 = new JButton("");
	JButton cameraIndication2 = new JButton("");
	
	
	
	private static final String cameraResult0Name = "有";
	JLabel cbCameraTitle = new JLabel("");
	
	JCheckBox cbjCamera0 = new JCheckBox(cameraResult0Name,false);
	
	private static final String cameraResult1Name = "没有";
    JCheckBox cbjCamera1 = new JCheckBox(cameraResult1Name,false);
    
	JButton gyroIndication0 = new JButton("");
	JButton gyroIndication1 = new JButton("");
	JButton gyroIndication2 = new JButton("");
	JButton gyroIndication3 = new JButton("");
	JButton gyroIndication4 = new JButton("");
	
	JLabel gyroTestResult0 = new JLabel(""); 
	JLabel gyroTestResult1 = new JLabel("");
	JLabel gyroTestResult2 = new JLabel("");
	
	JLabel gyroMinTitle = new JLabel("最小值",JLabel.CENTER);
	JLabel gyroMaxTitle = new JLabel("最大值",JLabel.CENTER);
	JLabel gyroMeasureTitle = new JLabel("角度",JLabel.CENTER);
	JLabel gyroXTitle = new JLabel("X轴",JLabel.CENTER);
	JLabel gyroYTitle = new JLabel("Y轴",JLabel.CENTER);
	JLabel gyroZTitle = new JLabel("Z轴",JLabel.CENTER);
	
	boolean gyroLockFlag = false;
	JButton gyroXYZLockTitle = new JButton("锁定");
	
	JTextField gyroXMinValue = new JTextField("-300");
	JTextField gyroXMaxValue = new JTextField("300");
	JTextField gyroYMinValue = new JTextField("-350");
	JTextField gyroYMaxValue = new JTextField("300");
	JTextField gyroZMinValue = new JTextField("-400");
	JTextField gyroZMaxValue = new JTextField("500");
	
	String gyroXMinKey = "-300";
	String gyroXMaxKey = "300";
	String gyroYMinKey = "-350";
	String gyroYMaxKey = "300";
	String gyroZMinKey = "-400";
	String gyroZMaxKey = "500";
	
	
	JButton acceIndication0 = new JButton("");
	JButton acceIndication1 = new JButton("");
	JButton acceIndication2 = new JButton("");
	JButton acceIndication3 = new JButton("");
	JButton acceIndication4 = new JButton("");
	JLabel acceTestResult0 = new JLabel(""); 
	JLabel acceTestResult1 = new JLabel("");
	JLabel acceTestResult2 = new JLabel("");
	
	JLabel acceMinTitle = new JLabel("最小值");
	JLabel acceMaxTitle = new JLabel("最大值");
	JLabel acceMeasureTitle = new JLabel("加速度");
	JLabel acceXTitle = new JLabel("X轴");
	JLabel acceYTitle = new JLabel("Y轴");
	JLabel acceZTitle = new JLabel("Z轴");
	
	boolean acceLockFlag = false;
	JButton acceXYZLockTitle = new JButton("锁定");
	
	JTextField acceXMinValue = new JTextField("60");
	JTextField acceXMaxValue = new JTextField("290");
	JTextField acceYMinValue = new JTextField("0");
	JTextField acceYMaxValue = new JTextField("50");
	JTextField acceZMinValue = new JTextField("900");
	JTextField acceZMaxValue = new JTextField("1520");
	
	String acceXMinKey = "60";
	String acceXMaxKey = "290";
	String acceYMinKey = "0";
	String acceYMaxKey = "50";
	String acceZMinKey = "900";
	String acceZMaxKey = "1520";
	
	
	JButton uart1Indication0 = new JButton("");
	JButton uart1Indication1 = new JButton("");
	JButton uart1Indication2 = new JButton("");
	JButton uart1Indication3 = new JButton("");
	JButton uart1Indication4 = new JButton("");
	
	
	JButton uart2Indication0 = new JButton("");
	JButton uart2Indication1 = new JButton("");
	JButton uart2Indication2 = new JButton("");
	JButton uart2Indication3 = new JButton("");
	JButton uart2Indication4 = new JButton("");
	
	private LinkedBlockingQueue<Test_Type> queue = new LinkedBlockingQueue<Test_Type>();
	private LinkedBlockingQueue<Flash_Type> flashqueue = new LinkedBlockingQueue<Flash_Type>();
	
	int audioFlashCnt = 0;
	int cameraFlashCnt = 0;
	int gyroFlashCnt = 0;
	int acceFlashCnt = 0;
	int uart1FlashCnt = 0;
	int uart2FlashCnt = 0;
	
	public static boolean testOnGoing = true;
	
	private DeviceControl device = new DeviceControl();
	
	MyPanel cameraArea = new MyPanel();
	
	List<FactoryRecord> savedRecord;
	List<FactoryRecord> passRecord;
	List<FactoryRecord> failRecord;
	int passNum = 0;
	int failNum = 0;
    public AutoTest() {
    	setSize(700, 700);
		setLocation(300, 300);
		setTitle("自动化测试");
		setLayout(null);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		System.out.println("autotest enter ..");
		
		
		loadRecordThread  tThSql = new loadRecordThread();
		Thread thread3 = new Thread(tThSql);
		thread3.start();
		
		
		
		
		
		
		testItem.add(Test_Type.USB_TEST);
		CbItemListener cbi = new CbItemListener();
		CbAudioItemListener cbiAudio = new CbAudioItemListener();
		CbCameraItemListener cbiCamera = new CbCameraItemListener();
		
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
		
        add(passNumberTitle);
        passNumberTitle.setBounds(910, 200, 300, 30);
        passNumberTitle.setFont(new Font("Dialog", 1, 30));
        passNumberTitle.setForeground(Color.green);
        
        add(failTitle);
	    failTitle.setBounds(1110, 150, 100, 30);
	    failTitle.setFont(new Font("Dialog", 1, 30));
        failTitle.setForeground(Color.red);
		
        add(failNumberTitle);
        failNumberTitle.setBounds(1110, 200, 300, 30);
        failNumberTitle.setFont(new Font("Dialog", 1, 30));
        failNumberTitle.setForeground(Color.red);
        
        
        add(autoTestBt);
        autoTestBt.setBounds(410, 200, 100, 50);
        autoTestBt.setForeground(Color.blue);
        autoTestBt.setFont(new Font("Dialog", 1, 30));
        autoTestBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("auto test start");
				
				popBarcodeIndication();
				loadSelectedItem();
				
				
			}
		});
    	
        
        
        add(autoTestInfo);
        autoTestInfo.setBounds(550, 200, 200, 40);
        autoTestInfo.setForeground(Color.blue);
        autoTestInfo.setFont(new Font("Dialog", 1, 20));
        
        //add(chipidInfo);
        //chipidInfo.setBounds(300, 250, 500, 40);
        //chipidInfo.setFont(new Font("Dialog", 1, 20));
        //chipidInfo.setForeground(Color.red);
        
        add(barcodeInfo);
        barcodeInfo.setBounds(300, 250, 500, 40);
        barcodeInfo.setFont(new Font("Dialog", 1, 20));
        barcodeInfo.setForeground(Color.blue);
        
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
        
        add(cbAudioTitle);
        cbAudioTitle.setBounds(100, 380, 50, 30);
        cbAudioTitle.setFont(new Font("Dialog", 1, 20));
        cbAudioTitle.setForeground(Color.blue);
        /*
        add(cbAudio0);
        cbAudio0.setBounds(100, 420, 100, 30);
        cbAudio0.setFont(new Font("Dialog", 1, 20));
        cbAudio0.setVisible(false);
        cbAudio0.addItemListener(cbiAudio);
        add(cbAudio1);
        cbAudio1.setBounds(100, 450, 100, 30);
        cbAudio1.setFont(new Font("Dialog", 1, 20));
        cbAudio1.setVisible(false);
        cbAudio1.addItemListener(cbiAudio);
        */
        
        add(cbjAudio0);
        cbjAudio0.setBounds(100, 420, 100, 30);
        cbjAudio0.setFont(new Font("Dialog", 1, 20));
        cbjAudio0.setVisible(false);
        cbjAudio0.addItemListener(cbiAudio);
        add(cbjAudio1);
        cbjAudio1.setBounds(100, 450, 100, 30);
        cbjAudio1.setFont(new Font("Dialog", 1, 20));
        cbjAudio1.setVisible(false);
        cbjAudio1.addItemListener(cbiAudio);
        


        
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
        
        add(cbCameraTitle);
        cbCameraTitle.setBounds(200, 380, 50, 30);
        cbCameraTitle.setForeground(Color.blue);
        cbCameraTitle.setFont(new Font("Dialog", 1, 20));
        
        add(cbjCamera0);
        cbjCamera0.setBounds(200, 420, 100, 30);
        cbjCamera0.setFont(new Font("Dialog", 1, 20));
        cbjCamera0.setVisible(false);
        cbjCamera0.addItemListener(cbiCamera);
        
        add(cbjCamera1);
        cbjCamera1.setBounds(200, 450, 100, 30);
        cbjCamera1.setFont(new Font("Dialog", 1, 20));
        cbjCamera1.setVisible(false);
        cbjCamera1.addItemListener(cbiCamera);
        
        add(cameraArea);
                
        cameraArea.setBounds(200, 500, 320, 240);
        cameraArea.
               
        
        add(chipidInfo);
        chipidInfo.setBounds(300, 750, 500, 40);
        chipidInfo.setFont(new Font("Dialog", 1, 20));
        chipidInfo.setForeground(Color.red);
        
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
        gyroTestResult0.setBounds(300, 340, 200, 25);
        gyroTestResult0.setFont(new Font("Dialog", 1, 12));
        
        add(gyroTestResult1);
        gyroTestResult1.setBounds(300, 365, 200, 25);
        gyroTestResult1.setFont(new Font("Dialog", 1, 12));
        add(gyroTestResult2);
        gyroTestResult2.setBounds(300, 390, 200, 25);
        gyroTestResult2.setFont(new Font("Dialog", 1, 12));
        
        
        add(gyroMinTitle);
        gyroMinTitle.setBounds(750, 300, 100, 30);
        add(gyroMeasureTitle);
        gyroMeasureTitle.setBounds(850, 300, 100, 30);
        add(gyroMaxTitle);
        gyroMaxTitle.setBounds(950, 300, 100, 30);
        
        
        add(gyroXMinValue);
        gyroXMinValue.setBounds(750, 340, 100, 25);
        gyroXMinValue.addCaretListener(new CaretListener(){
        	

			@Override
			public void caretUpdate(CaretEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(gyroXMinValue.getText());
				gyroXMinKey = gyroXMinValue.getText();
			}
        });
        
        add(gyroXTitle);
        gyroXTitle.setBounds(850, 340, 100, 25);
    	add(gyroXMaxValue);
    	gyroXMaxValue.setBounds(950, 340, 100, 25);
        gyroXMaxValue.addCaretListener(new CaretListener(){
        	

			@Override
			public void caretUpdate(CaretEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(gyroXMinValue.getText());
				gyroXMaxKey = gyroXMinValue.getText();
			}
        });
        
    	add(gyroYMinValue);
        gyroYMinValue.setBounds(750, 365, 100, 25);
        
        gyroYMinValue.addCaretListener(new CaretListener(){
        	

			@Override
			public void caretUpdate(CaretEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(gyroXMinValue.getText());
				gyroYMinKey = gyroYMinValue.getText();
			}
        });
        
        
        
        add(gyroYTitle);
        gyroYTitle.setBounds(850, 365, 100, 25);
    	add(gyroYMaxValue);
    	gyroYMaxValue.setBounds(950, 365, 100, 25);
        gyroYMaxValue.addCaretListener(new CaretListener(){
        	

			@Override
			public void caretUpdate(CaretEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(gyroXMinValue.getText());
				gyroYMaxKey = gyroYMaxValue.getText();
			}
        });
        
    	add(gyroZMinValue);
        gyroZMinValue.setBounds(750, 390, 100, 25);
        gyroZMinValue.addCaretListener(new CaretListener(){
        	

			@Override
			public void caretUpdate(CaretEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(gyroXMinValue.getText());
				gyroZMinKey = gyroZMinValue.getText();
			}
        });
        
        
        add(gyroZTitle);
        gyroZTitle.setBounds(850, 390, 100, 25);
    	add(gyroZMaxValue);
    	gyroZMaxValue.setBounds(950, 390, 100, 25);
        gyroZMaxValue.addCaretListener(new CaretListener(){
        	

			@Override
			public void caretUpdate(CaretEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(gyroXMinValue.getText());
				gyroZMaxKey = gyroZMaxValue.getText();
			}
        });
        
        
        add(gyroXYZLockTitle);
        gyroXYZLockTitle.setBounds(1050, 365, 100, 25);
        gyroXYZLockTitle.setForeground(Color.blue);
        gyroXYZLockTitle.setBackground(Color.red);
        gyroXYZLockTitle.setFont(new Font("Dialog", 1, 20));
        gyroXYZLockTitle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("auto test start");
				
						if(gyroLockFlag) {
							gyroXMinValue.setEditable(true);
							gyroXMaxValue.setEditable(true);
							gyroYMinValue.setEditable(true);
							gyroYMaxValue.setEditable(true);
							gyroZMinValue.setEditable(true);
							gyroZMaxValue.setEditable(true);
							gyroXYZLockTitle.setText("锁定");
							gyroXYZLockTitle.setBackground(Color.red);
							gyroLockFlag = false;
						} else {
							gyroXMinValue.setEditable(false);
							gyroXMaxValue.setEditable(false);
							gyroYMinValue.setEditable(false);
							gyroYMaxValue.setEditable(false);
							gyroZMinValue.setEditable(false);
							gyroZMaxValue.setEditable(false);
							gyroXYZLockTitle.setBackground(Color.green);
							gyroXYZLockTitle.setText("解锁");
							gyroLockFlag = true;
						}
				
			}
		});
        
        
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
        acceTestResult0.setFont(new Font("Dialog", 1, 12));
        add(acceTestResult1);
        acceTestResult1.setBounds(400, 365, 100, 25);
        acceTestResult1.setFont(new Font("Dialog", 1, 12));
        add(acceTestResult2);
        acceTestResult2.setBounds(400, 390, 100, 25);
        acceTestResult2.setFont(new Font("Dialog", 1, 12));
        
        
        
        add(acceMinTitle);
        acceMinTitle.setBounds(750, 430, 100, 30);
        add(acceMeasureTitle);
        acceMeasureTitle.setBounds(850, 430, 100, 30);
        add(acceMaxTitle);
        acceMaxTitle.setBounds(950, 430, 100, 30);
        
        
        add(acceXMinValue);
        acceXMinValue.setBounds(750, 470, 100, 25);
        acceXMinValue.addCaretListener(new CaretListener(){
        	

			@Override
			public void caretUpdate(CaretEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(gyroXMinValue.getText());
				acceXMinKey = acceXMinValue.getText();
			}
        });
        
        add(acceXTitle);
        acceXTitle.setBounds(850, 470, 100, 25);
    	add(acceXMaxValue);
    	acceXMaxValue.setBounds(950, 470, 100, 25);
        acceXMaxValue.addCaretListener(new CaretListener(){
        	

			@Override
			public void caretUpdate(CaretEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(gyroXMinValue.getText());
				acceXMaxKey = acceXMinValue.getText();
			}
        });
        
    	add(acceYMinValue);
        acceYMinValue.setBounds(750, 495, 100, 25);
        
        acceYMinValue.addCaretListener(new CaretListener(){
        	

			@Override
			public void caretUpdate(CaretEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(gyroXMinValue.getText());
				acceYMinKey = acceYMinValue.getText();
			}
        });
        
        
        
        add(acceYTitle);
        acceYTitle.setBounds(850, 495, 100, 25);
    	add(acceYMaxValue);
    	acceYMaxValue.setBounds(950, 495, 100, 25);
        acceYMaxValue.addCaretListener(new CaretListener(){
        	

			@Override
			public void caretUpdate(CaretEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(gyroXMinValue.getText());
				acceYMaxKey = acceYMaxValue.getText();
			}
        });
        
    	add(acceZMinValue);
        acceZMinValue.setBounds(750, 520, 100, 25);
        acceZMinValue.addCaretListener(new CaretListener(){
        	

			@Override
			public void caretUpdate(CaretEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(gyroXMinValue.getText());
				acceZMinKey = acceZMinValue.getText();
			}
        });
        
        
        add(acceZTitle);
        acceZTitle.setBounds(850, 520, 100, 25);
    	add(acceZMaxValue);
    	acceZMaxValue.setBounds(950, 520, 100, 25);
        acceZMaxValue.addCaretListener(new CaretListener(){
        	

			@Override
			public void caretUpdate(CaretEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(gyroXMinValue.getText());
				acceZMaxKey = acceZMaxValue.getText();
			}
        });
        
        
        add(acceXYZLockTitle);
        acceXYZLockTitle.setBounds(1050, 495, 100, 25);
        acceXYZLockTitle.setForeground(Color.blue);
        acceXYZLockTitle.setBackground(Color.red);
        acceXYZLockTitle.setFont(new Font("Dialog", 1, 20));
        acceXYZLockTitle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("auto test start");
				
						if(acceLockFlag) {
							acceXMinValue.setEditable(true);
							acceXMaxValue.setEditable(true);
							acceYMinValue.setEditable(true);
							acceYMaxValue.setEditable(true);
							acceZMinValue.setEditable(true);
							acceZMaxValue.setEditable(true);
							acceXYZLockTitle.setText("锁定");
							acceXYZLockTitle.setBackground(Color.red);
							acceLockFlag = false;
						} else {
							acceXMinValue.setEditable(false);
							acceXMaxValue.setEditable(false);
							acceYMinValue.setEditable(false);
							acceYMaxValue.setEditable(false);
							acceZMinValue.setEditable(false);
							acceZMaxValue.setEditable(false);
							acceXYZLockTitle.setBackground(Color.green);
							acceXYZLockTitle.setText("解锁");
							acceLockFlag = true;
						}
				
			}
		});
        
        
        
        
        add(uart1Indication0);
        uart1Indication0.setBounds(500, 300, 20, 30);
        uart1Indication0.setBackground(Color.blue);
        uart1Indication0.setBorderPainted(false);
        uart1Indication0.setFocusPainted(false);
        uart1Indication0.setEnabled(false);
        uart1Indication0.setVisible(false);
        add(uart1Indication1);
        uart1Indication1.setBounds(520, 300, 20, 30);
        uart1Indication1.setBackground(Color.blue);
        uart1Indication1.setBorderPainted(false);
        uart1Indication1.setFocusPainted(false);
        uart1Indication1.setEnabled(false);
        uart1Indication1.setVisible(false);
        add(uart1Indication2);
        uart1Indication2.setBounds(540, 300, 20, 30);
        uart1Indication2.setBackground(Color.blue);
        uart1Indication2.setBorderPainted(false);
        uart1Indication2.setFocusPainted(false);
        uart1Indication2.setEnabled(false);
        uart1Indication2.setVisible(false);
        add(uart1Indication3);
        uart1Indication3.setBounds(560, 300, 20, 30);
        uart1Indication3.setBackground(Color.blue);
        uart1Indication3.setBorderPainted(false);
        uart1Indication3.setFocusPainted(false);
        uart1Indication3.setEnabled(false);
        uart1Indication3.setVisible(false);
        add(uart1Indication4);
        uart1Indication4.setBounds(580, 300, 20, 30);
        uart1Indication4.setBackground(Color.blue);
        uart1Indication4.setBorderPainted(false);
        uart1Indication4.setFocusPainted(false);
        uart1Indication4.setEnabled(false);
        uart1Indication4.setVisible(false);
        
        
        add(uart2Indication0);
        uart2Indication0.setBounds(600, 300, 20, 30);
        uart2Indication0.setBackground(Color.blue);
        uart2Indication0.setBorderPainted(false);
        uart2Indication0.setFocusPainted(false);
        uart2Indication0.setEnabled(false);
        uart2Indication0.setVisible(false);
        add(uart2Indication1);
        uart2Indication1.setBounds(620, 300, 20, 30);
        uart2Indication1.setBackground(Color.blue);
        uart2Indication1.setBorderPainted(false);
        uart2Indication1.setFocusPainted(false);
        uart2Indication1.setEnabled(false);
        uart2Indication1.setVisible(false);
        add(uart2Indication2);
        uart2Indication2.setBounds(640, 300, 20, 30);
        uart2Indication2.setBackground(Color.blue);
        uart2Indication2.setBorderPainted(false);
        uart2Indication2.setFocusPainted(false);
        uart2Indication2.setEnabled(false);
        uart2Indication2.setVisible(false);
        add(uart2Indication3);
        uart2Indication3.setBounds(660, 300, 20, 30);
        uart2Indication3.setBackground(Color.blue);
        uart2Indication3.setBorderPainted(false);
        uart2Indication3.setFocusPainted(false);
        uart2Indication3.setEnabled(false);
        uart2Indication3.setVisible(false);
        add(uart2Indication4);
        uart2Indication4.setBounds(680, 300, 20, 30);
        uart2Indication4.setBackground(Color.blue);
        uart2Indication4.setBorderPainted(false);
        uart2Indication4.setFocusPainted(false);
        uart2Indication4.setEnabled(false);
        uart2Indication4.setVisible(false);
        
        
        testThread  tTh = new testThread();
		Thread thread1 = new Thread(tTh);
		thread1.start();
		
		flashThread  tThFlash = new flashThread();
		Thread thread2 = new Thread(tThFlash);
		thread2.start();
		
		
		
    }
    
    
    class CbAudioItemListener implements ItemListener  
    {  
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource().equals(cbjAudio0) && cbjAudio0.isSelected()){
				
				testResult.put(Test_Type.AUDIO_TEST, 1);
				cbjAudio1.setSelected(false);
				System.out.println("audio manual check pass");
			} 
			
			if (e.getSource().equals(cbjAudio1) &&  cbjAudio1.isSelected()){
				
				cbjAudio0.setSelected(false);
				testResult.put(Test_Type.AUDIO_TEST, 0);
				System.out.println("audio manual check fail");
			}
			
		}
    }
    
    class CbCameraItemListener implements ItemListener  
    {  
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
             if(e.getSource().equals(cbjCamera0) && cbjCamera0.isSelected()){
				
				testResult.put(Test_Type.CAMERA_TEST, 1);
				cbjCamera1.setSelected(false);
				System.out.println("camera manual check pass");
			}
			
			if (e.getSource().equals(cbjCamera1) &&  cbjCamera1.isSelected()){
				
				cbjCamera0.setSelected(false);
				testResult.put(Test_Type.CAMERA_TEST, 0);
				System.out.println("camera manual check fail");
			}
			
		}
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
				break;
			}
			
			
			
			
			
			}
			
		}
    }
    
    
    private void popBarcodeIndication() {
    	String str = JOptionPane.showInputDialog("请输入条形码数字");
    	System.out.println(str);
    	barcodeInfo.setText("条形码       :  " + str);
    	barcode = str;
    }
    
    
    private void loadSelectedItem() {
    	
    	testResult.clear();
    	mUsbCheckFail = true;
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
		
		
        
		cbjAudio0.setVisible(true);
		cbjAudio1.setVisible(true);
		cbjAudio0.setSelected(false);
		cbjAudio1.setSelected(false);
		//cbAudio1.setState(false);
		
		cbAudioTitle.setText("声音");
		
    }
    
    private void displayCameraTest() {
    	cameraIndication0.setVisible(true);        
    	cameraIndication1.setVisible(true);
    	cameraIndication2.setVisible(true);
    	cameraIndication0.setBackground(Color.blue);
    	cameraIndication1.setBackground(Color.blue);
    	cameraIndication2.setBackground(Color.blue); 
    	cbjCamera0.setVisible(true);
		cbjCamera1.setVisible(true);
		cbjCamera0.setSelected(false);
		cbjCamera1.setSelected(false);
    	deleteCameraFiles();
		showBmp();
		cbCameraTitle.setText("图像");
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
		cbjAudio0.setVisible(false);
		cbjAudio1.setVisible(false);
		cbAudioTitle.setText("");
   }
    
    private void displayGyroTest() {
    	gyroIndication0.setVisible(true);        
    	gyroIndication1.setVisible(true);
    	gyroIndication2.setVisible(true);
    	gyroIndication3.setVisible(true);
    	gyroIndication4.setVisible(true);
    	gyroTestResult0.setText("");
    	gyroTestResult1.setText("");
    	gyroTestResult2.setText("");
    	    	
    	gyroIndication0.setBackground(Color.blue);
    	gyroIndication1.setBackground(Color.blue);
    	gyroIndication2.setBackground(Color.blue); 
    	gyroIndication3.setBackground(Color.blue);
    	gyroIndication4.setBackground(Color.blue); 
		
    }
    
    private void displayUart1Test() {
    	uart1Indication0.setVisible(true);
    	uart1Indication1.setVisible(true);
    	uart1Indication2.setVisible(true);
    	uart1Indication3.setVisible(true);
    	uart1Indication4.setVisible(true);
    	
    	uart1Indication0.setBackground(Color.blue);
    	uart1Indication1.setBackground(Color.blue);
    	uart1Indication2.setBackground(Color.blue);
    	uart1Indication3.setBackground(Color.blue);
    	uart1Indication4.setBackground(Color.blue);
    	
    }
    
    private void displayUart2Test() {
    	uart2Indication0.setVisible(true);
    	uart2Indication1.setVisible(true);
    	uart2Indication2.setVisible(true);
    	uart2Indication3.setVisible(true);
    	uart2Indication4.setVisible(true);
    	
    	uart2Indication0.setBackground(Color.blue);
    	uart2Indication1.setBackground(Color.blue);
    	uart2Indication2.setBackground(Color.blue);
    	uart2Indication3.setBackground(Color.blue);
    	uart2Indication4.setBackground(Color.blue);
    }
    
    void gyroClear() {
    	gyroIndication0.setVisible(false);
    	gyroIndication1.setVisible(false);
    	gyroIndication2.setVisible(false);
    	gyroIndication3.setVisible(false);
    	gyroIndication4.setVisible(false);
    	gyroTestResult0.setText("");
    	gyroTestResult1.setText("");
    	gyroTestResult2.setText("");
				 		
   }
    
    void uart1Clear() {
    	uart1Indication0.setVisible(false);
    	uart1Indication1.setVisible(false);
    	uart1Indication2.setVisible(false);
    	uart1Indication3.setVisible(false);
    	uart1Indication4.setVisible(false);
    	
    }
    
    void uart2Clear() {
    	uart2Indication0.setVisible(false);
    	uart2Indication1.setVisible(false);
    	uart2Indication2.setVisible(false);
    	uart2Indication3.setVisible(false);
    	uart2Indication4.setVisible(false);
    }
    
    private void displayAcceTest() {
    	acceIndication0.setVisible(true);        
    	acceIndication1.setVisible(true);
    	acceIndication2.setVisible(true);
    	acceIndication3.setVisible(true);
    	acceIndication4.setVisible(true);
    	acceTestResult0.setText("");
    	acceTestResult1.setText("");
    	acceTestResult2.setText("");
    	
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
    	acceTestResult0.setText("");
    	acceTestResult1.setText("");
    	acceTestResult2.setText("");
				 		
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
						    autoTestInfo.setText("环境检测失败，结束");
						    break;
						} else {
							chipidInfo.setText("SN   :  " + id);
							chipid = id;
						}
								
						autoTestInfo.setText("环境通过");
						mUsbCheckFail = false;
						
					    break;
				    }
				    
				    case AUDIO_TEST :{

				    	boolean audioret = false;
				    	if(mUsbCheckFail) {
				    		System.out.println("skip, usb is faill");
				    		testResult.put(Test_Type.AUDIO_TEST, 0);
				    		break;
				    	}
				    	
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
						    
						} else {
							flashqueue.put(Flash_Type.AUDIO_END_FAIL);
							testResult.put(Test_Type.AUDIO_TEST, 0);
						}
												
						
					    break;
				    }
				    
				    case CAMERA_TEST : {

                        //cameraTestResult.setText("");
				    	
				    	boolean cameraret = false;
				    	if(mUsbCheckFail) {
				    		System.out.println("skip, usb is faill");
				    		testResult.put(Test_Type.CAMERA_TEST, 0);
				    		break;
				    	}
				    	cameraFlashCnt = 0;
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
						    
						}else {
							flashqueue.put(Flash_Type.CAMERA_END_FAIL);
							testResult.put(Test_Type.CAMERA_TEST, 0);
						}
						
					    break;
				    
				    }
				    
				case GYRO_TEST : {
					boolean gyroret = false;
					if(mUsbCheckFail) {
			    		System.out.println("skip, usb is faill");
			    		testResult.put(Test_Type.GYRO_TEST, 0);
			    		break;
			    	}
					
					if(!checkGyroXYZSet()) {
						JOptionPane.showMessageDialog(null, "角度最大值，最小值输入不完整", "角度", JOptionPane.ERROR_MESSAGE);
						testResult.put(Test_Type.GYRO_TEST, 0);
			    		break;
					}
					
					gyroFlashCnt = 0;
					flashqueue.put(Flash_Type.GYRO);
					
					if(device.checkGyro()){
			    	    device.controlGyro(true);
			    	    gyroret = showGyrodata();
			    	    device.controlGyro(false);
			    	    
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
                	acceFlashCnt = 0;
                	if(mUsbCheckFail) {
			    		System.out.println("skip, usb is faill");
			    		testResult.put(Test_Type.ACCE_TEST, 0);
			    		break;
			    	}
                	
                	if(!checkAcceXYZSet()) {
						JOptionPane.showMessageDialog(null, "加速度最大值，最小值输入不完整", "角度", JOptionPane.ERROR_MESSAGE);
						testResult.put(Test_Type.ACCE_TEST, 0);
			    		break;
					}
                	
					flashqueue.put(Flash_Type.ACCE);
					
                	if(device.checkAcce()){
			    	    device.controlAcce(true);
			    	    acceret = showAccedata();
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
                	uart1FlashCnt = 0;
                	if(mUsbCheckFail) {
			    		System.out.println("skip, usb is faill");
			    		testResult.put(Test_Type.UART1_TEST, 0);
			    		break;
			    	}
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
                	uart2FlashCnt = 0;
                	if(mUsbCheckFail) {
			    		System.out.println("skip, usb is faill");
			    		testResult.put(Test_Type.UART2_TEST, 0);
			    		break;
			    	}
                	
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
					
					
					checkAudioCameraResult();
					printTestResult();
					processResult();
					autoTestInfo.setText("结束");
                    autoTestBt.setEnabled(true);
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
    
    
    private boolean checkGyroXYZSet() {
    	if(gyroXMinKey == null || gyroXMinKey.isEmpty() || gyroXMinKey.length() == 0) {
    		return false;
    	}
    	
    	if(gyroXMaxKey == null || gyroXMaxKey.isEmpty() || gyroXMaxKey.length() == 0) {
    		return false;
    	}
    	
    	if(gyroYMinKey == null || gyroYMinKey.isEmpty() || gyroYMinKey.length() == 0) {
    		return false;
    	}
    	
    	if(gyroYMaxKey == null || gyroYMaxKey.isEmpty() || gyroYMaxKey.length() == 0) {
    		return false;
    	}
    	
    	if(gyroZMinKey == null || gyroZMinKey.isEmpty() || gyroZMinKey.length() == 0) {
    		return false;
    	}
    	
    	if(gyroZMaxKey == null || gyroZMaxKey.isEmpty() || gyroZMaxKey.length() == 0) {
    		return false;
    	}
    	
    	return true;
    	
    }
    
    
    private boolean checkAcceXYZSet() {
    	if(acceXMinKey == null || acceXMinKey.isEmpty() || acceXMinKey.length() == 0) {
    		return false;
    	}
    	
    	if(acceXMaxKey == null || acceXMaxKey.isEmpty() || acceXMaxKey.length() == 0) {
    		return false;
    	}
    	
    	if(acceYMinKey == null || acceYMinKey.isEmpty() || acceYMinKey.length() == 0) {
    		return false;
    	}
    	
    	if(acceYMaxKey == null || acceYMaxKey.isEmpty() || acceYMaxKey.length() == 0) {
    		return false;
    	}
    	
    	if(acceZMinKey == null || acceZMinKey.isEmpty() || acceZMinKey.length() == 0) {
    		return false;
    	}
    	
    	if(acceZMaxKey == null || acceZMaxKey.isEmpty() || acceZMaxKey.length() == 0) {
    		return false;
    	}
    	
    	return true;
    	
    }
    
    private void processResult() {
    	
    	int globalResult = 0;
    	java.util.Date utilDate = new Date();
    	int oknum = 0;
    	//Iterator<Map.Entry<Test_Type, Integer>> iter = testResult.entrySet().iterator();
    	
   	    System.out.println("processResult ...");
   	    if (testResult.size() == 6){
   	    	System.out.println("result number is right 6");
   	    } else {
   	    	globalResult = 0;
   	    	System.out.println("result number is wrong " + testResult.size());
   	    }
   	    
   	    FactoryRecord record = new FactoryRecord();
   	    
   	    record.SetBar(barcode);
   	    if (mUsbCheckFail) {
   	        record.SetSn("000000");
   	    } else {
   	    	record.SetSn(chipid);
   	    }
    	
   	    java.sql.Timestamp stp = new java.sql.Timestamp(utilDate.getTime());
   	    record.SetTime(stp);
   	    System.out.println(stp);
   	    
   	    if(testResult.containsKey(Test_Type.AUDIO_TEST)) {
   	    	int res = testResult.get(Test_Type.AUDIO_TEST);
   	    	record.SetAudio(res);
   	    	if(res == 1)
   	    	    oknum++;
   	    } else {
   	    	record.SetAudio(-1);
   	    }
   	    
   	    if(testResult.containsKey(Test_Type.CAMERA_TEST)) {
   	    	int res = testResult.get(Test_Type.CAMERA_TEST);
   	    	record.SetCamera(res);
   	    	if(res == 1)
   	    	    oknum++;
   	    }else {
   	    	record.SetCamera(-1);
   	    }
   	    
   	    if(testResult.containsKey(Test_Type.GYRO_TEST)) {
   	    	int res = testResult.get(Test_Type.GYRO_TEST);
	    	record.SetGyro(res);
	    	if(res == 1)
	    	    oknum++;
	    }else {
	    	record.SetGyro(-1);
	    }
   	 
   	    if(testResult.containsKey(Test_Type.ACCE_TEST)) {
	    	int res = testResult.get(Test_Type.ACCE_TEST);
   	    	record.SetAcce(res);
	    	if(res == 1)
	    	oknum++;
	    }else {
	    	record.SetAcce(-1);
	    }
   	
   	    if(testResult.containsKey(Test_Type.UART1_TEST)) {
   	    	int res = testResult.get(Test_Type.UART1_TEST);
	    	record.SetUart1(res);
	    	if(res == 1)
	    	    oknum++;
	    }else {
	    	record.SetUart1(-1);
	    }
   	
   	    if(testResult.containsKey(Test_Type.UART2_TEST)) {
   	    	int res = testResult.get(Test_Type.UART2_TEST);
	    	record.SetUart2(res);
	    	if(res == 1)
	    	    oknum++;
	    }else {
	    	record.SetUart2(-1);
	    }
   	    
   	    if (oknum == (testItem.size() - 1)) {
   	    	globalResult = 1;
   	    	System.out.println("test case all pass" + oknum);
   	    } else {
   	    	System.out.println("test case is " + oknum + "/" + testItem.size());
   	    	globalResult = 0;
   	    }
   	   	    
   	    record.SetResult(globalResult);
   	    
   	    
   	    if(barcode == null || barcode.isEmpty()) {
    	    System.out.println("no barcode, do not visit sql" );
   	    }else {
   	        FactoryRecord oldrecord = database.Read(barcode);
   	    
   	        if (oldrecord == null) {
   	    	    System.out.println("no record write" );
   	            database.Write(record);
   	        } else {
   	    	    System.out.println("has record " + oldrecord.GetBar() + "  update");
   	    	    database.Update(record);
   	        }
   	    }
   	 
   	    TestEntry.writeLog("auto test result " + globalResult);
   	    
   	    if (globalResult == 1) {
   	    	passNum++;
   	    	passNumberTitle.setText(Integer.toString(passNum));
   	    	device.ledPassFlash();
   	    } else {
   	    	failNum++;
   	    	failNumberTitle.setText(Integer.toString(failNum));
   	    	device.ledFailFlash();
   	    }
   	       	    
    }
    
    private void checkAudioCameraResult() {
    	
    	if(testItem.contains(Test_Type.AUDIO_TEST) && !testResult.containsKey(Test_Type.AUDIO_TEST)) {
    	
    	    int ret = JOptionPane.showConfirmDialog(this, "是否听到声音", "测试遗漏", JOptionPane.INFORMATION_MESSAGE);
    	    System.out.println(" 声音  " + ret);
    	    if (ret == 0) {
    	    	testResult.put(Test_Type.AUDIO_TEST, 1);
    	    	cbjAudio0.setSelected(true);
    	    } else if(ret ==  1) {
    	    	testResult.put(Test_Type.AUDIO_TEST, 0);
    	    	cbjAudio1.setSelected(true);
    	    } else if (ret == 2) {
    	    	testResult.put(Test_Type.AUDIO_TEST, -1);
    	    }
    	}
    	
    	if(testItem.contains(Test_Type.CAMERA_TEST) && !testResult.containsKey(Test_Type.CAMERA_TEST)) {
        	
    		int ret = JOptionPane.showConfirmDialog(this, "是否看到图像", "测试遗漏", JOptionPane.INFORMATION_MESSAGE);
    	    System.out.println(" 图像  " + ret);
    	    if (ret == 0) {
    	    	testResult.put(Test_Type.CAMERA_TEST, 1);
    	    	cbjCamera0.setSelected(true);
    	    } else if(ret ==  1) {
    	    	testResult.put(Test_Type.CAMERA_TEST, 0);
    	    	cbjCamera1.setSelected(true);
    	    } else if (ret == 2) {
    	    	testResult.put(Test_Type.CAMERA_TEST, -1);
    	    }
    	}
    	
    }
    
    
    private void printTestResult() {
    	Iterator<Map.Entry<Test_Type, Integer>> iter = testResult.entrySet().iterator();
    	
    	 while (iter.hasNext()) {
             @SuppressWarnings("rawtypes")
			  Map.Entry entry = (Map.Entry) iter.next();
              System.out.println("" + entry.getKey() + "" + (Integer) entry.getValue()); 
              
    	 }
    	
    }
    
    
    
    
    /*
    private void removeKeyFromTestResult(Test_Type key) {
    	Iterator<Map.Entry<Test_Type, Integer>> iter = testResult.entrySet().iterator();
    	
   	 while (iter.hasNext()) {
            @SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
            if(entry.getKey().equals(key)) {
            	iter.remove();
            }
             
   	     }
    }
    */
    
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

		TestEntry.writeLog("conversion is done");
		
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
		boolean ret = true;
		String command = "adb shell getevent";
		int num = 0;
		//testState = Test_Type.GSENSOR_TEST;
    	
		BigInteger xKey;
        int xMinKey = Integer.parseInt(gyroXMinKey);
        int xMaxKey = Integer.parseInt(gyroXMaxKey);
        int yMinKey = Integer.parseInt(gyroYMinKey);
        int yMaxKey = Integer.parseInt(gyroYMaxKey);
        int zMinKey = Integer.parseInt(gyroZMinKey);
        int zMaxKey = Integer.parseInt(gyroZMaxKey);
        
        gyroTestResult0.setForeground(Color.black);
        gyroTestResult1.setForeground(Color.black);
        gyroTestResult2.setForeground(Color.black);
        
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
                        String keyValue = line.substring(startIndex+4, line.length()).trim();
    	        		
    	        		xKey = new BigInteger(keyValue, 16);
    	        		
    	        		if(xKey.intValue() < xMinKey || xKey.intValue() > xMaxKey) {
    	        			gyroTestResult0.setText(Integer.toString(xKey.intValue()) + " 出界");
    	        			gyroTestResult0.setForeground(Color.red);
    	        			ret = false;
    	        			break;
    	        		} else {
    	        		    gyroTestResult0.setText(content);
    	        		}
    	        	}
    	        	if(line.contains("0003 0001")){
    	        		int startIndex = line.indexOf("0001");
    	        		String content = line.substring(startIndex, line.length());
                        String keyValue = line.substring(startIndex+4, line.length()).trim();
    	        		
                        xKey = new BigInteger(keyValue, 16);
    	        		
    	        		if(xKey.intValue() < yMinKey || xKey.intValue() > yMaxKey) {
    	        			gyroTestResult1.setText(Integer.toString(xKey.intValue()) + " 出界");
    	        			gyroTestResult1.setForeground(Color.red);
    	        			ret = false;
    	        			break;
    	        		} else {
    	        		    gyroTestResult1.setText(content);
    	        		}
    	        	}
    	        	if(line.contains("0003 0002")){
    	        		int startIndex = line.indexOf("0002");
    	        		String content = line.substring(startIndex, line.length());
                        String keyValue = line.substring(startIndex+4, line.length()).trim();
    	        		
                        xKey = new BigInteger(keyValue, 16);
    	        		
    	        		if(xKey.intValue() < zMinKey || xKey.intValue() > zMaxKey) {
    	        			gyroTestResult2.setText(Integer.toString(xKey.intValue()) + " 出界");
    	        			gyroTestResult2.setForeground(Color.red);
    	        			ret = false;
    	        			break;
    	        		} else {
    	        		    gyroTestResult2.setText(content);
    	        		}
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
		boolean ret = true;
		String command = "adb shell getevent";
		int num = 0;
		//testState = Test_Type.GSENSOR_TEST;
    	BigInteger xKey;
        int xMinKey = Integer.parseInt(acceXMinKey);
        int xMaxKey = Integer.parseInt(acceXMaxKey);
        int yMinKey = Integer.parseInt(acceYMinKey);
        int yMaxKey = Integer.parseInt(acceYMaxKey);
        int zMinKey = Integer.parseInt(acceZMinKey);
        int zMaxKey = Integer.parseInt(acceZMaxKey);
		
		
        acceTestResult0.setForeground(Color.black);
        acceTestResult1.setForeground(Color.black);
        acceTestResult2.setForeground(Color.black);
        
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
    	        		
                        String keyValue = line.substring(startIndex+4, line.length()).trim();
    	        		
    	        		xKey = new BigInteger(keyValue, 16);
    	        		
    	        		if(xKey.intValue() < xMinKey || xKey.intValue() > xMaxKey) {
    	        			acceTestResult0.setText(Integer.toString(xKey.intValue()) + " 出界");
    	        			acceTestResult0.setForeground(Color.blue);
    	        			ret = false;
    	        			break;
    	        		} else {
    	        		    acceTestResult0.setText(content);
    	        		}
    	        		
    	        	}
    	        	if(line.contains("0003 0001")){
    	        		int startIndex = line.indexOf("0001");
    	        		String content = line.substring(startIndex, line.length());
    	        		String keyValue = line.substring(startIndex+4, line.length()).trim();
    	        		
                        xKey = new BigInteger(keyValue, 16);
    	        		
    	        		if(xKey.intValue() < yMinKey || xKey.intValue() > yMaxKey) {
    	        			acceTestResult1.setText(Integer.toString(xKey.intValue()) + " 出界");
    	        			acceTestResult1.setForeground(Color.blue);
    	        			ret = false;
    	        			break;
    	        		} else {
    	        		    acceTestResult1.setText(content);
    	        		}
    	        	}
    	        	if(line.contains("0003 0002")){
    	        		int startIndex = line.indexOf("0002");
    	        		String content = line.substring(startIndex, line.length());
    	        		String keyValue = line.substring(startIndex+4, line.length()).trim();
    	        		
                        xKey = new BigInteger(keyValue, 16);
    	        		
    	        		if(xKey.intValue() < zMinKey || xKey.intValue() > zMaxKey) {
    	        			acceTestResult2.setText(Integer.toString(xKey.intValue()) + " 出界");
    	        			acceTestResult2.setForeground(Color.blue);
    	        			ret = false;
    	        			break;
    	        		} else {
    	        		    acceTestResult2.setText(content);
    	        		}
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
				
				    System.out.println("flashThead receive :" + cmd);
				
				    switch(cmd) {
				    case AUDIO : {
				    	
				    	audioFlash(audioFlashCnt%10);
				    	try {
				    		Thread.sleep(500);				    		
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
				    	flashqueue.removeIf(value -> value==Flash_Type.AUDIO);
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
				    	flashqueue.removeIf(value -> value==Flash_Type.CAMERA);
				    	break;
				    }
				    
				    case CAMERA_END_FAIL : {
				    	cameraFail();
				    	flashqueue.removeIf(value -> value==Flash_Type.CAMERA);
				    	break;
				    }
				    
				    case GYRO : {
				    	gyroFlash(gyroFlashCnt%5);
				    	try {
				    		Thread.sleep(1000);				    		
				    	} catch (InterruptedException e) {
				    		e.printStackTrace();
				    	}
				    	gyroFlashCnt++;
				    	flashqueue.put(Flash_Type.GYRO);
				    	break;
				    }
				    
				    case GYRO_END_OK : {
				    	flashqueue.removeIf(value -> value==Flash_Type.GYRO);
				    	break;
				    }
				    
				    case GYRO_END_FAIL : {
				    	gyroFail();
				    	flashqueue.removeIf(value -> value==Flash_Type.GYRO);
				    	break;
				    }
				    
				    case ACCE : {
				    	acceFlash(acceFlashCnt%5);
				    	try {
				    		Thread.sleep(1000);				    		
				    	} catch (InterruptedException e) {
				    		e.printStackTrace();
				    	}
				    	acceFlashCnt++;
				    	flashqueue.put(Flash_Type.ACCE);
				    	break;
				    }
				    
				    case ACCE_END_OK : {
				    	flashqueue.removeIf(value -> value==Flash_Type.ACCE);
				    	break;
				    }
				    
				    case ACCE_END_FAIL : {
				    	acceFail();
				    	flashqueue.removeIf(value -> value==Flash_Type.ACCE);
				    	break;
				    }
				    
				    case UART1 : {
				    	uart1Flash(uart1FlashCnt%5);
				    	try {
				    		Thread.sleep(900);				    		
				    	} catch (InterruptedException e) {
				    		e.printStackTrace();
				    	}
				    	uart1FlashCnt++;
				    	flashqueue.put(Flash_Type.UART1);
				    	break;
				    }
				    
				    case UART1_END_OK : {
				    	uart1Flash(4);
				    	flashqueue.removeIf(value -> value==Flash_Type.UART1);
				    	break;
				    }
				    
				    case UART1_END_FAIL : {
				    	uart1Fail();
				    	flashqueue.removeIf(value -> value==Flash_Type.UART1);
				    	break;
				    }
				    
				    case UART2 : {
				    	uart2Flash(uart2FlashCnt%5);
				    	try {
				    		Thread.sleep(900);				    		
				    	} catch (InterruptedException e) {
				    		e.printStackTrace();
				    	}
				    	uart2FlashCnt++;
				    	flashqueue.put(Flash_Type.UART2);
				    	break;
				    }
				    
				    case UART2_END_OK : {
				    	uart2Flash(4);
				    	flashqueue.removeIf(value -> value==Flash_Type.UART2);
				    	break;
				    }
				    
				    case UART2_END_FAIL : {
				    	uart2Fail();
				    	flashqueue.removeIf(value -> value==Flash_Type.UART2);
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
    
    
    class loadRecordThread implements Runnable {
		@Override
		public void run () {
			System.out.println("loadRecordThread start ...");
			database = new FactoryRecordSqlite();
			
			database.OpenRecord(dbName);
			
			savedRecord = database.FetchAll();
			passRecord = database.QueryByFilter("RESULT == 1");
			failRecord = database.QueryByFilter("RESULT == 0");
			passNum = passRecord.size();
			failNum = failRecord.size();
			
			passNumberTitle.setText(Integer.toString(passNum));
			failNumberTitle.setText(Integer.toString(failNum));
			
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
    		cbjAudio0.setVisible(false);
    		cbjAudio1.setVisible(false);
    		cbAudioTitle.setText("");
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
    		cbjCamera0.setVisible(false);
    		cbjCamera1.setVisible(false);
    		cbCameraTitle.setText("");
	}
    
    void cameraClear() {
		cameraIndication0.setVisible(false);
		
		cameraIndication1.setVisible(false);
		
		cameraIndication2.setVisible(false);
		
		cbjCamera0.setVisible(false);
		cbjCamera1.setVisible(false);
		cbCameraTitle.setText("");
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
        
    void uart1Flash(int num) {
		switch(num) {
    	case 0:
    		uart1Indication0.setBackground(Color.green);
    		break;
    	case 1:
    		uart1Indication1.setBackground(Color.green);
    		break;
    	case 2:
    		uart1Indication2.setBackground(Color.green);
    		break;
    	case 3:
    		uart1Indication3.setBackground(Color.green);
    		break;
    	case 4:
    		uart1Indication4.setBackground(Color.green);
    		break;
    	
    	}
	}
    
    void uart1Fail() {
    	uart1Indication0.setBackground(Color.red);
    	uart1Indication1.setBackground(Color.red);
    	uart1Indication2.setBackground(Color.red);
    	uart1Indication3.setBackground(Color.red);
    	uart1Indication4.setBackground(Color.red);
    }
    
    void uart2Flash(int num) {
		switch(num) {
    	case 0:
    		uart2Indication0.setBackground(Color.green);
    		break;
    	case 1:
    		uart2Indication1.setBackground(Color.green);
    		break;
    	case 2:
    		uart2Indication2.setBackground(Color.green);
    		break;
    	case 3:
    		uart2Indication3.setBackground(Color.green);
    		break;
    	case 4:
    		uart2Indication4.setBackground(Color.green);
    		break;
    	}
	}
    
    void uart2Fail() {
    	uart2Indication0.setBackground(Color.red);
    	uart2Indication1.setBackground(Color.red);
    	uart2Indication2.setBackground(Color.red);
    	uart2Indication3.setBackground(Color.red);
    	uart2Indication4.setBackground(Color.red);
    }
    
    public void close() {
    	System.out.println("AutoTest close ...");
    	database.Close();
    }

        

}
