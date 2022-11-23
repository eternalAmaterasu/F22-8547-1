package org.uwindsor.mac.f22.acc.compute_engine.engine.parser;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Vivek
 * @since 23/11/22
 */
@Slf4j
public class KayakParser {

    public static List<SearchResponse> parseKayakData(String data) {
        List<SearchResponse> list = new ArrayList<>();
        Document document = Jsoup.parse(data);
        Elements elementsByClass = document.getElementsByClass("resultInner");
        AtomicInteger index = new AtomicInteger(1);
        elementsByClass.forEach(element -> {
            System.out.println(index.get() + " :: " + element.text());
            index.incrementAndGet();


        });
        return list;
    }
}