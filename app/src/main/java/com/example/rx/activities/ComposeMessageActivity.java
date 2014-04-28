package com.example.rx.activities;

import com.example.rx.Events;
import com.example.rx.Properties;
import com.example.rx.R;
import com.example.rx.models.Message;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.functions.Func1;

public class ComposeMessageActivity extends Activity {

    @InjectView(R.id.phone_number_edit)
    EditText phoneNumberEditText;

    @InjectView(R.id.message_body_edit)
    EditText messageBodyEditText;

    @InjectView(R.id.remaining_characters_text)
    TextView remainingCharactersTextView;

    @InjectView(R.id.send_message_button)
    Button sendMessageButton;

    @InjectView(R.id.message_list)
    ListView messageListView;

    private ArrayAdapter<String> messageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);
        ButterKnife.inject(this);

        setupUsingRx();
        //setupNotUsingRx();
    }

    private void setupUsingRx() {
        final Observable<String> phoneNumberText = Events.text(phoneNumberEditText);
        final Observable<String> messageBodyText = Events.text(messageBodyEditText);
        final Observable<Object> sendMessageClick = Events.click(sendMessageButton);

        messageBodyText
                .map(text -> !text.trim().equals(""))
                .subscribe(Properties.enabledFrom(sendMessageButton));

        final int maxBodyLength = getResources().getInteger(R.integer.message_body_max_length);
        messageBodyText
                .map(text -> maxBodyLength - text.length())
                .map(remainingChars -> getString(R.string.remaining_characters_text, remainingChars,
                        maxBodyLength))
                .subscribe(Properties.textFrom(remainingCharactersTextView));

        sendMessageClick
                .flatMap(o -> Observable.combineLatest(
                        phoneNumberText,
                        messageBodyText,
                        Message::new
                ).take(1))
                .subscribe(message -> {
                    if (message.getPhoneNumber().trim().equals("")) {
                        phoneNumberEditText.requestFocus();
                    } else {
                        messageBodyEditText.setText("");
                        messageListAdapter.add(message.getMessageBody());
                    }
                });

        messageListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        messageListView.setAdapter(messageListAdapter);
    }

    private void setupNotUsingRx() {
        final int maxBodyLength = getResources().getInteger(R.integer.message_body_max_length);
        remainingCharactersTextView.setText(getString(
                R.string.remaining_characters_text,
                maxBodyLength,
                maxBodyLength));

        messageBodyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (TextUtils.isEmpty(text)) {
                    sendMessageButton.setEnabled(false);
                } else {
                    sendMessageButton.setEnabled(true);

                    int remainingChars = maxBodyLength - text.length();
                    String remainingCharactersText = getString(
                            R.string.remaining_characters_text,
                            remainingChars,
                            maxBodyLength);
                    remainingCharactersTextView.setText(remainingCharactersText);
                }
            }
        });

        sendMessageButton.setOnClickListener((view) -> {
            String phoneNumberText = phoneNumberEditText.getText().toString();
            if (phoneNumberText.trim().equals("")) {
                phoneNumberEditText.requestFocus();
            } else {
                String text = messageBodyEditText.getText().toString();
                messageBodyEditText.setText("");

                String remainingCharactersText = getString(
                        R.string.remaining_characters_text,
                        maxBodyLength,
                        maxBodyLength);
                remainingCharactersTextView.setText(remainingCharactersText);

                messageListAdapter.add(text);
            }
        });

        messageListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        messageListView.setAdapter(messageListAdapter);
    }
}
