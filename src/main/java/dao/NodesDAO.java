package dao;

import com.mongodb.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Minglu SHAO
 * Created by Weiwei on 2015/4/27.
 */
public class NodesDAO {

    private DBCollection nodes;

    public NodesDAO(final DB siteDatabase) {
        nodes = siteDatabase.getCollection("nodes");
    }

    public List<DBObject> getHotPages(int topk) {

        // $project
        DBObject fields = new BasicDBObject("url", 1);
        fields.put("inDegree", 1);
        DBObject project = new BasicDBObject("$project", fields);
        // $group
        DBObject groupFields = new BasicDBObject("_id", "$url");
        groupFields.put("nums", new BasicDBObject("$sum", "$inDegree"));
        DBObject group = new BasicDBObject("$group", groupFields);
        // $sort
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("nums", -1));
        //limit,ȡtop20
        DBObject limit = new BasicDBObject("$limit", topk);
        //run
        List<DBObject> pipeline = Arrays.asList(project, group, sort, limit);
        //allowDiskUse
        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
        Cursor cursor = nodes.aggregate(pipeline, options);

        List<DBObject> pageList = new ArrayList<DBObject>();
        while (cursor.hasNext()) {
            DBObject item = cursor.next();
            pageList.add(item);
        }

        return pageList;
    }

    public List<DBObject> getHotPages(String start,String  end) throws ParseException{
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        BasicDBObject[] arrayCond={
                new BasicDBObject("date",new BasicDBObject("$gt",sdf.parse(start))),
                new BasicDBObject("date",new BasicDBObject("$ltegit",sdf.parse(end))),

        };
        BasicDBObject cond=new BasicDBObject();
        cond.put("$and",arrayCond);
        DBObject match=new BasicDBObject("$match",cond);
;

        // $project
        DBObject fields = new BasicDBObject("url", 1);
        fields.put("inDegree", 1);
        DBObject project = new BasicDBObject("$project", fields);
        // $group
        DBObject groupFields = new BasicDBObject("_id", "$url");
        groupFields.put("nums", new BasicDBObject("$sum", "$inDegree"));
        DBObject group = new BasicDBObject("$group", groupFields);
        // $sort
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("nums", -1));

        //run
        List<DBObject> pipeline = Arrays.asList(match,project, group, sort);
        //allowDiskUse
        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).batchSize(10000).build();

        Cursor cursor = nodes.aggregate(pipeline, options);

        List<DBObject> pageList = new ArrayList<DBObject>();
        while (cursor.hasNext()) {
            DBObject item = cursor.next();
            pageList.add(item);
        }

        return pageList;
    }

    public List<DBObject> getHotCategory(int topk) {
        // $match
        DBObject condition=new BasicDBObject("category",new BasicDBObject("$ne",0));
        DBObject match=new BasicDBObject("$match", condition);
        // $project
        DBObject fields = new BasicDBObject("category", 1);
        fields.put("degree", 1);
        DBObject project = new BasicDBObject("$project", fields);
        // $group
        DBObject groupFields = new BasicDBObject("_id", "$category");
        groupFields.put("nums", new BasicDBObject("$sum", "$degree"));
        DBObject group = new BasicDBObject("$group", groupFields);
        // $sort
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("nums", -1));
        //limit,取topk
        DBObject limit = new BasicDBObject("$limit", topk);
        List<DBObject> pipeline = Arrays.asList(match,project,group, sort, limit);
        //allowDiskUse
        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
        Cursor cursor = nodes.aggregate(pipeline, options);
        // output
        List<DBObject> categoryList = new ArrayList<DBObject>();
        while (cursor.hasNext()) {
            DBObject item = cursor.next();
            categoryList.add(item);
        }
        return categoryList;
    }

    public List<DBObject> getHotCategory(String start, String end) throws ParseException{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        BasicDBObject[] arrayCond={
                new BasicDBObject("date",new BasicDBObject("$gt",sdf.parse(start))),
                new BasicDBObject("date",new BasicDBObject("$lte",sdf.parse(end))),
                new BasicDBObject("category",new BasicDBObject("$ne",0)),
        };
        BasicDBObject cond=new BasicDBObject();
        cond.put("$and",arrayCond);
        DBObject match=new BasicDBObject("$match",cond);

        // $project
        DBObject fields = new BasicDBObject("category", 1);
        fields.put("degree", 1);
        DBObject project = new BasicDBObject("$project", fields);
        // $group
        DBObject groupFields = new BasicDBObject("_id", "$category");
        groupFields.put("nums", new BasicDBObject("$sum", "$degree"));
        DBObject group = new BasicDBObject("$group", groupFields);
        // $sort
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("nums", -1));
;
        List<DBObject> pipeline = Arrays.asList(match,project,group, sort);
        //allowDiskUse
        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
        Cursor cursor = nodes.aggregate(pipeline, options);
        // output
        List<DBObject> categoryList = new ArrayList<DBObject>();
        while (cursor.hasNext()) {
            DBObject item = cursor.next();
            categoryList.add(item);
        }
        return categoryList;
    }

}
