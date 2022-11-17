package com.manish.creditcardscanner.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.manish.creditcardscanner.R;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {
    private Context context;
    ArrayList<Transaction> activityArray;

    public ListAdapter(Context context,
                       int layout,
                       ArrayList<Transaction> activityArray) {
        super(context, layout, new String[activityArray.size()]);
        this.context = context;
        this.activityArray = activityArray;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview, parent, false);

        TextView tv1 = (TextView) rowView.findViewById(R.id.txnDescriptionTxtVw);
        TextView tv2 = (TextView) rowView.findViewById(R.id.dateTxtVw);
        TextView tv3 = (TextView) rowView.findViewById(R.id.amountTxtVw);
        TextView tv4 = (TextView) rowView.findViewById(R.id.txnTypeTxtVw);

        Transaction transaction = this.activityArray.get(position);
        tv1.setText(transaction.getTxnDescriptionTxtVw());
        tv2.setText(transaction.getDateTxtVw());
        tv3.setText(transaction.getAmountTxtVw());
        tv4.setText(transaction.getTxnTypeTxtVw());

        return rowView;
    }
}
