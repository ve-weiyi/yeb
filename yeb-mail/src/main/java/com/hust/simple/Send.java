package com.hust.simple;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 智者乐山仁者乐水●程序员乐字节 第 49／76页
 上海市浦东新区汇通南园文化创意园
 * topic主题模式队列-消息发送者
 */
public class Send {
    // 队列名称
    // 如果不声明队列，会使用默认值，RabbitMQ会创建一个排他队列，连接断开后自动删除
    // 交换机名称
    private static final String EXCHANGE_NAME = "exchange_topic";
    public static void main(String[] args) {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("shop");
        factory.setPassword("shop");
        factory.setVirtualHost("/shop");
        Connection connection = null;
        Channel channel = null;
        try {
            // 通过工厂创建连接
            connection = factory.newConnection();
            // 获取通道
            channel = connection.createChannel();
            // 绑定交换机 topic：主题模式
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            // 创建消息，模拟商品模块
            String message = "商品查询操作";
            //String message = "商品更新操作";
            // 设置路由routingKey
            String routingKey = "select.goods.byId";
            //String routingKey = "update.goods.byId.andName";
            // 将产生的消息发送至交换机
            channel.basicPublish(EXCHANGE_NAME, routingKey, null,
                    message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭通道
                if (null != channel && channel.isOpen()) {
                    channel.close();
                }
                // 关闭连接
                if (null != connection && connection.isOpen()) {
                    connection.close();
                }
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}