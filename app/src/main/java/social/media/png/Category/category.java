package social.media.png.Category;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import social.media.png.MainActivity;
import social.media.png.R;
import social.media.png.util.Global;

public class category extends AppCompatActivity {
    static Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ListView category_list = (ListView)findViewById(R.id.list_category);
        next = (Button)findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_category();
                Intent intent = new Intent(category.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ArrayList<String> categories = new ArrayList<String>();
        int index = R.string.category_1;
        for (; index < R.string.category_29 +1 ; index ++){
            String category = getApplicationContext().getString(index);
            categories.add(category);
        }
        category_Adapter adapter = new category_Adapter(this,R.layout.item_category_list,categories);
        category_list.setAdapter(adapter);
    }

    private void upload_category() {
        String categories = "";
        for(int i = 0; i< Global.selected_id.size(); i ++){
            categories = categories +"," + String.valueOf(Global.selected_id.get(i));
        }

        FirebaseApp.initializeApp(this);
        String id = "Member/" + Global.current_user_name;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(id+"/Category");
        myRef.setValue(categories);
    }

    public static void get_visiblilty(){
        if (Global.selected_id.size()>0)
            next.setVisibility(View.VISIBLE);
        else
            next.setVisibility(View.INVISIBLE);
    }
}
