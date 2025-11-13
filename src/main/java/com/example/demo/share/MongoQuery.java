package com.example.demo.share;

//import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongoQuery {

    private MongoQuery(){}

    public Query byField(String field, Object value){
        if(value == null){
            throw new IllegalArgumentException("Value field cannot be empty");
        }
        return new Query(Criteria.where(field).is(value));
    }

//    public <T> Query update(T entity){
//        if(entity == null){
//            throw new IllegalArgumentException("No values to update");
//        }
//
//
//    }
}
