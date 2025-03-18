import javax.swing.*;
import java.awt.*;

public class NodePanel
extends JPanel {
	
	private JLabel numberOfStation;
	private JLabel numberOfProcessedMessages;
	private JLabel numberOfAwaitingMessages;
	private NodeInterface nodeInterface;
	public NodePanel(NodeInterface nodeInterface) {
		this.nodeInterface=nodeInterface;
		this.setPreferredSize(new Dimension(140, 75));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		numberOfProcessedMessages = new JLabel("Messages:");
		numberOfStation = new JLabel("StationNumber: "+ INBTS.inbtsID);
		numberOfAwaitingMessages = new JLabel("MessagesToSend: " + nodeInterface.getNumberOfMessages());

		this.add(numberOfStation);
		this.add(numberOfProcessedMessages);
		this.add(numberOfAwaitingMessages);
		update();
	}
	public void update() {
		SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
			@Override
			protected Void doInBackground() throws Exception {
				while (!isCancelled()) {
					int processedMessages = nodeInterface.getNumberOfProcessedMessages();
					int numberOfMessages = nodeInterface.getNumberOfMessages();
					publish(processedMessages);
					publish(numberOfMessages);
					Thread.sleep(100);
				}
				return null;
			}
			@Override
			protected void process(java.util.List<Integer> chunks) {
				int latestNumberOfProcessedMessages = chunks.get(chunks.size()-2);
				int latestNumberOfMessages = chunks.get(chunks.size() - 1);
				numberOfAwaitingMessages.setText("MessagesToSend: " + latestNumberOfMessages);
				numberOfProcessedMessages.setText("ProcessedMessages " + latestNumberOfProcessedMessages);
			}
		};
		worker.execute();
	}
}
