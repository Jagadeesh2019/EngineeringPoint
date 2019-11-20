package com.example.myengineeringpoint.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.myengineeringpoint.R;
import com.example.myengineeringpoint.models.GetPapersModel;

import java.util.ArrayList;

public class GetPapersActivity extends AppCompatActivity {

    private ListView hold_papers_listView;
    public ArrayList<GetPapersModel> getPapersModelArrayList;
    public MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_papers);

        hold_papers_listView = findViewById(R.id.hold_papers_listView);

        getPapersModelArrayList = new ArrayList<>();
        GetPapersModel getPapersModel1 = new GetPapersModel("06CS32","VTU Electronic circuits DEC 2010 Question Paper",
                "Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel2 = new GetPapersModel("06CS32","VTU Electronic circuits DEC 2011 Question Paper",
                "Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel3 = new GetPapersModel("06CS32","VTU Electronic circuits JAN 2008 Question Paper","Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel4 = new GetPapersModel("10CS32","VTU Electronic circuits JAN 2009 Question Paper","Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel5 = new GetPapersModel("15CS32","VTU Electronic circuits JAN 2010 Question Paper","Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel6 = new GetPapersModel("17CS32","VTU Electronic circuits JULY 2008 Question Paper","Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel7 = new GetPapersModel("02CS32","VTU Electronic circuits JULY 2009 Question Paper","Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel8 = new GetPapersModel("06CS32","VTU Electronic circuits JULY 2011 Question Paper","Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel9 = new GetPapersModel("06CS32","VTU Electronic Circuits JAN 2013 Question Paper","Electronic circuits Previous Question Papers");

        getPapersModelArrayList.add(getPapersModel1);
        getPapersModelArrayList.add(getPapersModel2);
        getPapersModelArrayList.add(getPapersModel3);
        getPapersModelArrayList.add(getPapersModel4);
        getPapersModelArrayList.add(getPapersModel5);
        getPapersModelArrayList.add(getPapersModel6);
        getPapersModelArrayList.add(getPapersModel7);
        getPapersModelArrayList.add(getPapersModel8);
        getPapersModelArrayList.add(getPapersModel9);

        myAdapter = new MyAdapter();
        myAdapter.notifyDataSetChanged();
        hold_papers_listView.setAdapter(myAdapter);

    }

    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return getPapersModelArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return getPapersModelArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GetPapersModel getPapersModel = getPapersModelArrayList.get(position);
            View v = getLayoutInflater().inflate(R.layout.hold_papers_list_item,null);
            AppCompatTextView subject_code_textView = v.findViewById(R.id.papers_item_ll_banner);
            AppCompatTextView paper_title_textView = v.findViewById(R.id.papers_item_ll_paper_title);
            AppCompatTextView paper_sub_title_textView = v.findViewById(R.id.papers_item_ll_paper_sub_title);
            subject_code_textView.setText(getPapersModel.getSubjectCode());
            paper_title_textView.setText(getPapersModel.getPaperName());
            paper_sub_title_textView.setText(getPapersModel.getPaperSubName());
            return v;
        }
    }
}
