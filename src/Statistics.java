import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Statistics {
	public static void main(String[] args) throws Exception {
		File file = new File("C:\\Users\\Administrator\\Desktop\\Desktop\\2016年统计局数据\\chm\\16\\cn16-7.htm");
        Document doc = Jsoup.parse(file, "gb2312");
        
        
        
        Elements styles = doc.select("style");
        DataNode style = ((DataNode)(styles.get(0).childNode(0)));
        String styleStr = style.getWholeData().trim().replaceAll("\n", "").replaceAll("\r", "");
        
        if(styleStr.startsWith("<!--")){
        	styleStr = styleStr.replaceFirst("<!--", "");
        }
        if(styleStr.endsWith("-->")){
        	styleStr = styleStr.substring(0,styleStr.length()-3);
        }
        String[] css01 =  styleStr.split("}");
        HashMap<String,HashMap<String,String>> css = new HashMap<String,HashMap<String,String>>();
        for(int i=0;i<css01.length;i++){
        	String[] css0102 = css01[i].split("\\{");
        	String key = css0102[0].replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "").replaceAll(" ", "");
        	css0102[1] = css0102[1].replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "");
        	String[] css010203 = css0102[1].split(";");
        	HashMap<String,String> cssTable = new HashMap<String,String>();
        	for(int j=0;j<css010203.length;j++){
        		String subKey = css010203[j].split(":")[0].replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "").replaceAll(" ", "");
        		String subData = css010203[j].split(":")[1].replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "");
        		cssTable.put(subKey, subData);
        	}
        	css.put(key, cssTable);
        }
        
        System.out.println(styleStr);
        
        Elements eles = doc.select("table");
        Element table = eles.get(0);
        Element colgroup = null;
        Element tbody = null;
        for(int i=0;i<table.childNodeSize();i++){
        	Object o = table.childNode(i);
        	if(o.getClass() == Element.class){
        		Element e = (Element)o;
        		System.out.println(e.tagName());
        		if("colgroup".equals(e.tagName())){
        			colgroup = e;
        		}else if("tbody".equals(e.tagName())){
        			tbody = e;
        		}
        	}
        }
        
        int colCount = 0;
        for(int j=0;j<colgroup.childNodeSize();j++){
        	Object o = colgroup.childNode(j);
        	if(o.getClass() == Element.class){
        		Element e = (Element)o;
        		if("col".equals(e.tagName())){
        			if(e.hasAttr("span")){
        				colCount = colCount + Integer.valueOf(e.attr("span"));
        			}else{
        				colCount++;
        			}
        		}
        	}
        }
        System.out.println("总列数为："+colCount);
        
        int splitIndex = -1;
        int remarkIndex = -1;

        // 标题
        String title = "";
        // 单位
        String unit = "";
        // 备注
        String remark = "";
        
        
        // 列名
        String [] colStrArray = new String [colCount];
        
        for(int i=0;i<colStrArray.length;i++){
        	colStrArray[i] = "";
        }
        Elements trs = tbody.select("tr");
        // 行名
        List<String> rowStrList = new ArrayList<String>();
        // 数据列表
        List<String []> dataList = new ArrayList<String []>();
        
        int splitinsexNumber = 0;
        for(int k=0;k<trs.size();k++){
        	Element tr = trs.get(k);
			Elements tds = tr.select("td");
			if(tr.hasAttr("height") && "5".equals(tr.attr("height"))){
				if(tds.size() < colCount){
					// 只考虑一个td和两个td的情况，其他的不是分隔线
					splitinsexNumber = k;//k之上的是title，之下的是数据
					break;
				}
			}
        }
        System.out.println(splitinsexNumber-2);
        String [][] colStrArray2 = new String [splitinsexNumber-2][colCount];
        for(int i=0;i<colStrArray2.length;i++){
        	for(int j=0;j<colCount;j++){
        		colStrArray2[i][j]="";
        	}
        }
        int titleRownum = 0;
        int titleRow = -1;
        
        for(int k=0;k<trs.size();k++){
        	Element tr = trs.get(k);
			Elements tds = tr.select("td");
			boolean isLine = false;
			if(tr.hasAttr("height") && "5".equals(tr.attr("height"))){
				isLine = true;
				if(tds.size() < colCount){
					// 只考虑一个td和两个td的情况，其他的不是分隔线
					splitIndex = k;//k之上的是title，之下的是数据
				}else if(-1 != splitIndex ){
					// 表格最后的线，下面是备注
					remarkIndex = k;
				}
			}
			
			if(k==0){
				for(int tdi=0;tdi<tds.size();tdi++){
					title = title + getTextFromHtml(tds.get(tdi).text());
				}
				System.out.println("这是标题，标题内容是："+title);
			}else if(k==1){
				for(int tdi=0;tdi<tds.size();tdi++){
					unit = unit + getTextFromHtml(tds.get(tdi).text());
				}
				System.out.println("这是单位，单位内容是："+unit);
			}else if(isLine){
				if(k == splitIndex){
					System.out.println("这是数据分隔线");
				}else if(k == remarkIndex){
					System.out.println("这是备注分隔线");
				}else{
					System.out.println("这是线");
				}
			}else if(-1 == splitIndex){
				System.out.println("这是标题");
				
				String preTdValue = "";
				for(int tdi=0,arrayi=0;tdi<tds.size();tdi++){
					Element td = tds.get(tdi);
					while(!StringUtil.isBlank(colStrArray2[titleRownum][arrayi])){
						arrayi++;
					}
					if(td.hasAttr("colspan") && Integer.valueOf(td.attr("colspan"))>1 ){
						for(int ci=0;ci<Integer.valueOf(td.attr("colspan"));ci++){
							colStrArray2[titleRownum][arrayi] = getTextFromHtml(tds.get(tdi).text());
							
							if(td.hasAttr("rowspan") && Integer.valueOf(td.attr("rowspan"))>1 ){
								for(int cj=1;cj<Integer.valueOf(td.attr("rowspan"));cj++){
									colStrArray2[titleRownum+cj][arrayi] = "NULL";
								}
							}
							arrayi++;
						}
					}else{
						if(!StringUtil.isBlank(getTextFromHtml(tds.get(tdi).text()))){
							// 有这个样式的，要顺延该字段border-right:none;
							
							preTdValue = getTextFromHtml(tds.get(tdi).text());
							colStrArray2[titleRownum][arrayi] = "["+getTextFromHtml(tds.get(tdi).text())+"]";
						}else{
							if(StringUtil.isBlank(preTdValue)){
								if(StringUtil.isBlank(getTextFromHtml(tds.get(tdi).text()))){
									colStrArray2[titleRownum][arrayi] = "NULL";
								}else{
									colStrArray2[titleRownum][arrayi] = "["+getTextFromHtml(tds.get(tdi).text())+"]";
								}
								
							}else{
								
								boolean find = false;
								Set<String> classNames = tds.get(tdi).classNames();
								for(String className:classNames){
									HashMap<String,String> c1 = css.get("."+className); 
									Iterator<String> it=c1.keySet().iterator(); 
									while(it.hasNext()){ 
									    String key=it.next().toString(); 
									    String value=c1.get(key); 
									    if("border-left".equals(key) && !"none".equals(value)){
									    	find = true;
									    	break;
									    }
									    //System.out.println(key+"--"+value); 
									} 
									if(find){
										break;
									}
									//System.out.println(className);
								}
								if(find){
									preTdValue = "";
									colStrArray2[titleRownum][arrayi] = "NULL";
								}else{
									colStrArray2[titleRownum][arrayi] = "("+preTdValue+")";
								}
								
								//colStrArray2[titleRownum][arrayi] = "("+preTdValue+")";
							}
						}
						
						
						
						if(td.hasAttr("rowspan") && Integer.valueOf(td.attr("rowspan"))>1 ){
							for(int cj=1;cj<Integer.valueOf(td.attr("rowspan"));cj++){
								colStrArray2[titleRownum+cj][arrayi] = "NULL";
							}
						}else{
							
						}
						arrayi++;
					}
				}
				
				titleRownum ++;
				
				// 遍历每个td然后拼接每行的title
			/*	preTdValue = "";
				int tdistart = 0;
				if(titleRow == -1){
					titleRow = Integer.valueOf(tds.get(0).attr("rowspan"));
				}else{
					tdistart = 1;
				}
				for(int tdi=0,arrayi=0;tdi<tds.size();tdi++,arrayi++){
					Element td = tds.get(tdi);
					
					if(td.hasAttr("colspan") && Integer.valueOf(td.attr("colspan"))>1 ){
						// 多个，要加 -
						int maxtdi = arrayi+tdistart + Integer.valueOf(td.attr("colspan"));
						String tdv = getTextFromHtml(tds.get(tdi).text());
						for(;arrayi+tdistart<maxtdi && arrayi+tdistart<colStrArray.length;arrayi++){
							colStrArray[arrayi+tdistart] = colStrArray[arrayi+tdistart]+ tdv+"-";
						}
						arrayi--;//由于过载，最外面的arrayi任然会加一次，所以要去除偏移
					}else{
						if(!StringUtil.isBlank(getTextFromHtml(tds.get(tdi).text()))){
							// 有这个样式的，要顺延该字段border-right:none;
							
							preTdValue = getTextFromHtml(tds.get(tdi).text());
							
							colStrArray[arrayi+tdistart] = colStrArray[arrayi+tdistart]+"["+getTextFromHtml(tds.get(tdi).text())+"]-";
						}else{
							if(StringUtil.isBlank(preTdValue)){
								colStrArray[arrayi+tdistart] = colStrArray[arrayi+tdistart]+"-";
							}else{
								
								boolean find = false;
								Set<String> classNames = tds.get(tdi).classNames();
								for(String className:classNames){
									HashMap<String,String> c1 = css.get("."+className); 
									Iterator<String> it=c1.keySet().iterator(); 
									while(it.hasNext()){ 
									    String key=it.next().toString(); 
									    String value=c1.get(key); 
									    if("border-left".equals(key) && !"none".equals(value)){
									    	find = true;
									    	break;
									    }
									    //System.out.println(key+"--"+value); 
									} 
									if(find){
										break;
									}
									//System.out.println(className);
								}
								System.out.println(k+"   "+find +"---"+preTdValue+" "+tds.get(tdi).text());
								if(find){
									preTdValue = "";
									colStrArray[arrayi+tdistart] = colStrArray[arrayi+tdistart]+"-";
								}else{
									colStrArray[arrayi+tdistart] = colStrArray[arrayi+tdistart]+"("+ preTdValue+")-";
								}
								
								
							}
							
						}
						
					}
					
					//colStrArray[tdi] = colStrArray[tdi]+"-"+getTextFromHtml(tds.get(tdi).text());
				}*/
			}else if(splitIndex != -1 && splitIndex != k && -1 == remarkIndex){
				System.out.println("这是数据");
				String [] data = new String [colCount];
				for(int i=0;i<data.length;i++){
					data[i] = "";
		        }
				for(int tdi=1;tdi<tds.size();tdi++){
					if(tds.get(tdi).hasAttr("x:num") 
							&& !StringUtil.isBlank(tds.get(tdi).attr("x:num"))){
						data[tdi] = tds.get(tdi).attr("x:num");
					}else{
						data[tdi] = getTextFromHtml(tds.get(tdi).text());
					}
				}
				for(int i=0;i<data.length;i++){
		        //	System.out.print(data[i]+"  ");
		        }
				dataList.add(data);
				rowStrList.add(getTextFromHtml(tds.get(0).text()));
			}else if(-1 != remarkIndex && remarkIndex != k){
				//remark = remark
				
				String bRemark = "";
				for(int tdi=0;tdi<tds.size();tdi++){
					bRemark = bRemark + getTextFromHtml(tds.get(tdi).text());
				}
				remark = remark + bRemark + "\n";
				System.out.println("这是备注");
			}
        }
     //   System.out.println(splitIndex);
        
