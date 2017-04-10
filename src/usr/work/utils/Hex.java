package usr.work.utils;

public class Hex {
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String printHexStringFormat(byte[] b) {
		String a = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			a = a + hex;
		}
		StringBuffer c = new StringBuffer(a);
		int pos;
		for(pos=4;pos<c.length();pos+=5){
			if(pos%40==39){
				c.insert(pos, "\n");
			}else{
				c.insert(pos, " ");
			}
			
		}
		return c.toString();
	}
	
	public static String printHexString(byte[] b) {
		String a = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			a = a + hex;
		}
		StringBuffer c = new StringBuffer(a);
		return c.toString();
	}
	
	public static int parseHex4(byte high,byte low){
		int b0 = high&0xff;
		int b1 = low&0xff;
		return b0*256+b1;
	}
	
	public static int parseHex4(byte[] bytes,int pos){
		int b0 = bytes[pos]&0xff;
		int b1 = bytes[pos+1]&0xff;
		return b0*256+b1;
	}

}
