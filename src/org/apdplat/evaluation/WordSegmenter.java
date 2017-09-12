/**
 * 
 * APDPlat - Application Product Development Platform
 * Copyright (c) 2013, 杨尚川, yang-shangchuan@qq.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.apdplat.evaluation;

import org.apdplat.evaluation.impl.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 获取文本的所有分词结果, 对比不同分词器结果
 * seg和segMore两个方法的区别在于返回值
 * 每一个分词器都可能有多种分词模式，每种模式的分词结果都可能不相同
 * 第一个方法忽略分词器模式，返回所有模式的所有不重复分词结果
 * 第二个方法返回每一种分词器模式及其对应的分词结果
 * @author 杨尚川
 */
public interface WordSegmenter {
    /**
     * 获取文本的所有分词结果
     * @param text 文本
     * @return 所有的分词结果，去除重复
     */
    default public Set<String> seg(String text) {
        return segMore(text).values().stream().collect(Collectors.toSet());
    }
    /**
     * 获取文本的所有分词结果
     * @param text 文本
     * @return 所有的分词结果，KEY 为分词器模式，VALUE 为分词器结果
     */
    public Map<String, String> segMore(String text);
    
    public static Map<String, Set<String>> contrast(String text){
        Map<String, Set<String>> map = new LinkedHashMap<>();
        map.put("word分词器", new WordEvaluation().seg(text));
        //map.put("Stanford分词器", new StanfordEvaluation().seg(text));
        map.put("Ansj分词器", new AnsjEvaluation().seg(text));
        map.put("HanLP分词器", new HanLPEvaluation().seg(text));
        //map.put("smartcn分词器", new SmartCNEvaluation().seg(text));
        //map.put("FudanNLP分词器", new FudanNLPEvaluation().seg(text));
        //map.put("Jieba分词器", new JiebaEvaluation().seg(text));
        //map.put("Jcseg分词器", new JcsegEvaluation().seg(text));
        //map.put("MMSeg4j分词器", new MMSeg4jEvaluation().seg(text));
        //map.put("IKAnalyzer分词器", new IKAnalyzerEvaluation().seg(text));
        return map;
    }
    public static Map<String, Map<String, String>> contrastMore(String text){
        Map<String, Map<String, String>> map = new LinkedHashMap<>();
        map.put("word分词器", new WordEvaluation().segMore(text));
        //map.put("Stanford分词器", new StanfordEvaluation().segMore(text));
        map.put("Ansj分词器", new AnsjEvaluation().segMore(text));
        map.put("HanLP分词器", new HanLPEvaluation().segMore(text));
        //map.put("smartcn分词器", new SmartCNEvaluation().segMore(text));
        //map.put("FudanNLP分词器", new FudanNLPEvaluation().segMore(text));
        //map.put("Jieba分词器", new JiebaEvaluation().segMore(text));
        //map.put("Jcseg分词器", new JcsegEvaluation().segMore(text));
        //map.put("MMSeg4j分词器", new MMSeg4jEvaluation().segMore(text));
        //map.put("IKAnalyzer分词器", new IKAnalyzerEvaluation().segMore(text));
        return map;
    }
    public static void show(Map<String, Set<String>> map){
        map.keySet().forEach(k -> {
            System.out.println(k + " 的分词结果：");
            AtomicInteger i = new AtomicInteger();
            map.get(k).forEach(v -> {
                System.out.println("\t" + i.incrementAndGet() + " 、" + v);
            });
        });
    }
    public static void showMore(Map<String, Map<String, String>> map){
        map.keySet().forEach(k->{
            System.out.println(k + " 的分词结果：");
            AtomicInteger i = new AtomicInteger();
            map.get(k).keySet().forEach(a -> {
                System.out.println("\t" + i.incrementAndGet()+ " 、【"   + a + "】\t" + map.get(k).get(a));
            });
        });
    }
    public static void run(String encoding) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, encoding))){
            String line = null;
            while((line = reader.readLine()) != null){
                if("exit".equals(line)){
                    System.exit(0);
                    return;
                }
                if(line.trim().equals("")){
                    continue;
                }
                process(line);
                showUsage();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void process(String text){
        System.out.println("********************************************");
        show(contrast(text));
        System.out.println("********************************************");
        showMore(contrastMore(text));
        System.out.println("********************************************");
    }
    public static void showUsage(){
        System.out.println("输入exit退出程序");
        System.out.println("输入要分词的文本后回车确认：");
    }
    public static void main(String[] args) {
        //process("（2004年3月14日第十届全国人民代表大会第二次会议通过　2004年3月14日全国人民代表大会公告公布施行）第十九条　宪法序言第十自然段第二句“在长期的革命和建设过程中，已经结成由中国共产党领导的，有各民主党派和各人民团体参加的，包括全体社会主义劳动者、拥护社会主义的爱国者和拥护祖国统一的爱国者的广泛的爱国统一战线，这个统一战线将继续巩固和发展。”修改为：“在长期的革命和建设过程中，已经结成由中国共产党领导的，有各民主党派和各人民团体参加的，包括全体社会主义劳动者、社会主义事业的建设者、拥护社会主义的爱国者和拥护祖国统一的爱国者的广泛的爱国统一战线，这个统一战线将继续巩固和发展。”第二十条　宪法第十条第三款“国家为了公共利益的需要，可以依照法律规定对土地实行征用。”修改为：“国家为了公共利益的需要，可以依照法律规定对土地实行征收或者征用并给予补偿。”第二十一条　宪法第十一条第二款“国家保护个体经济、私营经济的合法的权利和利益。国家对个体经济、私营经济实行引导、监督和管理。”修改为：“国家保护个体经济、私营经济等非公有制经济的合法的权利和利益。国家鼓励、支持和引导非公有制经济的发展，并对非公有制经济依法实行监督和管理。”第二十二条　宪法第十三条“国家保护公民的合法的收入、储蓄、房屋和其他合法财产的所有权。”“国家依照法律规定保护公民的私有财产的继承权。”修改为：“公民的合法的私有财产不受侵犯。”“国家依照法律规定保护公民的私有财产权和继承权。”“国家为了公共利益的需要，可以依照法律规定对公民的私有财产实行征收或者征用并给予补偿。”第二十三条　宪法第十四条增加一款，作为第四款：“国家建立健全同经济发展水平相适应的社会保障制度。”第二十四条　宪法第三十三条增加一款，作为第三款：“国家尊重和保障人权。”第三款相应地改为第四款。第二十五条　宪法第五十九条第一款“全国人民代表大会由省、自治区、直辖市和军队选出的代表组成。各少数民族都应当有适当名额的代表。”修改为：“全国人民代表大会由省、自治区、直辖市、特别行政区和军队选出的代表组成。各少数民族都应当有适当名额的代表。”第二十六条　宪法第六十七条全国人民代表大会常务委员会职权第二十项“（二十）决定全国或者个别省、自治区、直辖市的戒严”修改为“（二十）决定全国或者个别省、自治区、直辖市进入紧急状态”。第二十七条　宪法第八十条“中华人民共和国主席根据全国人民代表大会的决定和全国人民代表大会常务委员会的决定，公布法律，任免国务院总理、副总理、国务委员、各部部长、各委员会主任、审计长、秘书长，授予国家的勋章和荣誉称号，发布特赦令，发布戒严令，宣布战争状态，发布动员令。”修改为：“中华人民共和国主席根据全国人民代表大会的决定和全国人民代表大会常务委员会的决定，公布法律，任免国务院总理、副总理、国务委员、各部部长、各委员会主任、审计长、秘书长，授予国家的勋章和荣誉称号，发布特赦令，宣布进入紧急状态，宣布战争状态，发布动员令。”第二十八条　宪法第八十一条“中华人民共和国主席代表中华人民共和国，接受外国使节；根据全国人民代表大会常务委员会的决定，派遣和召回驻外全权代表，批准和废除同外国缔结的条约和重要协定。”修改为：“中华人民共和国主席代表中华人民共和国，进行国事活动，接受外国使节；根据全国人民代表大会常务委员会的决定，派遣和召回驻外全权代表，批准和废除同外国缔结的条约和重要协定。”第二十九条　宪法第八十九条国务院职权第十六项“（十六）决定省、自治区、直辖市的范围内部分地区的戒严”修改为“（十六）依照法律规定决定省、自治区、直辖市的范围内部分地区进入紧急状态”。第三十条　宪法第九十八条“省、直辖市、县、市、市辖区的人民代表大会每届任期五年。乡、民族乡、镇的人民代表大会每届任期三年。”修改为：“地方各级人民代表大会每届任期五年。”第三十一条　宪法第四章章名“国旗、国徽、首都”修改为“国旗、国歌、国徽、首都”。宪法第一百三十六条增加一款，作为第二款：“中华人民共和国国歌是《义勇军进行曲》。”");
        process("8月9日上午，税镇镇组织召开“百日除患铸安”专项行动动员会，全面部署“百日除患铸安”专项行动。会议由镇人大主席李志主持，镇村全体干部参加会议。会议解读了《税镇镇“百日除患铸安”专项行动实施方案》，提出了目标任务和专项行动范围等，要求各村委各单位按照文件要求履职尽责，认真开展本单位专项行动，确保行动取得实效。会议分析了上半年全镇安全生产形势，要求各村委各单位履职担当，多措并举，力推“百日除患铸安”专项行动有力有效开展，全力以赴抓好当前安全生产各项工作，营造安全稳定的安全生产环境。会议指出，此次专项行动采取突击检查、明查暗访、随机抽查、回头检查、交叉检查等多种方式，深入开展督查检查。对检查发现的违法违规行为采取“零容忍”，依法按照“四个一律”，即对非法生产经营建设和经停产整顿仍未达到要求的，一律关闭取缔；对非法违法生产经营建设的有关单位和责任人，一律按规定上限予以处罚；对存在违法生产经营建设的单位，一律责令停产整顿，并严格落实监管措施；对触犯法律的有关单位和人员，一律依法严格追究法律责任。会议要求：一要高度重视，周密部署，结合实际，认真制定本单位专项行动工作方案；二要各部门协调配合，涉及的安监办、民政办、派出所等部门要相互沟通，联合开展执法检查；三要广泛开展、全面检查，此次专项行动涉及道路交通、危险化学品、建筑施工、烟花爆竹、消防、涉爆粉尘、青少年防溺水、电力等多个行业，安全大检查必须全覆盖式开展检查，严查违法违规行为，消除安全隐患；四是严格执法、严肃惩处，为确保群众生产生活安全，以近期各类安全事故为鉴，此次检查对于发现的隐患要责令有关单位立即整改到位，不能立即整改的限期整改，拒不执行的将挂牌督办，并严厉打击违法违规行为，对违法违规企业将实行联合惩戒，突出此次专项行动效果。");
        String encoding = "utf-8";
        if(args==null || args.length == 0){
            showUsage();
            run(encoding);
        }else if(Charset.isSupported(args[0])){
            showUsage();
            run(args[0]);
        }
    }
}
