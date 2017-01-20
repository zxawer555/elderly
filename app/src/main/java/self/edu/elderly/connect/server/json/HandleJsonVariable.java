package self.edu.elderly.connect.server.json;

import java.util.HashMap;

/**
 * Created by Creasant on 4/8/2015.
 */
public class HandleJsonVariable {

    //<for receive Json String use
    public String eid;
    public String identifier;


    private static HandleJsonVariable _Instance;

    public static HandleJsonVariable getInstance() {
        if (_Instance == null) {
            _Instance = new HandleJsonVariable();
        }
        return _Instance;
    }

    public HandleJsonVariable() {
    }

    public HandleJsonVariable hashMapToList(HashMap<String, String> hashMapVarName) {
        //<for receive Json String use
        HandleJsonVariable jsonVariableController = new HandleJsonVariable();
        jsonVariableController.eid = hashMapVarName.get("eid");
        jsonVariableController.identifier = hashMapVarName.get("identifier");

        return jsonVariableController;
    }

}
