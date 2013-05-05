package model;

public class VisualMachine {
	
	public String vmName;
	public String vmSystem;
	public enum vmState{Power_ON ,Power_OFF, Suspend};
	
	public int allocated_CPU;
	public long allocated_Mem;
	public long allocated_Disk;
	
	public String allocated_IP;
	
	public CPUInfo cpuInfo;
	public MemInfo memInfo;
	public DiskInfo	diskInfo;
	public NetworkInfo networkInfo;

}
