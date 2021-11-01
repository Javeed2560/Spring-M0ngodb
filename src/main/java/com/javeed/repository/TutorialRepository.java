package com.javeed.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.javeed.model.Tutorial;

public interface TutorialRepository extends MongoRepository<Tutorial, String> {
	
	List<Tutorial> findByTitleContaining(String title);
	
	List<Tutorial> findByPublished(boolean published);
	

}
