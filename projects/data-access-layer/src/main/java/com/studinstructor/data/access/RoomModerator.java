package com.studinstructor.data.access;

import java.util.*; 

public interface RoomModerator extends Moderator{

    public List<Student> getMemberInfo(User user);

 }