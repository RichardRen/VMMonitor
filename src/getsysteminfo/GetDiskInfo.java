package getsysteminfo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import utils.SystemCommand;

public class GetDiskInfo {

	/**
	 * @author richard
	 */
	
	public static String getDiskInfo(){
		
//		String diskCommand = System.getenv("windir") + "\\system32\\wbem\\wmic.exe logicaldisk get size, freespace";//wmic OS get FreePhysicalMemory
		String diskCommand = SystemCommand.windowsCommand.get(3);
		String result = "";
		try{
			result = read(Runtime.getRuntime().exec(diskCommand));
		}catch(Exception e){
			System.out.println("getMemoryInfo exception");
		}
		return result;
	}
	
	private static String read(final Process proc){
		String result =  null;
		long totalfreeSpace = 0;
		long totalSize = 0;
		try{
			InputStreamReader ir = new InputStreamReader(proc.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line = input.readLine();
			int freeSpaceidx = line.indexOf("FreeSpace");
			int sizeidx = line.indexOf("Size");
			String freeSpaceStr = "";
			String sizeStr = "";
			while((line = input.readLine()) != null){
				line = line.trim();
				if(!line.equals("")){
					freeSpaceStr = line.substring(freeSpaceidx, sizeidx-1).trim();
					sizeStr = line.substring(sizeidx, line.length()).trim();
					long freeSpace = Long.parseLong(freeSpaceStr);
					long size = Long.parseLong(sizeStr);
					totalfreeSpace += freeSpace;
					totalSize += size;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("error");
		}finally{
			try {
				proc.getInputStream().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}					
		
		result = "DiskFreeSpace is "+ totalfreeSpace/1073741824 +"GB, DiskTotalSpace is "+ totalSize/1073741824 +"GB";
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(getDiskInfo());
	}

}
