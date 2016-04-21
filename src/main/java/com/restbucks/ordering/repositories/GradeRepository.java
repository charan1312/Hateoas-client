package com.restbucks.ordering.repositories;

import java.util.HashMap;
import java.util.Map.Entry;

import com.restbucks.ordering.model.Appeal;
import com.restbucks.ordering.model.Grade;
import com.restbucks.ordering.model.Identifier;
import com.restbucks.ordering.model.Order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GradeRepository {
    
    private static final Logger LOG = LoggerFactory.getLogger(GradeRepository.class);

    private static final GradeRepository theRepository = new GradeRepository();
    private HashMap<Integer, Grade> backingStore = new HashMap<>(); // Default implementation, not suitable for production!

    public static GradeRepository current() {
        return theRepository;
    }
    
    private GradeRepository(){
        LOG.debug("GradeRepository Constructor");
    }
    
    public Grade get(Integer id) {
        LOG.debug("Retrieving Grade object for id :", id);
        return backingStore.get(id);
     }
    
    public void store(Grade grade) {
        LOG.debug("Storing Grade object with id:", grade.getGradeID());
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