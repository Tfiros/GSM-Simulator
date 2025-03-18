import java.time.LocalDateTime;
public class PDUEncoder {

	public static byte[] encode(String messageToEncode, String numberVBD, String numberVRD) {
		LocalDateTime now = LocalDateTime.now();
		byte[] firstNumber = new byte[(numberVBD.length()/2)+2];
		byte[] secondNumber = new byte[(numberVRD.length()/2)+2];
		byte[] timestamp = new byte[7];
		byte[] encodedMessage = new byte[messageToEncode.length()+2];

		firstNumber[0]=(byte)numberVBD.length();
		firstNumber[1]= (byte)0xA0;
		int index = 2;
		byte bufor =0;
		for (int i = 0 ; i < numberVBD.length() ; i++) {
			char c = numberVBD.charAt(i);
			if ( i%2 ==0){
				bufor = (byte) ((Integer.parseInt(String.valueOf(c),16)));
			}else {
				bufor = (byte) (((Integer.parseInt(String.valueOf(c), 16)) << 4) | bufor);
				firstNumber[index] = bufor;
				index++;
			}
		}
		if(numberVBD.length()%2 != 0) {
			bufor = (byte) ((0xF0) | bufor);
			firstNumber[firstNumber.length-2] = bufor;
		}

		secondNumber[0]=(byte)numberVRD.length();
		secondNumber[1]= (byte)0xA0;
		index = 2;
		bufor =0;
		for (int i = 0 ; i < numberVRD.length() ; i++) {
			char c = numberVRD.charAt(i);
			if ( i%2 ==0){
				bufor = (byte) ((Integer.parseInt(String.valueOf(c),16)));
			}else {
				bufor = (byte) (((Integer.parseInt(String.valueOf(c), 16)) << 4) | bufor);
				secondNumber[index] = bufor;
				index++;
			}
		}
		if(numberVRD.length()%2 != 0) {
			bufor = (byte) ((0xF0) | bufor);
			secondNumber[secondNumber.length-2] = bufor;
		}

		timestamp[0] = (byte)(now.getYear() & 0b1111);
		timestamp[1] = (byte)(now.getMonth().getValue());
		timestamp[2] = (byte)(now.getDayOfMonth());
		timestamp[3] = (byte)(now.getHour());
		timestamp[4] = (byte)(now.getMinute());
		timestamp[5]= (byte)(now.getSecond());
		timestamp[6] = (byte)(1);

		encodedMessage[0] = (byte)4;
		encodedMessage[1] = (byte)messageToEncode.length();
		int cont = 2;
		for (int i = 0; i < messageToEncode.length() ; i++) {
			encodedMessage[cont] = (byte)(messageToEncode.charAt(i));
			cont++;
		}

		int k = 0;
		byte[] result = new byte[encodedMessage.length + timestamp.length + firstNumber.length + secondNumber.length];
		for (int i = 0; i < firstNumber.length ; i++) {
			result[i] = firstNumber[i];
			k=i;
		}
		for (int i = 0; i < secondNumber.length ; i++) {
			result[k] = firstNumber[i];
			k++;
		}
		for (int i = 0 ; i < timestamp.length; i++) {
			result[k] = timestamp[i];
			k++;
		}
		for (int i =0 ; i < encodedMessage.length ; i++) {
			result[k] = encodedMessage[i];
			k++;
		}
		return result;
	}
	public static byte[] encodeMess(String str){
		byte[] encodedMessage = new byte[str.length()+2];
		encodedMessage[0] = (byte)4;
		encodedMessage[1] = (byte)str.length();
		int cont = 2;
		for (int i = 0; i < str.length() ; i++) {
			encodedMessage[cont] = (byte)(str.charAt(i));
			cont++;
		}
		return encodedMessage;
	}
}
