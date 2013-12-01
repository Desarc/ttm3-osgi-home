package controller.impl.numkeypad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

public class NumKeyPadGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	protected final class ButtonPressActionListener implements
			ActionListener {
		private final int number;
		public ButtonPressActionListener(int number) {
			this.number = number;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			display.setText(new String(display.getPassword())+number);
		}

	}

	private static final String TEXT_CROSS =
			new String(
					new byte[]{(byte)0xE2, (byte)0x9C, (byte)0x97}, 
					Charset.forName("UTF-8")
				);
	private static final String TEXT_CHECK =
			new String(
					new byte[]{(byte)0xE2, (byte)0x9C, (byte)0x93}, 
					Charset.forName("UTF-8")
				);
	
	protected final NumKeyPadController controller;
	
	protected final JPasswordField display;
	protected final JPanel buttonPanel;
	
	protected final JButton[] buttons;

	private char passwordChar;
	private boolean failed;
	
	/**
	 * @throws HeadlessException
	 */
	public NumKeyPadGUI(NumKeyPadController controller) throws HeadlessException {
		super("Keypad");
		this.controller = controller;
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationByPlatform(true);
		
		display = new JPasswordField();
		display.setBackground(Color.BLACK);
		display.setForeground(Color.ORANGE);
		display.setFont(display.getFont().deriveFont(display.getFont().getSize2D()*2F));
		display.setHorizontalAlignment(SwingConstants.CENTER);
		passwordChar = display.getEchoChar();
		buttonPanel = new JPanel(new GridLayout(4, 3));
		
		buttons = new JButton[12];
		for(int i=0;i<9;i++) {
			buttons[i] = new JButton((i+1)+"");
			buttons[i].addActionListener(new ButtonPressActionListener(i+1));
			buttonPanel.add(buttons[i]);
		}
		buttons[9] = new JButton(TEXT_CROSS);
		buttonPanel.add(buttons[9]);
		buttons[9].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		
		buttons[10] = new JButton("0");
		buttons[10].addActionListener(new ButtonPressActionListener(10));
		buttonPanel.add(buttons[10]);
		
		buttons[11] = new JButton(TEXT_CHECK);
		buttonPanel.add(buttons[11]);
		buttons[11].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submit();
			}
		});
		
		getRootPane().setDefaultButton(buttons[11]);
		
		reset();
		add(display, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.CENTER);

		pack();
	}

	public void activate() {
		if (failed)
			throw new IllegalStateException("GUI is failed.");
		display.setText("");
		display.setEchoChar(passwordChar);
		display.setEditable(true);
		for(int i=0;i<buttons.length;i++)
			buttons[i].setEnabled(true);
	}
	
	public void reset() {
		if (failed)
			throw new IllegalStateException("GUI is failed.");
		display.setText("inaktiv");
		display.setEchoChar((char) 0);
		display.setEditable(false);
		display.setSelectionStart(display.getCaretPosition());
		for(int i=0;i<buttons.length;i++)
			buttons[i].setEnabled(false);
	}
	
	protected void submit() {
		try {
			controller.createAuthorizationRequest(new String(display.getPassword()));
			activate();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private void fail() {
		display.setText("SYSTEM ERROR");
		display.setEchoChar((char) 0);
		display.setEditable(false);
		display.setSelectionStart(display.getCaretPosition());
		display.setForeground(Color.RED);
		for(int i=0;i<buttons.length;i++)
			buttons[i].setEnabled(false);
		failed = true;
	}

	public void dispose() {
		super.dispose();
		controller.dispose();
	}
	
	public void displayResult(boolean success) {
		String text;
		if (success) {
			text = "Success!";
		}
		else {
			text = "Failed.";
		}
		display.setText(text);
		display.setEchoChar((char) 0);
		display.setEditable(false);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		display.setText("");
		display.setEchoChar(passwordChar);
		display.setEditable(true);
	}

}
