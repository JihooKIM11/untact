package com.untact.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                // getter,setter, toString
@AllArgsConstructor  // 생성자
@NoArgsConstructor   // 인자가 없는 생성자
public class Article extends EntityDto {
	
	// 중요한 순서로 나열
	private int id;
	private String regDate;
	private String updateDate;
	private int boardId;
	private int memberId;
	private String title;
	private String body;

	private String extra__writer;
	private String extra__boardName;
	private String extra__thumbImg;
	
	public String getWriterThumbImgUrl() {
		return "/common/genFile/file/member/" + memberId + "/common/attachment/1";
	}
	
	
	
	
	
	

	
	
	
	
}
