package com.rjstudio.dogi.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by r0man on 2018/4/29.
 */

public class TimeUtilty {

    private Date date;
    private SimpleDateFormat simpleDateFormat;
    private int HOUR;
    private int MINUTER;
    private int SECOND;
    private static TimeUtilty timeUtilty = null;

    public static TimeUtilty getInstance()
    {
        if (timeUtilty == null)
        {
            timeUtilty = new TimeUtilty();
        }
        return timeUtilty;
    }

    private TimeUtilty()
    {
        HOUR = 0;
        MINUTER = 0;
        SECOND = 0;
        date = new Date(System.currentTimeMillis());
        simpleDateFormat = null;
    }

    public int getCurrentHour()
    {
        //TODO
        simpleDateFormat = new SimpleDateFormat("HH");
        HOUR = Integer.valueOf(simpleDateFormat.format(date));
        return HOUR;
    }

    public int getCurrentMinuter()
    {
        //TODO 不能即时获取，原因出在date上面，date并不是时时更新的。
        simpleDateFormat = new SimpleDateFormat("mm");
        MINUTER = Integer.valueOf(simpleDateFormat.format(date));
        return MINUTER;
    }

    public int getCurrentSecond()
    {
        date = new Date(System.currentTimeMillis());
        simpleDateFormat = new SimpleDateFormat("ss");
        SECOND = Integer.valueOf(simpleDateFormat.format(date));
        return SECOND;
    }

    public String getCurrentTime()
    {
        String SSecond = "";
        int ISecond = 0;
        if ((ISecond = getCurrentSecond()) < 10)
        {
            SSecond = "0" + ISecond;
        }
        else
        {
            SSecond = ISecond+"";
        }
        String SMinuter ;
        int IMinuter = 0;
        if ((IMinuter = getCurrentMinuter() )< 10)
        {
            SMinuter = "0"+IMinuter;
        }
        else
        {
            SMinuter = IMinuter +"";
        }
        String SHour = "";
        int IHour = 0;
        if ( (IHour = getCurrentHour()) <10)
        {
            SHour = " "+IHour;
        }
        else
        {
            SHour = ""+IHour;
        }

        return SHour +":"+SMinuter+":"+SSecond;
    }
}
