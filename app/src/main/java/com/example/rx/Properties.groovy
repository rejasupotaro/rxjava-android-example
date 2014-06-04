package com.example.rx

import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView


import rx.Observable
import rx.functions.Action1

final class Properties {

    static EnabledProperty enabledFrom(View view) {
        return new EnabledProperty(view)
    }

    static TextProperty textFrom(TextView view) {
        return new TextProperty(view)
    }

    static <T> ArrayAdapterProperty<T> dataSetFrom(ArrayAdapter<T> adapter) {
        return new ArrayAdapterProperty<T>(adapter)
    }

    static class EnabledProperty implements Action1<Boolean> {

        private final View view

        private EnabledProperty(View view) {
            this.view = view
        }

        void set(Observable<Boolean> observable) {
            observable.subscribe(this)
        }

        @Override
        void call(Boolean enabled) {
            view.setEnabled(enabled)
        }
    }

    static class TextProperty implements Action1<String> {

        private final TextView view

        private TextProperty(TextView view) {
            this.view = view
        }

        void set(Observable<String> observable) {
            observable.subscribe(this)
        }

        @Override
        void call(String text) {
            view.setText(text)
        }
    }

    static class ArrayAdapterProperty<T> implements Action1<List<T>> {

        private final ArrayAdapter<T> adapter

        ArrayAdapterProperty(ArrayAdapter<T> adapter) {
            this.adapter = adapter
        }

        void set(Observable<List<T>> observable) {
            observable.subscribe(this)
        }

        @Override
        void call(List<T> items) {
            adapter.clear()
            for (T item : items) {
                adapter.add(item)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private Properties() {
    }
}
