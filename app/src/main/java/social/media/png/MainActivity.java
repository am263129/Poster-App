package social.media.png;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import social.media.png.Fragment.Explore;
import social.media.png.Fragment.make_post;
import social.media.png.User.Member;
import social.media.png.util.Global;

public class MainActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private TextView current_user_name, current_user_email;
    private DrawerLayout drawerLayout;
    private ProgressDialog progressDialog;

    private ArrayList<Member> array_all_members = new ArrayList<Member>();

    public static MainActivity myself;
    String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myself = this;
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()){
                    case R.id.menu_Explore:
                        fragment = new Explore();
                        change_mainframe(fragment);
                        drawerLayout.closeDrawer(navigationView);
                        return true;
                    case R.id.menu_new_post:
                        fragment = new make_post();
                        change_mainframe(fragment);
                        drawerLayout.closeDrawer(navigationView);
                        return true;
                    case R.id.menu_term_of_service:
                        return true;
                    case R.id.menu_privacy_policy:
                        return true;
                    case R.id.menu_contact_us:
                        return true;
                    case R.id.menu_about_us:
                        return true;
                    case R.id.menu_profile:
                        return true;
                    default:
                        return true;
                }


            }
        });
        View headerView = navigationView.getHeaderView(0);
        current_user_name = (TextView)headerView.findViewById(R.id.current_user_name);
        current_user_email = (TextView)headerView.findViewById(R.id.current_user_email);

        init_data();


    }

    private void init_data() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Member");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Now data loading");
        progressDialog.show();
//        Toast.makeText(this, myRef.toString(), Toast.LENGTH_LONG).show();
//        myRef.setValue("test@test.com");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is:" + value);
                Global.array_all_members.clear();
                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
//                    Member Member = dataSnapshot.getValue(Member.class);
//                    Log.d(TAG, "User name: " + Member.getName() + ", email " + Member.getEmail());
                    for (String key : dataMap.keySet()){

                        Object data = dataMap.get(key);

                        try{
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            String userName = userData.get("Name").toString();
                            String userEmail = userData.get("Email").toString();
                            String userGender = userData.get("Gender").toString();
                            String password = userData.get("Password").toString();
                            String base64photo = userData.get("Photo").toString();
                            String location = userData.get("Location").toString();

                            if(userEmail.equals(Global.current_user_email)) {
                                Global.current_user_name = userName;
                                current_user_name.setText(Global.current_user_name);
                                current_user_email.setText(Global.current_user_email);
                                Global.current_user_index = array_all_members.size();
                                try {
                                    Global.current_user_photo = base64photo;
                                    setPhoto(base64photo);
                                }
                                catch (Exception e){
                                    Log.e(TAG,e.toString());
                                }
                            }
                            array_all_members.add(new Member(userName, userEmail, userGender, base64photo, location, password));
                        }catch (Exception cce){
                            Log.e(TAG, cce.toString());
                        }

                    }
                    Global.array_all_members = array_all_members;
                    progressDialog.dismiss();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to rad value ", databaseError.toException());
                Toast.makeText(MainActivity.this, "Failed to read Data", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void setPhoto(String base64photo) {
        String imageDataBytes = base64photo.substring(base64photo.indexOf(",")+1);

        InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));

        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        ImageView userPhoto  = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.nav_header_userphoto);
        userPhoto.setImageBitmap(bitmap);
    }


    public void change_mainframe(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    public static MainActivity getInstance(){
        return myself;
    }
}
