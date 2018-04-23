package com.party.planner.controller;

public class RepositoryExceptions extends RuntimeException{
    public RepositoryExceptions(){

    }

    public RepositoryExceptions(String message){
        super(message);
    }

    public RepositoryExceptions (String message, Throwable cause){
        super (message, cause);
    }

}
