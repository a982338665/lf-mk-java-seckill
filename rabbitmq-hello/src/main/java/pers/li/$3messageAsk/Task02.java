package pers.li.$3messageAsk;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import pers.li.utils.RabbitMqUtils;

import java.util.Scanner;

public class Task02 {
    private static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] argv) throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {
//            第二个参数修改为true，使消息队列持久化，如果队列存在，则需要先删除再执行该代码
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入信息");
            while (sc.hasNext()) {
                String message = sc.nextLine();
            //第三个参数 让消息实现持久化，将消息标记为持久化并不能完全保证不会丢失消息。尽管它告诉 RabbitMQ 将消息保存到磁盘，但是
            //这里依然存在当消息刚准备存储在磁盘的时候 但是还没有存储完，消息还在缓存的一个间隔点。此时并没
            //有真正写入磁盘。持久性保证并不强，但是对于我们的简单任务队列而言，这已经绰绰有余了。如果需要
            //更强有力的持久化策略，参考后边课件发布确认章节
                channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
                System.out.println("生产者发出消息" + message);
            }
        }
    }
}
