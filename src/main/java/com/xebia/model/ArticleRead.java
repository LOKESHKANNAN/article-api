package com.xebia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRead{
	private String slug;
	private TimetoRead timeToRead;;	
}