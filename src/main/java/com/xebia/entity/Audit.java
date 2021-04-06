package com.xebia.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Embeddable;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Audit implements Serializable{
	
	@CreatedDate
	private Calendar createdAt;
	@LastModifiedDate
	private Calendar updatedAt;
	
	
}