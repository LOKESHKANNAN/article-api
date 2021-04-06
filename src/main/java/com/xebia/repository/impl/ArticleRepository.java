package com.xebia.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.xebia.entity.Article;
import com.xebia.repository.ArticleJpaRepository;

@Repository
public class ArticleRepository{
	
	@Autowired
	ArticleJpaRepository articleJpaRepository;
	
	public Article createArticle(Article article) {
		return articleJpaRepository.save(article);
	}
	
	public Optional<Article> findBySlug(String slugId) {
		return articleJpaRepository.findBySlug(slugId);
	}

	public Article updateArticle(Article article) {
		return articleJpaRepository.save(article);
	}

	public void deleteByArticleId(Optional<Article> articleList) {
		articleJpaRepository.deleteById(articleList.get().getArticleId());
	}

	public Page<Article> findAll(Pageable paging) {
		return	articleJpaRepository.findAll(paging);
	}

	public List<String> getBody() {
		return	articleJpaRepository.getBody();
	}

}