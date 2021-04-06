package com.xebia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xebia.entity.Tag;
import com.xebia.entity.TagId;

public interface ArticleTagJpaRepository extends JpaRepository<Tag, TagId>{
	
	List<Tag> findByTagIdArticleId(int articleId);
	
	void deleteByTagIdIn(List<TagId> tagIdList);

	@Query(value="select Article_tag from Tag",nativeQuery = true)
	List<String> getTagOccurence();
	
	
}








