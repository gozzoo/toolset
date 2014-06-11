package toolset;

public class HexString {
    public static String encode(byte[] buf) {
        StringBuilder sb = new StringBuilder(buf.length * 2);
        for (byte b : buf) {
            int ub = b & 0xff;
            if (ub < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(ub));
        }
        return sb.toString();
    }

    public static byte[] decode(String s) {
        int bufLen = s.length() / 2;
        byte[] buf = new byte[bufLen];

        for (int i = 0; i < bufLen; i++) {
            int index = i * 2;
            String twoDigits = s.substring(index, index + 2);
            int ub = Integer.parseInt(twoDigits, 16);
            buf[i] = (byte)ub;
        }
        return buf;
    }
}
