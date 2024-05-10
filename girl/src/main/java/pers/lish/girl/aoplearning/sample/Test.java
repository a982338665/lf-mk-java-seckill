package pers.lish.girl.aoplearning.sample;

/**
 * @author : Mr huangye
 * @URL : CSDN 皇夜_
 * @createTime : 2022/4/7 23:25
 * @Description :
 */
public class Test {
    public static void main(String[] args) {
        Mao mao = new Mao();
        LI li = new LI(mao);
        li.eat();
        li.high();

        Lii ll = new Lii();
        ll.eat();
        ll.night();
    }
}
