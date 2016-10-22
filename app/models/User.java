package models;


import com.avaje.ebean.Model;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by lubuntu on 10/22/16.
 */
@Entity
public class User extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String name;
    public String email;
    public String password;
    @OneToOne
    public Profile profile;
    @ManyToMany
    @JoinTable (name="user_connection",
            joinColumns = {
                    @JoinColumn (name= "user_id")
            },
            inverseJoinColumns = {
                    @JoinColumn (name="connection_id")
            })
    public Set<User> connections;
    @OneToMany(mappedBy = "sender")
    public List<ConnectionRequest> connectionRequestsSent;
    @OneToMany(mappedBy = "receiver")
    public List<ConnectionRequest> connectionRequestsReceived;

    public User(String email, String password) {
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static Model.Finder<Long,User> find=new Model.Finder<Long,User>(User.class);
}
