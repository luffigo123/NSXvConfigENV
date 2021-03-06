package com.vmware.Utils;

import java.net.URLEncoder;

public class VSMManagement {
	private VCUtils vc ;
	private HttpReq httpReq;
	public VSMManagement(){
		vc = VCUtils.getInstance();
		httpReq = HttpReq.getInstance();	
	}
	
	
	public Boolean configVCToNSX(String vcIP, String vcUserName, String vcPWD, String vsmIP) throws Exception {		
		String ep = "https://" + vsmIP + "/api/2.0/services/vcconfig";
		String certificateThumbprint = vc.getVcSslThumbprint();
        String reqBody = XmlFileOp.generateXMLStringCommon("ConnectVC.xml",
													 "ipAddress", vcIP,
													 "userName", vcUserName,
													 "password", vcPWD,
													 "certificateThumbprint", certificateThumbprint);
        httpReq.putRequest(ep, reqBody);	
		return queryVCConfigDetails(vsmIP);
	}
	
	
	/**
	 * 
	 * @param vcIP
	 * @param vcUserName
	 * @param vcPWD
	 * @param vsmIP
	 * @param vcFingerprint
	 * @return
	 * @throws Exception
	 */
	public Boolean registerNSXtoVC(String vcIP, String vcUserName, String vcPWD, String vsmIP, String vcFingerprint){		
		boolean whetherRegistered = false;
		String ep = "https://" + vsmIP + "/api/2.0/services/vcconfig"; 
        String reqBody = XmlFileOp.generateXMLStringCommon("ConnectVC.xml",
													 "ipAddress", vcIP,
													 "userName", vcUserName,
													 "password", vcPWD,
													 "certificateThumbprint", vcFingerprint);
       
        try {
        	 ep = URLEncoder.encode(ep,"UTF-8");
        	 reqBody = URLEncoder.encode(reqBody,"UTF-8");
			httpReq.putRequest(ep, reqBody);
			whetherRegistered = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return whetherRegistered;
	}
	
	
	
	public Boolean queryVCConfigDetails(String vsmIP) throws Exception {
		boolean flag = false;
		String ep = "https://" + vsmIP + "/api/2.0/services/vcconfig";
		String xmlString = httpReq.getRequest(ep);
//	    return XmlFileOp.checkConfigVsGetXmlValue(xmlString, cfg.getVCIP(), cfg.getVCUserName());
		String ipAddress = XmlFileOp.getValueBySpecificTag(xmlString, "ipAddress");
		if(ipAddress.equalsIgnoreCase(vsmIP)){
			flag = true;
		}
	    return flag;
	}

}
