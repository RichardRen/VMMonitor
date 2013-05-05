package getsysteminfo;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import utils.SystemCommand;

public class GetNetworkInfo {

	/**
	 * @param args
	 */
	
	public static String getNetworkInfo(){
		//netstat -e
//		String networkCommand = System.getenv("windir") + "\\system32\\netstat.exe -e";
		String networkCommand = SystemCommand.windowsCommand.get(4);
		long[] result1 = new long[2];
		long[] result2 = new long[2];
		try{
			result1 = read(Runtime.getRuntime().exec(networkCommand));
			Thread.sleep(1000);
			result2 = read(Runtime.getRuntime().exec(networkCommand));
		}catch(Exception e){
			System.out.println("getMemoryInfo exception");
		}
		long downloadRate = (result2[0] - result1[0])/1000;
		long uploadRate = (result2[1] - result1[1])/1000;
		String result = "DownloadRate is "+downloadRate+" KB/S, UploadRate is "+uploadRate+" KB/S";
		return result;
	}
	private static long[] read(final Process proc){
		long[] result = new long[2];
		long result0 = 0;
		long result1 = 0;
		try{
			String downloadByte = "";
			String uploadByte = "";
			InputStreamReader ir = new InputStreamReader(proc.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line = "";
			while((line = input.readLine()) != null){
				line = line.trim();
				if(!line.equals("") && line.startsWith("×Ö½Ú")){
					line = line.substring(4).trim();
					downloadByte = line.substring(0, line.length()/2+1).trim();
					uploadByte = line.substring(line.length()/2, line.length()).trim();
				}
			}
			result0 = Long.parseLong(downloadByte);
			result1 = Long.parseLong(uploadByte);
		}catch(Exception e){
			e.printStackTrace();
		}
		result[0] = result0;
		result[1] = result1;
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(getNetworkInfo());
	}

}
