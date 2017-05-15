package cn.flying.rest.onlinefile.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 字符串工具类
 * @author longjunhao 20150324
 *
 */
public class StringUtil {
    
    /**
     * 如果str为null，则返回空字符串
     * @param str
     * @return
     */
    public static String string(String str) {
        if (null == str) {
            str = "";
        }
        return str;
    }
    
    /**
     * 将list<HashMap<Object, Object>> 根据Map 指定key 进行整合排序
     * @param  list
     * @param  key
     * xyc
     * */
    public static List<HashMap<String, String>> listMapSortByKeys(List<HashMap<String, String>> obj, final String key) {
    	List<HashMap<String, String>> returnList = new ArrayList<HashMap<String, String>>(); 
    	 Collections.sort(obj, new Comparator<HashMap<String, String>>() {
	            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
	                return Integer.valueOf(o1.get(key))  > Integer.valueOf(o2.get(key))? (Integer.valueOf(o1.get(key)) == Integer.valueOf(o2.get(key)) ? 0 : -1) : 1;
	            }
	        });
	        returnList.addAll(obj);
	        return returnList;
    }
    
    /**
     * 将list转化为string类型，使用指定分隔符分隔
     * @param stringList
     * @param separator
     * @return
     * 
     */
    public static String list2String(List<String> stringList, String separator) {
        if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(separator);
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }
    
    /**
     * 将集合转为
     * @param stringList
     * @param separator
     * @param separator2
     * @return
     */
    public static String list2String2(List<String> stringList, String separator,String separator2) {
        if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(separator2);
            }else {
                flag=true;
            }
            result.append(separator).append(string).append(separator);
        }
        return result.toString();
    }
    
   /**
    * xiewenda 20150728判断一个字符串能否转换为日期 
    * @param str
    * @return
    */
   public static boolean isValidDate(String str) {
           boolean convertSuccess=true;
             // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
             SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
             try {
               // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
                format.setLenient(false);
                format.parse(str);
            } catch (Exception e) {
                // e.printStackTrace();
                // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
                 convertSuccess=false;
            } 
             return convertSuccess;
      }
   /**
    * xiewenda 判断文件类型 是否将内容加入索引
    * @param fileType
    * @return
    */
   public static boolean isValidFileTypeForIndex(String fileType) {
     boolean flag = true;
     fileType = fileType == null ? "" : fileType.toLowerCase();
     switch (fileType) {
       case "txt":break;
       case "doc":break;
       case "docx":break;
       case "xls":break;
       case "xlsx":break;
       case "ppt":break;
       case "pptx":break;
       case "pdf": break;
       default:flag = false;
         break;
     }
     return flag;
   }
   /**
    * 按字节截取utf-8字符串 不出现乱码 
    * @param src 字符串
    * @param len 截取多少字节数
    * @return
    */
   public static String splitStringBySize(String src, int len)  {  
     String s = "";
     try {
     byte[] buf = src.getBytes("utf-8");
     
     if(len>buf.length) len = buf.length;
     int count = 0;  
     
     while(buf[len-count-1]<0){
           count++;
           if(count>=len || count>2){ count=len; break;}
     }
     //utf-8 三个字节一个字
     s = new String(buf,0,len-(count%3),"utf-8");
     } catch (Exception e) {
       e.printStackTrace();
     }   
     return s;
   }
   /**
    * xyc  201507 
    * 输出request所有参数及值
    * */
   public static String getRequerstVal(HttpServletRequest request){
   	Iterator iterator = request.getParameterMap().entrySet().iterator(); 
   	   StringBuffer param = new StringBuffer();
   	   int i = 0;
   	   while (iterator.hasNext()) {  
   	    i++;
   	       Entry entry = (Entry) iterator.next(); 
   	       if(i == 1)
   	            param.append("?").append(entry.getKey()).append("="); 
   	       else
   	         param.append("&").append(entry.getKey()).append("=");
   	       if (entry.getValue() instanceof String[]) {  
   	           param.append(((String[]) entry.getValue())[0]);  
   	       } else {  
   	           param.append(entry.getValue());  
   	       }  
   	   }
		return param.toString();
   }
   /**
	 * xyc  201508 返回具体图片路径
	 * @author 
	 * @param String
	 * @param Path
	 * @return Strin
	 */
	public static String getGifImg(String ImgPath,String s){
		Pattern pattern = Pattern.compile("\\[.{1,3}\\]");
		Matcher matcher = pattern.matcher(s);
		StringBuffer sb=new StringBuffer();
		while (matcher.find()) {
			String ddString=getStrGifImg(ImgPath,matcher.group());
			matcher.appendReplacement(sb,ddString);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	public static String getStrGifImg(String ImgPath,String str){
		String strImg="";
		switch (str) {
		case "[呲牙]":
			str="f000.gif";
			break;
		case "[调皮]":
			str="f001.gif";
			break;
		case "[流汗]":
			str="f002.gif";
			break;
		case "[偷笑]":
			str="f003.gif";
			break;
		case "[再见]":
			str="f004.gif";
			break;
		case "[敲打]":
			str="f005.gif";
			break;
		case "[擦汗]":
			str="f006.gif";
			break;
		case "[猪头]":
			str="f007.gif";
			break;
		case "[玫瑰]":
			str="f008.gif";
			break;
		case "[流泪]":
			str="f009.gif";
			break;
		case "[大哭]":
			str="f010.gif";
			break;
		case "[嘘]":
			str="f011.gif";
			break;
		case "[酷]":
			str="f012.gif";
			break;
		case "[抓狂]":
			str="f013.gif";
			break;
		case "[委屈]":
			str="f014.gif";
			break;
		case "[便便]":
			str="f015.gif";
			break;
		case "[炸弹]":
			str="f016.gif";
			break;
		case "[菜刀]":
			str="f017.gif";
			break;
		case "[可爱]":
			str="f018.gif";
			break;
		case "[色]":
			str="f019.gif";
			break;
		case "[害羞]":
			str="f020.gif";
			break;
		case "[得意]":
			str="f021.gif";
			break;
		case "[吐]":
			str="f022.gif";
			break;
		case "[微笑]":
			str="f023.gif";
			break;
		case "[发怒]":
			str="f024.gif";
			break;
		case "[尴尬]":
			str="f025.gif";
			break;
		case "[惊恐]":
			str="f026.gif";
			break;
		case "[冷汗]":
			str="f027.gif";
			break;
		case "[爱心]":
			str="f028.gif";
			break;
		case "[亲吻]":
			str="f029.gif";
			break;
		case "[白眼]":
			str="f030.gif";
			break;
		case "[傲慢]":
			str="f031.gif";
			break;
		case "[难过]":
			str="f032.gif";
			break;
		case "[惊讶]":
			str="f033.gif";
			break;
		case "[疑问]":
			str="f034.gif";
			break;
		case "[睡觉]":
			str="f035.gif";
			break;
		case "[亲亲]":
			str="f036.gif";
			break;
		case "[憨笑]":
			str="f037.gif";
			break;
		case "[爱情]":
			str="f038.gif";
			break;
		case "[衰]":
			str="f039.gif";
			break;
		case "[撇嘴]":
			str="f040.gif";
			break;
		case "[阴险]":
			str="f041.gif";
			break;
		case "[奋斗]":
			str="f042.gif";
			break;
		case "[发呆]":
			str="f043.gif";
			break;
		case "[右哼哼]":
			str="f044.gif";
			break;
		case "[拥抱]":
			str="f045.gif";
			break;
		case "[坏笑]":
			str="f046.gif";
			break;
		case "[飞吻]":
			str="f047.gif";
			break;
		case "[鄙视]":
			str="f048.gif";
			break;
		case "[晕]":
			str="f049.gif";
			break;
		case "[大兵]":
			str="f050.gif";
			break;
		case "[可怜]":
			str="f051.gif";
			break;
		case "[强]":
			str="f052.gif";
			break;
		case "[弱]":
			str="f053.gif";
			break;
		case "[握手]":
			str="f054.gif";
			break;
		case "[胜利]":
			str="f055.gif";
			break;
		case "[抱拳]":
			str="f056.gif";
			break;
		case "[凋谢]":
			str="f057.gif";
			break;
		case "[饭]":
			str="f058.gif";
			break;
		case "[蛋糕]":
			str="f059.gif";
			break;
		case "[西瓜]":
			str="f060.gif";
			break;
		case "[啤酒]":
			str="f061.gif";
			break;
		case "[飘虫]":
			str="f062.gif";
			break;
		case "[勾引]":
			str="f063.gif";
			break;
		case "[OK]":
			str="f064.gif";
			break;
		case "[爱你]":
			str="f065.gif";
			break;
		case "[咖啡]":
			str="f066.gif";
			break;
		case "[钱]":
			str="f067.gif";
			break;
		case "[月亮]":
			str="f068.gif";
			break;
		case "[美女]":
			str="f069.gif";
			break;
		case "[刀]":
			str="f070.gif";
			break;
		case "[发抖]":
			str="f071.gif";
			break;
		case "[差劲]":
			str="f072.gif";
			break;
		case "[拳头]":
			str="f073.gif";
			break;
		case "[心碎]":
			str="f074.gif";
			break;
		case "[太阳]":
			str="f075.gif";
			break;
		case "[礼物]":
			str="f076.gif";
			break;
		case "[足球]":
			str="f077.gif";
			break;
		case "[骷髅]":
			str="f078.gif";
			break;
		case "[挥手]":
			str="f079.gif";
			break;
		case "[闪电]":
			str="f080.gif";
			break;
		case "[饥饿]":
			str="f081.gif";
			break;
		case "[困]":
			str="f082.gif";
			break;
		case "[咒骂]":
			str="f083.gif";
			break;
		case "[折磨]":
			str="f084.gif";
			break;
		case "[抠鼻]":
			str="f085.gif";
			break;
		case "[鼓掌]":
			str="f086.gif";
			break;
		case "[糗大了]":
			str="f087.gif";
			break;
		case "[左哼哼]":
			str="f088.gif";
			break;
		case "[哈欠]":
			str="f089.gif";
			break;
		case "[快哭了]":
			str="f090.gif";
			break;
		case "[吓]":
			str="f091.gif";
			break;
		case "[篮球]":
			str="f092.gif";
			break;
		case "[乒乓球]":
			str="f093.gif";
			break;
		case "[NO]":
			str="f094.gif";
			break;
		case "[跳跳]":
			str="f095.gif";
			break;
		case "[怄火]":
			str="f096.gif";
			break;
		case "[转圈]":
			str="f097.gif";
			break;
		case "[磕头]":
			str="f098.gif";
			break;
		case "[回头]":
			str="f099.gif";
			break;
		case "[跳绳]":
			str="f0100.gif";
			break;
		case "[激动]":
			str="f0101.gif";
			break;
		case "[街舞]":
			str="f0102.gif";
			break;
		case "[献吻]":
			str="f0103.gif";
			break;
		case "[左太极]":
			str="f0104.gif";
			break;
		case "[右太极]":
			str="f0105.gif";
			break;
		case "[闭嘴]":
			str="f0106.gif";
			break;
		default:
			strImg=str;
			break;
		}
		if(str.contains(".gif")){
			strImg="<img src=\""+ImgPath+str+"\" style=\"height:24px;width:24px;\">";
		}
		return strImg;
	}
	
	/**
	 * xiewenda
	 * 字符串时间装换为时间long型
	 */
	public static Long dateStringFormatLong(String dateStr,String format){
	    Date date =  new Date();
	    if(dateStr==null || "".equals(dateStr)) return date.getTime();
	    if(format==null || "".equals(format)) format = "yyyy-MM-dd HH:mm:ss";
	    SimpleDateFormat formatter=new SimpleDateFormat(format);  
	    try {
        date=formatter.parse(dateStr);
      } catch (Exception e) {
        e.printStackTrace();
      }  
	  return date.getTime();
	}
	
	/**
     * xiewenda
     * 时间long型转换为时间字符串
     */
    public static String dateLongFormatString(Long dateLong,String format){
        String dateTime =  "";
        if(format==null || "".equals(format)) format = "yyyy-MM-dd HH:mm:ss";
        if(dateLong==null || "".equals(dateLong)) return "";
        SimpleDateFormat formatter=new SimpleDateFormat(format);  
        try {
          dateTime=formatter.format(dateLong);
        } catch (Exception e) {
          e.printStackTrace();
        }  
      return dateTime;
    }
}
