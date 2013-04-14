package controller;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import oshi.SystemInfo;
import oshi.hardware.Processor;
import oshi.software.os.windows.WindowsHardwareAbstractionLayer;
import oshi.software.os.windows.nt.CentralProcessor;
import oshi.software.os.windows.nt.GlobalMemory;
import oshi.software.os.windows.nt.OSNativeSystemInfo;


public class monitor {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// getSystemInfo CPU Mem Disk Network 
		
//		SystemInfo si = new SystemInfo();
//		if(si.getOperatingSystem().toString().equals("Microsoft Windows 7 SP1")){
//		}
//		
//		/* Total number of processors or cores available to the JVM */
//	    System.out.println("Available processors (cores): " + 
//	        Runtime.getRuntime().availableProcessors());
//
//	    /* Get a list of all filesystem roots on this system */
//	    File[] roots = File.listRoots();
//
//	    /* For each filesystem root, print some info */
//	    for (File root : roots) {
//	      System.out.println("File system root: " + root.getAbsolutePath());
//	      System.out.println("Total space (bytes): " + root.getTotalSpace());
//	      System.out.println("Free space (bytes): " + root.getFreeSpace());
//	      System.out.println("Usable space (bytes): " + root.getUsableSpace());
//	    }	
		
		
		
		
		
	}
	
	
	public static String getMemery(){
		
		
		GlobalMemory gm = new GlobalMemory();
		  // 总的物理内存
		  long totalvirtualMemory = gm.getTotal();
		  // 剩余的物理内存
		  long freePhysicalMemorySize = gm.getAvailable();
		  Double compare=(Double)(1-freePhysicalMemorySize*1.0/totalvirtualMemory)*100;
		  String str="内存已使用:"+compare.intValue()+"%";
		  return str;
		 }
	
	 public static List<String> getDisk() {
		  // 操作系统
		  List<String> list=new ArrayList<String>();
		  for (char c = 'A'; c <= 'Z'; c++) {
		   String dirName = c + ":/";
		   File win = new File(dirName);
		         if(win.exists()){
		          long total=(long)win.getTotalSpace();
		          long free=(long)win.getFreeSpace();
		          Double compare=(Double)(1-free*1.0/total)*100;
		          String str=c+":盘  已使用 "+compare.intValue()+"%";
		          list.add(str);
		         }
		     }
		        return list;
		 }
	
	 public static String getCpuRatioForWindows() {
         try {
             String procCmd = System.getenv("windir") + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
             // 取进程信息
             long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
             Thread.sleep(500);
             long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
             if (c0 != null && c1 != null) {
                 long idletime = c1[0] - c0[0];
                 long busytime = c1[1] - c0[1];
                 return "CPU使用率:"+Double.valueOf(100 * (busytime)*1.0 / (busytime + idletime)).intValue()+"%";
             } else {
                 return "CPU使用率:"+0+"%";
             }
         } catch (Exception ex) {
             ex.printStackTrace();
             return "CPU使用率:"+0+"%";
         }
     }
	 
	 
	 private static long[] readCpu(final Process proc) {
	        long[] retn = new long[2];
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
	                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
	                // ThreadCount,UserModeTime,WriteOperation
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
	            retn[0] = idletime;
	            retn[1] = kneltime + usertime;
	            return retn;
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
		   String tgt = "";
		   for (int i = start_idx; i <= end_idx; i++) {
		    tgt += (char) b[i];
		   }
		   return tgt;
		  }
		
	
}
