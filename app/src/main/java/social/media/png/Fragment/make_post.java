package social.media.png.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;

import social.media.png.MainActivity;
import social.media.png.R;
import social.media.png.util.Global;

public class make_post extends Fragment {

    EditText post_title, post_description;
    Spinner post_category;
    ImageView post_image;
    Button upload;

    String TAG = "make_post";

    public static final int PICK_IMAGE = 1;
    public Bitmap bitmap = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_post, container, false);
        post_title = view.findViewById(R.id.edt_post_title);
        post_description = view.findViewById(R.id.edt_post_description);
        post_image = (ImageView)view.findViewById(R.id.post_image);
        upload = (Button)view.findViewById(R.id.btn_upload);
        post_category = (Spinner)view.findViewById(R.id.post_category);
        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (verify()){
                    case 1:
                        Toast.makeText(MainActivity.getInstance(),"Please input Title",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.getInstance(),"Please input Description",Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        upload_post();
                        break;
                }
            }
        });
        return view;
    }

    private void upload_post() {
        FirebaseApp.initializeApp(MainActivity.getInstance());
        String id = "Post/" + Global.current_user_name + "/" +Global.getToday();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(id+"/Title");
        myRef.setValue(post_title.getText().toString());
        myRef = database.getReference(id+"/Description");
        myRef.setValue(post_description.getText().toString());
        myRef = database.getReference(id+"/Category");
        myRef.setValue(post_category.getSelectedItem().toString());
        if (bitmap!=null) {
            myRef = database.getReference(id + "/Image");
            myRef.setValue(getBase64String(bitmap));
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.w(TAG, "Value is:" + value);
                Toast.makeText(MainActivity.getInstance(),"New Post Uploaded Successfully", Toast.LENGTH_SHORT).show();
                make_notification(post_title.getText().toString());
                Fragment fragment = new Explore();
                MainActivity.getInstance().change_mainframe(fragment);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to rad value ", databaseError.toException());
            }
        });
    }

    private void make_notification(String title) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String notification_message;
            String status = "";
            notification_message = "Congratulations! " +  "Your new post "+ title +" uploaded successfully!";
            status = "Late";
            notificationDialog(title, notification_message, status);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog(String creator, String message, String status) {
        NotificationManager notificationManager = (NotificationManager) MainActivity.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, creator, NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription(message);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.getInstance(), NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Png foresight")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("New Post")
                .setContentText(message)
                .setContentInfo(creator);
        notificationBuilder.setSmallIcon(R.drawable.ico_notification);
        notificationManager.notify(1, notificationBuilder.build());
    }

    private int verify() {
        if (post_title.getText().toString().equals(""))
            return 1;
        else if (post_description.getText().toString().equals(""))
            return 2;
        else
            return 0;
    }


    private String getBase64String(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            //TODO: action

            try {
                bitmap = MediaStore.Images.Media.getBitmap(MainActivity.getInstance().getContentResolver(), data.getData());
                post_image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
