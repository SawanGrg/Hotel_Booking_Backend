package com.fyp.hotel.exception;

public class HotelAlreadyExist extends RuntimeException{

    /**
     * serialVersionUID = 1L 
     * defines the version of the class which is used for serialization
     * when serializing and deserializing an object,
     * the JVM needs to know which class version the object is based on
     */
    private static final long serialVersionUID = 1L; 

    public HotelAlreadyExist(String message) {
        super(message); // call the constructor of the parent class which is RuntimeException
    }
    
}
