package pers.lish.girl.aoplearning.sample;

/**
 * @author : Mr huangye
 * @URL : CSDN 皇夜_
 * @createTime : 2022/4/7 23:23
 * @Description :
 */
public class LI {

    private Mao mao;

    public LI(Mao mao) {
        this.mao = mao;
    }

    public void eat(){
        System.err.println(1);
        System.err.println("先收拾");
        mao.eat();
        System.err.println("最后回家");
        System.err.println(1);
    }

    public void high(){
        System.err.println(1);
        System.err.println("high前");
        mao.night();
        System.err.println("high后");
        System.err.println(1);
    }


}
