import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
public class OUTBTS
		extends Thread
		implements NodeInterface {

	private volatile boolean isActive;
	private List<PDU> messagesContainer;
	private Service service;
	private int processedMessages = 0;
	static int outid;
	public OUTBTS(Service service) {
		this.messagesContainer = new ArrayList<>();
		this.isActive = true;
		this.service = service;
		start();
	}

	@Override
	public int getNumberOfProcessedMessages() {
		return processedMessages;
	}

	@Override
	public int getNumberOfMessages() {
		return messagesContainer.size();
	}

	@Override
	public void receiveMessage(PDU pdu) {
		pdu.setTime(LocalTime.now());
		pdu.setSeconds(3);
		this.messagesContainer.add(pdu);
	}


	void sendMessage(VRD vrd, PDU pdu) {
		vrd.reciveMessage(pdu);
	}

	@Override
	public void run() {
		while (isActive) {
			if (messagesContainer.size() > 5) {
				System.out.println("added new outbts");
				OUTBTS outbts = new OUTBTS(service);
				outid++;
				outbts.receiveMessage(messagesContainer.get(messagesContainer.size() - 1));
				this.messagesContainer.remove(messagesContainer.size() - 1);
				service.addOutputBTS(outbts);
				continue;
			}
			synchronized (this) {
				if (messagesContainer.size() != 0) {
					List<PDU> messagesToSend = new ArrayList<>();

					for (int i = 0; i < messagesContainer.size(); i++) {
						PDU pdu = messagesContainer.get(i);
						if (pdu.timePassed()) {
							messagesToSend.add(pdu);
						}
					}

					for (PDU pdu : messagesToSend) {
						System.out.println("Sent message to VRD");
						sendMessage(pdu.getVrd(), pdu);
						messagesContainer.remove(pdu);
						processedMessages++;
					}
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
	}
}