package com.xebia.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xebia.entity.Tag;
import com.xebia.entity.TagId;
import com.xebia.repository.ArticleTagJpaRepository;

@Repository
public class ArticleTagRepository{
	
	@Autowired
	ArticleTagJpaRepository articleTagJpaRepository;
	
	public void createAllTag(List<Tag> tags) {
		articleTagJpaRepository.saveAll(tags);
	}

	public List<Tag> findByTagIdArticleId(int articleId) {
		return articleTagJpaRepository.findByTagIdArticleId(articleId);
	}

	public void updateAllTag(List<Tag> articleTags) {
		 articleTagJpaRepository.saveAll(articleTags);
	}

	public void deleteAllByTagId(List<TagId> tagIdList) {
		articleTagJpaRepository.deleteByTagIdIn(tagIdList);
	}
	
	public List<String> getTagOccurence() {
		return articleTagJpaRepository.getTagOccurence();
	}


}