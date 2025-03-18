import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class VRDPanel
extends JPanel {
	
	private JButton closingControler;
	private JLabel numberOfMessagesVisualization;
	private JCheckBox numberOfMessagesCleaner;
	private final Border border;
	private Service service;
	private VRD vrd;
	public VRDPanel(Service service) {
		this.service = service;
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(100,100));
		this.border = BorderFactory.createLineBorder(Color.BLACK);
		closingControler= new JButton("X");
		this.add(closingControler, BorderLayout.NORTH);
		closingControler.addActionListener(e -> {
			closeVRD();
		});

		numberOfMessagesCleaner = new JCheckBox();
		numberOfMessagesCleaner.addActionListener(e -> {
			if(numberOfMessagesCleaner.isSelected()){
				vrd.toSleep();
			}else{
				vrd.wake();
			}
		});
		this.add(numberOfMessagesCleaner, BorderLayout.SOUTH);
		update();
	}
	private DeleteOperator del;
	void setVrd(VRD vrd){
		this.vrd=vrd;
		numberOfMessagesVisualization = new JLabel();
		numberOfMessagesVisualization.setText("Number of messages"+vrd.getHowManyReceivedMessages());
		this.add(numberOfMessagesVisualization,BorderLayout.CENTER);
	}

	public void addListener(DeleteOperator listener){
		this.del=listener;
	}
	public void closeVRD() {
		InterPanelEvent evt = new InterPanelEvent( this, this);
		vrd.interrupt();
		service.getListOfVRD().remove(vrd);
		this.del.delete(evt);
	}

	public void update() {
		SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
			@Override
			protected Void doInBackground() throws Exception {
				while (!isCancelled()) {
					int numberOfMessages = vrd.getHowManyReceivedMessages();
					publish(numberOfMessages);
					Thread.sleep(100); // Aktualizuj co 100 milisekund
				}
				return null;
			}
			@Override
			protected void process(java.util.List<Integer> chunks) {
				int latestNumberOfMessages = chunks.get(chunks.size() - 1);
				numberOfMessagesVisualization.setText("Number of messages: " + latestNumberOfMessages);
			}
		};

		worker.execute();
	}
	
}
