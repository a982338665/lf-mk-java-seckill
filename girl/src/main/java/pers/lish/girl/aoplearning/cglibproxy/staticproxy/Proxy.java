package pers.lish.girl.aoplearning.cglibproxy.staticproxy;

/**
 * @author : Mr huangye
 * @URL : CSDN 皇夜_
 * @createTime : 2022/4/10 21:41
 * @Description :
 */
public class Proxy extends AAA {
    @Override
    public void eat() {
        System.err.println("11111");
        super.eat();
        System.err.println("11111");
    }

    @Override
    public void sleep() {
        System.err.println("11111");
        super.sleep();
        System.err.println("11111");
    }
}
