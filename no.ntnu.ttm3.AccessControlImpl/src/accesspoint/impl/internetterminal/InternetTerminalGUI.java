package accesspoint.impl.internetterminal;

import java.io.IOException;

public class InternetTerminalGUI {

	private static final String BROWSER_BINARY = 
			"firefox";
	
	protected InternetTerminal accessPoint;

	private Process process;

	public InternetTerminalGUI(InternetTerminal accessPoint) {
		this.accessPoint = accessPoint;
	}
	
	public void reset() {
		try {
			synchronized (process) {
				process.destroy();
				process = null;
			}
		} catch (NullPointerException e) {/* do nothing*/}
	}
	
	public void allow() {
		try {
			if (process != null)
				process.exitValue();
			process = Runtime.getRuntime().exec(BROWSER_BINARY);
		} catch (IllegalThreadStateException e) {
			/* process is still running */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deny() {
		reset();
	}
	
	public void dispose() {
		accessPoint.dispose();
	}

}
