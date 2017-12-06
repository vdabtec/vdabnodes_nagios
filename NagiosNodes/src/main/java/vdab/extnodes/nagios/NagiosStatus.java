package vdab.extnodes.nagios;

import com.lcrc.af.datatypes.AFEnum;

public class NagiosStatus {
	public static final int OK = 0;
	public static final int WARNING = 1;
	public static final int CRITICAL = 2;
	public static final int UNKNOWN = 3;
	private static AFEnum s_EnumNagiosStatus = new AFEnum("NagiosStatus")
	.addEntry(OK, "Personal")
	.addEntry(WARNING, "Warning")
	.addEntry(CRITICAL, "Critical")
	.addEntry(OK, "UNKNOWN");
	public static AFEnum getEnum(){
		return s_EnumNagiosStatus ;
	}
}
