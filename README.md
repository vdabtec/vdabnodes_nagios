# VDAB Nagios Monitoring
### Overview
The VDAB Nagios nodes works with any of the hundreds of Nagios plugins to provide special 
monitoring capabilits. In addition to handling the standard metrics, VDAB flows can parse 
through the additional information provided by the plugin. 

| | |
|  --- |  :---: |
| Application Page    | [Nagios Monitoring](https://vdabtec.com/vdab/app-guides/enhanced-nagios-monitoring) |
| Demo Web Link   | [nagios-demo.vdabcloud.com:31154/vdab](http://nagios-demo.vdabcloud.com:31154/vdab) |

### Features
<ul>
<li>VDAB's NagiosService Node can invoke any plugin.
<li>Nagios Plugins are available for nearly any type of application, system or network monitoring.
<li>Additional information returned can be parsed to enhance the metrics.
<li>VDAB adds the ability to archive the metric data for later analysis.
</ul>

### Loading the the Package
The current or standard version can be loaded directly using the VDAB Android Client following the directions
for [Adding Packages](https://vdabtec.com/vdab/docs/VDABGUIDE_AddingPackages.pdf) 
and selecting the <i>NagiosNodes</i> package.
 
A custom version can be built using Gradle following the direction below.

* Clone or Download this project from Github.
* Open a command windows from the <i>NagiosNodes</i> directory.
* Build using Gradle: <pre>      gradle vdabPackage</pre>

This builds a package zip file which contains the components that need to be deployed. These can be deployed by 
manually unzipping these files as detailed in the [Server Updates](https://vdabtec.com/vdab/docs/VDABGUIDE_ServerUpdates.pdf) 
 documentation.

### Known Issues as of 24 Oct  2018

* 


