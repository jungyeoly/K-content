//package com.example.myapp.cms.content.service;
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class CrawlingExample {
//
//    /**
//     * 조회할 URL셋팅 및 Document 객체 로드하기
//     */
//
//    public void process(String query) {
//        String url = "https://www.instagram.com/explore/tags/" + query+"/";
//        #mount_0_0_Be > div > div > div.x9f619.x1n2onr6.x1ja2u2z > div > div > div > div.x78zum5.xdt5ytf.x1t2pt76.x1n2onr6.x1ja2u2z.x10cihs4 > div.x9f619.xvbhtw8.x78zum5.x168nmei.x13lgxp2.x5pf9jr.xo71vjh.x1uhb9sk.x1plvlek.xryxfnj.x1c4vz4f.x2lah0s.x1q0g3np.xqjyukv.x1qjc9v5.x1oa3qoh.x1qughib > div.x1gryazu.xh8yej3.x10o80wk.x14k21rp.x17snn68.x6osk4m.x1porb0y > section > main > article > div > div > div > div:nth-child(1) > div:nth-child(1) > a > div._aagu > div._aagv > img
//        //*[@id="mount_0_0_e1"]/div/div/div[2]/div/div/div/div[1]/section/main/article/div/div/div/div[1]/div[1]/a/div/div[1]
//        Connection conn = Jsoup.connect(url);
//        //Jsoup 커넥션 생성
//
//        Document document = null;
//        try {
//            document = conn.get();
////            System.out.println(document);
//            //url의 내용을 HTML Document 객체로 가져온다.
//            //https://jsoup.org/apidocs/org/jsoup/nodes/Document.html 참고
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        List<Object> list = getDataList(document);
//    }
//
//
//    /**
//     * data가져오기
//     */
//    private List<Object> getDataList(Document document) {
//        List<Object> list = new ArrayList<>();
//
//        Elements selects = document.select("._aagv").select("[src]");	//⭐⭐⭐
//        //select 메서드 안에 css selector를 작성하여 Elements를 가져올 수 있다.
//        System.out.println(selects);
//        for (Element select : selects) {
//            System.out.println(select.html());		//⭐⭐⭐
//            //html(), text(), children(), append().... 등 다양한 메서드 사용 가능
//            //https://jsoup.org/apidocs/org/jsoup/nodes/Element.html 참고
//        }
//        System.out.println(list);
//        return list;
//    }
//
//}
