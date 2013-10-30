package controller.impl;

public abstract class AccessController {

	public static final String NUMKEYPAD = "numkeypad";
	public static final String NFC = "nfc";
	public static final String MOTIONDETECT = "motiondetect";
	
	protected String type;
	
	public String getType() {
		return this.type;
	}

}
