import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CenterPanel
extends JPanel
implements AddNodeObserver {
	private Service service;
	private JPanel firstBTS;
	private JPanel secondBTS;
	private JPanel BSCPanels;

	private ArrayList<JPanel> addedBSC;
	private JPanel buttonsContainer;
	private JButton plusLayer;
	private JButton minusLayer;
	public CenterPanel(Service service) {
		this.service=service;
		addedBSC = new ArrayList<>();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(400, 200));

		firstBTS = new JPanel();
		firstBTS.setPreferredSize(new Dimension(200,200));
		firstBTS.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		firstBTS.setLayout(new BoxLayout(firstBTS,BoxLayout.Y_AXIS));
		this.add(firstBTS,BorderLayout.WEST);

		secondBTS = new JPanel();
		secondBTS.setPreferredSize(new Dimension(200,200));
		secondBTS.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		secondBTS.setLayout(new BoxLayout(secondBTS,BoxLayout.Y_AXIS));
		this.add(secondBTS,BorderLayout.EAST);

		BSCPanels = new JPanel();
		BSCPanels.setLayout(new GridLayout(1,100));
		BSCPanels.setPreferredSize(new Dimension(400,200));
		BSCPanels.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel m = new JPanel();
		m.setPreferredSize(new Dimension(50,50));
		m.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		m.setLayout(new BoxLayout(m, BoxLayout.Y_AXIS));
		NodeInterface ne1 = service.getListOfBSC().get(0).get(0);
		NodePanel nodePanel1 = new NodePanel(ne1);
		m.add(nodePanel1);
		addedBSC.add(m);
		BSCPanels.add(m);
		this.add(BSCPanels,BorderLayout.CENTER);

		NodeInterface ne2 = service.getInputBTS().get(service.getInputBTS().size()-1);
		NodePanel nodePanel = new NodePanel(ne2);
		firstBTS.add(nodePanel);

		NodeInterface ne3 = service.getOutputBTS().get(service.getOutputBTS().size()-1);
		NodePanel nodePanel2 = new NodePanel(ne3);
		secondBTS.add(nodePanel2);

		this.buttonsContainer = new JPanel();
		this.buttonsContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.plusLayer = new JButton("+");
		this.buttonsContainer.add(plusLayer, BorderLayout.LINE_START);
		this.minusLayer = new JButton("-");
		this.buttonsContainer.add(minusLayer, BorderLayout.LINE_END);
		this.add(buttonsContainer, BorderLayout.SOUTH);
		setPlusLayer();
		setMinusLayer();
		service.adoINBTS.add(this);
		service.adoOUTBTS.add(this);
		service.adoBSCS.add(this);
	}
	void setPlusLayer() {
		this.plusLayer.addActionListener(e -> {
			JPanel m = new JPanel();
			m.setPreferredSize(new Dimension(50,50));
			m.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			m.setLayout(new BoxLayout(m, BoxLayout.Y_AXIS));
			BSC bsc = service.addNewBSCLayer();
			NodePanel nodePanel = new NodePanel(bsc);
			m.add(nodePanel);

			addedBSC.add(m);
			BSCPanels.add(m);
			BSCPanels.revalidate();
			BSCPanels.repaint();
		});
	}
	void setMinusLayer() {
		this.minusLayer.addActionListener(e -> {
			if(addedBSC.size() > 1) {
				this.BSCPanels.remove(addedBSC.get(addedBSC.size() - 1));
				addedBSC.remove(addedBSC.size() - 1);
				service.removeBSCLayer();
				BSCPanels.revalidate();
				this.BSCPanels.repaint();
			}else{
				System.out.println("There must be at least one BSC");
			}
		});

	}

	@Override
	public void updateNodePanel1(NodeInterface nodeInterface) {
		NodePanel nodePanel = new NodePanel(nodeInterface);
		firstBTS.add(nodePanel);
		firstBTS.revalidate();
		firstBTS.repaint();
	}

	@Override
	public void updateNodePanel2(NodeInterface nodeInterface) {
		NodePanel nodePanel = new NodePanel(nodeInterface);
		secondBTS.add(nodePanel);
		secondBTS.revalidate();
		secondBTS.repaint();
	}

	@Override
	public void updateNodePanel3(BSC bsc) {
		NodePanel nodePanel = new NodePanel(bsc);
		addedBSC.get(bsc.getNumOfLayer()-1).add(nodePanel);
		addedBSC.get(bsc.getNumOfLayer()-1).revalidate();
		addedBSC.get(bsc.getNumOfLayer()-1).repaint();
	}
}
