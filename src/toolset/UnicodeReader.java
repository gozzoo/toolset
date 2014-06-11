package toolset;

import java.io.*;

public class UnicodeReader {
    private static final byte
        FF = (byte)0xFF, FE = (byte)0xFE, EF = (byte)0xEF,
        BB = (byte)0xBB, BF = (byte)0xBF, OO = (byte)0x00;
    
    enum Enc {
        UTF_8   (EF, BB, BF),
        UTF_16BE(FE, FF),
        UTF_16LE(FF, FE),
        UTF_32BE(OO, OO, FE, FF),
        UTF_32LE(FF, FE, OO, OO);
            
        Enc(byte ... marker) {
            this.bom = marker;
            this.name = name().replace('_', '-');
        }

        byte[] bom;
        String name;
        
        boolean checkBOM(byte[] buf) {
            for (int i = 0; i < bom.length; i++)
                if (bom[i] != buf[i])
                    return false;
            return true;
        }

        static Enc findEncoding(byte[] buf) {
            for (Enc e : Enc.values())
                if (e.checkBOM(buf))
                    return e;
            return null;
        }
    }
    
    static final int BOM_SIZE = 4;
    
    /**
     * @param in Input stream.
     * @param defaultEncoding Default encoding to be used if BOM is not found,
     * or <code>null</code> to use system default encoding.
     * @throws IOException If an I/O error occurs.
     */
    static public Reader newReader(InputStream in, String encoding) throws IOException {
        byte[] bom = new byte[BOM_SIZE];
        PushbackInputStream pushbackStream = new PushbackInputStream(in, BOM_SIZE);
        int n = pushbackStream.read(bom, 0, BOM_SIZE);

        int unreadPos = 0;
        Enc e = Enc.findEncoding(bom);
        if (e != null) {
            encoding = e.name;
            unreadPos = e.bom.length;
        }
        pushbackStream.unread(bom, unreadPos, n - unreadPos);

        return (encoding == null) ?
            new InputStreamReader(pushbackStream) :
            new InputStreamReader(pushbackStream, encoding);
    }
}