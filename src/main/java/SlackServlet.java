import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "SlackServlet", urlPatterns = {"/updateDB"})
public class SlackServlet extends javax.servlet.http.HttpServlet {
    SlackModel slackModel;
    MongoDBModel mongoDBModel;

    @Override
    public void init() {
        // init two model
        slackModel = new SlackModel();
        mongoDBModel = new MongoDBModel();
    }

    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {

//        System.out.println("Console: doGET visited");

        // refresh User Collection
        mongoDBModel.deleteAll("Users");
        mongoDBModel.doWrite(slackModel.getAllUserInfo(), "Users");



        //Update Messages Collection
        String newestTimeStamp = mongoDBModel.getNewestTimeStamp("Messages");
        List<String> channelList = slackModel.getChannelNames();
        for(String channelName : channelList){
            JSONArray jsonArray = slackModel.filterRawMessage(channelName, newestTimeStamp);
            if(jsonArray.length() > 0){
                mongoDBModel.doWrite( jsonArray,"Messages");
            }

        }
        JSONObject result = new JSONObject();
        result.put("result", "Update finish");
        // Things went well so set the HTTP response code to 200 OK
        response.setStatus(200);
        // tell the client the type of the response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // return the value from a GET request
        PrintWriter out = response.getWriter();
        out.println(result);
        out.flush();
    }


}