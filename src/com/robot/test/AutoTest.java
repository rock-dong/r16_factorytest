package com.robot.test;



import javax.swing.JFrame;

public class AutoTest extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FactoryRecordSqlite database;
	private static final String dbName = "test.db";
	
    public AutoTest() {
    	setSize(700, 700);
		setLocation(300, 300);
		setTitle("×Ô¶¯»¯²âÊÔ");
		setLayout(null);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		System.out.println("autotest enter ..");
		
		
		database = new FactoryRecordSqlite();
		
		database.OpenRecord(dbName);
		
    }
    
    
    public void close() {
    	System.out.println("AutoTest close ...");
    	database.Close();
    }

        

}
