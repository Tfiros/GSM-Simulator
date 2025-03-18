import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class LeftPanel
extends JPanel
implements DeleteOperator{
	@Override
	public void delete(InterPanelEvent evt) {
		inPanel.remove(evt.getjPanel());
		inPanel.revalidate();
		jScrollPane.repaint();
	}

	private Service service;
	private Border border;
	private JScrollPane jScrollPane;
	private JButton jButton;
	private JPanel inPanel;
	public LeftPanel(Service service) {
		this.service = service;
		this.border = BorderFactory.createLineBorder(Color.BLACK);
		this.setBorder(border);
		this.setLayout(new BorderLayout());

		this.inPanel = new JPanel();
		inPanel.setLayout(new BoxLayout(inPanel, BoxLayout.Y_AXIS));
		inPanel.setPreferredSize(new Dimension(200, 1000));
		this.jScrollPane = new JScrollPane(inPanel);
		this.add(this.jScrollPane, BorderLayout.CENTER);

		this.jButton = new JButton("Add");
		this.add(jButton, BorderLayout.PAGE_END);
		addButtonFunction();
	}
	void addButtonFunction() {
		this.jButton.addActionListener(
				e -> {
					String mess = JOptionPane.showInputDialog(this, "Input your message");
					VBDPanel vbdPanel = new VBDPanel(service);
					vbdPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, vbdPanel.getPreferredSize().height));
					service.addVBD(mess);
					vbdPanel.setVbd(service.getListOfVBD().get(service.getListOfVBD().size()-1));
					this.inPanel.add(vbdPanel);
					vbdPanel.addListener(this);
					inPanel.revalidate();
					jScrollPane.repaint();
				});
	}

}
