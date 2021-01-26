package com.untact.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.untact.dto.Article;

@Controller
public class UsrArticleController {
	
	/*
	@Controller가 부여된 클래스안에서 사용자가 요청한 url에 대응되는
	@RequestMapping(value="")어노테이션을 가지고 있는 Method가 실행됩니다.
	최종적으로 해당 메소드에서 return 하는 String에 대응되는 view를 사용자에게 응답하게 됩니다.
	(return 하는 String에 대응하는 view를 찾기 위해서는
	/WEB-INF/spring/appServlet/servlet-context.xml에 명시된
	InternalResourceViewResolverBean의 prefix와 suffix를 합쳐서 view를 찾습니다.)
	*/
	
	private int articlesLastId;
	private List<Article> articles;
	
	public UsrArticleController() {
		
		articlesLastId = 0;
		articles = new ArrayList<>(); 
		
		articles.add(new Article(++articlesLastId, "2020-12-12", "제목1", "내용1"));
		articles.add(new Article(++articlesLastId, "2020-12-12", "제목2", "내용2"));
		// ※ ++가 앞에 붙는지 뒤에 붙는지 차이를 아는가.

		
		
	}
	

	
	
	@RequestMapping("/usr/article/detail")
	@ResponseBody
	public Article showDetail(int id) {
		
		return articles.get(id - 1);
		// List는 0번부터 시작하므로.
	}
	
	@RequestMapping("/usr/article/list")
	@ResponseBody
	public List<Article> showList() {
		return articles;
	}
	
	
	@RequestMapping("/usr/article/doAdd")
	@ResponseBody
	public Map<String, Object> doAdd(String regDate, String title, String body) {
		articles.add(new Article(++articlesLastId, regDate, title, body));
		
		Map<String, Object> rs = new HashMap<>();
		rs.put("resultCode", "S-1");
		rs.put("msg", "성공하였습니다.");
		rs.put("id", articlesLastId);
		
		return rs;
		
	}
	
	
	//※URL에서 받은 값이 없을 경우, NULL이 DEFAULT로 들어가게 된다.
	//  위의 doAdd 메서드는 URL에서 String값을 받을 때, 값이 없더라도 null이 들어가는 게
	//  문제가 없지만, 아래 doDelete 메서드는 primitive인 int형을 받기 때문에 URL에 값이
	//  없어서 NULL이 들어갈 경우, IllegalStateException이 발생한다.
	//  (위의 showDetail 메서드도 마찬가지) = Map<String, Object>와는 관련이 없다.
	//  URL에서 받는 값이 primitive 타입인 경우에 발생한다는 점.
	
	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public Map<String, Object> doDelete(int id) {
		
		boolean deleteArticleRs = deleteArticle(id);
		
		Map<String, Object> rs = new HashMap<>();
		
		if(deleteArticleRs) {
			
			rs.put("resultCode", "S-1");
			rs.put("msg", "성공하였습니다.");
			
		}else {
			rs.put("resultCode", "F-1");
			rs.put("msg", "해당 게시물은 존재하지 않습니다.");
			
		}
		
		rs.put("id", id);
		
		return rs;
		
	}
	
	private boolean deleteArticle(int id) {
		for( Article article : articles) {
			
			if(article.getId() == id) {
				articles.remove(article);

				return true;
			}
			
		}
		return false;
		
	}
	

	
}
