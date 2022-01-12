package com.hust.simple;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 * topic主题模式队列-消息接收者
 */
public class Recv01 {
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
        try {
            // 通过工厂创建连接
            final Connection connection = factory.newConnection();
            // 获取通道
            final Channel channel = connection.createChannel();
            // 绑定交换机 topic：主题模式
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            // 获取队列名称
            String queueName = channel.queueDeclare().getQueue();
            // 设置路由routingKey
            String routingKey = "select.goods.*";
            // 绑定队列
            channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
            /*
                限制RabbitMQ只发不超过1条的消息给同一个消费者。
                当消息处理完毕后，有了反馈，才会进行第二次发送。
             */
            int prefetchCount = 1;
            channel.basicQos(prefetchCount);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            // 获取消息，按|分割以后一个消费者发短信，一个消费者发邮件
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received01 '" + message + "'");
                // 手动回执消息
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),
                        false);
            };
            // 监听队列
            /*
                autoAck = true代表自动确认消息
                autoAck = false代表手动确认消息
             */
            boolean autoAck = false;
            channel.basicConsume(queueName, autoAck, deliverCallback,
                    consumerTag -> {
                    });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}