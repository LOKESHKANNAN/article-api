package com.xebia.model;

import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleModel{
	
	private String slug;
	private String title;
	private String description;
	private String body;
	@JsonInclude(Include.NON_NULL)
	private List<String> tags;
	@JsonInclude(Include.NON_NULL)
	private Calendar createdAt;
	@JsonInclude(Include.NON_NULL)
	private Calendar updatedAt;
	
}
