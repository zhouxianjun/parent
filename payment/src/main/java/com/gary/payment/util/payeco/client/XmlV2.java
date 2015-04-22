package com.gary.payment.util.payeco.client;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.gary.payment.util.payeco.common.MD5;
import com.gary.payment.util.payeco.common.ToolKit;

public class XmlV2 implements Serializable{
	public XmlV2() {
		
	}
	public XmlV2(String xml) {
		try {
			setDoc(getDocument(xml));
		} catch (Exception e) {
			ToolKit.writeLog(this.getClass().getName(), "xml转成文档报错",e);
		}
		setVersion(getValue("Version", getDoc()));
		setProcCode(getValue("ProcCode", getDoc()));
		setProcessCode(getValue("ProcessCode", getDoc()));
		setAccountNo(getValue("AccountNo", getDoc()));
		setAmount(getValue("Amount", getDoc()));
		setTransDatetime(getValue("TransDatetime", getDoc()));
		setAcqSsn(getValue("AcqSsn", getDoc()));
		setOrderNo(getValue("OrderNo", getDoc()));
		setTransData(getValue("TransData", getDoc()));
		setRespCode(getValue("RespCode", getDoc()));
		setReference(getValue("Reference", getDoc()));
		setTerminalNo(getValue("TerminalNo", getDoc()));
		setMerchantNo(getValue("MerchantNo", getDoc()));
		setMerchantOrderNo(getValue("MerchantOrderNo", getDoc()));
		setOrderState(getValue("OrderState", getDoc()));
		setMAC(getValue("MAC", getDoc()));
		setAccountType(getValue("AccountType", getDoc()));
		setMobileNo(getValue("MobileNo", getDoc()));
		setCurrency(getValue("Currency", getDoc()));
		setSynAddress(getValue("SynAddress", getDoc()));
		setAsynAddress(getValue("AsynAddress", getDoc()));
		setMerchantName(getValue("MerchantName", getDoc()));
		setOrderFrom(getValue("OrderFrom", getDoc()));
		setLanguage(getValue("Language", getDoc()));
		setDescription(getValue("Description", getDoc()));
		setOrderType(getValue("OrderType", getDoc()));
		setSettleDate(getValue("SettleDate", getDoc()));
		setRemark(getValue("Remark", getDoc()));
		setUpsNo(getValue("UpsNo", getDoc()));
		setTsNo(getValue("TsNo", getDoc()));
		setIDCardName(getValue("IDCardName", getDoc()));
		setIDCardNo(getValue("IDCardNo", getDoc()));
		setBankAddress(getValue("BankAddress", getDoc()));
		setIDCardType(getValue("IDCardType", getDoc()));
		setBeneficiaryName(getValue("BeneficiaryName", getDoc()));
		setBeneficiaryMobileNo(getValue("BeneficiaryMobileNo", getDoc()));
		setDeviceData(getValue("DeviceData", getDoc()));
		setDeliveryAddress(getValue("DeliveryAddress", getDoc()));
		setIpAddress(getValue("IpAddress", getDoc()));
		setLocation(getValue("Location", getDoc()));
		setUserFlag(getValue("UserFlag", getDoc()));
		setRiskFlags(getValue("RiskFlags", getDoc()));
		setIDCardAddress(getValue("IDCardAddress", getDoc()));
		setBankMobileNo(getValue("BankMobileNo", getDoc()));
		setIDCardPhoto(getValue("IDCardPhoto", getDoc()));
		setSMSCode(getValue("SMSCode", getDoc()));
		setCalleeNo(getValue("CalleeNo", getDoc()));
		setBankName(getValue("BankName", getDoc()));
		setValidTime(getValue("ValidTime", getDoc()));
		setTransType(getValue("TransType", getDoc()));
		setCommission(getValue("Commission", getDoc()));
		setTransDate(getValue("TransDate", getDoc()));
		setContractNo(getValue("ContractNo", getDoc()));
	}
	@Override
	public String toString() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.newDocument();
	        Element root = doc.createElement("x:NetworkRequest");
	        root.setAttribute("xmlns:x", "http://www.payeco.com");
	        root.setAttribute("xmlns:xsi", "http://www.w3.org");
	        doc.appendChild(root);
	        createElement(doc, root, getVersion(), "Version");
	        createElement(doc, root, getProcCode(), "ProcCode");
	        createElement(doc, root, getProcessCode(), "ProcessCode");
	        createElement(doc, root, getAccountNo(), "AccountNo");
	        createElement(doc, root, getAccountType(), "AccountType");
	        createElement(doc, root, getMobileNo(), "MobileNo");
	        createElement(doc, root, getAmount(), "Amount");
	        createElement(doc, root, getCurrency(), "Currency");
	        createElement(doc, root, getSynAddress(), "SynAddress");
	        createElement(doc, root, getAsynAddress(), "AsynAddress");
	        createElement(doc, root, getTerminalNo(), "TerminalNo");
	        createElement(doc, root, getMerchantNo(), "MerchantNo");
	        createElement(doc, root, getMerchantName(), "MerchantName");
	        createElement(doc, root, getMerchantOrderNo(), "MerchantOrderNo");
	        createElement(doc, root, getOrderNo(), "OrderNo");
	        createElement(doc, root, getOrderFrom(), "OrderFrom");
	        createElement(doc, root, getLanguage(), "Language");
	        createElement(doc, root, getDescription(), "Description");
	        createElement(doc, root, getOrderType(), "OrderType");
	        createElement(doc, root, getOrderState(), "OrderState");
	        createElement(doc, root, getAcqSsn(), "AcqSsn");
	        createElement(doc, root, getReference(), "Reference");
	        createElement(doc, root, getTransDatetime(), "TransDatetime");
	        createElement(doc, root, getSettleDate(), "SettleDate");
	        createElement(doc, root, getRespCode(), "RespCode");
	        createElement(doc, root, getRemark(), "Remark");
	        createElement(doc, root, getUpsNo(), "UpsNo");
	        createElement(doc, root, getTsNo(), "TsNo");
	        createElement(doc, root, getMAC(), "MAC");
	        createElement(doc, root, getIDCardName(), "IDCardName");
	        createElement(doc, root, getIDCardNo(), "IDCardNo");
	        createElement(doc, root, getBankAddress(), "BankAddress");
	        createElement(doc, root, getIDCardType(), "IDCardType");
	        createElement(doc, root, getBeneficiaryName(), "BeneficiaryName");
	        createElement(doc, root, getBeneficiaryMobileNo(), "BeneficiaryMobileNo");
	        createElement(doc, root, getDeviceData(), "DeviceData");
	        createElement(doc, root, getDeliveryAddress(), "DeliveryAddress");
	        createElement(doc, root, getIpAddress(), "IpAddress");
	        createElement(doc, root, getLocation(), "Location");
	        createElement(doc, root, getTransData(), "TransData");
	        createElement(doc, root, getUserFlag(), "UserFlag");
	        createElement(doc, root, getRiskFlags(), "RiskFlags");
	        createElement(doc, root, getIDCardAddress(), "IDCardAddress");
	        createElement(doc, root, getBankMobileNo(), "BankMobileNo");
	        createElement(doc, root, getIDCardPhoto(), "IDCardPhoto");
	        createElement(doc, root, getSMSCode(), "SMSCode");
	        createElement(doc, root, getCalleeNo(), "CalleeNo");
	        createElement(doc, root, getBankName(), "BankName");
	        createElement(doc, root, getValidTime(), "ValidTime");
	        createElement(doc, root, getTransType(), "TransType");
	        createElement(doc, root, getCommission(), "Commission");
	        createElement(doc, root, getTransDate(), "TransDate");
	        createElement(doc, root, getContractNo(), "ContractNo");
	        return getStringFromDocument(doc);
		} catch (Exception e) {
			ToolKit.writeLog(this.getClass().getName(), "toString", e);
		}
        return "";
	}
	
	public String toBase64String() throws Exception {
		return ToolKit.base64Encode(toString().getBytes("UTF-8"));
	}
	
	public XmlV2(String Version,String ProcCode,String ProcessCode,String AccountNo,String Amount,String TransDatetime,String AcqSsn,
			String OrderNo,String TransData,String RespCode,String Reference,String TerminalNo,String MerchantNo,String MerchantOrderNo,String OrderState,
			String AccountType,String MobileNo,String Currency,String SynAddress,String AsynAddress,String MerchantName,String OrderFrom,String Language,
			String Description,String OrderType,String SettleDate,String Remark,String UpsNo,String TsNo,String IDCardName,String IDCardNo,
			String BankAddress,String IDCardType,String BeneficiaryName,String BeneficiaryMobileNo,String DeviceData,String DeliveryAddress,
			String IpAddress,String Location,String UserFlag,String RiskFlags,String IDCardAddress,String BankMobileNo,String IDCardPhoto,String SMSCode,String CalleeNo,
			String BankName,String ValidTime,String TransType,String Commission,String TransDate,String ContractNo,String MAC) {
		setVersion(Version);
		setProcCode(ProcCode);
		setProcessCode(ProcessCode);
		setAccountNo(AccountNo);
		setAmount(Amount);
		setTransDatetime(TransDatetime);
		setAcqSsn(AcqSsn);
		setOrderNo(OrderNo);
		setTransData(TransData);
		setRespCode(RespCode);
		setReference(Reference);
		setTerminalNo(TerminalNo);
		setMerchantNo(MerchantNo);
		setMerchantOrderNo(MerchantOrderNo);
		setOrderState(OrderState);
		setAccountType(AccountType);
		setMobileNo(MobileNo);
		setCurrency(Currency);
		setSynAddress(SynAddress);
		setAsynAddress(AsynAddress);
		setMerchantName(MerchantName);
		setOrderFrom(OrderFrom);
		setLanguage(Language);
		setDescription(Description);
		setOrderType(OrderType);
		setSettleDate(SettleDate);
		setRemark(Remark);
		setUpsNo(UpsNo);
		setTsNo(TsNo);
		setIDCardName(IDCardName);
		setIDCardNo(IDCardNo);
		setBankAddress(BankAddress);
		setIDCardType(IDCardType);
		setBeneficiaryName(BeneficiaryName);
		setBeneficiaryMobileNo(BeneficiaryMobileNo);
		setDeviceData(DeviceData);
		setDeliveryAddress(DeliveryAddress);
		setIpAddress(IpAddress);
		setLocation(Location);
		setUserFlag(UserFlag);
		setRiskFlags(RiskFlags);
		setIDCardAddress(IDCardAddress);
		setBankMobileNo(BankMobileNo);
		setIDCardPhoto(IDCardPhoto);
		setSMSCode(SMSCode);
		setCalleeNo(CalleeNo);
		setBankName(BankName);
		setValidTime(ValidTime);
		setTransType(TransType);
		setCommission(Commission);
		setTransDate(TransDate);
		setContractNo(ContractNo);
		setMAC(MAC);
	}
	private static final long serialVersionUID = -4994439100055144400L;
	private Document doc=null;
	private String procCode="";
	private String version="";
	private String processCode="";
	private String accountNo="";
	private String accountType="";
	private String mobileNo="";
	private String amount="";
	private String currency="";
	private String terminalNo="";
	private String merchantNo="";
	private String acqSsn="";
	private String reference="";
	private String respCode="";
	private String remark="";
	private String transDatetime="";
	private String mAC="";
	private String iDCardName="";
	private String iDCardNo="";
	private String iDCardType="";
	private String bankName="";
	private String beneficiaryName="";
	private String beneficiaryMobileNo="";
	private String transData="";
	private String deviceData="";
	private String deliveryAddress="";
	private String ipAddress="";
	private String location="";
	private String userFlag="";
	private String riskFlags="";
	private String iDCardAddress="";
	private String bankMobileNo="";
	private String iDCardPhoto="";
	private String sMSCode="";
	private String calleeNo="";
	private String settleDate="";
	private String orderNo="";
	private String merchantOrderNo="";
	private String orderState="";
	private String description="";
	private String validTime="";
	private String orderType="";
	private String synAddress="";
	private String asynAddress="";
	private String merchantName="";
	private String orderFrom="";
	private String language="";
	private String upsNo="";
	private String tsNo="";
	private String bankAddress="";
	private String transDate="";
	private String commission="";
	private String contractNo="";
	private String transType="";
	
	public Document getDoc() {
		return doc;
	}
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	public String getProcCode() {
		return procCode;
	}
	public void setProcCode(String procCode) {
		this.procCode = procCode;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getProcessCode() {
		return processCode;
	}
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getTerminalNo() {
		return terminalNo;
	}
	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getAcqSsn() {
		return acqSsn;
	}
	public void setAcqSsn(String acqSsn) {
		this.acqSsn = acqSsn;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTransDatetime() {
		return transDatetime;
	}
	public void setTransDatetime(String transDatetime) {
		this.transDatetime = transDatetime;
	}
	public String getMAC() {
		return mAC;
	}
	public void setMAC(String mAC) {
		this.mAC = mAC;
	}
	public String getIDCardName() {
		return iDCardName;
	}
	public void setIDCardName(String iDCardName) {
		this.iDCardName = iDCardName;
	}
	public String getIDCardNo() {
		return iDCardNo;
	}
	public void setIDCardNo(String iDCardNo) {
		this.iDCardNo = iDCardNo;
	}
	public String getIDCardType() {
		return iDCardType;
	}
	public void setIDCardType(String iDCardType) {
		this.iDCardType = iDCardType;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBeneficiaryName() {
		return beneficiaryName;
	}
	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}
	public String getBeneficiaryMobileNo() {
		return beneficiaryMobileNo;
	}
	public void setBeneficiaryMobileNo(String beneficiaryMobileNo) {
		this.beneficiaryMobileNo = beneficiaryMobileNo;
	}
	public String getTransData() {
		return transData;
	}
	public void setTransData(String transData) {
		this.transData = transData;
	}
	public String getDeviceData() {
		return deviceData;
	}
	public void setDeviceData(String deviceData) {
		this.deviceData = deviceData;
	}
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getUserFlag() {
		return userFlag;
	}
	public void setUserFlag(String userFlag) {
		this.userFlag = userFlag;
	}
	public String getRiskFlags() {
		return riskFlags;
	}
	public void setRiskFlags(String riskFlags) {
		this.riskFlags = riskFlags;
	}
	public String getIDCardAddress() {
		return iDCardAddress;
	}
	public void setIDCardAddress(String iDCardAddress) {
		this.iDCardAddress = iDCardAddress;
	}
	public String getBankMobileNo() {
		return bankMobileNo;
	}
	public void setBankMobileNo(String bankMobileNo) {
		this.bankMobileNo = bankMobileNo;
	}
	public String getIDCardPhoto() {
		return iDCardPhoto;
	}
	public void setIDCardPhoto(String iDCardPhoto) {
		this.iDCardPhoto = iDCardPhoto;
	}
	public String getSMSCode() {
		return sMSCode;
	}
	public void setSMSCode(String sMSCode) {
		this.sMSCode = sMSCode;
	}
	public String getCalleeNo() {
		return calleeNo;
	}
	public void setCalleeNo(String calleeNo) {
		this.calleeNo = calleeNo;
	}
	public String getSettleDate() {
		return settleDate;
	}
	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}
	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}
	public String getOrderState() {
		return orderState;
	}
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getValidTime() {
		return validTime;
	}
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getSynAddress() {
		return synAddress;
	}
	public void setSynAddress(String synAddress) {
		this.synAddress = synAddress;
	}
	public String getAsynAddress() {
		return asynAddress;
	}
	public void setAsynAddress(String asynAddress) {
		this.asynAddress = asynAddress;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getOrderFrom() {
		return orderFrom;
	}
	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getUpsNo() {
		return upsNo;
	}
	public void setUpsNo(String upsNo) {
		this.upsNo = upsNo;
	}
	public String getTsNo() {
		return tsNo;
	}
	public void setTsNo(String tsNo) {
		this.tsNo = tsNo;
	}
	public String getBankAddress() {
		return bankAddress;
	}
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getCommission() {
		return commission;
	}
	public void setCommission(String commission) {
		this.commission = commission;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	
	private void createElement(Document doc, Element root, String value, String tag) {
		if(!ToolKit.isNullOrEmpty(value) && !ToolKit.isNullOrEmpty(tag)){
			Element element = doc.createElement(tag);
			element.appendChild(doc.createTextNode(value));
			root.appendChild(element);
		}
	}
	
	private String getStringFromDocument(Document doc) {
        String result = null;

        if (doc != null) {
            StringWriter strWtr = new StringWriter();
            StreamResult strResult = new StreamResult(strWtr);
            TransformerFactory tfac = TransformerFactory.newInstance();
            try {
                Transformer t = tfac.newTransformer();
                t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                t.setOutputProperty(OutputKeys.INDENT, "yes");
                t.setOutputProperty(OutputKeys.STANDALONE, "yes");
                t.setOutputProperty(OutputKeys.METHOD, "xml");
                t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                t.transform(new DOMSource(doc.getDocumentElement()), strResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = strResult.getWriter().toString();
        }

        return result;
    }
	
	public Document getDocument(String xml) throws Exception {
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(xml)));
        return doc;
	}
	
	public String getValue(String name, Document doc){
		return ToolKit.getElementValue(name, doc);
	}
	
	public String getString(String src) {
		return (ToolKit.isNullOrEmpty(src) ? "" : (" " + src.trim())).toUpperCase();
	}
	
	public String generateMac(String merchantPassword){
		String src = getProcCode()
				+ getString(getAccountNo())
				+ getString(getProcessCode())
				+ getString(getAmount())
				+ getString(getTransDatetime())
				+ getString(getAcqSsn())
				+ getString(getOrderNo())
				+ getString(getTransData())
				+ getString(getReference())
				+ getString(getRespCode())
				+ getString(getTerminalNo())
				+ getString(getMerchantNo())
				+ getString(getMerchantOrderNo())
				+ getString(getOrderState());
		ToolKit.writeLog(this.getClass().getName(), "[MAC source:" + src + "] has merchant pass:" + !ToolKit.isNullOrEmpty(merchantPassword.trim()));
		return new MD5().getMD5ofStr(src + getString(merchantPassword));
	}
	
	public boolean verifyMac(String pass){
		String calcMac = generateMac(pass);
		ToolKit.writeLog(this.getClass().getName(), "[receive MAC:" + getMAC() + "] [calc Mac:" + calcMac + "]");
		if(!getMAC().equals(calcMac)){
			return false;
		}
		return true;
	}
	
	public void assertAttribute(String pass) {
//		assert getAmount() != null && !"".equals(getAmount()) : "Amount 是必填项，不可以为空";
//		assert getMerchantNo() != null && !"".equals(getMerchantNo()) : "MerchantNo 是必填项，不可以为空";
//		assert getAcqSsn() != null && !"".equals(getAcqSsn()) : "AcqSsn 是必填项，不可以为空";
//		assert pass != null && !"".equals(pass) : "MerchantPWD 是必填项，不可以为空";
//		assert getTransDatetime() != null && !"".equals(getTransDatetime()) : "TransDatetime 是必填项，不可以为空";
	}
}
