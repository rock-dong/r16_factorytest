package com.robot.test;

import java.sql.Timestamp;

public class FactoryRecord
{

    /*
     * Thing Property
     */
    public String sn;
   
    public Timestamp time;
    public int  result;
    public int  audio;
    public int  camera;
    public int gyro;
    public int acce;
    public int uart1;
    public int uart2;
    
   
    public final String GetSn(){return sn;}

    // public boolean active = false;

    public final Timestamp GetTime()
    {
        return time;
    }

    public final int GetResult()
    {
        return result;
    }

    public final int GetAudio()
    {
        return audio;
    }
    
    public final int GetCamera()
    {
        return camera;
    }
    
    public final int GetGyro()
    {
        return gyro;
    }

    public final int GetAcce()
    {
        return acce;
    }
    
    public final int GetUart1()
    {
        return uart1;
    }
    
    public final int GetUart2()
    {
        return uart2;
    }
    
    public void SetSn(String str)
    {
        sn = str;
    }

    public void SetTime(Timestamp str)
    {
        time = str;
    }

    public void SetResult(int rst)
    {
        result = rst;
    }

    public void SetAudio(int value)
    {
        audio = value;
    }
    
    public void SetCamera(int value)
    {
        camera = value;
    }
    
    public void SetGyro(int value)
    {
        gyro = value;
    }

    public void SetAcce(int value)
    {
        acce = value;
    }
    
    public void SetUart1(int value)
    {
        uart1 = value;
    }
    
    public void SetUart2(int value)
    {
        uart2 = value;
    }
    
    

}
