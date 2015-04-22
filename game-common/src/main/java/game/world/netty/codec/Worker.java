package game.world.netty.codec;

import game.world.WorldManager;
import game.world.disruptor.DisruptorEvent;
import game.world.event.ReceivedEvent;
import game.world.protobuf.ResultPro;
import game.world.utils.ClassUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:对象事件工作
 * @date 2015/4/14 14:54
 */
@Slf4j
public abstract class Worker<T, E extends ReceivedEvent> {
    public final String ip;
    private final Channel channel;
    private volatile T object;
    private AtomicBoolean offlineProcessed = new AtomicBoolean(false);

    private volatile DisruptorEvent taskExec;
    private volatile DisruptorEvent taskInExec;

    public Worker(Channel channel) {
        if (channel == null){
            throw new NullPointerException("新建Worker, channel为null");
        }
        this.channel = channel;
        this.ip = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
        taskInExec = new DisruptorEvent("WorkerIn_", WorldManager.CORE_NUM);
        log.info("connection ip = {}", ip);
    }

    /**
     * 接收到消息
     * @param buffer
     * @param length
     * @param cmd
     * @param result
     * @param body
     */
    public void messageReceived(ByteBuf buffer, int length, short cmd, ResultPro.Result result, byte[] body){
        if (!ping(channel, object, cmd)){
            Class<? extends ReceivedEvent> eventClass = getEventClass();
            try {
                Class<?> t = ClassUtil.getSuperClassGenricType(getClass(), 0);
                Constructor<? extends ReceivedEvent> constructor = eventClass.getDeclaredConstructor(new Class[]{int.class, short.class, t, Channel.class, ResultPro.Result.class, Worker.class, byte[].class});
                ReceivedEvent event = constructor.newInstance(length, cmd, object, channel, result, this, body);
                taskInExec.publish(event);
            } catch (Exception e) {
                log.error("事件创建失败!", e);
            }
        }
    }

    /**
     * 断线
     */
    public void processDisconnection() {
        if (offlineProcessed.compareAndSet(false, true)) {
            disconnection(object, channel, ip);
        }
    }

    /**
     * 已通过服务器验证(例如登录后)调用
     * @param object
     * @param id
     */
    public void login(T object, int id){
        taskExec = WorldManager.getInstance().getExecutorService(id);
        this.object = object;
    }

    /**
     * ping包处理 返回false则执行eventHandler
     * @param channel
     * @param object
     * @param cmd
     * @return
     */
    protected abstract boolean ping(Channel channel, T object, short cmd);

    /**
     * 获取接收事件Event 类
     * @return
     */
    protected Class<? extends ReceivedEvent> getEventClass(){
        return (Class<? extends ReceivedEvent>) ClassUtil.getSuperClassGenricType(getClass(), 1);
    }

    /**
     * 断开连接
     * @param object
     * @param channel
     * @param ip
     */
    protected void disconnection(T object, Channel channel, String ip){}

    /**
     * 在对象线性线程中执行
     * @param event
     */
    public void executeTask(Runnable event) {
        if (taskExec == null){
            throw new NullPointerException("对象线性线程未初始化");
        }
        taskExec.publish(event);
    }
}
