package com.manish.creditcardscanner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] dataset1;
    private String[] dataset2;
    private String[] dataset3;
    private String[] dataset4;

    public ListAdapter(Context context,
                       int layout,
                       String[] ListArray1,
                       String[] ListArray2,
                       String[] ListArray3,
                       String[] ListArray4) {
        super(context, layout, ListArray1);
        this.context = context;
        this.dataset1 = ListArray1;
        this.dataset2 = ListArray2;
        this.dataset3 = ListArray3;
        this.dataset4 = ListArray4;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview, parent, false);

        TextView tv1 = (TextView) rowView.findViewById(R.id.txnDescriptionTxtVw);
        TextView tv2 = (TextView) rowView.findViewById(R.id.dateTxtVw);
        TextView tv3 = (TextView) rowView.findViewById(R.id.amountTxtVw);
        TextView tv4 = (TextView) rowView.findViewById(R.id.txnTypeTxtVw);

        //TODO : Dyanamic Changes
        tv1.setText("Transfer");
        tv2.setText("March 27, 2020");
        tv3.setText("GBP 123.00");
        tv4.setText("Dr");

        return rowView;
    }
}
