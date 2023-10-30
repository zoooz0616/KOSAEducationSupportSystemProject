package com.finalprj.kess.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawling")
public class CrawlingController {

	@PostMapping("/news")
	public JSONArray news() {
		JSONArray newsArray = new JSONArray();

		try {
			String url = "https://news.naver.com/main/list.naver?mode=LS2D&mid=shm&sid1=105&sid2=283";
			Document doc = Jsoup.connect(url).get();

			Elements newsItems = doc.select("ul.type06_headline > li:lt(5)");

			for (Element newsItem : newsItems) {
				// 사진 링크
				String imageUrl = newsItem.selectFirst("dt.photo > a > img").attr("src");
				// 제목
				String title = newsItem.select("dt > a").text();
				// 뉴스 링크
				String newsLink = newsItem.select("dt > a").attr("href");
				// 요약
				String summary = newsItem.select("dd > span.lede").text();

				System.out.println("사진 링크: " + imageUrl);
				System.out.println("제목: " + title);
				System.out.println("뉴스 링크: " + newsLink);
				System.out.println("요약: " + summary);
				System.out.println("--------------------");

				// Create a JSON object for each news item
				JSONObject newsObject = new JSONObject();
				newsObject.put("imageUrl", imageUrl);
				newsObject.put("title", title);
				newsObject.put("newsLink", newsLink);
				newsObject.put("summary", summary);
				System.out.println("newsObject: " + newsObject);

				// Add the news object to the JSON array
				newsArray.put(newsObject);
				System.out.println("newsArray: " + newsArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Return an empty array in case of an exception
			return new JSONArray();
		}
		System.out.println("보내");
		System.out.println("보내" + newsArray);
		return newsArray;
	}
}

