package com.mcterni.androidlabs;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<MyMessage> messages = new ArrayList<>( );

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

        Button sendButton = findViewById(R.id.button_send);
        sendButton.setOnClickListener( click ->
        {
            setMessage(true);
        });

        Button receiveButton = findViewById(R.id.button_receive);
        receiveButton.setOnClickListener( click ->
        {
                setMessage(false);

        });

//        SwipeRefreshLayout refresher = findViewById(R.id.refresher);
//        refresher.setOnRefreshListener(() -> {
//            refresher.setRefreshing(false);  //get rid of spinning wheel;
//        });

    }

    private void setMessage(boolean isSend){
        EditText editText = findViewById(R.id.edit_chat);
        if(!editText.getText().toString().equals("")){
            messages.add(new MyMessage(editText.getText().toString(), isSend));
            myAdapter.notifyDataSetChanged(); //update yourself
        }
    }
    private class MyMessage{
        String message;
        Boolean isSend;

        MyMessage(String message, boolean isSend){
            this.message=message;
            this.isSend = isSend;
        }
    }


    //Need to add 4 functions here:
    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return messages.size();  } //This function tells how many objects to show

        public MyMessage getItem(int position) {
            return messages.get(position);  }  //This returns the string at position p

        public long getItemId(int p) {
            return p; } //This returns the database id of the item at position p

        public View getView(int p, View recycled, ViewGroup parent)
        {
            View thisRow = recycled;

            if(recycled == null)
                thisRow = getLayoutInflater().inflate(R.layout.table_row_layout, null);

            defineMessage(thisRow, getItem(p).message,getItem(p).isSend);

            return thisRow;
        }
    }

    private void defineMessage(View thisRow, String s, boolean isSend) {

        ImageButton image = thisRow.findViewById(R.id.image_chat  );
        image.setImageResource(isSend? R.drawable.row_send : R.drawable.row_receive);

        TextView messageText = thisRow.findViewById(R.id.numberField);
        messageText.setText(s);


        RelativeLayout messageLayout = (RelativeLayout) (thisRow.findViewById(R.id.messageLayout));
        messageLayout.removeAllViews();


        RelativeLayout.LayoutParams layoutParamsImage = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParamsImage.addRule(isSend? RelativeLayout.ALIGN_PARENT_LEFT
                                    : RelativeLayout.ALIGN_PARENT_RIGHT);
        messageLayout.addView(image,layoutParamsImage);

        RelativeLayout.LayoutParams layoutParamsMessage = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParamsMessage.addRule(isSend?RelativeLayout.RIGHT_OF:RelativeLayout.LEFT_OF
                                    ,thisRow.findViewById(R.id.image_chat).getId());
        messageLayout.addView(messageText,layoutParamsMessage);
    }
}
