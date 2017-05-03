package controllers;



import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;


/**
 * Created by zuopanpan on 2016/2/24.
 */
public class VerifyCodeAction extends Controller {
    public static final int WIDTH = 130;
    public static final int HEIGHT = 40;
    public static  String test;
    /**
     *@Description: 生成验证码
     * @return
     */
    public static Result verifyCode(){
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        /** 1.设置背景色*/
        setBackGround(g);
        /** 2.设置边框*/
        setBorder(g);
        /** 3.画干扰线*/
        drawRandLine(g);
        /** 4.写随机数*/
        String random = drawRandNum((Graphics2D) g);
        test = random;
        //session("verifyCode", random);
        /** 5.图形写给浏览器*/
        Http.Response response=response();
        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = new ByteArrayInputStream(out.toByteArray());
        return  ok(is);
    }
    /**
     * @Description: 设置背景色
     * @param g
     * @return
     */
    private static void setBackGround(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }
    /**
     * @Description: 设置边框
     * @param g
     * @return
     */
    private static void setBorder(Graphics g) {
        g.setColor(new Color(225,225,225));
        g.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);
    }
    /**
     * @Description: 画干扰线
     * @param g
     * @return
     */
    private static void drawRandLine(Graphics g) {
        g.setColor(Color.green);
        for (int i = 0; i < 15; i++) {
            int x1 = new Random().nextInt(WIDTH);
            int y1 = new Random().nextInt(HEIGHT);
            int x2 = new Random().nextInt(WIDTH);
            int y2 = new Random().nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }
    }
    /**
     * @Description: 写随机数
     * @param g
     * @return
     */
    private static String drawRandNum(Graphics2D g) {
        g.setFont(new Font("宋体", Font.BOLD, 30));// 宋体。粗体。size=30；
        int m = 10; // m为字体与左边的距离
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            int radom=new Random().nextInt(4)+0;//产生0~3随机整数
            String str="";
            switch (radom){
                case 0:str=randomNum();//获取0~9随机整数
                    g.setColor(Color.blue);
                    break;
                case 1:str=randomLetter(65);//获取大写随机字母
                    g.setColor(Color.black);
                    break;
                case 2:str=randomLetter(97);//获取小写随机字母
                    g.setColor(Color.MAGENTA);
                    break;
                default:str=randomLetter(65);//获取大写随机字母
                    g.setColor(Color.red);
                    break;
            }
            int hudu = new Random().nextInt() % 30;// 取-30~30之内整数
            g.rotate(hudu * Math.PI / 180, m, 30);// 字体旋转
            g.drawString(str, m, 30);
            g.rotate(-hudu * Math.PI / 180, m, 30);// 回归原点
            m += 30;
            sb.append(str);
        }
        return sb.toString();
    }
    /**
     * @Description: 获取0~9随机整数
     * @param
     * @return
     */
    public static String randomNum(){
        int num =new Random().nextInt(10)+0;
        return num+"";
    }
    /**
     * @Description: 获取大小写随机字母
     * @param
     * @return
     */
    public static String randomLetter(int first){
        int num =new Random().nextInt(26)+first;
        return (char)num+"";
    }
}
