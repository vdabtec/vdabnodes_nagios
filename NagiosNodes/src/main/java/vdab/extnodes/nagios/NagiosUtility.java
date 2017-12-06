package vdab.extnodes.nagios;

import java.util.ArrayList;
import java.util.StringTokenizer;


import com.lcrc.af.AnalysisCompoundData;
import com.lcrc.af.AnalysisData;
import com.lcrc.af.util.StringUtility;

public class NagiosUtility {

	public static boolean hasMetricData(String line){
		return(line.indexOf("|") > 0);
	}
	public static AnalysisData getOneMetric(String mStr, ArrayList<MetricLimits> l){
		StringTokenizer st = new StringTokenizer(mStr,"=;");
		if (st.countTokens() < 2)
			return null;
		String label = st.nextToken(); 		// Metric label
		String valStr = st.nextToken(); 
		if (l != null)
			l.add(new MetricLimits(label, st));
		Object val = StringUtility.getNumericValue(valStr);
		String units = StringUtility.getUnits(valStr);
		if (val != null){
			AnalysisData ad = new AnalysisData(label,val);
			ad.setUnits(units);
			return ad;
		}
		return null;
	}
	public static AnalysisData getMetricData(String label, String line, ArrayList<MetricLimits> l){		
		int mPos = line.indexOf("|");
		if (mPos <= 0)
			return null;
		String mStr = line.substring(mPos+1);
		StringTokenizer st = new StringTokenizer(mStr);
		if (st.countTokens() <= 0)
			return null;
		AnalysisCompoundData acd = new AnalysisCompoundData(label);
		while (st.hasMoreTokens()){
			String next = st.nextToken();
			AnalysisData ad = getOneMetric(next, l);
			if (ad != null)
				acd.addAnalysisData(ad);
		}
		if (acd.getChildData().length <= 0)
			return null;
		return acd;
	}
	

}
