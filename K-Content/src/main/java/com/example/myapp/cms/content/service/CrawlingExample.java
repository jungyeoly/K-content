package com.example.myapp.cms.content.service;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlingExample {

    /**
     * 조회할 URL셋팅 및 Document 객체 로드하기
     */

    public void process(String query) {
        String url = "https://www.instagram.com/explore/tags/" + query;
        Connection conn = Jsoup.connect(url);
        //Jsoup 커넥션 생성

        Document document = null;
        try {
            document = conn.get();
//            System.out.println(document);
            //url의 내용을 HTML Document 객체로 가져온다.
            //https://jsoup.org/apidocs/org/jsoup/nodes/Document.html 참고
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Object> list = getDataList(document);
    }


    /**
     * data가져오기
     */
    private List<Object> getDataList(Document document) {
        List<Object> list = new ArrayList<>();
        Elements selects = document.select("._aagv");	//⭐⭐⭐
        //select 메서드 안에 css selector를 작성하여 Elements를 가져올 수 있다.
        System.out.println(selects.size());
        for (Element select : selects) {
            System.out.println(select.html());		//⭐⭐⭐
            //html(), text(), children(), append().... 등 다양한 메서드 사용 가능
            //https://jsoup.org/apidocs/org/jsoup/nodes/Element.html 참고
        }
        System.out.println(list);
        return list;
    }

}
