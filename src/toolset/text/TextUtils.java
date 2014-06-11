package toolset.text;

public class TextUtils {
    public static String textAfter(String text, String separator) {
        if (text == null)
            return null;
        
        int i = text.indexOf(separator);
        return (i < 0) ? null :
                text.substring(i + separator.length()); 
    }
    
    public static String textBefore(String text, String separator) {
        if (text == null)
            return null;
        
        int i = text.indexOf(separator);
        return (i < 0) ? null :
                text.substring(0, i); 
    }
    
    public static String textBeforeOrAll(String text, String separator) {
        if (text == null)
            return null;
        
        int i = text.indexOf(separator);
        return (i < 0) ? text :
                text.substring(0, i); 
    }
    
    public static String textBetween(String text, String sep1, String sep2) {
        if (text == null)
            return null;

        int i1 = text.indexOf(sep1);
        if (i1 < 0)
            return null;
        
        i1 += sep1.length();
        
        int i2 = text.indexOf(sep2, i1);
        if (i2 < 0)
            return null;
        
        return text.substring(i1, i2); 
    }
    
    public static String textBetweenOrAfter(String text, String sep1, String sep2) {
        if (text == null)
            return null;

        int i1 = text.indexOf(sep1);
        if (i1 < 0)
            return null;
        
        i1 += sep1.length();
        
        int i2 = text.indexOf(sep2, i1);
        return (i2 > 0) ?
            text.substring(i1, i2) : 
            text.substring(i1);
    }
    
    public static String lastTextBetween(String s, String s1, String s2) {
        int i = s.lastIndexOf(s2);
        if (i < 0) 
            return null;
        int j = s.lastIndexOf(s1, i - s2.length());
        if (j < 0)
            return null;
        return s.substring(j + s1.length(), i);
    }
    
    public static String textAfterLast(String s, String s1) {
        int i = s.lastIndexOf(s1);
        if (i < 0) 
            return null;
        return s.substring(i + s1.length());
    }
}
