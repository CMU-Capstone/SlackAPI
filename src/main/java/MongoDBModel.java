import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class MongoDBModel {
    final String MongoURL = "mongodb+srv://testUser:test123@slackcluster-qxoc1.mongodb.net/test?retryWrites=true&w=majority";

    MongoDBModel(){
        final LogManager lm = LogManager.getLogManager();
        for(final Enumeration<String> i = lm.getLoggerNames(); i.hasMoreElements(); ) {
            lm.getLogger( i.nextElement()).setLevel( Level.OFF );
        }
    }

    public MongoDatabase getDatabase(){
        MongoClientURI uri = new MongoClientURI(MongoURL);
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("SlackDatabase");
        return database;
    }
    public void doWrite(JSONArray jsonArrayToWrite, String collectionName){
        MongoClientURI uri = new MongoClientURI(MongoURL);
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("SlackDatabase");
        MongoCollection<Document> collection = database.getCollection(collectionName);
        List<Document> documents = new ArrayList<Document>();
        for(int i = 0; i < jsonArrayToWrite.length(); i++){
            JSONObject tmp = jsonArrayToWrite.getJSONObject(i);
            Document doc = new Document(tmp.toMap());
            documents.add(doc);
        }
        collection.insertMany(documents);
        mongoClient.close();
    }
    public JSONArray getCollectionData(String collectionName){
        MongoClientURI uri = new MongoClientURI(MongoURL);
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("SlackDatabase");
        MongoCollection<Document> collection = database.getCollection(collectionName);
        MongoCursor<Document> cursor = collection.find().iterator();
//        System.out.println("Here is all documents currently stored in the database.");
        JSONArray res = new JSONArray();
        try {
            while (cursor.hasNext()) {
                // parse doc to json
                res.put(new JSONObject(cursor.next().toJson()));
            }
        } finally {
            cursor.close();
        }
//        collection.deleteMany(new Document());
//        System.out.println(collection.count());
        mongoClient.close();
        return res;
    }

}
