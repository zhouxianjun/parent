package game.world.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.ZParams.Aggregate;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisBase {

	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private ShardedJedisPool shardedJedisPool;

	/** 操作Key的方法 */
	public Keys KEYS;
	/** 对存储结构为String类型的操作 */
	public Strings STRINGS;
	/** 对存储结构为List类型的操作 */
	public Lists LISTS;
	/** 对存储结构为Set类型的操作 */
	public Sets SETS;
	/** 对存储结构为HashMap类型的操作 */
	public Hash HASH;
	/** 对存储结构为Set(排序的)类型的操作 */
	public SortSet SORTSET;

	public Lock LOCK;

	@PostConstruct
	public void init() {
		KEYS = new Keys();
		STRINGS = new Strings();
		LISTS = new Lists();
		SETS = new Sets();
		SORTSET = new SortSet();
		HASH = new Hash();
		LOCK = new Lock();
	}

	public class Keys {
		
		private final Logger LOGGER = LoggerFactory.getLogger(Keys.class);  
		
		/**
		 * 一个key-value并设置过期时间
		 * 
		 * @param key
		 * @param seconds
		 * @param value
		 * @return OK或者fail
		 */
		public String setex(String key, int seconds, String value) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				String status = jedis.setex(key, seconds, value);
				return status;
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException setex [key={}, value={}, seconds={}] errorMsg={}.", new Object[]{key, value, seconds, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("redis Exception setex [key={},value={},seconds={}] errorMsg={}.", new Object[]{key, value, seconds, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return "fail";
		}
		
		/**
		 * 设置key的过期时间，以秒为单位
		 * 
		 * @param String
		 *            key
		 * @param 时间
		 *            ,已秒为单位
		 * @return 影响的记录数
		 * */
		public long expired(String key, int seconds) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				long count = jedis.expire(key, seconds);
				return count;
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException expired [key={}, seconds={}] errorMsg={}", new Object[]{key, seconds, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("redis Exception expired [key={}, seconds={}] errorMsg={}", new Object[]{key, seconds, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return 0;
		}
		
		/**
		 * 删除指定key，如果删除的key不存在，则直接忽略
		 * @param key
		 * @return	被删除的keys的数量
		 */
		public Long del(String key){
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				Long count = jedis.del(key);
				return count;
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException expired [key={}] errorMsg={}", new Object[]{key, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("redis Exception expired [key={}] errorMsg={}", new Object[]{key, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return 0L;
		}
		
		/**
		 * 删除指定key，如果删除的key不存在，则直接忽略
		 * @param key
		 * @return	被删除的keys的数量
		 */
		public Long del(byte[] key){
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				Long count = jedis.del(key);
				return count;
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException expired [key={}] errorMsg={}", new Object[]{key, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("redis Exception expired [key={}] errorMsg={}", new Object[]{key, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return 0L;
		}

		/**
		 * 检查给定 key 是否存在。
		 * @param key
		 * @return
		 */
		public Boolean exists(String key){
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				return jedis.exists(key);
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException exists [key={}] errorMsg={}", new Object[]{key, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("redis Exception exists [key={}] errorMsg={}", new Object[]{key, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return false;
		}

	}
	
	public class Strings {
		
		private final Logger LOGGER = LoggerFactory.getLogger(Strings.class);  
		
		/**
		 * 根据key获取记录
		 * 
		 * @param String
		 *            key
		 * @return 值
		 * */
		public String get(String key) {
			ShardedJedis sjedis = null;
			try {
				sjedis = shardedJedisPool.getResource();
				String value = sjedis.get(key);
				return value;
			} catch (JedisConnectionException je) {
				shardedJedisPool.returnBrokenResource(sjedis);
				LOGGER.error("JedisConnectionException get key={} errorMsg={}.", key, je.getMessage());
			} catch (Exception e) {
				LOGGER.error("redis Exception get key={} errorMsg={}", key, e.getMessage());
			} finally {
				shardedJedisPool.returnResource(sjedis);
			}
			return null;
		}
		/**
		 * 根据key获取记录
		 * 
		 * @param byte[] key
		 * @return 值
		 * */
		public byte[] get(byte[] key) {
			ShardedJedis sjedis = null;
			try {
				sjedis = shardedJedisPool.getResource();
				byte[] value = sjedis.get(key);
				return value;
				
			} catch (JedisConnectionException je) {
				shardedJedisPool.returnBrokenResource(sjedis);
				LOGGER.error("JedisConnectionException get key={} errorMsg={}.", new String(key), je.getMessage());
			} catch (Exception e) {
				LOGGER.error("Exception get key={} errorMsg={}.", new String(key), e.getMessage());
			} finally {
				shardedJedisPool.returnResource(sjedis);
			}
			return null;
		}
		/**
		 * 添加有过期时间的记录
		 * 
		 * @param String
		 *            key
		 * @param int seconds 过期时间，以秒为单位
		 * @param String
		 *            value
		 * @return String 操作状态
		 * */
		public String setEx(String key, int seconds, String value) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				String str = jedis.setex(key, seconds, value);
				return str;
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException setEx [key={}, value={},seconds={}] errorMsg={}.", new Object[]{key, value, seconds, je.getMessage()});
			} catch (Exception e) {
				LOGGER.error("JedisConnectionException setEx [key={}, value={},seconds={}] errorMsg={}.", new Object[]{key, value, seconds, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return null;
		}
		
		/**
		 * 添加有过期时间的记录
		 * 
		 * @param String
		 *            key
		 * @param String
		 *            value
		 * @return String 操作状态
		 * */
		public String set(String key, String value) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				String str = jedis.set(key, value);
				return str;
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException setEx [key={}, value={}] errorMsg={}.", new Object[]{key, value, je.getMessage()});
			} catch (Exception e) {
				LOGGER.error("JedisConnectionException setEx [key={}, value={}] errorMsg={}.", new Object[]{key, value, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return null;
		}

		/**
		 * 添加有过期时间的记录
		 * 
		 * @param String
		 *            key
		 * @param int seconds 过期时间，以秒为单位
		 * @param String
		 *            value
		 * @return String 操作状态
		 * */
		public String setEx(byte[] key, int seconds, byte[] value) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				String str = jedis.setex(key, seconds, value);
				return str;
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException setEx [key={}, value={},seconds={}] errorMsg={}.", new Object[]{new String(key), value, seconds, je.getMessage()});
			} catch (Exception e) {
				LOGGER.error("Exception setEx [key={}, value={},seconds={}] errorMsg={}.", new Object[]{new String(key), value, seconds, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return null;
		}
		/**
		 * 批量获取记录,如果指定的key不存在返回List的对应位置将是null
		 * 
		 * @param String
		 *            keys
		 * @return List<String> 值得集合
		 * */
		public List<byte[]> mget(byte[]... keys) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				List<byte[]> str = jedis.mget(keys);
				return str;
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException mget errorMsg={}.", e.getMessage());
			} catch (Exception e) {
				LOGGER.error("Exception mget errorMsg={}.", e.getMessage());
			} finally {
				jedisPool.returnResource(jedis);
			}
			return null;
		}
		/**
		 * 批量存储记录
		 * 
		 * @param String
		 *            keysvalues 例:keysvalues="key1","value1","key2","value2";
		 * @return String 状态码
		 * */
		public String mset(byte[]... keysvalues) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				String str = jedis.mset(keysvalues);
				return str;
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException mset errorMsg={}.", je.getMessage());
			} catch (Exception e) {
				LOGGER.error("Exception mset errorMsg={}.", e.getMessage());
			} finally {
				jedisPool.returnResource(jedis);
			}
			return null;
		}
		
		/**
		 * 原子加1
		 * 
		 * @param String
		 *            key
		 * @return Long 操作后的数
		 * */
		public Long incr(String key) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				Long value = jedis.incr(key);
				return value;
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException incr [key={}] errorMsg={}.", new Object[]{key, je.getMessage()});
			} catch (Exception e) {
				LOGGER.error("JedisConnectionException incr [key={}] errorMsg={}.", new Object[]{key, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return null;
		}
		
		/**
		 * 原子减1
		 * 
		 * @param String
		 *            key
		 * @return Long 操作后的数
		 * */
		public Long decr(String key) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				Long value = jedis.decr(key);
				return value;
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException decr [key={}] errorMsg={}.", new Object[]{key, je.getMessage()});
			} catch (Exception e) {
				LOGGER.error("JedisConnectionException decr [key={}] errorMsg={}.", new Object[]{key, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return null;
		}
		
		/**
		 * 如果key不存在，就设置key对应字符串value。在这种情况下，该命令和SET一样。当key已经存在时，就不做任何操作。
		 * @param key
		 * @param value
		 * @return 返回值
		 * <br>数字，只有以下两种值：
	     * <br>1 如果key被set
		 * <br>0 如果key没有被set
		 */
		public Long setnx(String key, String value) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				Long ret = jedis.setnx(key, value);
				return ret;
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException setnx [key={}, value={}] errorMsg={}.", new Object[]{key, value, je.getMessage()});
			}catch(Exception e) {
				LOGGER.error("JedisConnectionException setnx [key={}, value={}] errorMsg={}.", new Object[]{key, value, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return null;
		}
	}
	public class Lists {
		private final Logger LOGGER = LoggerFactory.getLogger(Lists.class);

		/**
		 * 将一个或多个值 value 插入到列表 key 的表头
		 * @param key
		 * @param vals
		 * @return
		 */
		public Long lpush(String key, String...vals){
			Long s = null;
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				s = jedis.lpush(key, vals);
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException lpush [key={},vals={}] errorMsg={}.", new Object[]{key, vals, je.getMessage()});
			} catch (Exception e) {
				LOGGER.error("redis Exception lpush [key={},vals={}] errorMsg={}", new Object[]{key, vals, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return s;
		}

		/**
		 * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
		 * @param key
		 * @param vals
		 * @return
		 */
		public Long rpush(String key, String...vals){
			Long s = null;
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				s = jedis.rpush(key, vals);
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException rpush [key={},vals={}] errorMsg={}.", new Object[]{key, vals, je.getMessage()});
			} catch (Exception e) {
				LOGGER.error("redis Exception rpush [key={},vals={}] errorMsg={}", new Object[]{key, vals, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return s;
		}

		/**
		 * 返回列表 key 的长度
		 * @param key
		 * @return
		 */
		public Long llen(String key){
			Long s = null;
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				s = jedis.llen(key);
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException llen [key={}] errorMsg={}.", new Object[]{key, je.getMessage()});
			} catch (Exception e) {
				LOGGER.error("redis Exception llen [key={}] errorMsg={}", new Object[]{key, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return s;
		}

		/**
		 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 end 指定。（包含自身）
		 * @param key
		 * @param start
		 * @param end
		 * @return
		 */
		public List<String> lrange(String key, int start, int end){
			List<String> s = null;
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				s = jedis.lrange(key, start, end);
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException lrange [key={}, start={}, end={}] errorMsg={}.", new Object[]{key, start, end, je.getMessage()});
			} catch (Exception e) {
				LOGGER.error("redis Exception lrange [key={}, start={}, end={}] errorMsg={}", new Object[]{key, start, end, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return s;
		}

		/**
		 * 将列表 key 下标为 index 的元素的值设置为 value 。
		 * @param key
		 * @param index
		 * @param val
		 * @return
		 */
		public String lset(String key, int index, String val){
			String s = null;
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				s = jedis.lset(key, index, val);
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException lset [key={}, index={}, val={}] errorMsg={}.", new Object[]{key, index, val, je.getMessage()});
			} catch (Exception e) {
				LOGGER.error("redis Exception lset [key={}, index={}, val={}] errorMsg={}", new Object[]{key, index, val, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return s;
		}

		/**
		 * 返回列表 key 中，下标为 index 的元素。
		 * @param key
		 * @param index
		 * @return
		 */
		public String lindex(String key, int index){
			String s = null;
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				s = jedis.lindex(key, index);
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException lindex [key={}, index={}] errorMsg={}.", new Object[]{key, index, je.getMessage()});
			} catch (Exception e) {
				LOGGER.error("redis Exception lindex [key={}, index={}] errorMsg={}", new Object[]{key, index, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return s;
		}
	}
	public class Sets {
		private final Logger LOGGER = LoggerFactory.getLogger(Sets.class);  
		
		/**
		 * 向Set添加一条记录，如果member已存在返回0,否则返回1
		 * 
		 * @param String
		 *            key
		 * @param String
		 *            member
		 * @return 操作码,0或1
		 * */
		public Long sadd(String key, String member) {
			Long s = null;
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				s = jedis.sadd(key, member);
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException sadd [key={},member={}] errorMsg={}.", new Object[]{key, member, je.getMessage()});
			} catch (Exception e) {
				LOGGER.error("redis Exception sadd [key={},member={}] errorMsg={}", new Object[]{key, member, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}

			return s;
		}
		
		/**
		 * 从集合中删除指定成员
		 * 
		 * @param String
		 *            key
		 * @param String
		 *            member 要删除的成员
		 * @return 状态码，成功返回1，成员不存在返回0
		 * */
		public Long srem(String key, String member) {
			
			Jedis jedis = null;
			Long s = 0L;
			try {
				jedis = jedisPool.getResource();
				s = jedis.srem(key, member);
				return s;
			} catch (JedisConnectionException je) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException srem [key={}, member={}] errorMsg={}.", new Object[]{key, member, je.getMessage()});
			} catch (Exception e) {
				LOGGER.error("redis Exception srem [key={}, member={}] errorMsg={}", new Object[]{key, member, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			
			return s;
		}
		
		/**
		 * 返回集合中的所有成员
		 * 
		 * @param String
		 *            key
		 * @return 成员集合
		 * */
		public Set<String> smembers(String key) {
			
			ShardedJedis sjedis = null;
			Set<String> set = null;
			try {
				sjedis = shardedJedisPool.getResource();
				set = sjedis.smembers(key);
				return set;
			} catch (JedisConnectionException je) {
				shardedJedisPool.returnBrokenResource(sjedis);
				LOGGER.error("JedisConnectionException smembers key={} errorMsg={}.", key, je.getMessage());
			} catch (Exception e) {
				LOGGER.error("Exception smembers key={} errorMsg={}.", key, e.getMessage());
			} finally {
				shardedJedisPool.returnResource(sjedis);
			}
			
			return set;
			
		}
	}
	public class Hash {
		
		private final Logger LOGGER = LoggerFactory.getLogger(Hash.class);  
		/**
		 * 以Map的形式返回hash中的存储和值
		 * 
		 * @param String
		 *            key
		 * @return Map<Strinig,String>
		 * */
		public Map<String, String> hgetall(String key) {
			
			ShardedJedis sjedis = null;
			try {
				sjedis = shardedJedisPool.getResource();
				Map<String, String> map = sjedis.hgetAll(key);
				return map;
			} catch(JedisConnectionException e) {
				shardedJedisPool.returnBrokenResource(sjedis);
				LOGGER.error("JedisConnectionException hgetall key={} errorMsg={}", key, e.getMessage());
				throw e;
			} catch (Exception e) {
				LOGGER.error("Exception hgetall key={}, errorMsg={}", key, e.getMessage());
			} finally {
				shardedJedisPool.returnResource(sjedis);
			}
			return new HashMap<String, String>();
		}
		
		/**
		 * 添加对应关系，如果对应关系已存在，则覆盖
		 * 
		 * @param Strin
		 *            key
		 * @param Map
		 *            <String,String> 对应关系
		 * @return 状态，成功返回OK
		 * */
		public String hmset(String key, Map<String, String> map) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				String s = jedis.hmset(key, map);
				return s;
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException hmset key={}, errorMsg={}", key, e.getMessage());
			} catch (Exception e) {
				LOGGER.error("Exception hmset key={}, errorMsg={}", key, e.getMessage());
			} finally {
				jedisPool.returnResource(jedis);
			}
			return null;
		}
		
		/**
		 * 返回hash中指定存储位置的值
		 * 
		 * @param String
		 *            key
		 * @param String
		 *            fieid 存储的名字
		 * @return 存储对应的值
		 * */
		public String hget(String key, String field) {
			ShardedJedis sjedis = null;
			try {
				sjedis = shardedJedisPool.getResource();
				String s = sjedis.hget(key, field);
				return s;
			} catch(JedisConnectionException e) {
				shardedJedisPool.returnBrokenResource(sjedis);
				LOGGER.error("JedisConnectionException hget [key={} field={}], errorMsg={}", new Object[]{key, field, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("Exception hget [key={} field={}], errorMsg={}", new Object[]{key, field, e.getMessage()});
			} finally {
				shardedJedisPool.returnResource(sjedis);
			}
			return null;
		}
		
		/**
		 * 增加 key 指定的哈希集中指定字段的数值。如果 key 不存在，会创建一个新的哈希集并与 key 关联。如果字段不存在，则字段的值在该操作执行前被设置为 0
		 * @param key
		 * @param field
		 * @param value
		 * @return 累加后的值
		 */
		public Long hincrby(String key, String field, int value) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				Long ret = jedis.hincrBy(key, field, value);
				return ret;
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException hincrby key={}, errorMsg={}", key, e.getMessage());
			} catch (Exception e) {
				LOGGER.error("Exception hincrby key={}, errorMsg={}", key, e.getMessage());
			} finally {
				jedisPool.returnResource(jedis);
			}
			return 0L;
		}
		
		/**
		 * 返回字段是否是 key 指定的哈希集中存在的字段
		 * @param key
		 * @param field
		 * @return	1 哈希集中含有该字段、0 哈希集中不含有该存在字段，或者key不存在
		 */
		public boolean hexists(String key, String field){
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				Boolean ret = jedis.hexists(key, field);
				return ret;
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException hincrby key={}, errorMsg={}", key, e.getMessage());
			} catch (Exception e) {
				LOGGER.error("Exception hincrby key={}, errorMsg={}", key, e.getMessage());
			} finally {
				jedisPool.returnResource(jedis);
			}
			return false;
		}
	}
	public class SortSet {
		private final Logger LOGGER = LoggerFactory.getLogger(SortSet.class);  
		/**
		 * 向集合中增加一条记录,如果这个值已存在，这个值对应的权重将被置为新的权重
		 * 
		 * @param String
		 *            key
		 * @param double score 权重
		 * @param String
		 *            member 要加入的值，
		 * @return 状态码 1成功，0已存在member的值
		 * */
		public long zadd(String key, double score, String member) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				long s = jedis.zadd(key, score, member);
				return s;
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException zadd [key={} score={}, member={}], errorMsg={}", new Object[]{key, score, member, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("Exception zadd [key={} score={}, member={}], errorMsg={}", new Object[]{key, score, member, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return 0;
		}

		/**
		 * 移除指定members
		 * @param key
		 * @param members
		 * @return
		 */
		public long zrem(String key, String... members) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				long s = jedis.zrem(key, members);
				return s;
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException zrem [key={} members={}], errorMsg={}", new Object[]{key, members, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("Exception zrem [key={} members={}], errorMsg={}", new Object[]{key, members, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return 0;
		}

		/**
		 * 移除直接rank区间的(包含自己)
		 * @param key
		 * @param start
		 * @param end
		 * @return
		 */
		public long zremrangebyrank(String key, int start, int end) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				long s = jedis.zremrangeByRank(key, start, end);
				return s;
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException zremrangebyrank [key={} start={} end={}], errorMsg={}", new Object[]{key, start, end, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("Exception zremrangebyrank [key={} start={} end={}], errorMsg={}", new Object[]{key, start, end, e.getMessage()});
			} finally {
				jedisPool.returnResource(jedis);
			}
			return 0;
		}
		/**
		 * 获取给定区间的元素，原始按照权重由高到低排序
		 * 
		 * @param String
		 *            key
		 * @param int start
		 * @param int end
		 * @return Set<String>
		 * */
		public Set<String> zrevrange(String key, int start, int end) {
			ShardedJedis sjedis = null;
			try {
				sjedis = shardedJedisPool.getResource();
				Set<String> set = sjedis.zrevrange(key, start, end);
				return set;
			} catch(JedisConnectionException e) {
				shardedJedisPool.returnBrokenResource(sjedis);
				LOGGER.error("JedisConnectionException zrevrange [key={} start={}, end={}], errorMsg={}", new Object[]{key, start, end, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("Exception zrevrange [key={} start={}, end={}], errorMsg={}", new Object[]{key, start, end, e.getMessage()});
			} finally {
				shardedJedisPool.returnResource(sjedis);
			}
			return new HashSet<String>();
		}
		/**
		 * 返回有序集key中，指定区间内的成员。其中成员的位置按score值递减(从大到小)来排列。 具有相同score值的成员按字典序的反序排列。
		 * 
		 * @param key
		 * @param start
		 *            开始位置
		 * @param end
		 *            结束位置
		 * @return ${Set<Tuple>}
		 */
		public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
			ShardedJedis sjedis = null;
			try {
				sjedis = shardedJedisPool.getResource();
				Set<Tuple> set = sjedis.zrevrangeWithScores(key, start, end);
				return set;
			} catch(JedisConnectionException e) {
				shardedJedisPool.returnBrokenResource(sjedis);
				LOGGER.error("JedisConnectionException zrangeWithScores [key={} start={}, end={}], errorMsg={}", new Object[]{key, start, end, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("Exception zrangeWithScores [key={} start={}, end={}], errorMsg={}", new Object[]{key, start, end, e.getMessage()});
			} finally {
				shardedJedisPool.returnResource(sjedis);
			}
			return null;
		}

		/**
		 * 返回有序集key中，指定区间内的成员。其中成员的位置按score值递增(从小到大)来排列。 具有相同score值的成员按字典序的反序排列。
		 * @param key
		 * @param start
		 * @param end
		 * @return
		 */
		public Set<Tuple> zrangeWithScores(String key, int start, int end) {
			ShardedJedis sjedis = null;
			try {
				sjedis = shardedJedisPool.getResource();
				Set<Tuple> set = sjedis.zrangeWithScores(key, start, end);
				return set;
			} catch(JedisConnectionException e) {
				shardedJedisPool.returnBrokenResource(sjedis);
				LOGGER.error("JedisConnectionException zrangeWithScores [key={} start={}, end={}], errorMsg={}", new Object[]{key, start, end, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("Exception zrangeWithScores [key={} start={}, end={}], errorMsg={}", new Object[]{key, start, end, e.getMessage()});
			} finally {
				shardedJedisPool.returnResource(sjedis);
			}
			return null;
		}
		/**
		 * 计算给定的numkeys个有序集合的最大交集,并且把结果放到 dstkey中.
		 * 
		 * @param dstkey
		 * @param sets
		 * @return
		 */
		public long zinterstoreByAggregate(String dstkey, String... sets) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				ZParams zParams = new ZParams();
				zParams.aggregate(Aggregate.MAX);
				long len = jedis.zinterstore(dstkey, zParams, sets);
				return len;
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("JedisConnectionException zinterstoreByAggregate key={} , errorMsg={}", dstkey, e.getMessage());
			} catch (Exception e) {
				LOGGER.error("Exception zinterstoreByAggregate key={} , errorMsg={}", dstkey, e.getMessage());
			} finally {
				jedisPool.returnResource(jedis);
			}
			return 0;
		}
		/**
		 * 获取指定值在集合中的位置，集合排序从低到高
		 * 
		 * @see zrevrank
		 * @param String
		 *            key
		 * @param String
		 *            member
		 * @return long 位置
		 * */
		public Long zrank(String key, String member) {
			ShardedJedis sjedis = null;
			try {
				sjedis = shardedJedisPool.getResource();
				Long index = sjedis.zrank(key, member);
				return index;
			} catch(JedisConnectionException e) {
				shardedJedisPool.returnBrokenResource(sjedis);
				LOGGER.error("JedisConnectionException zrank [key={} member={}], errorMsg={}", new Object[]{key, member, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("Exception zrank [key={} member={}], errorMsg={}", new Object[]{key, member, e.getMessage()});
			} finally {
				shardedJedisPool.returnResource(sjedis);
			}
			return -1L;
		}
		/**
		 * 获取指定值在集合中的位置，集合排序从高到低
		 * 
		 * @see zrank
		 * @param String
		 *            key
		 * @param String
		 *            member
		 * @return long 位置
		 * */
		public Long zrevrank(String key, String member) {
			ShardedJedis sjedis = null;
			try {
				sjedis = shardedJedisPool.getResource();
				Long index = sjedis.zrevrank(key, member);
				return index;
			} catch(JedisConnectionException e) {
				shardedJedisPool.returnBrokenResource(sjedis);
				LOGGER.error("JedisConnectionException zrevrank [key={} member={}], errorMsg={}", new Object[]{key, member, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("Exception zrevrank [key={} member={}], errorMsg={}", new Object[]{key, member, e.getMessage()});
			} finally {
				shardedJedisPool.returnResource(sjedis);
			}
			return null;
		}
		/**
		 * 获取集合中元素的数量
		 * 
		 * @param String
		 *            key
		 * @return 如果返回0则集合不存在
		 * */
		public long zcard(String key) {
			ShardedJedis sjedis = null;
			try {
				sjedis = shardedJedisPool.getResource();
				long len = sjedis.zcard(key);
				return len;
			} catch(JedisConnectionException e) {
				shardedJedisPool.returnBrokenResource(sjedis);
				LOGGER.error("JedisConnectionException zcard key={}, errorMsg={}", key, e.getMessage());
			} catch (Exception e) {
				LOGGER.error("Exception zcard key={}, errorMsg={}", key, e.getMessage());
			} finally {
				shardedJedisPool.returnResource(sjedis);
			}
			return 0;
		}
		
		/**
		 * 获取指定成员在key中的值
		 * @param key
		 * @param member
		 * @return
		 */
		public Double zscore(String key, String member) {
			ShardedJedis sjedis = null;
			try {
				sjedis = shardedJedisPool.getResource();
				Double score = sjedis.zscore(key, member);
				return score;
			} catch(JedisConnectionException e) {
				shardedJedisPool.returnBrokenResource(sjedis);
				LOGGER.error("JedisConnectionException zscore [key={} member={}], errorMsg={}", new Object[]{key, member, e.getMessage()});
			} catch (Exception e) {
				LOGGER.error("Exception zscore [key={} member={}], errorMsg={}", new Object[]{key, member, e.getMessage()});
			} finally {
				shardedJedisPool.returnResource(sjedis);
			}
			return null;
		}
		
	}
	public class Lock {
		private final Logger LOGGER = LoggerFactory.getLogger(Lock.class);  
		 
		private static final int DEFAULT_SINGLE_EXPIRE_TIME = 3;  
		 
		@SuppressWarnings("resource")
		public boolean tryLock(String key, long timeout, TimeUnit unit) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				long nano = System.nanoTime();
				timeout = unit.toNanos(timeout);
				do {
					LOGGER.debug("try lock key: " + key);
					Long i = jedis.setnx(key, key);
					if (i == 1) {
						jedis.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);
						return Boolean.TRUE;
					}
					Thread.sleep(50);
				} while ((System.nanoTime() - nano) < timeout);
				return Boolean.FALSE;
			} catch (JedisConnectionException je) {
				returnBrokenResource(jedis);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			} finally {
				returnResource(jedis);
			}
			return Boolean.FALSE;
		}
		
		/**
		 * 释放锁
		 */
		public long delLock(String key) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				long count = jedis.del(key);
				return count;
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("redis connection error.msg={}", e.getMessage());
				throw e;
			} catch (Exception e) {
				LOGGER.error("redis connection error.msg={}", e.getMessage());
			} finally {
				jedisPool.returnResource(jedis);
			}
			return 0;
		}

		private void returnBrokenResource(Jedis jedis) {
			if (jedis == null) {
				return;
			}
			try {
				// 容错
				jedisPool.returnBrokenResource(jedis);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		private void returnResource(Jedis jedis) {
			if (jedis == null) {
				return;
			}
			try {
				jedisPool.returnResource(jedis);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		/**
		 * 获取一个分布式锁
		 * @param lockKey		锁的key
		 * @param expireTime	key的过期时间(单位：毫秒)
		 * @return
		 */
		public boolean tryLock(String lockKey, Long expireTime) {
			
			long DEFAULT_EXPIRE_TIME = 2000; 
			if(expireTime <= 0){
				expireTime = DEFAULT_EXPIRE_TIME;
			}
			
			Jedis jedis = null;
			boolean success = false;
			try {
				/* 1. 通过SETNX试图获取一个lock */
				jedis = jedisPool.getResource();
				long timeOut = System.currentTimeMillis() + expireTime + 1;
				lockKey = lockKey+1;
				/* SETNX成功，则成功获取一个锁 */
				long acquired = jedis.setnx(lockKey, String.valueOf(timeOut));
				if(acquired == 1){
					jedis.expire(lockKey, expireTime.intValue());
					success = true;
				}else{
					/* SETNX失败，说明锁仍然被其他对象保持，检查其是否已经超时 */
					long whileTimeOut = System.currentTimeMillis() + 5000;
					do {
						long oldTime = Long.valueOf(jedis.get(lockKey));
						
						synchronized (this) {
							
							long currentTime = System.currentTimeMillis();
							/* 检查是否超时 */
							if(oldTime < currentTime){
								String lockTime = jedis.getSet(lockKey, String.valueOf(currentTime));
								/* 获取锁成功 */
								if(currentTime == Long.parseLong(lockTime)){
									success = true;
								}else{
									/* 已被其他进程捷足先登了 */
									success = false;
								}
							}else{
								/* 未超时，则直接返回失败 */
								success = false;
							}
							
						}
						/* 成功获得锁,则跳出 */
						if(success){
							break;
						}
						Thread.sleep(50);
						
					} while (System.currentTimeMillis() < whileTimeOut);
					
				}
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("redis connection error.msg={}", e.getMessage());
				return success;
			} catch (Exception e) {
				LOGGER.error("redis connection error.msg={}", e.getMessage());
				return success;
			} finally {
				jedisPool.returnResource(jedis);
			}
			return success;
		}
		
		// 释放锁
		public void releaseLock(String lock) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				long current = System.currentTimeMillis();
				// 避免删除非自己获取得到的锁
				if (current < Long.valueOf(jedis.get(lock)))
					jedis.del(lock);
			} catch(JedisConnectionException e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error("redis connection error.msg={}", e.getMessage());
				throw e;
			} catch (Exception e) {
				LOGGER.error("redis connection error.msg={}", e.getMessage());
			} finally {
				jedisPool.returnResource(jedis);
			}
		}

	}
}