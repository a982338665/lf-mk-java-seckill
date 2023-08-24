

# 1. MQ使用场景

    流量消峰：使用消息队列做缓冲，把一秒内下的订单分散成一段时间来处理
    应用解耦：
        以电商应用为例，应用中有订单系统、库存系统、物流系统、支付系统。用户创建订单后，如果耦合
        调用库存系统、物流系统、支付系统，任何一个子系统出了故障，都会造成下单操作异常。当转变成基于
        消息队列的方式后，系统间调用的问题会减少很多，比如物流系统因为发生故障，需要几分钟来修复。在
        这几分钟的时间里，物流系统要处理的内存被缓存在消息队列中，用户的下单操作可以正常完成。当物流
        系统恢复后，继续处理订单信息即可，中单用户感受不到物流系统的故障，提升系统的可用性。
    异步处理

# 2. MQ选择

    kafka：日志，大厂
    rocketmq：金融，双11，支付
    rabbitmq：中小公司

# 3.4大核心概念

    生产者
    交换机
    队列
    消费者
    
    每个生产者和mq会建立一个连接，连接里面通过多个信道进行消息投递
    一个borker里面包含多个虚拟主机，每个虚拟主机包含多个交换机+队列
    每个消费者和mq建立一个连接，连接通过多个信道对消息进行消费

# 4.rabbitmq工作模式

    hello：rabbitmq-hello：
        最简单的消息投递与消费，没有申明式交换机，使用默认交换机
        一个生产，一个线程消费
    work queues：rabbitmq-hello：
        一个生产，多个消费者轮询进行消费
    fanout：广播模式
        一个生产：多个消费，每个消费者要将所有的消息都消费一遍

# 5.消息应答

    1.自动应答
    2.手动应答：防止程序执行一半宕机，导致消费失败，一般使用此种方式进行
        A.Channel.basicAck(用于肯定确认)RabbitMQ 已知道该消息并且成功的处理消息，可以将其丢弃了，一般都使用单个 应答，而不使用批量应答
            Channel.basicAck(deliveryTag,true);第一个参数为为当前收到的消息，第二个参数表示是否开启批量应答：
                true 代表批量应答 channel 上未应答的消息
                    比如说 channel 上有传送 tag 的消息 5,6,7,8 当前 tag 是 8 那么此时
                    5-8 的这些还未应答的消息都会被确认收到消息应答
                false 同上面相比
                    只会应答 tag=8 的消息 5,6,7 这三个消息依然不会被确认收到消息应答
        B.Channel.basicNack(用于否定确认)
        C.Channel.basicReject(用于否定确认)与 Channel.basicNack 相比少一个参数,不处理该消息了直接拒绝，可以将其丢弃了
    3.消息自动重新入队
        未ack的消息会重新入队
    4.持久化队列：
        如果已经存在没有持久化的队列，那么直接创建就会报错，需要先删除再创建
        //第三个参数 让消息实现持久化，将消息标记为持久化并不能完全保证不会丢失消息。尽管它告诉 RabbitMQ 将消息保存到磁盘，但是
            //这里依然存在当消息刚准备存储在磁盘的时候 但是还没有存储完，消息还在缓存的一个间隔点。此时并没
            //有真正写入磁盘。持久性保证并不强，但是对于我们的简单任务队列而言，这已经绰绰有余了。如果需要
            //更强有力的持久化策略，参考后边课件发布确认章节
                channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
    5.预取值
        当 prefetchCount设置为1的时候为不公平分发，
        >=1的时候为预取值，等同于线程池一样，例如prefetchCount=5，表示最多能预分5条数据，等待应答，若其中一条ack了，则此时还能在进一条继续等待。
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);
    6.发布确认：-如何保证消息一定是存储成功的
        1.设置要求队列必须持久化
            pers.li.$3messageAsk.Task02
                //第二个参数修改为true，使消息队列持久化，如果队列存在，则需要先删除再执行该代码
                channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        2.设置队列中的消息必须持久化
            pers.li.$3messageAsk.Task02
                //第三个参数
                channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
        3.发布确认，需要等待mq回执说明消息已经成功保存到磁盘
            开启发布确认：
                try (Channel channel = RabbitMqUtils.getChannel()) {
                //开启确认发布
                channel.confirmSelect();
            发布确认的三种方式：
                单个确认发布-同步确认：
                批量确认发布:
                    优点：上面那种方式非常慢，与单个等待确认消息相比，先发布一批消息然后一起确认可以极大地
                            提高吞吐量
                    缺点：当发生故障导致发布出现问题时，不知道是哪个消息出现问题了，我们必须将整个批处理保存在内存中，
                        以记录重要的信息而后重新发布消息。当然这种方案仍然是同步的，也一样阻塞消息的发布
                异步确认发布：速度最快，回调实现，并且可以确定哪个投递失败，常用
    7.交换机的作用
            生产者生产的消息从不会直接发送到队列
            生产者只能将消息发送到交换机(exchange)
            它接收来自生产者的消息，另一方面将它们推入队列
            交换机也会依据他自身的类型，来选择是将消息推到特定队列，还是多个队列还是丢弃消息
        默认的交换机：仅支持一个消费者
        交换机类型：
            直接(direct)： 路由
            主题(topic) ：
            标题(headers) ：
            扇出(fanout)：发布订阅
            无名类型：默认类型：代码为空串
                channel.basicPublish("", queueName, null, message.getBytes());
                消息能路由发送到队列中其实是由 routingKey(bindingkey)绑定 key 指定的，如果它存在的话         
    8.临时队列：
        创建临时队列的方式如下:String queueName = channel.queueDeclare().getQueue();
    9.绑定：
        什么是 bingding 呢，binding 其实是 exchange 和 queue 之间的桥梁，它告诉我们 exchange 和那个队
        列进行了绑定关系。比如说下面这张图告诉我们的就是 X 与 Q1 和 Q2 进行了绑定
# 6.
