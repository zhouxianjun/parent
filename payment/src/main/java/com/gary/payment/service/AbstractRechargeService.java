package com.gary.payment.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gary.payment.dto.OrderStore;
import com.gary.payment.dto.PayRequest;
import com.gary.payment.db.Jdbc;
import com.gary.payment.db.ObjectFileUtil;

/**
 * 充值抽象父类
 * 订单号默认为16位：0~6（年月日）7~8（随机号码）9~12（时间分与秒）13~16（原子序列，当为空的时候,超出最大序列的时候,不是当天的时候,重置为1）
 * 存储订单号方式：文件(file)、数据库(jdbc)默认为file,可以使用spring配置实现类重写
 */
public abstract class AbstractRechargeService implements RechargeService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private double minAmount = 0.01d;
	private double maxAmount = 99999999d;
	private BigDecimal fee = new BigDecimal(1);
	protected SimpleDateFormat orderDate = new SimpleDateFormat("yyMMdd##mmss");
	protected SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
	protected DecimalFormat df = new DecimalFormat("0000");
	private String orderNoSqe = "file";
	private String orderNoSqeFile = "/recharge/orderNo.sqe";
	private String orderSqeTable = "recharge_order_sqe";
	private String jdbcUrl;
	private String jdbcDriver;
	private Jdbc db;
	private ObjectFileUtil dbFile;
	private OrderStore orderStore = new OrderStore();
	
	public void saveSqe(OrderStore sqe){
		if(db != null && "jdbc".equalsIgnoreCase(orderNoSqe)){
			try {
				List<Object> params = new ArrayList<Object>();
				params.add(sqe.getSqe().get());
				params.add(sqe.getUpdateDate());
				db.execuetUpdate("update " + orderSqeTable + " set id=?,updateDate=?", params);
				return;
			} catch (Exception e) {
				logger.warn("更新SQE数据库失败!");
			}
		}
		if(dbFile != null && "file".equalsIgnoreCase(orderNoSqe)){
			try {
				dbFile.write(sqe);
			} catch (Exception e) {
				logger.warn("更新SQE文件失败!");
			}
		}
	}
	
	public OrderStore getSqe(){
		try {
			if("jdbc".equalsIgnoreCase(orderNoSqe)){
				if(db == null && jdbcUrl != null && jdbcDriver != null)
					db = new Jdbc(jdbcUrl, jdbcDriver);
				if(!db.hasTable(orderSqeTable)){
					try {
						db.execuetUpdate("create table "+orderSqeTable+"(id number,updateDate datetime)", null);
					} catch (Exception e) {
						logger.warn("创建SQE数据库失败!");
						throw new Exception(e);
					}
				}
				try {
					ResultSet result = db.execuetQuery("select * form " + orderSqeTable, null);
					int id = result.getInt("id");
					if(id <= 0){
						id = 1;
					}
					orderStore.setSqe(new AtomicInteger(id));
					orderStore.setUpdateDate(result.getDate("updateDate"));
					return orderStore;
				} catch (Exception e) {
					logger.warn("查询SQE数据库失败!");
					throw new Exception(e);
				}
			}
			if("file".equalsIgnoreCase(orderNoSqe)){
				if(dbFile == null){
					File file = new File(orderNoSqeFile);
					if(!file.exists()){
						try {
							if(!file.getParentFile().exists())
								file.getParentFile().mkdirs();
							file.createNewFile();
						} catch (IOException e) {
							logger.warn("创建SQE文件失败!");
							throw new Exception(e);
						}
					}
					dbFile = new ObjectFileUtil(file);
				}
				try {
					return orderStore = dbFile.read(OrderStore.class);
				} catch (Exception e) {
					logger.warn("查询SQE文件失败!");
					throw new Exception(e);
				}
			}
		} catch (Exception e) {
			logger.warn("获取SQE失败!");
		}
		return orderStore;
	}

	public int getNoIndex(OrderStore orderStore) {
		//当为空的时候,超出最大序列的时候,不是当天的时候,重置为1
		if(orderStore.getSqe() == null || orderStore.getSqe().get() < 1 || orderStore.getSqe().get() > 1000 ||
				!dateFmt.format(orderStore.getUpdateDate()).equals(dateFmt.format(new Date()))){
			orderStore = new OrderStore();
		}
		return orderStore.getSqe().getAndIncrement();
	}
	
	public synchronized String createOrderNo() {
		orderStore = getSqe();
		int index = getNoIndex(orderStore);
		String oNo = orderDate.format(new Date());
		int r = (int)(Math.random() * 100); //产生10到99的随机数
		String rs = r < 10 ? "0" + r : String.valueOf(r);
		oNo = oNo.replace("##", rs);
		oNo += df.format(index);
		saveSqe(orderStore);
		return oNo;
	}

	public PayRequest createPayRequest(String uid, BigDecimal amount, String type, String goods) {
		if(amount.doubleValue() < minAmount() || amount.doubleValue() > maxAmount() || !isUserEnable(uid)){
			logger.info("充值金额错误或用户未启用!");
			return null;
		}
		PayRequest request = new PayRequest();
		request.setAmount(amount);
		request.setAmountArrive(amount.multiply(fee()));
		request.setCreateDate(new Date());
		request.setFee(fee());
		request.setOrderNo(createOrderNo());
		request.setType(type);
		request.setUid(uid);
		request.setGoods(goods);
		logger.debug("创建充值请求:{}", request);
		return request;
	}

	public boolean isUserEnable(String uid) {
		return true;
	}

	public double minAmount() {
		return minAmount;
	}

	public double maxAmount() {
		return maxAmount;
	}

	public BigDecimal fee() {
		return fee;
	}

	public void setMinAmount(double minAmount) {
		this.minAmount = minAmount;
	}

	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public void setOrderNoSqe(String orderNoSqe) {
		this.orderNoSqe = orderNoSqe;
	}

	public void setOrderNoSqeFile(String orderNoSqeFile) {
		this.orderNoSqeFile = orderNoSqeFile;
	}

	public void setOrderSqeTable(String orderSqeTable) {
		this.orderSqeTable = orderSqeTable;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}
}
