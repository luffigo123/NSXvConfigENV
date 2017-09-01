package com.vmware.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import com.vmware.Utils.DateUtils;
import com.vmware.Utils.Log4jInstance;
import com.vmware.Utils.PropertiesUtils;

public class MultiEnvSetup01 {
	private Logger log = Log4jInstance.getLoggerInstance();
	
	private String currentTimeString = null;

	private PropertiesUtils pu = null;
	
	private String startTime;
	private String endTime;
	private DateUtils dateUtils;
	
	public MultiEnvSetup01() {
		this.init();
		dateUtils = new DateUtils();
	}

	public void init(){
		pu = new PropertiesUtils("tempResult.properties");

		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
	    currentTimeString = df.format(new Date()).toString();
		log.info("The time stamp is - " + currentTimeString);
	}
	
	/***
	 * Clear the tempResult.properties file
	 */
	public void clearPropertieValue(){
		pu.writeValueByKey("executionTime", "");
		pu.writeValueByKey("cloneVM1Flag", "false");
		pu.writeValueByKey("cloneVM2Flag", "false");
	}
	
	/**
	 * Setup the NSX-V testing Environment
	 */
	public void envSetup(){
		startTime = dateUtils.getCurrentTime(new Date());
		
		log.info("If the VMs are read, clone the guestOS to the esxi01!");
		CloneUtils cloneUtils = new CloneUtils();
		boolean cloneStatus = cloneUtils.cloneVMToEXSi();
		ConfigureTestENV configureTestENV = new ConfigureTestENV();
		boolean configureStatus = false;
		if(cloneStatus){
			configureStatus = configureTestENV.setupTestEnv();
		}
		
		log.info("Create the configure file to record the testing environment infomation!");
		CreateConfigurationFile createConfigurationFile = new CreateConfigurationFile();
		if(configureStatus){
			log.info("**********  Create AutoConfigFile.cfg *************");
			createConfigurationFile.createConfigFile();
		}
		
		log.info("Write the esecusiob time in the properties file!");
		endTime = dateUtils.getCurrentTime(new Date());
		String executionTime = dateUtils.getDuringTime(startTime, endTime);
		log.info("Deply the environment cost time:" + executionTime);
		pu.writeValueByKey("executionTime", executionTime);
	}
}
