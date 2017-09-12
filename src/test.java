import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class test {

	
	/**
	 * 从html中获取出css文本
	 * 参考extractCss网站
	 * 借助于Js
	 * @param html    含有style格式的html文本
	 * @return        提取出的css文本
	 * @throws IOException 
	 */
	public static String extractCss(String html) throws IOException{
		Set<String> allClasses = new HashSet<String>();   // 初始化参数
		StringBuffer resultCss = new StringBuffer();
		
		File file = new File("C:\\Users\\Administrator\\Desktop\\Desktop\\2016年统计局数据\\chm\\1\\cn1-7.htm");
        Document doc = Jsoup.parse(file, "gb2312");
		Elements classes = doc.select("*[class]");
		for(Iterator<Element> ite = classes.iterator(); ite.hasNext();){
			Element element = ite.next();
			String splits[] = element.attr("class").split(" ");
			for(int i=0; i< splits.length; i++){
				allClasses.add(splits[i]);
				if(i==splits.length-1){
					Elements children = doc.select("."+splits[i]+"> *");
					for(Iterator<Element> tor = children.iterator(); tor.hasNext();){
						Element e = tor.next();
						if(!e.hasAttr("class")){                   //如果没有定义class属性
							allClasses.add(splits[i]+" > "+e.tagName().toLowerCase());
						}
					}
				}
			}
		}
		for(String clss: allClasses){
			Elements element = doc.select("."+clss);
			if(element.hasAttr("style")){
				resultCss.append("."+clss+"{"+element.attr("style")+"}");
			}
		}
		return resultCss.toString();
	}
	public static void main(String[] args) throws Exception {
		System.out.println(extractCss(""));
	}

}
