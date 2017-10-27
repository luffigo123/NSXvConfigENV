package com.vmware.Utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import com.vmware.vshield.vsmclient.VSMManager;
import com.vmware.vshield.vsmclient.exceptions.VSMAccessForbiddenException;
import com.vmware.vshield.vsmclient.exceptions.VSMNotReachableException;
import com.vmware.vshield.vsmclient.exceptions.VSMOperationFailedException;
import com.vmware.vshield.vsmclient.exceptions.VSMRequestTimedOutException;
import com.vmware.vshield.vsmclient.exceptions.VSMUnauthorizedAccessException;
import com.vmware.vshield.vsmclient.restcall.RestFulCallManager;

public class HttpReq {
//	private static HttpReq httpReq = null;
	private VSMManager vsmgr;
	private VSMManager.Info vsmInfo;
	private RestFulCallManager rfcm;
//	private Configuration cfg = Configuration.getInstance();
	
	private String vsmIP;
	private String vsmUserName;
	private String vsmPasswd;
	
	private String PATH_RestCallXML =  "src\\main\\resources";
	
	public HttpReq()
	{
		rfcm = new RestFulCallManager();
//		envUtils = new GetEnvironmentVMIP();
//		vsmIP = envUtils.getVSMIP();
		
		vsmIP = DefaultEnvironment.vsmIP;
		vsmUserName = DefaultEnvironment.vsmUserName;
		vsmPasswd = DefaultEnvironment.vsmPasswd;
			
		try {
			vsmgr = VSMManager.loginToVSM(vsmIP, vsmUserName, vsmPasswd);
		} catch (VSMOperationFailedException | VSMNotReachableException
				| VSMRequestTimedOutException | VSMUnauthorizedAccessException
				| VSMAccessForbiddenException e) {
			e.printStackTrace();
		}
		try {
			vsmInfo = vsmgr.new Info(vsmUserName, vsmPasswd);
		} catch (VSMOperationFailedException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Send the get request to the endPoint and return the response body, used as a environment checking utility
	 * @param endPoint
	 * @return the response body
	 * @throws Exception 
	 */
	public String getRequest (String endPoint) throws Exception {
		String respBody = "";
		respBody = rfcm.sendGetRequest(endPoint, vsmInfo, "", "");
		System.out.println("The response to the GET request is: " + respBody);		
	    return respBody;
	}
	
	
	/**
	 * Send the delete request to the endPoint and return the response body, used as checking utility
	 * @param endPoint
	 * @return response body
	 * @throws Exception 
	 */
	public String delRequest (String endPoint) throws Exception {
		String respBody = "";
		respBody = rfcm.sendDeleteRequest(endPoint, vsmInfo, "", null);
		System.out.println("The response to the DELETE request is: " + respBody);
	    return respBody;
	}
	
	
	/**
	 * Send the post request to the endpoint url with the xml contents or  the specified xml file's (xml file name only, no need to include the full path, with correct value filled) contents
	 * @param endPoint
	 * @param requestBodyFile
	 * @return the response body
	 * @throws Exception 
	 */
	public String postRequest (String endPoint, String requestBodyOrFileName) throws Exception {
		String respBody = "";
		String fullFilePath = PATH_RestCallXML + "\\" + requestBodyOrFileName;
		String postBody;
		if(chekFileOrString(fullFilePath)) {
			postBody = readFileToString(fullFilePath);
		}
		else {
			postBody = requestBodyOrFileName;
		}
		StringWriter sw = new StringWriter();
		respBody = rfcm.postData(new StringReader(postBody), endPoint, vsmInfo, "", sw, "");		    
		return respBody;
	}
	
	
	/**
	 * Send the put request to the endpoint url with the xml contents or specified xml file's (xml file name only, no need to include the full path, with correct value filled) contents
	 * @param endPoint
	 * @param requestBodyFile
	 * @return true if no exceptions
	 * @throws Exception 
	 */
	public void putRequest (String endPoint, String requestBodyOrFileName) throws Exception {
		String fullFilePath = PATH_RestCallXML + "\\" + requestBodyOrFileName;
		String postBody;
		
		if(chekFileOrString(fullFilePath)) {
			postBody = readFileToString(fullFilePath);
		}
		else {
			postBody = requestBodyOrFileName;
		}
		rfcm.putData(new StringReader(postBody), endPoint, vsmInfo, "", null, null);
	}
	
	public void putRequest2 (String endPoint,StringReader sr) throws VSMOperationFailedException, VSMNotReachableException, VSMRequestTimedOutException, VSMUnauthorizedAccessException, VSMAccessForbiddenException{
		Boolean isPassed = false;
		    rfcm.putData(sr, endPoint, vsmInfo, "", null, null);
	}
	
	/**
	 * Read the text file into a string in UTF-8 encoding
	 * @param filePath
	 * @return the file content string
	 * @throws IOException 
	 */
	public String readFileToString (String fullFilePath) throws IOException {
		String fileString = "";
		BufferedReader br = null;
		
			File f = new File(fullFilePath);
			if(f.exists()) {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
				String lineStr;
				while ((lineStr =br.readLine())!=null) {
					fileString += lineStr;
				}
				br.close();
			}
			else {
				System.out.println("File " + fullFilePath + "does not exist, please check it!");
			}

		System.out.println(fileString.trim());
		return fileString.trim();
	}
	
	
	
	/**
	 * Print specified number of tab chars in the same line 
	 * @param tabNumber
	 */
	public void printTabs (int tabNumber) {
		for (int i = 0 ; i < tabNumber; i++) {
			System.out.print("\t");
		}
		
	}
	
	
	public void print4Spaces (int spacesNumber) {
		for (int i = 0; i < spacesNumber; i++) {
			System.out.print("    ");
		}
	}
	private Boolean chekFileOrString (String filePath) {
		Boolean isFile;
		File f = new File(filePath);
		if(f.exists()) {
			isFile = true;
		}
		else 
			isFile = false;
		
		return isFile;
	}
	
}

