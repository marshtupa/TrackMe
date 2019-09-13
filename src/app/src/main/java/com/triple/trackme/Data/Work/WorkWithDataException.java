package com.triple.trackme.Data.Work;

public class WorkWithDataException extends Exception {

    WorkWithDataException(final String errorMessage) {
        super(errorMessage);
    }

    WorkWithDataException() {
        super();
    }
}
