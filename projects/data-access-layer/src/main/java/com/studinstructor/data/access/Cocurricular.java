package com.studinstructor.data.access;

import java.util.Set;

public abstract class Cocurricular{
    public abstract Set<User> getMembers();
    public abstract User getCustodian();
    public abstract CocurrType getType();
    public abstract String getName();
}