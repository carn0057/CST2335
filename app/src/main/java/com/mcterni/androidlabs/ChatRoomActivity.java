package com.mcterni.androidlabs;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<String> objects = new ArrayList<>(Arrays.asList("Item 1", "Item 2", "Item 3" ) );

    BaseAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //You only need 2 lines in onCreate to actually display data:
        ListView theList = findViewById(R.id.list_chat);
        theList.setAdapter( myAdapter = new MyListAdapter() );
        theList.setOnItemClickListener( ( lv, vw, pos, id) ->{


        } );

    }


    //Need to add 4 functions here:
    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return objects.size();  } //This function tells how many objects to show

        public String getItem(int position) {
            return objects.get(position);  }  //This returns the string at position p

        public long getItemId(int p) {
            return p; } //This returns the database id of the item at position p

        public View getView(int p, View recycled, ViewGroup parent)
        {
            View thisRow = recycled;

            if(recycled == null)
                thisRow = getLayoutInflater().inflate(R.layout.table_row_layout, null);

            TextView itemText = thisRow.findViewById(R.id.itemField  );
            itemText.setText( "Array at:" + p + " is " + getItem(p) );

            TextView numberText = thisRow.findViewById(R.id.numberField);
            numberText.setText("Your number is " + p);
            return thisRow;
        }
    }
}
