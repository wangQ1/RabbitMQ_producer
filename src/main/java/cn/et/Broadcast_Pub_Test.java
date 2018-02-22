package cn.et;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
/**
 * 生产者 —— 广播模式
 * @author Administrator
 *
 */
public class Broadcast_Pub_Test {

	/**  
     * 交换器名称  不允许使用  *.*格式
     */  
    private static final String EXCHANGE_NAME = "inform";
    public static void main(String[] args) throws Exception {
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
        //定义创建一个交换器 参数1 交换器名称  参数2 交换器类型   参数3 表示将交换器信息持久化  永久保存在服务器磁盘上  关闭rabbitmqserver也不会丢失
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
        String message = null;
        //同时发送10条消息
        for(int i = 0; i < 10; i++){
            message = "发送第"+i+"消息";
            //将消息发送到交换器    第二个参数就是 routingkey  不填   默认会转发给所有的订阅者队列
            channel.basicPublish(EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));  
        }
        System.out.println(" [x] Sent 10 message");
        channel.close();
        connection.close();
    }
}
