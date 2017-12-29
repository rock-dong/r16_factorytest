package com.robot.test;


public class FactoryRecord
{

    /*
     * Thing Property
     */
    public String sn;
    public String time;
    public int  result;
    public String details;
    
   
    public final String GetSn(){return sn;}

    // public boolean active = false;

    public final String GetTime()
    {
        return time;
    }

    public final int GetResult()
    {
        return result;
    }

    public final String GetDetails()
    {
        return details;
    }

    
    public void SetSn(String str)
    {
        sn = str;
    }

    public void SetTime(String str)
    {
        time = str;
    }

    public void SetResult(int rst)
    {
        result = rst;
    }

    public void SetDetails(String dtl)
    {
        details = dtl;
     
    }

}
