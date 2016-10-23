package controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ConnectionRequest;
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
     public Result getProfile(Long id){
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
                            Profile connectionProfile= connectionUser.profile.find.byId(connection.profile.id);
                            connectionJson.put("email",connectionUser.email);
                            connectionJson.put("firstName",connectionProfile.firstName);
                            connectionJson.put("lastName",connectionProfile.lastName);
                            connectionJson.put("company",connectionProfile.company);
                             return connectionJson;
                         }
                 ).collect(Collectors.toList()))

         );

         data.set("connectionRequests",
                 objectMapper.valueToTree(
                         user.connectionRequestsReceived.stream().filter(
                                 requestStatus ->requestStatus.status.equals(ConnectionRequest.Status.WAITING)).map(
                                 connectionRequest->{
                                     ObjectNode Request=objectMapper.createObjectNode();
                                     Profile senderProfile = Profile.find.byId(connectionRequest.sender.profile.id);
                                     Request.put("firstName",senderProfile.firstName);
                                     Request.put("id",connectionRequest.id);
                                     return Request;

                                 }
                         ).collect(Collectors.toList()))
         );
         User.find.all().stream().
                 filter(x->!user.equals(x)).
                 filter(x->!user.connections.contains(x)).
                 filter(x->!user.connectionRequestsReceived.stream().map(y->y.sender).collect(Collectors.toList()).contains(x)).
                 filter(x->!user.connectionRequestsSent.stream().map(y->y.receiver).collect(Collectors.toList()).contains(x))
                 .map(x->{
                     Profile suggestionProfile = Profile.find.byId(x.profile.id);
                                      ObjectNode suggestionJson=objectMapper.createObjectNode();

                                     suggestionJson.put("firstName",suggestionProfile.firstName);
                                     suggestionJson.put("id",x.id);
                                     return suggestionJson;

                                 }
                         ).collect(Collectors.toList());


         return ok(data);

     }

}



