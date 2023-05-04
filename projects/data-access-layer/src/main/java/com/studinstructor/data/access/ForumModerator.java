package com.studinstructor.data.access;

import java.util.*;

public interface ForumModerator extends Moderator{
   
    public default void createSubForum(Forum forum){};
    public default void closeSubForum(Forum forum){};
    public default void createThreadModerator(Student stud, Thread thread){};
    public default void createSubForumModerator(){};
    //create banner object
    public default void createBanner(){};
    public default void setTags(){};
    //be inside an ejb
    public default void uploadHeroImage(){};
    public default void uploadEvent(){};
}