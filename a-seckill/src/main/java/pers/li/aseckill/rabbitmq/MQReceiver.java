package pers.li.aseckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.li.aseckill.entity.SOrderInfo;
import pers.li.aseckill.entity.SOrderSeckill;
import pers.li.aseckill.entity.SUser;
import pers.li.aseckill.redis.RedisService;
import pers.li.aseckill.result.CodeMsg;
import pers.li.aseckill.result.Result;
import pers.li.aseckill.service.SGoodService;
import pers.li.aseckill.service.SOrderService;
import pers.li.aseckill.service.SeckillService;
import pers.li.aseckill.utils.Convert;
import pers.li.aseckill.vo.SGoodsVo;

/**
 * create by lishengbo 2019/5/27
 */
@Service
@Slf4j
public class MQReceiver {

    @Autowired
    SGoodService sGoodService;
    @Autowired
    SOrderService sOrderService;
    @Autowired
    SeckillService seckillService;
    @Autowired
    RedisService redisService;

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receiver(String msg) {
        log.info("receive ======》"+msg);
    }

    @RabbitListener(queues = MQConfig.TOP_QUEUE1)
    public void receiverTopic1(String msg) {
        log.info("receive topic ======》"+msg);
    }

    @RabbitListener(queues = MQConfig.TOP_QUEUE2)
    public void receiverTopic2(String msg) {
        log.info("receive topic ======》"+msg);
    }
    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void header(byte[] msg) {
        log.info("receive topic ======》"+new String(msg));
    }

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receiverSeckill(String msg) {
        SeckillMessage seckillMessage = Convert.stringToBean(msg, SeckillMessage.class);
        SUser user = seckillMessage.getSUser();
        long goodsId = seckillMessage.getGoodsId();
        //mysql ：判断库存//10个商品，req1 req2 --
        SGoodsVo goods = sGoodService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getGoodsStock();
        if (stock <= 0) {
            return ;
        }
        //判断是否已经秒杀到了
        SOrderSeckill orderSeckill = sOrderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (orderSeckill != null) {
            return ;
        }
        //减库存 下订单 写入秒杀订单
        SOrderInfo orderInfo = seckillService.seckillMQ(user, goods);
        log.info("receive ======》"+msg);
    }
}
