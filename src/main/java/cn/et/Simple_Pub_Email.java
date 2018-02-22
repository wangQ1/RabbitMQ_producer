package cn.et;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * 生产者 —— 简单模式
 * @author Administrator
 *
 */
public class Simple_Pub_Email {
	/**
	 * 对象序列化为字节数组
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static byte[] serialize(Object obj) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		return baos.toByteArray();
	}
	/**
	 * 字节数组反序列化为对象
	 * @param b
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object reverseSerialize(byte[] b) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
	}
	/**
	 * 消息被发送的队列 
	 */
	private final static String QUEUE_NAME = "sendMail_queue";
	public static void main(String[] args) throws IOException, TimeoutException {
		//模拟一个任务消息
		Map<String, String> map = new HashMap<String, String>();
		map.put("sendTo", "912161419@qq.com");
		map.put("subject", "rabbitmq test1");
		map.put("content", "注册成功");
		map.put("from", "mr.wang1@aliyun.com");
        /**
         * 连接远程rabbitmq-server服务器
         */
		//新建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //指定主机
        factory.setHost("192.168.48.128");
        //指定端口号
        factory.setPort(5762);
        //通过主机与端口获得连接
        Connection connection = factory.newConnection();
        //通过连接创建消息读写的通道
        final Channel channel = connection.createChannel();
        /**    定义创建一个队列
         * 参数2  true表示将队列和消息持久化到磁盘
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";
        /**   发送消息
         * 第三个参数 持久化文本(MessageProperties.PERSISTENT_TEXT_PLAIN)
         * 注意发送和接受段相同字符集否则容易出现乱码
         */
        channel.basicPublish("", QUEUE_NAME, null, serialize(map));
        System.out.println(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();
	}
}
