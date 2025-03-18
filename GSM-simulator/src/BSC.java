import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BSC
		extends Thread
		implements NodeInterface {

	private volatile List<PDU> messagesContainer;
	private volatile boolean isActive;
	private int processedMessages = 0;

	@Override
	public int getNumberOfProcessedMessages() {
		return processedMessages;
	}

	@Override
	public void receiveMessage(PDU pdu) {
		Random random = new Random();
		int toWait = random.nextInt(5,16);
		pdu.setTime(LocalTime.now());
		pdu.setSeconds(toWait);
		this.messagesContainer.add(pdu);
	}
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

	private Service service;
	private int numOfLayer=1;

	public void setNumOfLayer(int numOfLayer) {
		this.numOfLayer = numOfLayer;
	}

	public BSC(Service service) {
		isActive=true;
		this.messagesContainer = new ArrayList<>();
		this.service = service;
		start();
	}

	public int getNumOfLayer() {
		return numOfLayer;
	}

	@Override
	public int getNumberOfMessages() {
		return this.messagesContainer.size();
	}
	void sendMessage(NodeInterface el, PDU pdu){
		el.receiveMessage(pdu);
	}

	@Override
	public void run() {
		while (isActive) {
			if ( messagesContainer.size() > 5) {
				System.out.println("added new BSC");
				BSC bsc = new BSC(service);
				service.addBSC(numOfLayer-1,bsc);
				sendMessage(bsc, messagesContainer.get(messagesContainer.size()-1));
				messagesContainer.remove(messagesContainer.get(messagesContainer.size()-1));
				continue;
			}
			if ( messagesContainer.size() != 0) {
				synchronized (this) {
					for (int i = 0; i < this.messagesContainer.size(); i++) {
						if (messagesContainer.get(i).timePassed()) {
							if (service.getListOfBSC().size() > numOfLayer ) {
								NodeInterface el = chooseBSC(service.getListOfBSC().get(numOfLayer));
								sendMessage(el, messagesContainer.get(i));
								messagesContainer.remove(messagesContainer.get(i));
								processedMessages++;
								System.out.println("Sent between BSC");
							} else {
								NodeInterface el = chooseBSC(service.getOutputBTS());
								System.out.println("Sent messages to out from "+numOfLayer);
								sendMessage(el, messagesContainer.get(i));
								messagesContainer.remove(messagesContainer.get(i));
								processedMessages++;
							}
						}
					}
				}
			}
			try{
				Thread.sleep(100);
			}catch (InterruptedException e){
				return;
			}
		}
	}
 }
