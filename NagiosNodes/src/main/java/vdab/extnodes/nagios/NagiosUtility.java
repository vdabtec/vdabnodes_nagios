/*LICENSE*
 * Copyright (C) 2013 - 2018 MJA Technology LLC 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
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
