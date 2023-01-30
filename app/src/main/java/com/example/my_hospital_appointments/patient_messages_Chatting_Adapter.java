package com.example.my_hospital_appointments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//declare a class called  recyclerViewer class that extends the RecyclerView.Adapter
public class patient_messages_Chatting_Adapter extends RecyclerView.Adapter<patient_messages_Chatting_Adapter.myViewHolder> {

private ArrayList<String> message=new ArrayList<>();
private ArrayList<String> newMessage=new ArrayList<>();
private ArrayList<String> userMessage=new ArrayList<>();
private ArrayList <Patient_Messages> myMessageList=new ArrayList<>();
private String myText;
private static int myCounter=0;

    private static final int SENT_MESSAGE_TYPE = 0;
    private static final int RECEIVED_MESSAGE_TYPE = 1;

    public void setData(ArrayList<String> myList) {
        this.message=myList;

      notifyDataSetChanged();

    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
    //public EditText myEditText;
    public TextView sentMessageTextView;
    public TextView receivedMessageTextView;

        public Button myButton;
        //a constructor
        public myViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if(viewType==SENT_MESSAGE_TYPE) {
                sentMessageTextView = (TextView) itemView.findViewById(R.id.sentTextViewMessage);

            }else {
                receivedMessageTextView = (TextView) itemView.findViewById(R.id.receivedTextViewMessage);
            }



        }
    }

    public patient_messages_Chatting_Adapter(ArrayList<String> myMessages)
    {
       this.message =myMessages;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myView;


        if(viewType==SENT_MESSAGE_TYPE )
        {
            myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_viewer_item_sent_message,parent, false );

        }
        else
        {
            myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_viewer_item_received_message,parent, false );

        }



                myViewHolder mv = new myViewHolder(myView,viewType);

        return mv;
    }


    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        if (getItemViewType(position) == SENT_MESSAGE_TYPE) {
            String original=message.get(position);
            String desired = original.substring(original.indexOf(":")+2);
            holder.sentMessageTextView.setText(desired);
        } else {
            String original=message.get(position);
            String desired = original.substring(original.indexOf(":")+2);
            holder.receivedMessageTextView.setText(desired);
        }


    }


    @Override
    public int getItemCount() {
        return message.size();
    }

    @Override
    public int getItemViewType(int position) {   //we use the getItemViewType method to get the view to use in the recycle Viewer




        if(message.get(position).contains("Doc : ")) //IF in the arraylist the text has the words "Sent_" we know its from
                                                    //the sender thus we return the sent_Message_type
        {
            //the message will determine type of view to be used

            return RECEIVED_MESSAGE_TYPE;
        }
        else
        {
            return SENT_MESSAGE_TYPE;
        }




    }
}
