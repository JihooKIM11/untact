package com.untact.dao;

import java.util.List;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.untact.dto.Board;
import com.untact.dto.Article;

@Mapper
public interface ArticleDao {
	
	// Controller로부터 요청받은 데이터를 Dao를 통해 가져오도록 요청함.
	
	//SELECT가 아닌 경우, return을 할 수 없으므로 void로 작성.
	

	Article getArticle(@Param("id") int id);

	void addArticle(Map<String, Object> param);

	void deleteArticle(@Param("id") int id);

	void modifyArticle(Map<String, Object> param);

	List<Article> getArticles(@Param("searchKeywordType") String searchKeywordType,
			@Param("searchKeyword") String searchKeyword);

	Article getForPrintArticle(@Param("id") int id);

	List<Article> getForPrintArticles(@Param("boardId") int boardId,
			@Param("searchKeywordType") String searchKeywordType, @Param("searchKeyword") String searchKeyword,
			@Param("limitStart") int limitStart, @Param("limitTake") int limitTake);

	Board getBoard(@Param("id") int id);

	void addReply(Map<String, Object> param);

	int getArticlesTotalCount(@Param("boardId") int boardId, @Param("searchKeywordType") String searchKeywordType,
			@Param("searchKeyword") String searchKeyword);

}
