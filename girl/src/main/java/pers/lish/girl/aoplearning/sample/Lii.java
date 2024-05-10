package pers.lish.girl.aoplearning.sample;

/**
 * @author : Mr huangye
 * @URL : CSDN 皇夜_
 * @createTime : 2022/4/15 23:20
 * @Description :
 */
public class Lii extends Mao {

    @Override
    public void eat() {
        System.err.println("============");
        super.eat();
        System.err.println("============");
    }

    @Override
    public void night() {
        System.err.println("============");
        super.night();
        System.err.println("============");
    }
}
