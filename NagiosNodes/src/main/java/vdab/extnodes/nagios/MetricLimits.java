package vdab.extnodes.nagios;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MetricLimits {
	public final static int P_WARN = 0;
	public final static int P_CRITICAL = 1;
	public final static int P_MIN = 2;
	public final static int P_MAX = 3;
	
	String c_Label;
	Double[] c_Limits = new Double[4];
	
	public MetricLimits(String label, StringTokenizer st){
		c_Label = label;
		int n = 0;
		while (st.hasMoreTokens() && n < 4){
			String next = st.nextToken();
			try {
				c_Limits[n] = Double.valueOf(next);			
			}
			catch (Exception e){}
			n++;
		}
	}
	public String getLabel(){
		return c_Label;
	}
	public Double getWarningLevel(){
		return c_Limits[P_WARN];
	}
	public Double getCriticalLevel(){
		return c_Limits[P_CRITICAL];
	}
	public Double getMinLevel(){
		return c_Limits[P_MIN];
	}
	public Double getMaxLevel(){
		return c_Limits[P_MAX];
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(c_Label);
		sb.append("{");
		for (int n=0; n < 4; n++){
			if (n > 0)
				sb.append(",");
			if (c_Limits[n] != null)
				sb.append(c_Limits[n]);
		}
		sb.append("}");
		return sb.toString();
	}
}
