package controllers;

import models.ConnectionRequest;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by lubuntu on 10/23/16.
 */
public class RequestControllers extends Controller{
    public Result sendRequest(Long sid,Long rid){
            if(sid==null||rid==null|| User.find.byId(sid)==null||User.find.byId(rid)==null)
                return ok();
        else
            {
                ConnectionRequest connectionRequest=new ConnectionRequest();
                connectionRequest.sender.id = sid;
                connectionRequest.receiver.id = rid;
                connectionRequest.status = ConnectionRequest.Status.WAITING;
                ConnectionRequest.db().save(connectionRequest);
                return ok();

            }
    }
    public Result acceptRequest(Long sid,Long rid){
        ConnectionRequest acceptRequest= ConnectionRequest.find.byId(rid);
        acceptRequest.status= ConnectionRequest.Status.ACCEPTED;
        ConnectionRequest.db().save(acceptRequest);
        acceptRequest.sender.connections.add(acceptRequest.receiver);
        acceptRequest.receiver.connections.add(acceptRequest.sender);
        User.db().save(acceptRequest.sender);
        User.db().save(acceptRequest.receiver);
        return ok();
    }
}
