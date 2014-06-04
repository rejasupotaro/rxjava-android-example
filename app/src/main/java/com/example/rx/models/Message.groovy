package com.example.rx.models

import groovy.transform.TupleConstructor

@TupleConstructor
class Message {

    String phoneNumber

    String messageBody

    @Override
    String toString() {
        return phoneNumber + " : " + messageBody
    }
}
