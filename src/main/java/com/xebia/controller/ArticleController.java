package com.xebia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xebia.model.ArticleModel;
import com.xebia.model.ArticleRead;
import com.xebia.model.TagMetricModel;
import com.xebia.service.IArticleService;

@RestController
@RequestMapping(path="api/v1")
public class ArticleController {
	
	@Autowired
	IArticleService iArticleService;
	
	@PostMapping("createArticle")
	public ArticleModel createArticle(@RequestBody ArticleModel articleModel) {
		return iArticleService.createArticle(articleModel);
	}
	
	@PostMapping("{slug}/updateArticle")
	public ArticleModel updateArticle(@RequestBody ArticleModel articleModel, @PathVariable String slug) {
		return iArticleService.updateArticle(articleModel, slug);
	}
	
	@DeleteMapping("{slug}/deleteArticle")
	public void deleteArticle(@PathVariable String slug) {
		iArticleService.deleteArticle(slug);
	}
	
	@GetMapping("{slug}/getArticle")
	public ArticleModel getArticle(@PathVariable String slug) {
		return iArticleService.getArticle(slug);
	}
	
	
	@GetMapping("{pageNo}/{pageSize}/{sortBy}/getArticles")
	public List<ArticleModel> getArticles(@PathVariable Integer pageNo, 
			@PathVariable Integer pageSize,
			@PathVariable String sortBy) {
		return iArticleService.getArticles(pageNo, pageSize, sortBy);
	}
	
	@GetMapping("/getTagOccurence")
	public List<TagMetricModel> getTagOccurence() {
		return iArticleService.getTagOccurence();
	}
	@GetMapping("{slug}/getAverageReadingTime")
	public ArticleRead getAverageReadingTime(@PathVariable String slug) {
		return iArticleService.getAverageReadingTime(slug);
	}
}