package cn.et;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * 生产者 —— 工作队列模式
 * @author Administrator
 *
 */
public class Work_Pub_Test {

	/**
	 * 消息被发送的队列 
	 */
	private final static String QUEUE_NAME = "work_queue";
	public static void main(String[] args) throws IOException, TimeoutException {
		/**
         * 连接远程rabbitmq-server服务器
         */
		//新建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //指定主机
        factory.setHost("192.168.48.128");
        //指定端口号
        factory.setPort(5672);
        //通过主机与端口获得连接
        Connection connection = factory.newConnection();
        //通过连接创建消息读写的通道
        final Channel channel = connection.createChannel();
        /**    定义创建一个队列
         * 参数2  true表示将队列和消息持久化到磁盘
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);;
        //同时发送10条消息
        for(int i = 0; i < 10; i++){
        	String message = "发送第" + i + "消息";
            /**   发送消息
             * 第三个参数 持久化文本(MessageProperties.PERSISTENT_TEXT_PLAIN)
             * 注意发送和接受段相同字符集否则容易出现乱码
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        }
        System.out.println(" [x] Sent 10 message");
        channel.close();
        connection.close();
	}

}
