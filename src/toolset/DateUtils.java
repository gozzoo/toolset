package toolset;

import java.text.*;
import java.util.*;

import static java.util.Calendar.*;

public class DateUtils {
    private static final int 
        DAYS_IN_WEEK            = 7,
        MILLISECONDS_IN_SECOND  = 1000, 
        MILLISECONDS_IN_MINUTE  = 60000,
        MILLISECONDS_IN_HOUR    = 3600000,
        MILLISECONDS_IN_DAY     = 86400000;

    private static DateFormatSymbols dfs = new DateFormatSymbols(new Locale("en"));
     
    public static String[] getMonths() {
        return dfs.getMonths(); 
    }
    
    public static String[] getWeekdays() {
        return dfs.getWeekdays();
    } 
    
    public static int getDayOfMonth() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.DAY_OF_MONTH);
    }
    
    public static boolean isCurrentMonth(Calendar date) {
        Calendar today = Calendar.getInstance();
        return  today.get(Calendar.MONTH) == date.get(Calendar.MONTH) && 
                today.get(Calendar.YEAR) == date.get(Calendar.YEAR);
    }
    
    public static boolean isSameDay(Calendar d1, Calendar d2) {
        return d1.get(Calendar.DAY_OF_MONTH) == d2.get(Calendar.DAY_OF_MONTH) && 
            d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH) && 
            d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR);
    }
    
    public static String getYear(Calendar date) {
        return Integer.toString(date.get(Calendar.YEAR));
    }
    
    public static Date getDateInUserTimeZone(Date date, String timezone) {
        TimeZone tz = TimeZone.getTimeZone(timezone);
        Calendar dateC = Calendar.getInstance(tz, new Locale("en"));
        dateC.setTime(date);
        return dateC.getTime();
    }
    
    public static boolean isToday(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar dbDay = Calendar.getInstance();
        dbDay.setTime(date);
        return today.get(Calendar.YEAR) == dbDay.get(Calendar.YEAR) && 
            today.get(Calendar.MONTH) == dbDay.get(Calendar.MONTH) && 
            today.get(Calendar.DAY_OF_MONTH) == dbDay.get(Calendar.DAY_OF_MONTH);
    }
    
    public static void setTime(Date d, TimeZone tz, int hour, int minute, int sec) {
        Calendar c = Calendar.getInstance(tz);
        c.setTime(d);
        setTime(c, hour, minute, sec);
        d.setTime(c.getTimeInMillis());
    }

    public static void add(Date d, int field, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(field, amount);     
        d.setTime(c.getTimeInMillis()); 
    }

    public static void setTime(Date d, int hour, int minute, int sec) {
        Calendar c = Calendar.getInstance();  
        c.setTime(d);
        setTime(c, hour, minute, sec);
        d.setTime(c.getTimeInMillis());
    }
    
    public static void setTime(Calendar c, int hour, int minute, int sec) {
        c.set(HOUR_OF_DAY, hour);
        c.set(MINUTE, minute); 
        c.set(SECOND, sec);
    } 
    
    public static void setDate(Calendar c, int month, int day, int year) {
        c.set(MONTH, month);
        c.set(DAY_OF_MONTH, day);
        c.set(YEAR, year); 
    } 
        
    public static int diffDatesInSeconds(Calendar c1, Calendar c2) {
        long diff =  c1.getTimeInMillis() - c2.getTimeInMillis(); 
        return (int)diff/MILLISECONDS_IN_SECOND;        
    }
    
    public static int diffDatesInMinutes(Calendar c1, Calendar c2) {
        return diffDatesInMinutes(c1.getTime(), c2.getTime());
    }
    
    public static int diffDatesInMinutes(Date d1, Date d2) {
        return diffDatesInPeriod(d1, d2, MILLISECONDS_IN_MINUTE);
    }
    
    public static int diffDatesInHours(Calendar c1, Calendar c2) {
        return diffDatesInHours(c1.getTime(), c2.getTime());
    }
    
    public static int diffDatesInHours(Date d1, Date d2) {
        return diffDatesInPeriod(d1, d2, MILLISECONDS_IN_HOUR);
    }
    
    public static int diffDatesInDays(Calendar c1, Calendar c2) {
        return diffDatesInDays(c1.getTime(), c2.getTime());
    }
    
    public static int diffDatesInDays(Date d1, Date d2) {
        return diffDatesInPeriod(d1, d2, MILLISECONDS_IN_DAY);        
    }
    
    public static int diffDatesInPeriod(Date d1, Date d2, long period) {
        double diff = d1.getTime() - d2.getTime();
        return (int)Math.round(diff/period);        
    }
    
    public static int diffDatesInWeeks(Calendar c1, Calendar c2) {
        return diffDatesInDays(c1, c2)/DAYS_IN_WEEK; 
    }
    
    public static int diffDatesInMonths(Calendar c1, Calendar c2) {
        int diff = diffDatesInDays(c1, c2)/c1.getActualMaximum(DAY_OF_MONTH);         
        if (diff > 1) {            
            diff = 0; 
            Calendar date = (Calendar)c2.clone();
            if (date.get(YEAR) != c1.get(YEAR)) {            
                while (date.get(YEAR) != c1.get(YEAR)) { 
                    diff++;
                    date.add(MONTH, 1);
                }            
            } 

            diff += diffDatesInDays(c1, date)/date.getActualMaximum(DAY_OF_MONTH);  
        }
        
        return diff;        
    }
    
    public static int diffDatesInYears(Calendar c1, Calendar c2) {
        int multiplier = 1;

        if (c1.before(c2)) {
            multiplier = -1;
            Calendar tc = c1;
            c1 = c2;
            c2 = tc;
        }
 
        int years = c1.get(YEAR) - c2.get(YEAR);
        int months = c1.get(MONTH) - c2.get(MONTH);
        int days = c1.get(DAY_OF_MONTH) - c2.get(DAY_OF_MONTH);
        if (years > 0 && (months < 0 || (months == 0 && days < 0))) {
            years -= 1;
        }
        return years * multiplier;
    }
    
    public static boolean before(Date d1, Date d2) {
        return d1.getTime() < d2.getTime();
    }
    
    public static boolean after(Date d1, Date d2) {
        return d1.getTime() > d2.getTime();
    }
    
    public static void setSunday(Calendar date) {
        date.add(DATE, 1 - date.get(Calendar.DAY_OF_WEEK));
    }
    
    public static boolean isWeekend(Date date) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        int dayOfWeek = dateCal.get(DAY_OF_WEEK);
        return (dayOfWeek == SUNDAY || dayOfWeek == SATURDAY);
    }

    public static int getFirstDayOfMonth(Calendar date) {
        date.set(DAY_OF_MONTH, 1);
        int firstDayOfMonth = date.get(DAY_OF_WEEK);
        return (firstDayOfMonth == 1) ? 7 : firstDayOfMonth -1;
    }
    
    public static boolean sameYearMonth(Calendar c1, Calendar c2) {
        return c1.get(MONTH) == c2.get(MONTH) && 
                c1.get(YEAR) == c2.get(YEAR);
    }
    
    public static String timeLeftTo(Date d) {
        int diff = diffDatesInMinutes(d, new Date());    

        int min = diff % 60;
        diff /= 60;
        int hours = diff % 24;
        int days = diff / 24;
        
        if (days >= 3)
            return days + "d";

        if (days >= 1) {
            String hs = hours > 0 ? " " + hours + "h" : "";        
            return days + "d" + hs;
        }
        
        if (hours < 1) 
            return min + "m";

        if (hours > 3) 
            return hours + "h";
        
        String mins = (min > 10) ? " " + min + "m" : "";
        return hours + "h" + mins;
    }
    
    public static boolean lessThanHourLeft(Date d) {
        int diff = diffDatesInMinutes(d, new Date());    

        diff /= 60;
        int hours = diff % 24;
        int days = diff / 24;
        
        return days == 0 && hours == 0;
    }
}