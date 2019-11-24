package sample.singletons;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sample.models.SustGroup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GroupsProvider {
    private static GroupsProvider ourInstance = new GroupsProvider();

    public static GroupsProvider getInstance() {
        return ourInstance;
    }

    private GroupsProvider() {
    }

    public List<SustGroup> getGroups() {

        try {
            URL url = new URL("https://sustkeys.herokuapp.com/sust_keys");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            System.out.println("Loading...");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            int status = con.getResponseCode();
            if (status == 200) {
                System.out.println("Done...");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();

                System.out.println(content);

                Object obj = new JSONParser().parse(content.toString());

                JSONArray ja = (JSONArray) obj;


                List<SustGroup> groups = new ArrayList<>();


                for (Object object : ja) {
                    JSONObject groupObject = (JSONObject) object;

                    groups.add(new SustGroup((String) groupObject.get("group_name"), (String) groupObject.get("group_public_key")));
                }

                return groups;
            } else {
                System.out.println("Error While uploading data from server...");

                return new ArrayList<SustGroup>();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<SustGroup>();
        }

    }
}




