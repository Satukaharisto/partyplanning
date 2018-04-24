package com.party.planner.controller.repository;

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
