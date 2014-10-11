package toolset.text;

public class Tokenizer {
	private String text;
	private int pos = 0;
	
	public Tokenizer(String text) {
		this.text = text;
	}

	private String _last() {
		String s = text.substring(pos);
		pos = -1;
		return s;
	}
	
	public String last() {
		if (pos == -1) 
			throw new ScannerException("end of text");
		return _last();
	}
	
	public String nextOrLast(String delimiter) {
		if (pos == -1) 
			throw new ScannerException("end of text");
		
		String s = _next(delimiter);
		if (s != null)
			return s;

		return last();
	}
	
	public String nextOrLast(String ... dels) {
		if (pos == -1) 
			throw new ScannerException("end of text");
		
		for (String delimiter : dels) {
			String s = _next(delimiter);
			if (s != null)
				return s;
		}
		
		return last();
	}
	
	public boolean hasNext() {
		return (pos != -1); 
	}
	
	public String next(String delimiter) {
		if (pos == -1) 
			throw new ScannerException("end of text");
		return _next(delimiter);
	}
	
	public String next(String ... dels) {
		if (pos == -1) 
			throw new ScannerException("end of text");
		
		for (String delimiter : dels) {
			String s = _next(delimiter);
			if (s != null)
				return s;
		}
		
		return null;
	}

	private String _next(String delimiter) {
		int i = text.indexOf(delimiter, pos);
		if (i < 0)
			return null;
		
		String s = text.substring(pos, i);
		pos = i + delimiter.length();
		
		return s;
	}
	
	private boolean _skip(String delimiter) {
		int i = text.indexOf(delimiter, pos);
		if (i < 0)
			return false;
		
		pos = i + delimiter.length();
		
		return true;
	}
	
	public boolean skip(String delimiter) {
		if (pos == -1) 
			throw new ScannerException("end of text");
		return _skip(delimiter);
	}
	
	public boolean skip(String ... dels) {
		if (pos == -1) 
			throw new ScannerException("end of text");
		
		for (String delimiter : dels) {
			if (_skip(delimiter))
				return true;
		}
		
		return false;
	}
	
	public String nextAfter(String delim1, String delim2) {
		if (skip(delim1))
			return _next(delim2);
		return null;
	}
}

@SuppressWarnings("serial")
class ScannerException extends java.lang.RuntimeException {
	ScannerException(String msg) {
		super(msg);		
	}
}
