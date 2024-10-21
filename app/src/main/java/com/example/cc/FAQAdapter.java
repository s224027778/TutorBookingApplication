package com.example.cc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class FAQAdapter extends ArrayAdapter<FAQ> {
    private Context context;
    private List<FAQ> faqList;

    public FAQAdapter(Context context, List<FAQ> faqList) {
        super(context, 0, faqList);
        this.context = context;
        this.faqList = faqList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FAQ faq = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.faq_item, parent, false);
        }

        TextView questionText = convertView.findViewById(R.id.faqQuestion);
        TextView answerText = convertView.findViewById(R.id.faqAnswer);

        questionText.setText(faq.getQuestion());
        answerText.setText(faq.getAnswer());

        return convertView;
    }
}
