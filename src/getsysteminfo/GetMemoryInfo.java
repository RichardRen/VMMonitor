package getsysteminfo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import utils.SystemCommand;


/**
 * 
 * @author richard
 *
 */
public class GetMemoryInfo {

	public static String getMemoryInfo(){
		  
//		String freeMemoryCommand = System.getenv("windir") + "\\system32\\wbem\\wmic.exe OS get FreePhysicalMemory";//wmic OS get FreePhysicalMemory
//		String totalMemoryCommand = System.getenv("windir") + "\\system32\\wbem\\wmic.exe ComputerSystem get TotalPhysicalMemory";
		String freeMemoryCommand = SystemCommand.windowsCommand.get(1);
		String totalMemoryCommand = SystemCommand.windowsCommand.get(2);
		long freeMemory = 0;
		long totalMemory = 0;
		try{
			freeMemory = read(Runtime.getRuntime().exec(freeMemoryCommand));
			totalMemory = read(Runtime.getRuntime().exec(totalMemoryCommand));
		}catch (Exception e){
			return null;
		}
		String result = "TotalMemory is "+ totalMemory/1048576 +"MB, FreeMemory is "+ freeMemory/1024 +"MB";
		return result;
	}
	
	private static long read(Process exec) {
		String result = "";
		try {
			InputStreamReader ir = new InputStreamReader(exec.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line = input.readLine();
			while((line = input.readLine()) != null){
				if(!line.equals("")){
					result = line.trim();
					}
				}
		} catch (IOException e) {
			System.out.println("read exception");
		}finally{
			try {
				exec.getInputStream().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Long.parseLong(result);
	}

	public static void main(String[] args){
		System.out.println(getMemoryInfo());
	}
}
