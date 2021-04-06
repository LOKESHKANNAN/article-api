package com.xebia.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.xebia.entity.Article;
import com.xebia.entity.Audit;
import com.xebia.entity.Tag;
import com.xebia.entity.TagId;
import com.xebia.exception.ArtileNotFoundException;
import com.xebia.exception.BadRequestException;
import com.xebia.model.ArticleModel;
import com.xebia.model.ArticleRead;
import com.xebia.model.TagMetricModel;
import com.xebia.model.TimetoRead;
import com.xebia.repository.impl.ArticleRepository;
import com.xebia.repository.impl.ArticleTagRepository;
import com.xebia.service.IArticleService;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;
@Service
@Transactional
public class ArticleService implements IArticleService {

	Calendar calendar;
	@Value("${average.humanReadingSpeed}")
	private int humanReadingSpeed;

	@Autowired
	ArticleRepository articleRepository;
	@Autowired
	ArticleTagRepository articleTagRepository;

	@Override
	public ArticleModel createArticle(ArticleModel articleModel) {
		calendar = Calendar.getInstance();
		Article article = new Article();
		article.setSlug(generateSlugId(articleModel.getTitle()));
		article.setTitle(articleModel.getTitle());
		article.setDescription(articleModel.getDescription());
		article.setBody(articleModel.getBody());
		article.setAudit(new Audit(calendar, calendar));		
		Boolean score = SimilarityCheck(articleModel.getBody(),"","add");
		if(score) {
			article = articleRepository.createArticle(article);
			if(!Objects.isNull(articleModel.getTags())&&!articleModel.getTags().isEmpty()) {
				AtomicInteger articleId =  new AtomicInteger(article.getArticleId());
				List<Tag> articleTags = new ArrayList<>();
				articleModel.setTags(articleModel.getTags().stream().map(tag->tag.toLowerCase().replace(" ", "-").trim()).collect(Collectors.toList()));
				articleModel.getTags().stream().forEach(tag ->{
					Tag articleTag = new Tag();
					articleTag.setTagId(new TagId(articleId.get(), tag));
					articleTag.setAudit(new Audit(calendar, calendar));	
					articleTags.add(articleTag);
				});
				articleTagRepository.createAllTag(articleTags);
			}
			articleModel.setSlug(article.getSlug());
			return articleModel;
		}else {
			throw new BadRequestException("Article Content is similar");
		}
	}



	private boolean SimilarityCheck(String source, String target, String mode) {
		SimilarityStrategy strategy = new JaroWinklerStrategy();
		StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
		List<String> bodyContentList = articleRepository.getBody(); 
		AtomicBoolean flag =new AtomicBoolean(true);
		if(!(mode.equals("update") && bodyContentList.size()==1)) {
			bodyContentList.stream().forEach(body->{
				double score = service.score(source, body);
				if(score > 0.7) {
					flag.set(false);
					return;
				}
			});
		}
		return flag.get();
	}



	private String generateSlugId(String title) {
		String modifiedTitle = title.toLowerCase().replace(" ", "-").trim();
		String slugId = RandomStringUtils.randomAlphanumeric(6).toLowerCase();
		return modifiedTitle+"-"+slugId;
	}

	@Override
	public ArticleModel updateArticle(ArticleModel articleModel, String slug) {
		calendar = Calendar.getInstance();
		Optional<Article> articleList = articleRepository.findBySlug(slug);
		if(articleList.isPresent()) {
			Article article = new Article();
			article.setArticleId(articleList.get().getArticleId());
			article.setSlug(generateSlugId(articleModel.getTitle().isEmpty()?articleList.get().getTitle():articleModel.getTitle()));
			article.setTitle(articleModel.getTitle().isEmpty()?articleList.get().getTitle():articleModel.getTitle());
			article.setDescription(articleModel.getDescription().isEmpty()?articleList.get().getDescription():articleModel.getDescription());
			article.setBody(articleModel.getBody().isEmpty()?articleList.get().getBody():articleModel.getBody());
			article.setAudit(new Audit(articleList.get().getAudit().getCreatedAt(), calendar));	
			Boolean score = SimilarityCheck(articleModel.getBody(),articleList.get().getBody(),"update");
			if(score) {
				article = articleRepository.updateArticle(article);

				if(!Objects.isNull(articleModel.getTags())&&!articleModel.getTags().isEmpty()) {
					List<Tag> tagList = articleTagRepository.findByTagIdArticleId(articleList.get().getArticleId());
					articleModel.setTags(articleModel.getTags().stream().map(tag->tag.toLowerCase().replace(" ", "-").trim()).collect(Collectors.toList()));
					if(!tagList.isEmpty()) {
						List<Tag> articleTags = new ArrayList<>();
						articleModel.getTags().stream().forEach(tag ->{
							Tag articleTag = new Tag();
							articleTag.setTagId(new TagId(articleList.get().getArticleId(), tag));
							articleModel.setCreatedAt(articleList.get().getAudit().getCreatedAt());
							articleModel.setUpdatedAt(calendar);
							articleTags.add(articleTag);
						});
						articleTagRepository.updateAllTag(articleTags);
					}else {
						List<Tag> articleTags = new ArrayList<>();
						articleModel.getTags().stream().forEach(tag ->{
							Tag articleTag = new Tag();
							articleTag.setTagId(new TagId(articleList.get().getArticleId(), tag));
							articleModel.setCreatedAt(calendar);
							articleModel.setUpdatedAt(calendar);
							articleTags.add(articleTag);
						});
						articleTagRepository.createAllTag(articleTags);	
					}
				}else {
					List<Tag> tagList = articleTagRepository.findByTagIdArticleId(articleList.get().getArticleId());
					List<TagId> tagIdList = tagList.stream().map(Tag::getTagId).collect(Collectors.toList());
					if(!tagIdList.isEmpty()) {
						articleModel.setTags(tagIdList.stream().map(TagId::getArticleTag).collect(Collectors.toList()));
					}
					articleModel.setCreatedAt(articleList.get().getAudit().getCreatedAt());
					articleModel.setUpdatedAt(calendar);
				}
				articleModel.setSlug(article.getSlug());
				return articleModel;
			}else {
				throw new BadRequestException("Article Content is similar");
			}
		}else {
			throw new ArtileNotFoundException("Article not present");
		}
	}

