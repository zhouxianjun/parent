package com.gary.payment.util.payeco.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.gary.payment.util.payeco.common.Formatter;
import com.gary.payment.util.payeco.common.SslConnection;
import com.gary.payment.util.payeco.common.ToolKit;

public final class TransactionClient {

    public TransactionClient(String url) {
        this.url = url;
    }
    private String payecoCert = "";
    private String merchantNo;
    private String terminalNo;
    private String merchantPWD;
    private String nameSpace;
    private String url;
    private String organizationNo="";
    private int timeout = 300000;
    public int getTimeout() {
        return timeout;
    }
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getNameSapce() {
        return nameSpace;
    }
    public void setNameSapce(String nameSpace) {
        this.nameSpace = nameSpace;
    }
    public String getPayecoCert() {
		return payecoCert;
	}
	public void setPayecoCert(String payecoCert) {
		this.payecoCert = payecoCert;
	}
	public String getMerchantNo() {
        return merchantNo;
    }
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }
    public String getMerchantPWD() {
        return merchantPWD;
    }
    public void setMerchantPassWD(String merchantPWD) {
        this.merchantPWD = merchantPWD;
    }
    public String getTerminalNo() {
        return terminalNo;
    }
    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }
    public String getOrganizationNo() {
		return organizationNo;
	}
	public void setOrganizationNo(String organizationNo) {
		this.organizationNo = organizationNo;
	}
	
	public XmlV2 accountQuery(String accountNo,String accountType,String mobileNo,String amount,
			String currency,String acqSsn,String idCardName,String idCardNo,String idCardType,String bankAddress,
			String idCardAddress,String idCardPhoto,String beneficiaryName,String beneficiaryMobileNo,
			String transData,String deviceData,String deliveryAddress,String ipAddress,String location,
			String userFlag,String bankMobileNo,String smsCode) throws Exception {
		Date time = new Date();
		XmlV2 v2 = new XmlV2();
		v2.setVersion("2.0.0");
		v2.setProcCode("0100");
		v2.setProcessCode("300002");
		v2.setAccountNo(accountNo);
		v2.setAccountType(accountType);
		v2.setMobileNo(mobileNo);
		v2.setAmount(amount);
		v2.setCurrency(currency);
		v2.setTerminalNo(terminalNo);
		v2.setMerchantNo(merchantNo);
		v2.setAcqSsn(acqSsn);
		v2.setReference("");
		v2.setRespCode("");
		v2.setRemark("");
		v2.setTransDatetime(Formatter.yyyyMMddHHmmss(time));
		v2.setIDCardName(idCardName);
		v2.setIDCardNo(idCardNo);
		v2.setIDCardType(idCardType);
		v2.setBankAddress(bankAddress);
		v2.setIDCardAddress(idCardAddress);
		v2.setIDCardPhoto(idCardPhoto);
		v2.setBankName("");
		v2.setBeneficiaryName(beneficiaryName);
		v2.setBeneficiaryMobileNo(beneficiaryMobileNo);
		v2.setTransData(transData);
		v2.setDeviceData(deviceData);
		v2.setDeliveryAddress(deliveryAddress);
		v2.setIpAddress(ipAddress);
		v2.setLocation(location);
		v2.setUserFlag(userFlag);
		v2.setRiskFlags("");
		v2.setBankMobileNo(bankMobileNo);
		v2.setIDCardPhoto(idCardPhoto);
		v2.setSMSCode(smsCode);
		v2.setCalleeNo("");
		v2.setMAC(v2.generateMac(merchantPWD));
		return transact(v2);
	}
	
	public XmlV2 orderQuery(String accountNo,String accountType,String mobileNo,String amount,
			String currency,String acqSsn,String orderNo,String merchantOrderNo) throws Exception {
		Date time = new Date();
		XmlV2 v2 = new XmlV2();
		v2.setVersion("2.0.0");
		v2.setProcCode("0120");
		v2.setProcessCode("310000");
		v2.setAccountNo(accountNo);
		v2.setAccountType(accountType);
		v2.setMobileNo(mobileNo);
		v2.setTransDatetime(Formatter.yyyyMMddHHmmss(time));
		v2.setTerminalNo(terminalNo);
		v2.setMerchantNo(merchantNo);
		v2.setAcqSsn(acqSsn);
		v2.setAmount(amount);
		v2.setCurrency(currency);
		v2.setOrderNo(orderNo);
		v2.setMerchantOrderNo(merchantOrderNo);
		v2.setMAC(v2.generateMac(merchantPWD));
		return transact(v2);
	}
	
	public XmlV2 pay(String processCode,String accountNo,String accountType,String mobileNo,String amount, String currency,
			String synAddress,String asynAddress,String merchantName,String acqSsn,String merchantOrderNo,String orderFrom,
			String language,String description,String orderType,String reference,String idCardName,String idCardNo,String idCardType,
			String bankAddress,String idCardAddress,String idCardPhoto,String beneficiaryName,String beneficiaryMobileNo,
			String deviceData,String deliveryAddress,String ipAddress,String location,String transData,String userFlag) throws Exception {
		Date time = new Date();
		XmlV2 v2 = new XmlV2();
		v2.setVersion("2.0.0");
		v2.setProcCode("0200");
		v2.setProcessCode(ToolKit.isNullOrEmpty(processCode)?"190000":processCode);
		v2.setAccountNo(accountNo);
		v2.setAccountType(accountType);
		v2.setMobileNo(mobileNo);
		v2.setAmount(amount);
		v2.setCurrency(currency);
		v2.setSynAddress(synAddress);
		v2.setAsynAddress(asynAddress);
		v2.setTerminalNo(terminalNo);
		v2.setMerchantNo(merchantNo);
		v2.setMerchantName(merchantName);
		v2.setAcqSsn(acqSsn);
		v2.setMerchantOrderNo(merchantOrderNo);
		v2.setOrderFrom(orderFrom);
		v2.setLanguage(language);
		v2.setDescription(description);
		v2.setOrderType(orderType);
		v2.setReference(reference);
		v2.setTransDatetime(Formatter.yyyyMMddHHmmss(time));
		v2.setIDCardName(idCardName);
		v2.setIDCardNo(idCardNo);
		v2.setBankAddress(bankAddress);
		v2.setIDCardType(idCardType);
		v2.setIDCardAddress(idCardAddress);
		v2.setIDCardPhoto(idCardPhoto);
		v2.setBeneficiaryName(beneficiaryName);
		v2.setBeneficiaryMobileNo(beneficiaryMobileNo);
		v2.setDeviceData(deviceData);
		v2.setDeliveryAddress(deliveryAddress);
		v2.setIpAddress(ipAddress);
		v2.setLocation(location);
		v2.setTransData(transData);
		v2.setUserFlag(userFlag);
		v2.setMAC(v2.generateMac(merchantPWD));
		return transact(v2);
	}
	
	public XmlV2 orderRefund(String processCode,String amount, String currency,String remark,String orderNo,String acqSsn,String merchantOrderNo,String reference) throws Exception {
		Date time = new Date();
		XmlV2 v2 = new XmlV2();
		v2.setVersion("2.0.0");
		v2.setProcCode("0220");
		v2.setProcessCode(ToolKit.isNullOrEmpty(processCode)?"290000":processCode);
		v2.setAmount(amount);
		v2.setCurrency(currency);
		v2.setTransDatetime(Formatter.yyyyMMddHHmmss(time));
		v2.setAcqSsn(acqSsn);
		v2.setReference(reference);
		v2.setRemark(remark);
		v2.setTerminalNo(terminalNo);
		v2.setMerchantNo(merchantNo);
		v2.setOrderNo(orderNo);
		v2.setMerchantOrderNo(merchantOrderNo);
		v2.setMAC(v2.generateMac(merchantPWD));
		return transact(v2);
	}
	
	public XmlV2 adjustApply(String accountNo,String amount, String currency,String remark,String orderNo,String acqSsn,String idCardName,String bankName,String reference) throws Exception {
		Date time = new Date();
		XmlV2 v2 = new XmlV2();
		v2.setVersion("2.0.0");
		v2.setProcCode("0220");
		v2.setProcessCode("290003");
		v2.setAccountNo(accountNo);
		v2.setAmount(amount);
		v2.setCurrency(currency);
		v2.setTransDatetime(Formatter.yyyyMMddHHmmss(time));
		v2.setAcqSsn(acqSsn);
		v2.setReference(reference);
		v2.setRemark(remark);
		v2.setTerminalNo(terminalNo);
		v2.setMerchantNo(merchantNo);
		v2.setOrderNo(orderNo);
		v2.setIDCardName(idCardName);
		v2.setBankName(bankName);
		v2.setMAC(v2.generateMac(merchantPWD));
		return transact(v2);
	}
	
	public XmlV2 adjustQuery(String applyTransDatetime,String applyAcqSsn) throws Exception {
		XmlV2 v2 = new XmlV2();
		v2.setVersion("2.0.0");
		v2.setProcCode("0220");
		v2.setProcessCode("290004");
		v2.setTransDatetime(applyTransDatetime);
		v2.setAcqSsn(applyAcqSsn);
		v2.setTerminalNo(terminalNo);
		v2.setMerchantNo(merchantNo);
		v2.setMAC(v2.generateMac(merchantPWD));
		return transact(v2);
	}
	
	public XmlV2 transact(XmlV2 request) throws Exception {
		ToolKit.writeLog(TransactionClient.class.getName(),"发送报文：" + request);
		String result = transact(request.toBase64String());
		String xmlResult = new String(ToolKit.base64Decode(result),"UTF-8");
		XmlV2 v2Result = new XmlV2(xmlResult);
		//if(!v2Result.verifyMac(merchantPWD)){
		//	throw new Exception("返回结果MAC校验失败");
		//}
		return v2Result;
	}
    
    public String transact(String request) throws Exception {
        try {
            HttpURLConnection connect = null;
            if (!url.contains("https:")) {
                URL urlConnect = new URL(url);
                connect = (HttpURLConnection) urlConnect.openConnection();
            } else {
                SslConnection urlConnect = new SslConnection();
                connect = (HttpURLConnection) urlConnect.openConnection(url);
            }

            connect.setReadTimeout(timeout);
            connect.setConnectTimeout(timeout);
            connect.setRequestMethod("POST");
            connect.setDoInput(true);
            connect.setDoOutput(true);
            connect.setRequestProperty("content-type", "text/html;charset=utf-8");

            connect.connect();
            BufferedOutputStream out = new BufferedOutputStream(connect.getOutputStream());
            String content = ToolKit.padLeft(request.length() + "", 6) + request;
            out.write(content.getBytes("UTF-8"));
            out.flush();
            out.close();

            BufferedInputStream in = new BufferedInputStream(connect.getInputStream());
            byte[] l_b = new byte[6];
            int l_l = 0,l_r = 0;
            while(l_l < 6 && (l_r = in.read(l_b,l_l,6-l_l)) != -1)
            	l_l += l_r;
            if(l_l < 6) {
                ToolKit.writeLog(this.getClass().getName(), "readLen", "invalid length:" + l_l);
                return "";
            }
            int t_l = Integer.parseInt(new String(l_b));
            
            byte[] bts = new byte[t_l];
            int totalLen = 0, len = 0;
            while (totalLen < t_l && (len = in.read(bts, totalLen, t_l-totalLen)) != -1) {
                totalLen += len;
            }
            if(totalLen < t_l) {
                ToolKit.writeLog(this.getClass().getName(), "read", "invalid request,read:" + totalLen + ",total:" + t_l);
                return "";
            }
            String result = ToolKit.toString(new String(bts, "UTF-8"));
            return result;
        } catch (Exception e) {
            ToolKit.writeLog(TransactionClient.class.getName(), "transactCA", e);
            throw e;
        }
    }
}
