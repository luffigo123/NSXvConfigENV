package com.vmware.Utils;

import com.vmware.AutoInfraVC.VMOperation;
import com.vmware.Utils.PropertiesUtils;

public class CloneVM_Thread implements Runnable{
	private VMOperation vmOperation;
	private String sourceVM;
	private String destVMName;
	private String destHostName;
	private String destDatastore;
	
	private PropertiesUtils pu;
	
	public CloneVM_Thread(VMOperation vmOperation, String sourceVM, String destVMName, String destHostName, String destDatastore){
		this.vmOperation = vmOperation;
		this.sourceVM = sourceVM;
		this.destVMName = destVMName;
		this.destHostName = destHostName;
		this.destDatastore = destDatastore;
		
		pu = new PropertiesUtils("tempResult.properties");
	}

	@Override
	public void run() {	
		try {
			 boolean result = vmOperation.CloneVM(sourceVM, destVMName, destHostName, destDatastore);
			 String temp = String.valueOf(result);
			 if(destVMName.contains("vm1")){
				 pu.writeValueByKey("cloneVM1Flag", temp);
			 }else{
				 pu.writeValueByKey("cloneVM2Flag", temp);
			 }
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
