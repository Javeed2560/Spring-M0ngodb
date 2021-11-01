package com.javeed.controll;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javeed.model.Tutorial;
import com.javeed.repository.TutorialRepository;

@CrossOrigin(origins = "http://loaclhost:8080")
@RestController
@RequestMapping("/api")
public class TutorialController {

	@Autowired
	TutorialRepository tutorialRepository;
		
	
	@GetMapping("/tutorials")
	public ResponseEntity<?> getAllTutorials(@RequestParam(required = false) String title) {
	
		try {
			List<Tutorial> tutorials = new ArrayList<>();
			if(title == null)
				tutorialRepository.findAll().forEach(tutorials::add);
			else
				tutorialRepository.findByTitleContaining(title).forEach(tutorials:: add);
			if(tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(tutorials,HttpStatus.OK);
			
		} catch (Exception e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	@GetMapping("/tutorials/{id}")
	public ResponseEntity<?> getTutorialById(@PathVariable("id") String id) throws Exception{
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
		
		if(tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(),HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/tutorials/published")
	public ResponseEntity<?> findByPublished() {
		
		try {
			List<Tutorial> tutorials = tutorialRepository.findByPublished(true);
			
			if(tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tutorials,HttpStatus.OK);
			
		} catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/tutorials")
	public ResponseEntity<?> createTutorial(@RequestBody Tutorial tutorial){
		try {
			Tutorial _tutorial = tutorialRepository.save(new Tutorial(tutorial.getTitle(),tutorial.getDescription(), false));
			
			return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
		} catch (Exception e) {
			
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PutMapping("/tutorials/{id}")
	public ResponseEntity<?> updateTutorial(@PathVariable("id") String id, @RequestBody Tutorial tutorial){
		
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
		
		if(tutorialData.isPresent()) {
			Tutorial _tutorial = tutorialData.get();
			_tutorial.setTitle(tutorial.getTitle());
			_tutorial.setDescription(tutorial.getDescription());
			_tutorial.setPublished(tutorial.isPublished());
			return new ResponseEntity<>(tutorialRepository.save(_tutorial),HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<HttpStatus>  deleteTutorial(@PathVariable("id") String id){
		try {
			tutorialRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@DeleteMapping("/tutorials")
	public ResponseEntity<?> deleteAllTutorial(){
		try {
			tutorialRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
