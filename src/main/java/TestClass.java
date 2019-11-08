import org.json.JSONArray;

public class TestClass {
    public static void main(String[] args){
        SlackModel slackModel = new SlackModel();
        MongoDBModel mongoDBModel = new MongoDBModel();
//        mongoDBModel.doWrite(slackModel.filterRawMessage("CMR6SBLRJ"),"Messages");
//        mongoDBModel.doWrite(slackModel.getAllUserInfo(), "Users");
        JSONArray jsonArray = mongoDBModel.getCollectionData("Messages");
        mongoDBModel.getCollectionData("Users");
    }
}
