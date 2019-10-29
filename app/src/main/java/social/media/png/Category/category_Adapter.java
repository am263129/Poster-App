package social.media.png.Category;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.constraintlayout.solver.GoalRow;

import java.util.ArrayList;

import social.media.png.R;
import social.media.png.util.Global;

public class category_Adapter extends ArrayAdapter<String> implements Filterable {

    TextView category_name;
    CheckBox category_selector;
    ArrayList<String> categories = new ArrayList<String>();
    String TAG = "Category_adapter";
    public category_Adapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        categories = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_category_list, null);

        category_name = (TextView)v.findViewById(R.id.name_category);
        category_name.setText(categories.get(position).toString());
        category_selector = (CheckBox) v.findViewById(R.id.cb_category);
        for (int i = 0; i < Global.selected_id.size(); i ++){
            if (Global.selected_id.get(i) == position)
            {
                category_selector.setChecked(true);
                break;
            }
        }
        category_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if(Global.selected_id.contains(position)) {
                        Object o = position;
                        boolean result = Global.selected_id.remove(o);
                    }
                    else
                        Global.selected_id.add(position);
                    category.get_visiblilty();
                }
                catch (Exception E){}
            }
        });
        return v;

    }
}