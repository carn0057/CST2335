package com.mcterni.androidlabs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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


    //get a database:
    private MyDatabaseOpenHelper dbOpener;
    private SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //You only need 2 lines in onCreate to actually display data:
        ListView theList = findViewById(R.id.list_chat);
        Button sendButton = findViewById(R.id.button_send);
        Button receiveButton = findViewById(R.id.button_receive);

        //Initialize the database:
        dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String [] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGE, MyDatabaseOpenHelper.COL_IS_SEND};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //print the result of the query
        printCursor(results);

        //find the column indices:
        int messageColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int isSendColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_IS_SEND);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String message = results.getString(messageColumnIndex);
            boolean isSend = Boolean.parseBoolean(results.getString(isSendColIndex));
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            messages.add(new MyMessage(message, isSend, id));
        }

        //setAdapter
        theList.setAdapter( myAdapter = new MyListAdapter() );
        //theList.setOnItemClickListener( ( lv, vw, pos, id) ->{ } );


        sendButton.setOnClickListener( click -> setMessage(true));
        receiveButton.setOnClickListener( click -> setMessage(false));

    }

    private MyMessage addMessageToDB(boolean isSend){
        EditText editText = findViewById(R.id.edit_chat);
        String message = editText.getText().toString();
        String strIsSend = Boolean.toString(isSend);

        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //put string name in the NAME column:
        newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, message);
        //put string email in the EMAIL column:
        newRowValues.put(MyDatabaseOpenHelper.COL_IS_SEND, strIsSend);
        //insert in the database:
        long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

        return new MyMessage(message, isSend, newId);
    }
    private void setMessage(boolean isSend){
        EditText editText = findViewById(R.id.edit_chat);
        if(!editText.getText().toString().equals("")){

            messages.add(addMessageToDB(isSend));
            myAdapter.notifyDataSetChanged(); //update yourself
            editText.setText("");

//            SwipeRefreshLayout refresher = findViewById(R.id.refresher);
//            refresher.setOnRefreshListener(() -> {
//
//                messages.add(new MyMessage(editText.getText().toString(), isSend));
//                myAdapter.notifyDataSetChanged(); //update yourself
//                editText.setText("");
//                refresher.setRefreshing(false);  //get rid of spinning wheel;
//            });
        }
    }
    private class MyMessage{

        long id;
        String message;
        boolean isSend;

        MyMessage(String message, boolean isSend, long id){
            this.message=message;
            this.isSend = isSend;
            this.id = id;
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
        TextView messageText = thisRow.findViewById(R.id.numberField);
        RelativeLayout.LayoutParams layoutParamsImage = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams layoutParamsMessage = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        //set layout parameters for image
        layoutParamsImage.addRule(isSend? RelativeLayout.ALIGN_PARENT_LEFT
                : RelativeLayout.ALIGN_PARENT_RIGHT);
        //set layout parameters for message
        layoutParamsMessage.addRule(isSend?RelativeLayout.RIGHT_OF:RelativeLayout.LEFT_OF
                                    ,thisRow.findViewById(R.id.image_chat).getId());
        //set image
        image.setImageResource(isSend? R.drawable.row_send : R.drawable.row_receive);
        image.setLayoutParams(layoutParamsImage);
        //set message
        messageText.setText(s);
        messageText.setLayoutParams(layoutParamsMessage);
    }

    private void printCursor(Cursor c){
        Log.d("Chat Room", "Database Version NUmber: " + MyDatabaseOpenHelper.VERSION_NUM);
        Log.d("Chat Room", "Number of Columns: " + c.getColumnCount());

        String colNames = "";
        for (String colName : c.getColumnNames()) {
            colNames = colNames + colName + ", ";
        }
        //clean the last comma
        Log.d("Chat Room", "Column Names: " + colNames.substring(0,colNames.length() - 2));

        Log.d("Chat Room", "Number of Results in the Cursor: " + c.getCount());

        while(c.moveToNext()){
            String results = "";
            for (String colName : c.getColumnNames()) {
                results = results + c.getString(c.getColumnIndex(colName)) + ", ";
            }
            Log.d("Chat Room", "Column Names: " + results.substring(0,results.length() - 2));
        }

        //reset cursor before the first element
        c.moveToFirst();
        c.moveToPrevious();


    }
}
