package com.ticketing.project.execption.location;

import com.ticketing.project.execption.messages.ErrorMessages;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException() {
        super(ErrorMessages.LOCATION_NOT_FOUND);
    }
}
