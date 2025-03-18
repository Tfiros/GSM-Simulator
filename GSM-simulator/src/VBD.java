import java.util.List;
import java.util.Random;

public class VBD
		extends Thread
		{
	private Service service;
	private volatile boolean isActive;
	protected PDU pdu;
	Random random = new Random();
	private int numberOfSentMessages=0;
	private volatile int temp=1;
	private String number = "";


	void setRandomNumber() {
		for ( int i = 0 ; i < 9; i++) {
			int randomDigit = random.nextInt(0,10);
			this.number+=Integer.toString(randomDigit);
		}
	}

	public int getNumberOfSentMessages() {
		return numberOfSentMessages;
	}

	void getMessageToPDU(String str) {
		pdu = new PDU(str);
	}
	VRD randomVRD() {
		int to = random.nextInt(0,service.getListOfVRD().size());
		return service.getListOfVRD().get(to);
	}
	NodeInterface chooseBTS(List<NodeInterface> list) {
		NodeInterface selectedNode = list.get(0);
		int minNumberOfMessages = selectedNode.getNumberOfMessages();

		for (int i = 1; i < list.size(); i++) {
			NodeInterface currentNode = list.get(i);
			int numberOfMessages = currentNode.getNumberOfMessages();

			if (numberOfMessages < minNumberOfMessages) {
				minNumberOfMessages = numberOfMessages;
				selectedNode = currentNode;
			}
		}

		return selectedNode;
	}

	void sendMessage(NodeInterface el) {
		el.receiveMessage(pdu);
	}
	void setFrequency(int time) {
		temp=time;
	}
	
	void mySuspend() {
		isActive=false;
	}
	void myResume() {
		isActive=true;
		synchronized (this) {this.notify();}
	}
	void myInterrupt() {this.interrupt();}
	public VBD(Service service) {
		this.service=service;
		getMessageToPDU("k");
		isActive=true;
		setRandomNumber();
		start();
	}
	@Override
	public void run() {
		while (true) {
			if(pdu == null)
				continue;
			pdu.setVrd(randomVRD());
			pdu.setSMS(PDUEncoder.encode(pdu.getMessage(), number, pdu.getVrd().getNumber()));
			if (!isActive) {
				synchronized (this) {
					try {
						System.out.println("VBD suspended");
						this.wait();
					} catch (InterruptedException e) {
						return;
					}
				}
			}
			try {
				Thread.sleep(temp*100);
			} catch (InterruptedException e) {
				return;
			}
			System.out.println("Message sent from: ");
			sendMessage(chooseBTS(service.getInputBTS()));
			numberOfSentMessages++;
		}
	}
}
