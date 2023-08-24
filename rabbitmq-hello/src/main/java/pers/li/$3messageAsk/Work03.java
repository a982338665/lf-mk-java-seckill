package pers.li.$3messageAsk;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import pers.li.utils.RabbitMqUtils;

public class Work03 {
    private static final String ACK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1 等待接收消息处理时间较短");
        //消息消费的时候如何处理消息
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            SleepUtils.sleep(1);
            System.out.println("接收到消息:" + message);
            /**
             * 1.消息标记 tag
             * 2.是否批量应答未应答消息，false表示只应答当前，true表示应答当前及之前
             * Channel.basicNack(用于否定确认)
             * Channel.basicReject(用于否定确认)与 Channel.basicNack 相比少一个参数,不处理该消息了直接拒绝，可以将其丢弃了
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        //设置不公平分发，常用此种方式
//        int prefetchCount = 1;
//        预取值，
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);
        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(ACK_QUEUE_NAME, autoAck, deliverCallback, (consumerTag) -> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        });
    }
}
