package self.edu.elderly.connect.server.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Creasant on 4/8/2015.
 */
public class HandleJsonMethod {

    private static HandleJsonMethod _Instance;

    public static HandleJsonMethod getInstance() {
        if (_Instance == null) {
            _Instance = new HandleJsonMethod();
        }
        return _Instance;
    }

    public List<HandleJsonVariable> convertJsonToVariable(String result,
                                                          String[] varName) {
        JSONObject jsonObj;
        List<HandleJsonVariable> list = new ArrayList<HandleJsonVariable>();
        try {
            jsonObj = new JSONObject(result);
            JSONArray jsonArr = jsonObj.getJSONArray("jsonInfo");
            HashMap<String, String> hashMapVarName = new HashMap<String, String>();

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject obj = jsonArr.getJSONObject(i);

                for (int k = 0; k < varName.length; k++) {
                    if (obj.isNull(varName[k])) {
                        hashMapVarName.put(varName[k], "N/A");
                    } else {
                        hashMapVarName.put(varName[k], obj.getString(varName[k]));
                    }
                }

                list.add(HandleJsonVariable.getInstance().hashMapToList(
                        hashMapVarName));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;

    }

}