	@Override
	public void deleteArticle(String slug) {
		Optional<Article> articleList = articleRepository.findBySlug(slug);
		if(articleList.isPresent()) {
		List<Tag> tagList = articleTagRepository.findByTagIdArticleId(articleList.get().getArticleId());
		List<TagId> tagIdList = tagList.stream().map(Tag::getTagId).collect(Collectors.toList());
		articleTagRepository.deleteAllByTagId(tagIdList);
		articleRepository.deleteByArticleId(articleList);
		}else {
			throw new ArtileNotFoundException("Article not present");
		}

	}

	@Override
	public List<ArticleModel> getArticles(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Article> pagedResult = articleRepository.findAll(paging);
		System.err.println(pagedResult.getTotalPages());
		if(pagedResult.hasContent()) {
			List<ArticleModel> articleModels = new ArrayList<>();
			pagedResult.getContent().stream().forEach(article->{
				ArticleModel articlemodel =new ArticleModel();
				articlemodel.setSlug(article.getSlug());
				articlemodel.setTitle(article.getTitle());
				articlemodel.setDescription(article.getDescription());
				articlemodel.setBody(article.getBody());
				articlemodel.setTags(article.getTag().stream().map(articleTag -> articleTag.getTagId().getArticleTag()).collect(Collectors.toList()));
				articlemodel.setCreatedAt(article.getAudit().getCreatedAt());
				articlemodel.setUpdatedAt(article.getAudit().getCreatedAt());
				articleModels.add(articlemodel);
			});
			return articleModels;
		} else {
			return new ArrayList<ArticleModel>();
		}
	}



	@Override
	public List<TagMetricModel> getTagOccurence() {
		List<String> tagMetricResult= articleTagRepository.getTagOccurence();
		Set<String> tagMetricSetResult =new HashSet<>(tagMetricResult);
		List<TagMetricModel> tagMetricModels = new ArrayList<>();
		tagMetricSetResult.stream().forEach(tagName->{
			TagMetricModel tagModel =new TagMetricModel();
			tagModel.setTag(tagName);
			tagModel.setOccurence(Collections.frequency(tagMetricResult, tagName));
			tagMetricModels.add(tagModel);
		});
		return tagMetricModels;
	}

	@Override
	public ArticleRead getAverageReadingTime(String slug) {
		Optional<Article> articleList = articleRepository.findBySlug(slug);
		String body = articleList.get().getBody();
		ArticleRead articleRead = new ArticleRead();
		articleRead.setSlug(articleList.get().getSlug());
		int secs=body.split(" ").toString().length()/humanReadingSpeed;
		TimetoRead timetoRead =new TimetoRead();
		timetoRead.setMins(secs/60);
		timetoRead.setSeconds(secs%60);
		articleRead.setTimeToRead(timetoRead);
		return articleRead;
	}



	@Override
	public ArticleModel getArticle(String slug) {
		calendar = Calendar.getInstance();
		Optional<Article> articleList = articleRepository.findBySlug(slug);
		if(articleList.isPresent()) {
			ArticleModel articleModel = new ArticleModel();
			articleModel.setSlug(articleList.get().getSlug());
			articleModel.setTitle(articleList.get().getTitle());
			articleModel.setDescription(articleList.get().getDescription());
			articleModel.setBody(articleList.get().getBody());
			List<Tag> tagList = articleTagRepository.findByTagIdArticleId(articleList.get().getArticleId());
			List<TagId> tagIdList = tagList.stream().map(Tag::getTagId).collect(Collectors.toList());
			if(!tagIdList.isEmpty()) {
				articleModel.setTags(tagIdList.stream().map(TagId::getArticleTag).collect(Collectors.toList()));
			}
			articleModel.setCreatedAt(articleList.get().getAudit().getCreatedAt());
			articleModel.setUpdatedAt(articleList.get().getAudit().getUpdatedAt());
			return articleModel;
		}else {
			throw new ArtileNotFoundException("Article not present");
		}
	}
}