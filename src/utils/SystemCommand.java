package utils;

import java.util.ArrayList;
import java.util.List;

public class SystemCommand {

	/**
	 * @author richard
	 */
	
	public static List<String> linuxCommand = new ArrayList<String>();
	static{
		
	}
	
	
	public static List<String> windowsCommand = new ArrayList<String>();
	static{
		String cpuCommand = System.getenv("windir") + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
		windowsCommand.add(cpuCommand);
		
		String freeMemoryCommand = System.getenv("windir") + "\\system32\\wbem\\wmic.exe OS get FreePhysicalMemory";//wmic OS get FreePhysicalMemory
		String totalMemoryCommand = System.getenv("windir") + "\\system32\\wbem\\wmic.exe ComputerSystem get TotalPhysicalMemory";
		windowsCommand.add(freeMemoryCommand);
		windowsCommand.add(totalMemoryCommand);

		String diskCommand = System.getenv("windir") + "\\system32\\wbem\\wmic.exe logicaldisk get size, freespace";//wmic OS get FreePhysicalMemory
		windowsCommand.add(diskCommand);
		
		String networkCommand = System.getenv("windir") + "\\system32\\netstat.exe -e";
		windowsCommand.add(networkCommand);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
