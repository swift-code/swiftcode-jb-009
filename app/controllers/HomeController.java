package controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Profile;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.stream.Collectors;

/**
 * Created by lubuntu on 10/23/16.
 */
public class HomeController extends Controller {
    @Inject
    ObjectMapper objectMapper;
     public Result getProfile(long id){
         User user=User.find.byId(id);
         Profile profile=user.profile.find.byId(id);
         ObjectNode data= objectMapper.createObjectNode();

         data.put("id",user.id);
         data.put("email",user.email);
         data.put("firstName",profile.firstName);
         data.put("lastName",profile.lastName);
         data .put("company",profile.company);

         data.set("connections",
                 objectMapper.valueToTree(
                 user.connections.stream().map(
                         connection->{
                            ObjectNode connectionJson=objectMapper.createObjectNode();
                            User connectionUser= User.find.byId(connection.id);
                            Profile connectionProfile= user.profile.find.byId(connection.profile.id);
                            connectionJson.put("email",connectionUser.email);
                            connectionJson.put("firstName",connectionProfile.firstName);
                            connectionJson.put("lastName",connectionProfile.lastName);
                            connectionJson.put("company",connectionProfile.company);
                            return connectionJson;
                         }
                 ).collect(Collectors.toList()))
         );
     }
}
