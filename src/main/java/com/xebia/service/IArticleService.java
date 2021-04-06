package com.xebia.service;

import java.util.List;

import com.xebia.model.ArticleModel;
import com.xebia.model.ArticleRead;
import com.xebia.model.TagMetricModel;

public interface IArticleService{
	
	ArticleModel createArticle(ArticleModel articleModel);

	ArticleModel updateArticle(ArticleModel articleModel, String slug);

	void deleteArticle(String slug);

	List<ArticleModel> getArticles(Integer pageNo, Integer pageSize, String sortBy);
	
	List<TagMetricModel> getTagOccurence();

	ArticleRead getAverageReadingTime(String slug);

	ArticleModel getArticle(String slug);
}