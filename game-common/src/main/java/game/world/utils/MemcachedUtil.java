package game.world.utils;

import com.google.common.collect.Sets;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rubyeye.xmemcached.MemcachedClient;

import java.util.Iterator;
import java.util.Set;

@Slf4j
public class MemcachedUtil {
	@Setter
	private static MemcachedClient memcachedClient;
	private static Set<String> keys = Sets.newHashSet();
	public static <T> T get(String key){
		try {
			return memcachedClient.get(key);
		} catch (Exception e) {
			log.warn("从Memcached获取{}缓存数据错误!", key);
		}
		return null;
	}
	
	/**
	 * 删除缓存,不抛出异常
	 * @param key
	 * @return boolean
	 */
	public static boolean delete(String key){
		try {
			return memcachedClient.delete(key);
		} catch (Exception e) {
			log.warn("从Memcached删除{}缓存数据错误!", key);
		}
		return false;
	}

	/**
	 * 设置缓存,不抛出异常
	 * @param key
	 * @param exp
	 * @param object
	 */
	public static boolean set(String key, int exp, Object object){
		try {
			return memcachedClient.set(key, exp, object);
		} catch (Exception e) {
			log.warn("设置Memcached KEY:{} 错误!", key);
		}
		return false;
	}

	/**
	 * 设置缓存,永不过期,需要调用delAll清除
	 * @param key
	 * @param object
	 * @return
	 */
	public static boolean set(String key, Object object){
		boolean ret = set(key, 0, object);
		if (ret){
			keys.add(key);
		}
		return ret;
	}

	/**
	 * 删除自身所有缓存
	 */
	public static void delAll(){
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()){
			delete(iterator.next());
		}
	}
}
