package com.untact.controller;

import java.util.List;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.untact.dto.Board;
import com.untact.dto.Member;
import com.untact.dto.Article;
import com.untact.dto.ResultData;
import com.untact.service.ArticleService;
import com.untact.util.Util;

@Controller
public class UsrArticleController {

	// Controller는 최소한의 필터링을 해서 Service로 넘겨준다.

	/*
	 * @Controller가 부여된 클래스안에서 사용자가 요청한 url에 대응되는
	 * 
	 * @RequestMapping(value="")어노테이션을 가지고 있는 Method가 실행됩니다. 최종적으로 해당 메소드에서 return
	 * 하는 String에 대응되는 view를 사용자에게 응답하게 됩니다. (return 하는 String에 대응하는 view를 찾기 위해서는
	 * /WEB-INF/spring/appServlet/servlet-context.xml에 명시된
	 * InternalResourceViewResolverBean의 prefix와 suffix를 합쳐서 view를 찾습니다.)
	 */

	// List는 Article의 변수들을 담기 위해 사용한 것이고,
	// Map은 웹에 보여주기 위해 사용한 것.

	// ※URL에서 받은 값이 없을 경우, NULL이 DEFAULT로 들어가게 된다.
	// 위의 doAdd 메서드는 URL에서 String값을 받을 때, 값이 없더라도 null이 들어가는 게
	// 문제가 없지만, 아래 doDelete 메서드는 primitive인 int형을 받기 때문에 URL에 값이
	// 없어서 NULL이 들어갈 경우, IllegalStateException이 발생한다.
	// (위의 showDetail 메서드도 마찬가지) = Map<String, Object>와는 관련이 없다.
	// URL에서 받는 값이 primitive 타입인 경우에 발생한다는 점.

	@Autowired
	private ArticleService articleService;

	@GetMapping("/usr/article/detail")
	@ResponseBody
	public ResultData showDetail(Integer id) {
		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		Article article = articleService.getForPrintArticle(id);

		if (article == null) {
			return new ResultData("F-2", "존재하지 않는 게시물번호 입니다.");
		}

		return new ResultData("S-1", "성공", "article", article);
	}

	@GetMapping("/usr/article/list")
	@ResponseBody
	public ResultData showList(@RequestParam(defaultValue = "1") int boardId, String searchKeywordType, String searchKeyword, @RequestParam(defaultValue = "1") int page) {

		Board board = articleService.getBoard(boardId);
		
		if ( board == null ) {
			return new ResultData("F-1", "존재하지 않는 게시판 입니다.");
		}
		
		if (searchKeywordType != null) {
			searchKeywordType = searchKeywordType.trim();
		}

		if (searchKeywordType == null || searchKeywordType.length() == 0) {
			searchKeywordType = "titleAndBody";
		}

		if (searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = null;
		}

		if (searchKeyword != null) {
			searchKeyword = searchKeyword.trim();
		}

		if (searchKeyword == null) {
			searchKeywordType = null;
		}
		
		int itemsInAPage = 20;

		List<Article> articles = articleService.getForPrintArticles(boardId, searchKeywordType, searchKeyword, page, itemsInAPage);

		return new ResultData("S-1", "성공", "articles", articles);
	}
	
	@PostMapping("/usr/article/doAddReply")
	@ResponseBody
	public ResultData doAddReply(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		int loginedMemberId = (int)req.getAttribute("loginedMemberId");

		if (param.get("body") == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}
		
		if (param.get("articleId") == null) {
			return new ResultData("F-1", "articleId를 입력해주세요.");
		}

		param.put("memberId", loginedMemberId);

		return articleService.addReply(param);
	}

	@PostMapping("/usr/article/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		int loginedMemberId = (int)req.getAttribute("loginedMemberId");
		
		if (param.get("title") == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}

		if (param.get("body") == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}

		param.put("memberId", loginedMemberId);

		return articleService.addArticle(param);
	}

	@PostMapping("/usr/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id, HttpServletRequest req) {
		Member loginedMember = (Member)req.getAttribute("loginedMember");

		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		Article article = articleService.getArticle(id);

		if (article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}

		ResultData actorCanDeleteRd = articleService.getActorCanDeleteRd(article, loginedMember);

		if (actorCanDeleteRd.isFail()) {
			return actorCanDeleteRd;
		}

		return articleService.deleteArticle(id);
	}

	@PostMapping("/usr/article/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		Member loginedMember = (Member)req.getAttribute("loginedMember");
		
		int id = Util.getAsInt(param.get("id"), 0);

		if ( id == 0 ) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		if ( Util.isEmpty(param.get("title")) ) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}

		if ( Util.isEmpty(param.get("body")) ) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}

		Article article = articleService.getArticle(id);

		if (article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}

		ResultData actorCanModifyRd = articleService.getActorCanModifyRd(article, loginedMember);

		if (actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}

		return articleService.modifyArticle(param);
	}
	
	

}
