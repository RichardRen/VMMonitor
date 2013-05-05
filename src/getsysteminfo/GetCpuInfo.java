package getsysteminfo;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import utils.SystemCommand;


/**
 * 
 * @author richard
 *
 */
public class GetCpuInfo {
	public static String getCpuInfo(){
		try{
//			String cpuCommand = System.getenv("windir") + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
			String cpuCommand = SystemCommand.windowsCommand.get(0);
			long[] c0 = readCpu(Runtime.getRuntime().exec(cpuCommand));
			Thread.sleep(500);
			long[] c1 = readCpu(Runtime.getRuntime().exec(cpuCommand));
			if(c0 != null && c1 != null){
				long idleTime = c1[0] - c0[0];
				long busyTime = c1[1] - c0[1];
				return Double.valueOf(100 * (busyTime)*1.0 / (busyTime + idleTime)).intValue() + "%"; 
			}else{
				return null;
			}
		}catch (Exception e){
			return null;
		}
	}
	
	private static long[] readCpu(final Process proc) {
	
		long[] returnValue = new long[2];
		try {
			proc.getOutputStream().close();
			InputStreamReader ir = new InputStreamReader(proc.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line = input.readLine();
			if (line == null || line.length() < 10) {
				return null;
			}
			int capidx = line.indexOf("Caption");
			int cmdidx = line.indexOf("CommandLine");
			int rocidx = line.indexOf("ReadOperationCount");
			int umtidx = line.indexOf("UserModeTime");
			int kmtidx = line.indexOf("KernelModeTime");
			int wocidx = line.indexOf("WriteOperationCount");
			long idletime = 0;
			long kneltime = 0;
			long usertime = 0;
			while ((line = input.readLine()) != null) {
				if (line.length() < wocidx) {
					continue;
				}
				String caption =substring(line, capidx, cmdidx - 1).trim();
				String cmd = substring(line, cmdidx, kmtidx - 1).trim();
				if (cmd.indexOf("wmic.exe") >= 0) {
					continue;
				}
				String s1 = substring(line, kmtidx, rocidx - 1).trim();
				String s2 = substring(line, umtidx, wocidx - 1).trim();
				if (caption.equals("System Idle Process") || caption.equals("System")) {
					if (s1.length() > 0)
                    idletime += Long.valueOf(s1).longValue();
                if (s2.length() > 0)
                    idletime += Long.valueOf(s2).longValue();
                continue;
                }
				if (s1.length() > 0)
					kneltime += Long.valueOf(s1).longValue();
				if (s2.length() > 0)
					usertime += Long.valueOf(s2).longValue();
            	}
				returnValue[0] = idletime;
				returnValue[1] = kneltime + usertime;
				return returnValue;
				} catch (Exception ex) {
					ex.printStackTrace();
					} finally {
						try {
							proc.getInputStream().close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
		return null;
	} 

	 private static String substring(String src, int start_idx, int end_idx) {

		 byte[] b = src.getBytes();
		 String target = "";
		 for (int i = start_idx; i <= end_idx; i++) {
			 target += (char) b[i];
		 }
		 return target;
	 }
	 
	public static void main(String[] args){
		System.out.println(getCpuInfo());
	}
}
