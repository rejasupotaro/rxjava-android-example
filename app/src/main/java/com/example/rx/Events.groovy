package com.example.rx

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.TextView

import rx.Observable
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject

final class Events {

    static Observable<String> text(TextView view) {
        def currentText = String.valueOf(view.getText())
        def subject = BehaviorSubject.create(currentText)
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                subject.onNext(editable.toString())
            }
        });
        return subject
    }

    static Observable<Object> click(View target) {
        def subject = PublishSubject.create()
        target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subject.onNext(new Object())
            }
        })
        return subject
    }

    static Observable<Integer> itemClick(AbsListView target) {
        def subject = PublishSubject.create()
        target.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                subject.onNext(position)
            }
        })
        return subject
    }

    private Events() {
    }
}
