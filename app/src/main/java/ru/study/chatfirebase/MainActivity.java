package ru.study.chatfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseListAdapter<Message> adapter;
    private FloatingActionButton buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        auth = FirebaseAuth.getInstance();

        buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textMessage = findViewById(R.id.editTextMessage);
                if (textMessage.getText().toString().equals("")) {
                    return;
                }
                FirebaseDatabase.getInstance().getReference().push().setValue(
                        new Message(auth.getCurrentUser().getEmail(),
                                textMessage.getText().toString())
                );
                textMessage.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        updateUI(user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        auth.signOut();
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Intent intent = new Intent(MainActivity.this,
                    EmailPasswordActivity.class);
            startActivity(intent);
            finish();
        } else {
            displayAllMessages();
        }
    }

    private void displayAllMessages() {
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .limitToLast(50);
        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLayout(R.layout.list_item)
                .setLifecycleOwner(this)
                .build();
        adapter = new FirebaseListAdapter<Message>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Message model, int position) {
                TextView messageUser, messageTime, messageText;
                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);
                messageText = v.findViewById(R.id.message_text);
                messageUser.setText(model.getUserName());
                messageTime.setText(DateFormat
                        .format("dd-MM-yyyy HH:mm:ss", model.getMessageTime()));
                messageText.setText(model.getTextMessage());
            }
        };
        listOfMessages.setAdapter(adapter);
    }
}