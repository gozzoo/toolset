package toolset;

import static toolset.text.TextUtils.*;
import static org.junit.Assert.*;

import org.junit.*;
import toolset.text.Tokenizer;

public class TextTests {
    @Test
    public void text() {
        String s = textBefore("aaa-bbb", "-");
        assertEquals(s, "aaa");

        s = textAfter("aaa bbb", " ");
        assertEquals(s, "bbb");

        s = textBetween("aaa fff ccc", " ", " ");
        assertEquals(s, "fff");
        
        s = textBetweenOrAfter("aaa, fff, ccc", ", ", ", ");
        assertEquals(s, "fff");
        
        s = textBetweenOrAfter("aaa fff", " ", " ");
        assertEquals(s, "fff");
        
        s = lastTextBetween("mmm, kkk, aaa, fff, ccc", ", ", ", ");
        assertEquals(s, "fff");
        
        s = textAfterLast("mmm, kkk, aaa, fff, ccc", ", ");
        assertEquals(s, "ccc");
    }
    
    @Test
    public void tokenizer1() {
        String s = "12 June 2004, 15:00 AM EDT";
        Tokenizer t = new Tokenizer(s);
        
        String day, month, year, hour, min, period, tz;
        
        day    = t.next(" ");
        month  = t.next(" ");
        year   = t.next(", ");
        hour   = t.next(":");
        min    = t.next(" ");
        period = t.next(" ");
        tz     = t.last();

        assertEquals(day,    "12");
        assertEquals(month,  "June");
        assertEquals(year,   "2004");
        assertEquals(hour,   "15");
        assertEquals(min,    "00");
        assertEquals(period, "AM");
        assertEquals(tz,     "EDT");
    }
    
    @Test
    public void tokenizer2() {
        String s = "12 June 2004, 9:00";
        Tokenizer t = new Tokenizer(s);
        
        String day, month, year, hour, min;
        String period = null, tz = null;
        
        day    = t.next(" ");
        month  = t.next(" ");
        year   = t.next(", ");
        hour   = t.next(":");
        min    = t.nextOrLast(" ");
        if (t.hasNext()) {
            period = t.next(" ");
            tz     = t.last();
        }

        assertEquals(day,    "12");
        assertEquals(month,  "June");
        assertEquals(year,   "2004");
        assertEquals(hour,   "9");
        assertEquals(min,    "00");
        assertNull(period);
        assertNull(tz);
    }
}