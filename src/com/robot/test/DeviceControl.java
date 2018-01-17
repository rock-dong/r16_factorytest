package com.robot.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;





public class DeviceControl {

	public DeviceControl() {
		// TODO Auto-generated constructor stub
	}

	public String getAdbDevices() {
    	//String command = "adb shell cameratest 640 480 photo 1 /tmp NV21";
    	String command = "adb devices";
    	System.out.println(command);
    	String deviceName = null;
    	//ArrayList<String> devices = new ArrayList<String>();
        
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
    	
    	return deviceName;
    }
	
	public String getChipId(){
		String command = "adb shell cat /sys/class/sunxi_info/sys_info";
        System.out.println(command);
    	
    	String result = " ß∞‹";
    	
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	    String line = bufferedReader.readLine();
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.contains("sunxi_chipid")){
    	        	int startIndex = line.indexOf(":");
    	        	
    	        	result = line.substring(startIndex + 1, line.length()).trim();
    	        	//break;
    	        } 
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
    	
    	return result;
	}
	
	
	
	
	
	public String audioAdbTest() {
    	String command = "adb shell tinyplayer /media/ring.mp3";
    	
    	
    	
    	System.out.println(command);
    	
    	String result = " ß∞‹";
    	
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
    	        	result = "“Ù¿÷Œƒº˛Œ¥’“µΩ";
    	        	break;
    	        } else if(line.endsWith("found")){
    	        	result = "“Ù¿÷»Ìº˛Œ¥’“µΩ";
    	            break;
    	        }
    	        line = bufferedReader.readLine();
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
    	
    	
    	
    	return result;
    }
	
	
	public int getAudioVolume() {
    	String command = "adb shell amixer get 'speaker volume control'";
    	int volume = 0;
    	
    	
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
    	
    	
    	
    	return volume;
    }
	
	public boolean setAudioVolume(int value) {
    	String command = "adb shell amixer set 'speaker volume control' ";
    	
    	
    	
    	
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
    	
    	
    	
    	return true;
    }
	
	
	public void removeYuvInDevice() {
        String command = "adb shell rm /tmp/source_data1.yuv ";
    	
    	
    	    	
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
    	
    	
    	return;
	}
	
	
	public boolean checkCameraDriver() {
		boolean ret = false;
		String command = "adb shell ls /dev/video0 ";
		
    	
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
        
    	
    	return ret;
	}
	
	public boolean startCameraCapture() {
    	boolean ret = false;
		String command = "adb shell cameratest 640 480 photo 1 /tmp NV21";
    	    	    	    	
    	System.out.println(command);
    	
    	TestEntry.writeLog("startCameraCapture ....");
    	
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        /*
    	        if (line.endsWith("finish!")){
    	        	ret = true;   
    	        	TestEntry.writeLog("cameratest cmd ok");
    	        	break;
    	        } 
    	        */
    	        line = bufferedReader.readLine();
    	    }
    	    ret = true;
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
    		
    		System.out.println("camera capture fail");
    		TestEntry.writeLog("cameratest fail");
    		return ret;
    	}
    	
    	ret = false;
    	//TestEntry.writeLog(" adb result check");
    	
    	command = "adb pull /tmp/source_data1.yuv " + TestEntry.jarPath;
    	System.out.println(command);
    	
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        
    	        if (line.contains("100%")){  	
    	        	ret = true;
    	        	TestEntry.writeLog("adb pull yuv ok");
    	        	//break;
    	        }
    	        
    	        line = bufferedReader.readLine();
    	        //ret = true;
    	    }
    	    
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	    TestEntry.writeLog(e.toString());
    	}
    	
    	
    	
    	return ret;
    }
	
	public boolean checkGyro() {
		boolean ret = false;
		String command = "adb shell cat /sys/class/input/input4/name";
		
    	
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
        
    	
    	return ret;
	}
	
	public boolean controlGyro(boolean onoff) {
		boolean ret = false;
		String command;
		if(onoff)
		    command = "adb shell \"echo 1 > /sys/class/input/input4/enable\"";
		else
			command = "adb shell \"echo 0 > /sys/class/input/input4/enable\"";
		
		
    	
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
        
    	
    	return ret;
	}
	
	public boolean controlAcce(boolean onoff) {
		boolean ret = false;
		String command;
		if(onoff)
		    command = "adb shell \"echo 1 > /sys/class/input/input2/enable\"";
		else
			command = "adb shell \"echo 0 > /sys/class/input/input2/enable\"";
		
		    	
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
            	
    	return ret;
	}
	
	public boolean checkAcce() {
		boolean ret = false;
		String command = "adb shell cat /sys/class/input/input2/name";
		    	
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
            	
    	return ret;
	}
	
	public boolean uart1test() {
		boolean ret = false;
		String command = "adb shell mincomtest 2 115200 2 100";
		
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.contains("test success")){
    	        	System.out.println("uart 1 test pass");
    	        	ret = true;    	        	                    	        	
    	        	break;
    	        } else if (line.contains("test fail")) {
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
    	
        
    	
    	return ret;
	}
	
	public boolean uart2test() {
		boolean ret = false;
		String command = "adb shell mincomtest 2 115200 3 100 ";
		
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  
    	        if (line.contains("test success")){
    	        	System.out.println("uart 2 test pass");
    	        	ret = true;    	        	                    	        	
    	        	break;
    	        } else if (line.contains("test fail")) {
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
    	
        
    	
    	return ret;
	}
	
	
	public boolean ledPassFlash() {
		boolean ret = false;
		String command = "adb shell \"echo 5000 > /sys/class/leds/led1/interval\"";
		TestEntry.writeLog("ledpassflash");
    	
    	System.out.println(command);
    	try {
    	    Process process = Runtime.getRuntime().exec(command);
    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	   
    	    String line = bufferedReader.readLine();
    	   
    	    while(line != null) {
    	        System.out.println(line);  

    	        line = bufferedReader.readLine();
    	        ret = true;
    	    }
    	    System.out.println("adb print end");
    	    process.destroy();
    	        	    
    	    
    	}catch(Exception e) {
    	    e.printStackTrace();
    	}
    	
    	if(ret == false){
    		return ret;
    	}
    	
    	ret = false;
    	
    	File localOkfile = new File(TestEntry.jarPath + "\\ok.local");
    	
    	command = "adb push " + TestEntry.jarPath + "\\ok.local /etc/rc.local";
    	
    	if(localOkfile.exists()) {
    		System.out.println("ok.local is exist");
    		TestEntry.writeLog("ok.local exist");
    		System.out.println(command);
        	try {
        	    Process process = Runtime.getRuntime().exec(command);
        	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        	   
        	    String line = bufferedReader.readLine();
        	   
        	    while(line != null) {
        	        System.out.println(line);
        	        if(line.contains("100%")){
        	        	ret = true;
        	        }
                    TestEntry.writeLog(line);
        	        line = bufferedReader.readLine();
        	    }
        	    System.out.println("adb print end");
        	    process.destroy();
        	        	    
        	    
        	}catch(Exception e) {
        	    e.printStackTrace();
        	}
    		
    	} else {
    		System.out.println("ok.local not exist, can not push");
    		TestEntry.writeLog("no ok.local");
    	}
    	
    	return ret;
	}
	
    public boolean ledFailFlash() {
    	boolean ret = false;
		String command = "adb shell \"echo 200 > /sys/class/leds/led1/interval\"";;
		
		TestEntry.writeLog("ledfailflash");
		
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
    	
        File localOkfile = new File(TestEntry.jarPath + "\\fail.local");
    	
    	command = "adb push " + TestEntry.jarPath + "\\fail.local /etc/rc.local";
    	
    	if(localOkfile.exists()) {
    		System.out.println("fail.local is exist");
    		TestEntry.writeLog("fail.local exist");
    		System.out.println(command);
        	try {
        	    Process process = Runtime.getRuntime().exec(command);
        	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        	   
        	    String line = bufferedReader.readLine();
        	   
        	    while(line != null) {
        	        System.out.println(line);  
                    TestEntry.writeLog(line);
        	        line = bufferedReader.readLine();
        	    }
        	    System.out.println("adb print end");
        	    process.destroy();
        	        	    
        	    
        	}catch(Exception e) {
        	    e.printStackTrace();
        	}
    		
    	} else {
    		TestEntry.writeLog("fail.local not ");
    		System.out.println("fail.local not exist, can not push");
    	}
    	
    	return ret;
	}
	
    
    
    
	
}
