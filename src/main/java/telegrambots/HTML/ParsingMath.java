package telegrambots.HTML;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ParsingMath {
    public ArrayList<String> getListQuestionsAndAnswers(){
        Document page = getHTML();
        String securityTag = page.select("body > div.wrapper > div.sgia-main-content").tagName("div").toString().split("\"")[7];
        Iterator<Element> iteratorElements = page.select("body > div.wrapper > div.sgia-main-content > div."+securityTag+" > div.prob_list > div.prob_view").iterator();
        ArrayList<String> list = new ArrayList<>();
        while (iteratorElements.hasNext()) {
            try {
                String urlSVG = iteratorElements.next().getElementsByClass("pbody").tagName("p").tagName("img").tagName("img").toString().split("\"")[11];
                String numFromSolution = iteratorElements.next().getElementsByClass("solution").tagName("p").tagName("img").text().split("Ответ")[1].split("\\.")[0].replaceAll("[: ]", "");
                list.add(urlSVG + " | "+numFromSolution);
            } catch (Exception e){}
        }
        return list;
    }
    private Document getHTML() {
        Document document = null;
        try {
            document = Jsoup.connect("https://math-ege.sdamgia.ru/test?a=view_many&cat_id[]=14&cat_id[]=9&cat_id[]=10&cat_id[]=11&cat_id[]=12&cat_id[]=13&filter=all").get();
        } catch (IOException e){
            e.printStackTrace();
        }
        return document;
    }
}
