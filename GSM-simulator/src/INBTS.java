import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class INBTS
extends Thread
implements NodeInterface {
	NodeInterface chooseBSC(List<NodeInterface> list) {
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
	@Override
	public void receiveMessage(PDU pdu) {
		pdu.setTime(LocalTime.now());
		pdu.setSeconds(3);
		this.messagesContainer.add(pdu);
	}

	@Override
	public int getNumberOfMessages() {
		return messagesContainer.size();
	}
	private Service service;
	private List<PDU> messagesContainer;
	private int howManyProcessedMessages=0;
	private volatile boolean isActive;
	static int inbtsID=1;
	public INBTS(Service service) {
		this.messagesContainer= new ArrayList<>();
		this.service = service;
		this.isActive = true;
		start();
	}

	@Override
	public int getNumberOfProcessedMessages() {
		return howManyProcessedMessages;
	}

	void sendMessage(NodeInterface el, PDU pdu){
		el.receiveMessage(pdu);
	}

	public List<PDU> getMessagesContainer() {
		return messagesContainer;
	}

	@Override
	public void run() {
		while (isActive) {
			if (messagesContainer.size() > 5) {
				System.out.println("added new inbts");
				INBTS inbts = new INBTS(service);
				inbtsID++;
				inbts.receiveMessage(messagesContainer.get(messagesContainer.size() - 1));
				this.messagesContainer.remove(messagesContainer.size() - 1);
				service.addInputBTS(inbts);
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
						System.out.println("Sent message to BSC");
						sendMessage(chooseBSC(service.getListOfBSC().get(0)), pdu);
						messagesContainer.remove(pdu);
						howManyProcessedMessages++;
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



