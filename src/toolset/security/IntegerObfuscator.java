package toolset.security;

public class IntegerObfuscator {
    private static byte[] key = "secretpass12".getBytes();
    static final int MASK = 0xff;

    static byte[] intToBytes(int i) {
        byte[] a = new byte[4];
        a[0] = (byte)(i & MASK);
        a[1] = (byte)((i >> 8) & MASK);
        a[2] = (byte)((i >> 16) & MASK);
        a[3] = (byte)((i >> 24) & MASK);
        return a;
    }

    static int bytesToInt(byte[] a) {
        int i = MASK & a[3];
        i = (i << 8) | (MASK & (int)a[2]);
        i = (i << 8) | (MASK & (int)a[1]);
        i = (i << 8) | (MASK & (int)a[0]);
        return i;
    }
    
    static byte[] longToBytes(long l) {
        byte[] a = new byte[8];
        for (int i = 7; i > 0; i--) {
            a[i] = (byte)(l & MASK);
            l >>= 8;
        }
        return a;
    }
    
    static long bytesToLong(byte[] a) {
        long l = 0;
        for (int i = 7; i > 0; i--) {
            l = MASK & a[i];
            l <<= 8;
        }
        return l;
    }
    
    public static String encodeInt(int id) {
        byte[] in = intToBytes(id);
        Skip32.encrypt(key, in);
        int eid = bytesToInt(in);
        return Base62.encode(eid);        
    }
    
    public static int decodeInt(String sid) {
        try {
            int i = Base62.decode(sid);
            byte[] in = intToBytes(i);
            Skip32.decrypt(key, in);
            return bytesToInt(in);        
        } catch(NumberFormatException e) {
            return -1;
        }
    }
    
    public static void setPassword(String p) {
        key = p.getBytes();
    }
}

/*     
 * uses skip32 algorithm as implemented here
 * http://search.cpan.org/~esh/Crypt-Skip32-0.15/lib/Crypt/Skip32.pm
 */

class Skip32 {
    private static final int ftable[] = {
         0xa3,0xd7,0x09,0x83,0xf8,0x48,0xf6,0xf4,0xb3,0x21,0x15,0x78,0x99,0xb1,0xaf,0xf9,
         0xe7,0x2d,0x4d,0x8a,0xce,0x4c,0xca,0x2e,0x52,0x95,0xd9,0x1e,0x4e,0x38,0x44,0x28,
         0x0a,0xdf,0x02,0xa0,0x17,0xf1,0x60,0x68,0x12,0xb7,0x7a,0xc3,0xe9,0xfa,0x3d,0x53,
         0x96,0x84,0x6b,0xba,0xf2,0x63,0x9a,0x19,0x7c,0xae,0xe5,0xf5,0xf7,0x16,0x6a,0xa2,
         0x39,0xb6,0x7b,0x0f,0xc1,0x93,0x81,0x1b,0xee,0xb4,0x1a,0xea,0xd0,0x91,0x2f,0xb8,
         0x55,0xb9,0xda,0x85,0x3f,0x41,0xbf,0xe0,0x5a,0x58,0x80,0x5f,0x66,0x0b,0xd8,0x90,
         0x35,0xd5,0xc0,0xa7,0x33,0x06,0x65,0x69,0x45,0x00,0x94,0x56,0x6d,0x98,0x9b,0x76,
         0x97,0xfc,0xb2,0xc2,0xb0,0xfe,0xdb,0x20,0xe1,0xeb,0xd6,0xe4,0xdd,0x47,0x4a,0x1d,
         0x42,0xed,0x9e,0x6e,0x49,0x3c,0xcd,0x43,0x27,0xd2,0x07,0xd4,0xde,0xc7,0x67,0x18,
         0x89,0xcb,0x30,0x1f,0x8d,0xc6,0x8f,0xaa,0xc8,0x74,0xdc,0xc9,0x5d,0x5c,0x31,0xa4,
         0x70,0x88,0x61,0x2c,0x9f,0x0d,0x2b,0x87,0x50,0x82,0x54,0x64,0x26,0x7d,0x03,0x40,
         0x34,0x4b,0x1c,0x73,0xd1,0xc4,0xfd,0x3b,0xcc,0xfb,0x7f,0xab,0xe6,0x3e,0x5b,0xa5,
         0xad,0x04,0x23,0x9c,0x14,0x51,0x22,0xf0,0x29,0x79,0x71,0x7e,0xff,0x8c,0x0e,0xe2,
         0x0c,0xef,0xbc,0x72,0x75,0x6f,0x37,0xa1,0xec,0xd3,0x8e,0x62,0x8b,0x86,0x10,0xe8,
         0x08,0x77,0x11,0xbe,0x92,0x4f,0x24,0xc5,0x32,0x36,0x9d,0xcf,0xf3,0xa6,0xbb,0xac,
         0x5e,0x6c,0xa9,0x13,0x57,0x25,0xb5,0xe3,0xbd,0xa8,0x3a,0x01,0x05,0x59,0x2a,0x46
    };

    static private int g(byte[] key, int k, int w) {
         int g1 = (w >> 8) & 0xff;
         int g2 = w & 0xff;

         g1 = ftable[(int)(g2 ^ key[(4*k)  %10])] ^ g1;
         g2 = ftable[(int)(g1 ^ key[(4*k+1)%10])] ^ g2;
         g1 = ftable[(int)(g2 ^ key[(4*k+2)%10])] ^ g1;
         g2 = ftable[(int)(g1 ^ key[(4*k+3)%10])] ^ g2;

         return ((g1 << 8) + g2);
     }

     static public void encrypt(byte key[], byte buf[]) {
         skip32(key, buf, 1, 0);
     }
     
     static public void decrypt(byte key[], byte buf[]) {
         skip32(key, buf, -1, 23);
     }
     
     static void skip32(byte key[], byte buf[], int kstep, int k) {
         // pack into words 
         int wl = ((buf[0] & 0xff) << 8) + (buf[1] & 0xff);
         int wr = ((buf[2] & 0xff) << 8) + (buf[3] & 0xff);

         // 24 feistel rounds, doubled up
         for (int i = 0; i < 24/2; ++i) {
             wr ^= g(key, k, wl) ^ k;
             k += kstep;
             wl ^= g(key, k, wr) ^ k;
             k += kstep;
         }

         // implicitly swap halves while unpacking
         buf[0] = (byte)(wr >> 8);   
         buf[1] = (byte)(wr & 0xFF);
         buf[2] = (byte)(wl >> 8);   
         buf[3] = (byte)(wl & 0xFF);
     }    
}

class Base62 {
    private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    static String encode(int pi) {
        long i = pi & 0xffffffffl;
        char buf[] = new char[6];
        for (int j = buf.length-1; j >= 0; j--) {
            long m = i % 62;
            i /= 62;
            buf[j] = CA[(int)m];
        }
        return new String(buf);
    }
    
    static int decode(String s) {
        if (s.length() != 6)
            throw new NumberFormatException();

        long j = 0;
        for (int i = 0; i < s.length(); i++) {
            j *= 62;
            char c = s.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                j += c - 'A';
            } else if (c >= 'a' && c <= 'z') {
                j += c - 'a' + 26;
            } else if (c >= '0' && c <= '9') {
                j += c - '0' + 52;
            } else
                throw new NumberFormatException();
        }
        if (j > 0xffffffffl)
            throw new NumberFormatException();
        
        return (int)j;
    }
}