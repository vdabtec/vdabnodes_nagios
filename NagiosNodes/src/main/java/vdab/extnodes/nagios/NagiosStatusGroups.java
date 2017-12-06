package vdab.extnodes.nagios;

import com.lcrc.af.datatypes.AFEnum;

public class NagiosStatusGroups {
	public static final int CRITICAL_ONLY = 10;
	public static final int CRITICAL_WARNING= 11;
	public static final int CRITICAL_WARNING_UNKNOWN = 12;
	public static final int ALL = 13;
	private static AFEnum s_EnumNagiosStatusGroups = new AFEnum("NagiosStatusGroups")
	.addEntry(CRITICAL_ONLY, "Critical")
	.addEntry(CRITICAL_WARNING, "Critical+Warning")
	.addEntry(CRITICAL_WARNING_UNKNOWN, "Critical+Warning+Unknown")
	.addEntry(ALL, "ALL");
	public static AFEnum getEnum(){
		return s_EnumNagiosStatusGroups ;
	}
}
