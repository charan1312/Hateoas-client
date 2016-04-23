package com.restbucks.ordering.repositories;

import java.util.HashMap;
import java.util.Map.Entry;

import com.restbucks.ordering.model.Appeal;
import com.restbucks.ordering.model.Grade;
import com.restbucks.ordering.model.GradeItem;
import com.restbucks.ordering.model.Identifier;
import com.restbucks.ordering.model.Order;
import com.sun.jersey.spi.resource.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class GradeRepository {
    
    private static final Logger LOG = LoggerFactory.getLogger(GradeRepository.class);

    private static final GradeRepository theRepository = new GradeRepository();
    private HashMap<Integer, Grade> backingStore = new HashMap<>(); // Default implementation, not suitable for production!

    public static GradeRepository current() {
        return theRepository;
    }
    
    public GradeRepository(){
        LOG.debug("GradeRepository Constructor");
        loadSomeGrades();
    }
    
    private void loadSomeGrades() {
        // TODO Auto-generated method stub
        Grade grades1a1 =  new Grade(1,1,80,"NA",GradeItem.ASSIGNMENT);
        Grade grades1a2 =  new Grade(2,2,81,"NA",GradeItem.ASSIGNMENT);
        Grade grades1l1 =  new Grade(3,1,82,"NA",GradeItem.LAB);
        Grade grades2a1 =  new Grade(4,1,83,"NA",GradeItem.ASSIGNMENT);
        Grade grades2a2 =  new Grade(5,2,84,"NA",GradeItem.ASSIGNMENT);
        Grade grades2l1 =  new Grade(6,1,85,"NA",GradeItem.LAB);
        Grade grades3a1 =  new Grade(7,1,86,"NA",GradeItem.ASSIGNMENT);
        Grade grades3a2 =  new Grade(8,2,87,"NA",GradeItem.ASSIGNMENT);
        Grade grades3l1 =  new Grade(9,1,88,"NA",GradeItem.LAB);
        this.backingStore.put(1,grades1a1);
        this.backingStore.put(2,grades1a2);
        this.backingStore.put(3,grades1l1);
        this.backingStore.put(4,grades2a1);
        this.backingStore.put(5,grades2a2);
        this.backingStore.put(6,grades2l1);
        this.backingStore.put(7,grades3a1);
        this.backingStore.put(8,grades3a2);
        this.backingStore.put(9,grades3l1);
    }
    
    public Grade get(Integer id) {
        LOG.debug("Retrieving Grade object for id : {} ", id);
        return backingStore.get(id);
     }
    
    public void store(Grade grade) {
        LOG.debug("Storing Grade object with id : {} ", grade.getGradeID());
        backingStore.put(grade.getGradeID(), grade);
    }

    public boolean has(Integer id) {
        LOG.debug("Checking to see if there is an Grade object associated with the id {} in the Grade store", id);
        
        boolean result =  backingStore.containsKey(id);
        LOG.debug("The result of the search is {}", result);
        
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Entry<Integer, Grade> entry : backingStore.entrySet()) {
            sb.append(entry.getKey());
            sb.append("\t:\t");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    public synchronized void clear() {
        backingStore = new HashMap<>();
    }

    public int size() {
        return backingStore.size();
    }
}