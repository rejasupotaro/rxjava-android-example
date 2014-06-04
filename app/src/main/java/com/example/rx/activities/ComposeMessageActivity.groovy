package com.example.rx.activities

import android.app.Activity
import android.os.Bundle
import android.widget.*
import com.example.rx.Events
import com.example.rx.Properties
import com.example.rx.R
import com.example.rx.models.Message
import rx.Observable

class ComposeMessageActivity extends Activity {

    private EditText phoneNumberEditText
    private EditText messageBodyEditText
    private TextView remainingCharactersTextView
    private Button sendMessageButton
    private ListView messageListView
    private ArrayAdapter<String> messageListAdapter

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose_message)
        findViews()
        setupViews()
        setDummyData()
    }

    private void findViews() {
        phoneNumberEditText = findViewById(R.id.phone_number_edit)
        messageBodyEditText = findViewById(R.id.message_body_edit)
        remainingCharactersTextView = findViewById(R.id.remaining_characters_text)
        sendMessageButton = findViewById(R.id.send_message_button)
        messageListView = findViewById(R.id.message_list)
    }

    private void setupViews() {
        def phoneNumberText = Events.text(phoneNumberEditText)
        def messageBodyText = Events.text(messageBodyEditText)
        def sendMessageClick = Events.click(sendMessageButton)

        messageBodyText
                .map({ text -> !text.trim().equals("") })
                .subscribe(Properties.enabledFrom(sendMessageButton))

        def maxBodyLength = getResources().getInteger(R.integer.message_body_max_length)
        messageBodyText
                .map({ text -> maxBodyLength - text.length() })
                .map({ remainingChars -> getString(R.string.remaining_characters_text, remainingChars, maxBodyLength) })
                .subscribe(Properties.textFrom(remainingCharactersTextView))

        sendMessageClick
                .flatMap({
                    Observable.combineLatest(phoneNumberText, messageBodyText,
                            { phoneNumber, messageBody -> new Message(phoneNumber, messageBody) }).take(1)})
                .subscribe({ message ->
                    if (!message.getPhoneNumber()?.trim()?.equals("")) {
                        messageBodyEditText.setText("")
                        messageListAdapter.add(message.getMessageBody())
                    } else {
                        phoneNumberEditText.requestFocus()
                    }
                })

        messageListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        messageListView.setAdapter(messageListAdapter)
    }

    private void setDummyData() {
        messageListAdapter.addAll(
                (1..10).collect({
                    new Message("xxx-xxxx", "message ${it}") as String
                })
        )
    }
}
