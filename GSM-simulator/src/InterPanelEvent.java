import javax.swing.*;
import java.util.EventObject;

public class InterPanelEvent extends EventObject {
	private JPanel jPanel;
	public InterPanelEvent(Object source, JPanel jp) {
		super(source);
		jPanel=jp;
	}

	public JPanel getjPanel() {
		return jPanel;
	}
}
