import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VBDPanel
extends JPanel
implements ChangeListener, ActionListener {
	private JSlider frequencyControler;
	private JTextField deviceNumber;
	private JComboBox stateControler;
	protected static int id=0;
	private JButton closingControler;
	private final Border border;
	private VBD vbd;
	private Service service;

	public VBDPanel(Service service) {
		this.setPreferredSize(new Dimension(100,100));
		this.service=service;
		this.border = BorderFactory.createLineBorder(Color.BLACK);
		this.frequencyControler = new JSlider(1,100,50);
		id++;
		frequencyControler.addChangeListener(this);

		this.deviceNumber = new JTextField(Integer.toString(id));
		this.stateControler = new JComboBox<>(new String[]{"ACTIVE","WAITING"});
		stateControler.addActionListener(this);
		this.deviceNumber.setEditable(false);
		this.closingControler = new JButton("X");
		closingControler.addActionListener(e -> {
			closeVBD();
		});
		this.add(closingControler);
		this.add(new JLabel("Device Number:"));
		this.add(deviceNumber);
		this.add(new JLabel("Frequency Controler"));
		this.add(frequencyControler);
		this.add(new JLabel("State Controler"));
		this.add(stateControler);


	}

	@Override
	public void stateChanged(ChangeEvent e) {
		vbd.setFrequency(frequencyControler.getValue());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ( stateControler.getSelectedItem() == "ACTIVE"){
			vbd.myResume();
		}else if (stateControler.getSelectedItem() == "WAITING"){
			vbd.mySuspend();
		}
	}

	void setVbd(VBD vbd){
		this.vbd=vbd;
	}
	private DeleteOperator del;

	public void addListener(DeleteOperator listener){
		this.del=listener;
	}
	public void closeVBD() {
		InterPanelEvent evt = new InterPanelEvent( this, this);
		id--;
		vbd.interrupt();
		service.getListOfVBD().remove(vbd);
		this.del.delete(evt);
	}


}
