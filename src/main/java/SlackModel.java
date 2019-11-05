import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SlackModel {
    final String OAuthToken = "xoxp-741285264438-739107245509-821721964039-5b9ca93766a54da4bd962bdb842f6e76";
    final String botToken = "xoxb-741285264438-808394586211-RAfo6hstnR4NYoZrDpRyB1YD";
    public static void main(String[] args){
        SlackModel slackModel = new SlackModel();
//        slackModel.getChannelList();
//        slackModel.getChannelNames();
//        System.out.println(slackModel.getChannelIDByName("capstone"));
//        slackModel.getChannelMessageHistory("CMR6SBLRJ");
//        slackModel.getMessageTextList("CMR6SBLRJ");
        slackModel.getMessageContainsWord("meeting","CMR6SBLRJ");
    }


    private JSONArray getChannelList(){
        String urlString = "https://slack.com/api/channels.list?token=" + botToken;
        String response = getResponseString(urlString);
//        System.out.println(response);
        JSONObject jsonObject = new JSONObject(response);
        //TODO: find a more graceful way to handle exception, when we can not get channels, might be other reasons
        try{
            JSONArray channelList = jsonObject.getJSONArray("channels");
            return channelList;
        }catch (Exception e){
            System.out.println("Invalid token");
            return null;
        }
    }


    private List<String> getChannelNames(){
        JSONArray channelListJSONArray = getChannelList();
        List<String> result = new ArrayList<String>();
        for(int i = 0; i < channelListJSONArray.length(); i++){
            result.add(channelListJSONArray.getJSONObject(i).get("name").toString());
        }
//        for(int i = 0; i < result.size(); i++){
//            System.out.println(result.get(i));
//        }
        return result;
    }

    //If not found, return null
    private String getChannelIDByName(String channelName){
        JSONArray channelListJSONArray = getChannelList();
        for(int i = 0; i < channelListJSONArray.length(); i++){
            if(channelListJSONArray.getJSONObject(i).get("name").toString().equals(channelName)){
                return channelListJSONArray.getJSONObject(i).get("id").toString();
            }
        }
        return null;
    }

    private JSONArray getChannelMessageHistory(String channelID){
        String urlString = "https://slack.com/api/channels.history?token=" + OAuthToken + "&channel=" + channelID;
        String response = getResponseString(urlString);
//        System.out.println(response);
        JSONObject jsonObject = new JSONObject(response);
        try{
            JSONArray messageList = jsonObject.getJSONArray("messages");
            return messageList;
        }catch (Exception e){
            return null;
        }


    }

    private List<String> getMessageTextList(String channelID){
        JSONArray messageJSONArray = getChannelMessageHistory(channelID);
        if(messageJSONArray == null){
            return null;
        }
        List<String> result = new ArrayList<String>();
        for(int i = 0; i < messageJSONArray.length(); i++){
            JSONObject jsonObject = messageJSONArray.getJSONObject(i);
            result.add(jsonObject.get("text").toString());
        }
//        for(int i = 0; i < result.size(); i++){
//            System.out.println(result.get(i));
//        }
        return result;
    }

    private String getResponseString(String urlString){
        String response = "";
        try {
//            System.out.println(urlString);
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                response += str;
            }
            in.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
       return response;
    }

    private List<String> getMessageContainsWord(String keyword, String channelID){
        List<String> messageTextList = getMessageTextList(channelID);
        if(messageTextList == null){
            return null;
        }
        List<String> result = new ArrayList<String>();
        for(String str : messageTextList){
            if (str.toLowerCase().contains(keyword.toLowerCase())){
                result.add(str);
            }
        }
//        for(String str: result){
//            System.out.println(str);
//        }
        return result;
    }
}
