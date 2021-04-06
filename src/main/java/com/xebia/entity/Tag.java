package com.xebia.entity;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
	@EmbeddedId
	private TagId tagId;
	@OneToOne
	@JoinColumn(name="articleId", insertable = false, updatable = false)
	@EqualsAndHashCode.Exclude
	@JsonIgnore
	@ToString.Exclude
	private Article article;
	@Embedded
	private Audit audit;
	
}