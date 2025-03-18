public interface NodeInterface {

	int getNumberOfMessages();
	int getNumberOfProcessedMessages();
	void receiveMessage(PDU SMS);

 }
