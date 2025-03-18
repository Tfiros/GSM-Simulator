import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VRD extends Thread
{
	private int howManyRecivedMessages=0;
	private List<PDU> messagesContainter;
	private Service service;
	private volatile boolean isActive;
	private String number = "";
	private boolean toSleep=false;
	void toSleep() {
		toSleep=true;
	}
	void wake() {
		toSleep=false;
	}
	public String getNumber() {
		return number;
	}

	void setRandomNumber() {
		Random random = new Random();
		for( int i = 0 ; i < 9; i++){
			int randomDigit = random.nextInt(0,10);
			this.number+=Integer.toString(randomDigit);
		}
	}
	public VRD(Service service) {
		messagesContainter=new ArrayList<>();
		this.service = service;
		setRandomNumber();
		isActive=true;
		start();
	}
	void mySuspend() {
		isActive=false;
	}

	public int getHowManyReceivedMessages() {
		return howManyRecivedMessages;
	}

	void reciveMessage(PDU pdu) {
		howManyRecivedMessages++;
		messagesContainter.add(pdu);
	}
	@Override
	public void run() {
		while (isActive) {
			System.out.println("VRD outprints "+howManyRecivedMessages);
			if (toSleep) {
				try {
					Thread.sleep(10000);
					//howManyRecivedMessages=0;
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
}