/*        System.out.println("行数据：");
        for(String rowName:rowStrList){
        	System.out.println(rowName);
        }*/
        
        
        for(int i=0;i<colStrArray2.length;i++){
        	for(int j=0;j<colCount;j++){
        		if(StringUtil.isBlank(colStrArray2[i][j])){
        			
        			colStrArray2[i][j] = "NULL";
        		}
            //	System.out.print(colStrArray2[i][j]+"  ");
            }
        	
        	//System.out.println();
        }
        // ()的数据说明是包容的
        for(int j=0;j<colCount;j++){
        	String colName = "";
        	for(int i=0;i<colStrArray2.length;i++){
        		if("NULL".equals(colStrArray2[i][j])){
    				colName = colName + "-";
    			}else{
    				colName = colName + colStrArray2[i][j]+"-";
    			}
        	}
        	colStrArray[j] = colName;
        }
        
        System.out.println("列数据：");
        for(int i=0;i<colStrArray.length;i++){
        	System.out.println(colStrArray[i]);
        }
        
        
        
        
      //  System.out.println(remark);
 //       
 //       System.out.println(tbody);
	}
	
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符
    
    /**
     * @param htmlStr
     * @return
     *  删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        return htmlStr.trim(); // 返回文本字符串
    }
    
    public static String getTextFromHtml(String htmlStr){
    	htmlStr = delHTMLTag(htmlStr);
    	htmlStr = htmlStr.replaceAll("&nbsp;", "");
    	htmlStr = htmlStr.replaceAll("　", "").trim();
    	return htmlStr;
    }
    
}
