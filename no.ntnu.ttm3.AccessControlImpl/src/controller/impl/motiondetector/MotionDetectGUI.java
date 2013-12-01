package controller.impl.motiondetector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MotionDetectGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;

		
	protected final MotionDetectController controller;
	
	protected final JTextField display;
	
	protected final JButton button;

	private boolean failed;
	
	/**
	 * @throws HeadlessException
	 */
	public MotionDetectGUI(MotionDetectController controller) throws HeadlessException {
		super("Motion detector");
		this.controller = controller;
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationByPlatform(true);
		
		display = new JTextField("Inactive");
		display.setBackground(Color.BLACK);
		display.setForeground(Color.ORANGE);
		display.setFont(display.getFont().deriveFont(display.getFont().getSize2D()*2F));
		display.setHorizontalAlignment(SwingConstants.CENTER);
		
		button = new JButton("Detect");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submit();
			}
		});
		
		getRootPane().setDefaultButton(button);
		
		reset();
		add(display, BorderLayout.NORTH);
		add(button, BorderLayout.CENTER);

		pack();
	}

	public void activate() {
		if (failed)
			throw new IllegalStateException("GUI is failed.");
		display.setText("Ready");
		button.setEnabled(true);
	}
	
	public void reset() {
		if (failed)
			throw new IllegalStateException("GUI is failed.");
		display.setText("Inactive");
		button.setEnabled(false);
	}
	
	protected void submit() {
		try {
			controller.createAuthorizationRequest();
			activate();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private void fail() {
		failed = true;
	}

	public void dispose() {
		super.dispose();
	}
	
	public void displayResult(boolean success) {
		String text;
		if (success) {
			text = "Success!";
		}
		else {
			text = "Failed";
		}
		display.setText(text);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		display.setText("Ready");
	}

}
