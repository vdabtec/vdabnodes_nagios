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

import java.io.File;
import java.util.ArrayList;


import com.lcrc.af.AnalysisCompoundData;
import com.lcrc.af.AnalysisData;
import com.lcrc.af.AnalysisDataDef;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.constants.ContainerDefaults;
import com.lcrc.af.constants.FilePathType;
import com.lcrc.af.constants.IconCategory;
import com.lcrc.af.datatypes.AFFile;
import com.lcrc.af.file.AFFileUtility;
import com.lcrc.af.file.FileUtility;
import com.lcrc.af.service.CommandService;

public class NagiosService extends CommandService  {
	static {
		NagiosStatus.getEnum();
		NagiosStatusGroups.getEnum();
	}
	private String c_Filename;
	private String c_Directory;
	private String c_Arguments;
	private String c_CommandName;

	private Integer c_PushTypes = NagiosStatusGroups.CRITICAL_WARNING_UNKNOWN;
	private Boolean c_PushMetrics;
	private boolean c_NeedsLimits;
	private ArrayList<MetricLimits> c_NagiosLimits;
	
	private ArrayList<String> c_Reply = new ArrayList<String>();
	

	public Integer get_IconCode(){
		return IconCategory.NODE_NAGIOS;
	}
	// ATTRIBUTE Methods
	public String get_PluginFilename(){
		return c_Filename;
	}
	public void set_PluginFilename(String name){
		 c_Filename = name;
		 c_CommandName = FileUtility.getFilenameNoExt(c_Filename);
		 c_NeedsLimits =  true;
	}
	public String get_CommandName(){
		return c_CommandName;
	}
	public AnalysisDataDef def_PluginFilename(AnalysisDataDef theDataDef){
		AFFile[] files = AFFileUtility.getAllAFFiles(FilePathType.ABSOLUTE, c_Directory);
		ArrayList<String> l = new ArrayList<String>();
		for (AFFile file: files){
			File f = file.getFile();
			if (f.canExecute())
				l.add(file.getFilenameAndExt());
		}
		theDataDef.setAllPickValues(l.toArray(new String[l.size()]));
		return theDataDef;
	}
	public String get_PluginDirectory(){
		return c_Directory;
	}
	public void set_PluginDirectory(String dir){
		 c_Directory = dir;
	}
	public AnalysisDataDef def_PluginDirectory(AnalysisDataDef theDataDef){
		String[] dirs = AFFileUtility.getAllDirectories(FilePathType.ABSOLUTE, c_Directory);
		theDataDef.setAllPickValues(dirs);
		return theDataDef;
	}
	public String get_Arguments(){
		return c_Arguments;
	}
	public void set_Arguments(String cmd){
		c_Arguments = cmd;
	}
	public Integer get_PushTypes(){		
		return c_PushTypes;
	}
	public void set_PushTypes(Integer pushTypes){		
		c_PushTypes = pushTypes ;
	}
	public Boolean get_PushMetrics(){
		return c_PushMetrics;
	}
	public void set_PushMetrics(Boolean push){
		c_PushMetrics = push;
		}
	public String get_Limits(){
		if (c_NagiosLimits == null)
			return null;
		
		StringBuilder sb = new StringBuilder();
		for (MetricLimits limit: c_NagiosLimits)
			sb.append(limit).append(" ");
		
		return sb.toString();
	}
	public File getExeDir(){
		return new File(c_Directory);
	}

	public void _init(){
		super._init();
		if (c_Directory == null){
			if (AFFileUtility.doesAFDirExist(ContainerDefaults.NAGIOS_SUBDIR)){
				c_Directory = AFFileUtility.getDirectoryPath(FilePathType.AF_RELATIVE, ContainerDefaults.NAGIOS_SUBDIR);
			}
		}
		c_NeedsLimits = true;
	}
	public synchronized void processEvent(AnalysisEvent ev){
		c_Reply.clear();
		StringBuilder sb = new StringBuilder();
		sb.append(c_Directory);
		sb.append("/");
		sb.append(c_Filename);	
		if (c_Arguments != null){
			sb.append(" ");
			sb.append(c_Arguments);
		}
		String cmd = sb.toString();
		set_Command(cmd);
		super.processEvent(ev);
	}
	@Override
	public void handleCommandReply(Object[] params, String replyLine) {
		c_Reply.add(replyLine);
	}

	@Override
	public void handleCommandError(Object[] params, String errorLine) {
		c_Reply.add(errorLine);		
	}

	@Override
	public void handleCommandFinished(Object[] params, int status) {
	}
	@Override
	public String getThreadLabel() {
		return "NagiosPlugin";
	}
	// Return something other than 0; Nagios 1-WARNING, 2-CRITICAL, 3-UNKNOWN
	public void serviceFailed(AnalysisEvent ae, int code) {
		pushEvents(ae, code);
	}
	// 0 Returned - Component OK
	public void serviceCompleted(AnalysisEvent ae) {	
		pushEvents(ae, NagiosStatus.OK);
	}
	private String getAllStatusInfo(){
		
		if (c_Reply.size() <= 0)
			return null;
		
		StringBuilder sb = new StringBuilder();
		for (String l: c_Reply)
			sb.append(l);
		return sb.toString();
	}
	private void pushEvents(AnalysisEvent inEvent, int status){
		if (c_PushMetrics != null && c_PushMetrics.booleanValue())
			pushMetricEvent(inEvent, status);
		pushStatusEvent(inEvent, status);
	}
	private void pushMetricEvent(AnalysisEvent inEvent, int status){
		String line = getAllStatusInfo();
		if (line  != null){
			c_NagiosLimits = null;
			if (c_NeedsLimits)
				c_NagiosLimits = new ArrayList<MetricLimits>();
			
			AnalysisData metrics = NagiosUtility.getMetricData("Metrics", line, c_NagiosLimits);
			if (metrics != null){
				publish (new AnalysisEvent(this, metrics));
			}
		}
	}
	private void pushStatusEvent(AnalysisEvent inEvent, int status){	
		
		// IF GROUPS ARE ALL JUST GO AHEAD
		if (c_PushTypes.intValue() != NagiosStatusGroups.ALL){
			switch (status){

			case NagiosStatus.OK:
				return;
	
			case NagiosStatus.WARNING:
				if (	c_PushTypes.intValue() != NagiosStatusGroups.CRITICAL_WARNING_UNKNOWN &&
					 	c_PushTypes.intValue() != NagiosStatusGroups.CRITICAL_WARNING              )
					return;
				break;

			case NagiosStatus.UNKNOWN:
				if (	c_PushTypes.intValue() != NagiosStatusGroups.CRITICAL_WARNING_UNKNOWN )
					return;
				break;
			}		
		}

		AnalysisCompoundData acd = new AnalysisCompoundData(c_CommandName);
		acd.addAnalysisData(new AnalysisData("Status",Integer.valueOf(status)));
		String info = getAllStatusInfo();
		if (info != null)
			acd.addAnalysisData("Info",info);		
		publish (new AnalysisEvent(this, acd));	
	}
	// SHOULD NEVER BE CALLED
	public void serviceResponse(AnalysisEvent origEvent, AnalysisEvent respEvent){
	}
	public void serviceResponse(AnalysisEvent origEvent, AnalysisEvent[] respEvents){
	}
}
