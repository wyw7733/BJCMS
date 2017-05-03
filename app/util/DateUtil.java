package util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zuopanpan on 2016/3/11.
 */
public class DateUtil {
    /**
     * 获取2016年至今年份列表
     *
     * @return
     */
    public static List<String> yearList(){
        List<String> yearList=new ArrayList<>();
        String nowYear = nowYear();
        for(int year=2016;year<Integer.parseInt(nowYear);year++){
            yearList.add(year+"");
        }
        yearList.add(nowYear);
        return yearList;
    }
    /**
     * 获取当前年份
     *
     * @return
     */
    public static String nowYear(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String nowdate=sdf.format(new Date());
        String nowYear=nowdate.substring(0, 4);
        return nowYear;
    }
}
