package com.xebia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xebia.entity.Article;

public interface ArticleJpaRepository extends JpaRepository<Article, Integer>{
	Optional<Article> findBySlug(String slugId);
	
	void deleteByArticleId(int articleId);
	
	@Query(value="select Body from Article",nativeQuery = true)
	List<String> getBody();
}