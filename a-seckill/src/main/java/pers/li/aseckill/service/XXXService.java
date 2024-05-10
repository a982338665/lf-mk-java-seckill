package pers.li.aseckill.service;

import com.sun.jnlp.ClipboardServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import pers.li.aseckill.entity.SOrderInfo;
import pers.li.aseckill.entity.SUser;
import pers.li.aseckill.vo.SGoodsVo;

/**
 * @author:luofeng
 * @createTime : 2018/10/10 9:31
 */
@Service
public class XXXService {

    @Transactional(rollbackFor = Exception.class)
    public SOrderInfo seckill(SUser user, SGoodsVo goods) {
        //before增强=======
        //begin =====
        //==========================================
        try{
//            没有事务满足不
            //update 01  success 01加钱     80

            //1查询李四账号当前金额  100
            //2.转账给李四 50
            //3.set到数据库 150

            //1.查询李四账号当前金额  100
            //2.转账给李四 50 20 10
            //3.set到数据库 总计180，但是多线程下 可能是 150 可能是120 可能是110


            //1.查询李四账号当前金额  100
                select jine,version from A where name=lisi;
                version 1
                update a set jine=150,num=num+1,version=version+1 where name=lisi;
                update a set jine=120,num=num-1,version=version+1 where name=lisi;
                update a set jine=110,num=num+1,version=version+1 where name=lisi;



            //2.转账给李四 50 20 10
            //3.set到数据库 总计180，但是多线程下 可能是 150 可能是120 可能是110


            //天使博的时候，添加老师，学生，学校的接口一个大接口
            //部署三台ng转发轮询
            //单个服务的逻辑：







            //update 02  fail    02减钱     90
            //update 03
        }catch (Exception e){
            throw new RuntimeException();
        }
        return new SOrderInfo();
        //==========================================
        //after增强============
    }




}
