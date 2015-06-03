import com.gary.netty.net.Packet;
import com.gary.netty.protobuf.ResultPro;

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
        Packet packet = Packet.createGlobalException(1);
        ResultPro.Result.Builder builder = ResultPro.Result.newBuilder();
        builder.setCode(1);
        builder.setMsg("success");
        byte[] b = builder.build().toByteArray();
        byte[] data = new byte[]{8, 1, 18, 12, -26, -109, -115, -28, -67, -100, -26, -120, -112, -27, -118, -97};
        dos.writeShort(3 + b.length); //输出总长度
        dos.writeShort(packet.getCmd()); //命令
        dos.writeShort(b.length);
        dos.write(b);
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
