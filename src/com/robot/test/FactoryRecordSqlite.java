package com.robot.test;

import java.sql.Connection;
//import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;



//FIXME: MUST reimplement database interface in more security way
/*String sql= "select * from users where username=? and password=?;
        PreparedStatement preState = conn.prepareStatement(sql);
        preState.setString(1, userName);
        preState.setString(2, password);
        ResultSet rs = preState.executeQuery();
 * */

public class FactoryRecordSqlite
{

    private Connection connection = null;
    private static final String table_name = "factory_record";

    public FactoryRecordSqlite()
    {
    	System.out.println("FactoryRecordSqlite ...");
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    
    public void OpenRecord(String uri)
    {
        String db = "jdbc:sqlite:" + uri;
        
        System.out.println("OpenRecord " + db);
        try
        {
        	
            connection = DriverManager.getConnection(db);
            
            /*
            if(checkTable()) {
            	System.out.println(table_name + "exist, not need to create new one");
            }else {
            	System.out.println(table_name + "NOT exist, create one");
            	createTable();
            }
            */
        	createTable();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
    public void Close()
    {
    	System.out.println("sqlite close");
        try
        {
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    
    public List<FactoryRecord> FetchAll()
    {
        String sql = "select * from " + table_name + ";";
        return Query(sql);
    }

   
    
    private void createTable() {
    	String sql = "CREATE TABLE IF NOT EXISTS " + table_name + 
    			" (BAR TEXT PRIMARY KEY    NOT NULL, " +
    			"SN       TEXT     NOT NULL, " +
    			"TIME     TIMESTAMP     NOT NULL, " +
    			"RESULT   INT      NOT NULL, " +
    			"AUDIO    INT      NOT NULL, " +
    			"CAMERA   INT      NOT NULL, " +
    			"GYRO     INT      NOT NULL, " +
    			"ACCE     INT      NOT NULL, " +
    			"UART1    INT      NOT NULL, " +
    			"UART2    INT      NOT NULL)";
    	
    	System.out.println("createTable ...");
    	try
        {
            Statement statement = connection.createStatement();
            
            statement.executeUpdate(sql);
                                    
            statement.close();
            
            
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    
    
    
    
    public List<FactoryRecord> Query(String tbname, String[] columns, String filter)
    {
        String sql = "Select *";

        int i = 0;
        for (String col : columns)
        {
            sql = sql + col;
            if (i < columns.length - 1)
                sql = sql + ",";
        }

        sql = sql + " from " + tbname + " where " + filter + ";";

        return Query(sql);
    }

    public List<FactoryRecord> QueryByFilter(String filter)
    {
        
        String sql = "Select * from " + table_name + " where " + filter + ";";

        return Query(sql);
    }
    
    public FactoryRecord Read(String bar)
    {
        String sql = "select * from " + table_name + " where BAR = \"" + bar + "\";";
        return QueryOne(sql);
    }

    
    public void Write(FactoryRecord a)
    {
        String sql = "insert into "+table_name+" values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preState;
        //Boolean result = false;
        try
        {
            preState = connection.prepareStatement(sql);
            preState.setString(1, a.GetBar());
            preState.setString(2, a.GetSn());
            preState.setTimestamp(3, a.GetTime());
            preState.setInt(4, a.GetResult());
            preState.setInt(5, a.GetAudio());
            preState.setInt(6, a.GetCamera());
            preState.setInt(7, a.GetGyro());
            preState.setInt(8, a.GetAcce());
            preState.setInt(9, a.GetUart1());
            preState.setInt(10, a.GetUart2());
            
            preState.execute();
            
            
            System.out.println("Write OK");
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
        	System.out.println("Write FAILED");
            e.printStackTrace();
        }
        
        /*
        String sql = "insert into " + table_name + " values( \"" + a.GetThingID() + "\", " + "\"" + a.GetName() + "\", "
                + "\"" + a.GetClassID().toString() + "\", " + "\"\",\""+a.config+"\""+ ") ;";

        Execute(sql);
        */
    }

    
    public void Remove(String bar)
    {
        String sql = "delete from "+table_name+" where BAR=?;";
        PreparedStatement preState;
        Boolean result = false;
        try
        {
            preState = connection.prepareStatement(sql);
            preState.setString(1, bar);
            result = preState.execute();
            if(result)
            {
                System.out.println("DELETE OK");
                return;
            }
                
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("DELETE FAILED");
      //  String sql = "delete from " + table_name + " where thing_id = \"" + thing_id + "\";";
      //  Execute(sql);
    }

    
    public void Update(FactoryRecord a)
    {
        String sql = "update "+table_name+" set BAR=?, SN=?, TIME=?, RESULT=?, AUDIO=?, CAMERA=?, GYRO=?, ACCE=?, UART1=?, UART2=? where BAR=?";
        PreparedStatement preState;
        try
        {
            preState = connection.prepareStatement(sql);
            preState.setString(1, a.GetBar());
            preState.setString(2, a.GetSn());
            preState.setTimestamp(3, a.GetTime());
            preState.setInt(4, a.GetResult());
            preState.setInt(5, a.GetAudio());
            preState.setInt(6, a.GetCamera());
            preState.setInt(7, a.GetGyro());
            preState.setInt(8, a.GetAcce());
            preState.setInt(9, a.GetUart1());
            preState.setInt(10, a.GetUart2());
            preState.setString(11, a.GetBar());
            
            preState.executeUpdate();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*
        String sql = "update " + table_name 
        + " set thing_id = \"" + a.GetThingID() 
        + "\", " + "thing_name = \""+ a.GetName() 
        + "\", " + "thing_classID = \"" + a.getClass().toString() + "\""
        + "\", " + "config = \"" + a.GetConfig() + "\""
        + " where thing_id = \"" + a.GetThingID() + "\";";

        Execute(sql);
        */
    }

    
    public void UpdateBAR(String old_BAR, FactoryRecord a)
    {
        String sql = "update " + table_name + " set BAR = \"" + a.GetBar() + "\"" + " where BAR = \""
                + old_BAR + "\"";
        Execute(sql);
    }

    private List<FactoryRecord> Query(String sql)
    {

        List<FactoryRecord> ret = new ArrayList<FactoryRecord>();

        try
        {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next())
            {
            	String bar = rs.getString("BAR");
                String sn = rs.getString("SN");
                Timestamp time = rs.getTimestamp("TIME");
                int result = rs.getInt("RESULT");
                int audio = rs.getInt("AUDIO");
                int camera = rs.getInt("CAMERA");
                int gyro = rs.getInt("GYRO");
                int acce = rs.getInt("ACCE");
                int uart1 = rs.getInt("UART1");
                int uart2 = rs.getInt("UART2");

                FactoryRecord p = new FactoryRecord();
                p.SetBar(bar);
                p.SetSn(sn);
                p.SetTime(time);
                p.SetResult(result);
                p.SetAudio(audio);
                p.SetCamera(camera);
                p.SetGyro(gyro);
                p.SetAcce(acce);
                p.SetUart1(uart1);
                p.SetUart2(uart2);
                ret.add(p);
            }

            rs.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ret;
    }

    
    
    
    private void Execute(String sql)
    {
        Statement statement;
        try
        {
            statement = connection.createStatement();
            statement.executeUpdate(sql);

            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private FactoryRecord QueryOne(String sql)
    {

        FactoryRecord ret = null;

        try
        {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next())
            {
            	String bar = rs.getString("BAR");
                String sn = rs.getString("SN");
                Timestamp time = rs.getTimestamp("TIME");
                int result = rs.getInt("RESULT");
                int audio = rs.getInt("AUDIO");
                int camera = rs.getInt("CAMERA");
                int gyro = rs.getInt("GYRO");
                int acce = rs.getInt("ACCE");
                int uart1 = rs.getInt("UART1");
                int uart2 = rs.getInt("UART2");
                // String location = rs.getString("location");

                FactoryRecord p = new FactoryRecord();
                p.SetBar(bar);
                p.SetSn(sn);
                p.SetTime(time);
                p.SetResult(result);
                p.SetAudio(audio);
                p.SetCamera(camera);
                p.SetGyro(gyro);
                p.SetAcce(acce);
                p.SetUart1(uart1);
                p.SetUart2(uart2);

                ret = p;
            }

            rs.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ret;
    }

    
    public void RemoveAll()
    {
        String sql = "delete from " + table_name + ";";
        Execute(sql);
    }

}
