import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main
extends JFrame {

	public Main() {
		Service service = new Service();
		RightPanel rp = new RightPanel(service);
		CenterPanel cp = new CenterPanel(service);
		LeftPanel lp = new LeftPanel(service);


		this.getContentPane().add(lp,BorderLayout.LINE_START);
		this.getContentPane().add(cp, BorderLayout.CENTER);
		this.getContentPane().add(rp, BorderLayout.LINE_END);

		this.setSize(1200, 800);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					FileOutputStream fileOutputStream = new FileOutputStream("vbd.bin");
					for(VBD element : service.exVBD) {
						fileOutputStream.write(element.getNumberOfSentMessages());
						fileOutputStream.write(PDUEncoder.encodeMess(element.pdu.getMessage()));
						fileOutputStream.write((byte)('\n'));
					}
				}catch (IOException ex){
					ex.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main();
			}
		});
	}
}