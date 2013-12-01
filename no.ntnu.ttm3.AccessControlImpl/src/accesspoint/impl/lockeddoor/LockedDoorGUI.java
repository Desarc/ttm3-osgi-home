package accesspoint.impl.lockeddoor;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.nio.charset.Charset;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class LockedDoorGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final String TEXT_STOP =
			new String(
					new byte[]{(byte)0xC3, (byte)0x97}, 
					Charset.forName("UTF-8")
				);
	private static final String TEXT_WALK =
			new String(
					new byte[]{(byte)0xE2, (byte)0xAC, (byte)0x86}, 
					Charset.forName("UTF-8")
				);
	
	protected JLabel stopLabel;
	protected JLabel walkLabel;

	protected LockedDoor accessPoint;

	public LockedDoorGUI(LockedDoor accessPoint) throws HeadlessException {
		super("Automatic Door");
		this.accessPoint = accessPoint;
		
		setLayout(new GridLayout(1, 2));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationByPlatform(true);
		setResizable(false);
		
		getContentPane().setBackground(Color.BLACK);
		
		stopLabel = new JLabel(TEXT_STOP);
		stopLabel.setFont(stopLabel.getFont().deriveFont(stopLabel.getFont().getSize2D()*3.5F));
		stopLabel.setForeground(Color.RED);
		stopLabel.setVisible(false);
		stopLabel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		
		walkLabel = new JLabel(TEXT_WALK);
		walkLabel.setFont(walkLabel.getFont().deriveFont(walkLabel.getFont().getSize2D()*3F));
		walkLabel.setForeground(Color.GREEN);
		walkLabel.setVisible(false);
		walkLabel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		
		add(stopLabel);
		add(walkLabel);
		
		pack();
	}
	
	public void reset() {
		stopLabel.setVisible(false);
		walkLabel.setVisible(false);
	}
	
	public void allow() {
		stopLabel.setVisible(false);
		walkLabel.setVisible(true);
	}
	
	public void deny() {
		walkLabel.setVisible(false);
		stopLabel.setVisible(true);
	}
	
	public void dispose() {
		super.dispose();
		accessPoint.dispose();
	}

}
