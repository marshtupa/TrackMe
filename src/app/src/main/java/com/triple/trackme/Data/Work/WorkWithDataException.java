package com.triple.trackme.Data.Work;

import java.lang.Exception;

public class WorkWithDataException extends Exception {

    WorkWithDataException(String errorMessage) {
        super(errorMessage);
    }

    WorkWithDataException() {
        super();
    }
}
