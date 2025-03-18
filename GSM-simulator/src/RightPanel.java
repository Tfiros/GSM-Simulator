import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class RightPanel
extends JPanel
implements DeleteOperator{
	private Border border;
	private JPanel inPanel;
	private JScrollPane scrollPane;
	private JButton addControler;
	private Service service;
	@Override
	public void delete(InterPanelEvent evt) {
		inPanel.remove(evt.getjPanel());
		inPanel.revalidate();
		scrollPane.repaint();
	}
	
	public RightPanel(Service service) {
		this.service= service;
		this.border = BorderFactory.createLineBorder(Color.BLACK);
		this.setBorder(border);

		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(200, 200));
		
		this.inPanel = new JPanel();
		inPanel.setLayout(new BoxLayout(inPanel, BoxLayout.Y_AXIS));
		inPanel.setPreferredSize(new Dimension(200, 1000));
		this.scrollPane = new JScrollPane(inPanel);
		this.add(this.scrollPane, BorderLayout.CENTER);

		addControler = new JButton("Add");
		this.add(addControler, BorderLayout.PAGE_END);
		addButtonFunction();

	}
	void addButtonFunction() {
		this.addControler.addActionListener(
				e -> {
					VRDPanel vrdPanel = new VRDPanel(service);
					vrdPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, vrdPanel.getPreferredSize().height));
					service.addVRD();
					vrdPanel.setVrd(service.getListOfVRD().get(service.getListOfVRD().size()-1));
					this.inPanel.add(vrdPanel);
					vrdPanel.addListener(this);
					inPanel.revalidate();
					scrollPane.repaint();
				});
	}
}
