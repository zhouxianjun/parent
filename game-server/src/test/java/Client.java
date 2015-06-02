import com.gary.netty.net.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/3/6 16:01
 */
public class Client {
    public static void main(String args[]) throws Exception {
        String host = "127.0.0.1";
        int port = 8586;
        Socket client = new Socket(host, port);
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        Packet packet = Packet.createGlobalException();
        dos.writeShort(packet.calcSize()); //输出总长度
        dos.writeShort(packet.getCmd()); //命令
        dos.writeShort(packet.getRet().toByteArray().length);
        dos.write(packet.getRet().toByteArray());
        if (packet.getBody() != null){
            dos.write(packet.getBody().toByteArray());
        }

//        DataInputStream dis = new DataInputStream(client.getInputStream());
        dos.flush();
        /*while (true){
            if (dis.readBoolean()) {
                short length = dis.readShort();
                short cmd = dis.readShort();
                //消息RET
                //byte[] ret = new byte[length - 2];
                //dis.read(ret);
                //byte[] body = new byte[buffer.readableBytes()];
                // dis.readFully();
                System.out.println("收到--" + length + "----0x" + Integer.toHexString(cmd));
            }
        }*/
        dos.close();
        client.close();
    }

    private static String getSetMethodName(String name){
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
