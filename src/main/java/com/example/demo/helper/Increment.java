//package com.example.demo.helper;
//
//import com.example.demo.model.DatabaseSequence;
//import org.springframework.data.mongodb.core.query.Update;
//
//public class Increment {
//
//    private String seqName;
//
//    public Increment(String seqName){
//        this.seqName = seqName;
//    }
//
//    public long generateSequence(String seqName){
//        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
//                new Update().inc("seq",1), options().returnNew(true).upsert(true),
//                DatabaseSequence.class);
//        return !Objects.isNull(counter) ? counter.getSeq() : 1;    }
//}
