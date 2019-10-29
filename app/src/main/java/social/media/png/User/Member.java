package social.media.png.User;

import java.security.PublicKey;

public class Member {
    public String name;
    public String email;
    public String gender;
    public String photo;
    public String location;
    public String password;


    public Member(){

    }
    public Member(String name,
                  String email,
                  String gender,
                  String photo,
                  String location,
                  String password){
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.photo = photo;
        this.location = location;
        this.password = password;
    }

    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public String getGender(){
        return this.gender;
    }
    public String getPhoto() {
        return photo;
    }
    public String getLocation() {
        return location;
    }
    public String getPassword() {
        return password;
    }

}
