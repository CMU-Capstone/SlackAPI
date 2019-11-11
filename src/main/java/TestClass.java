public class TestClass {
    public static void main(String[] args){
        SlackModel slackModel = new SlackModel();
        MongoDBModel mongoDBModel = new MongoDBModel();
//        mongoDBModel.doWrite(slackModel.filterRawMessage("capstone", "0"),"Messages");
//        mongoDBModel.doWrite(slackModel.getAllUserInfo(), "Users");
//        JSONArray jsonArray = mongoDBModel.getCollectionData("Messages");
//        mongoDBModel.getCollectionData("Users");
        System.out.println(mongoDBModel.getNewestTimeStamp("Messages"));
//        mongoDBModel.deleteAll("Users");
//        mongoDBModel.deleteAll("Messages");
    }
}
