package toolset.text;

public class Tokenizer {
    private String text;
    private int pos = 0;
    
    public Tokenizer(String text) {
        this.text = text;
    }

    public String last() {
        if (pos == -1) 
            throw new ScannerException("end of text");
        
        String s = text.substring(pos);
        pos = -1;
        
        return s;
    }
    
    public String nextOrLast(String delimiter) {
        if (pos == -1) 
            throw new ScannerException("end of text");
        
        int i = text.indexOf(delimiter, pos);
        String s = (i < 0) ?
            text.substring(pos) :
            text.substring(pos, i);

        pos = -1;
        
        return s;
    }
    
    public boolean hasNext() {
        return (pos != -1); 
    }
    
    public String next(String delim1, String delim2) {
        if (skip(delim1))
            return next(delim2);
        return null;
    }
    
    public String next(String delimiter) {
        if (pos == -1) 
            throw new ScannerException("end of text");
        
        int i = text.indexOf(delimiter, pos);
        if (i < 0)
            return null;
        
        String s = text.substring(pos, i);
        pos = i + delimiter.length();
        
        return s;
    }
    
    public boolean skip(String delimiter) {
        if (pos == -1) 
            throw new ScannerException("end of text");
        
        int i = text.indexOf(delimiter, pos);
        if (i < 0)
            return false;
        
        pos = i + delimiter.length();
        
        return true;
    }
}

@SuppressWarnings("serial")
class ScannerException extends java.lang.RuntimeException {
    ScannerException(String msg) {
        super(msg);        
    }
}