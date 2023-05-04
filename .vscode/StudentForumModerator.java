package com.studinstructor.data.access;

import java.sql.Connection; 

public class StudentForumModerator extends Student implements ThreadModerator{
    


    private StudentForumModerator(Student stud, Connection con){
        
        super(stud.getStudId(), con);
        
    }

    protected static synchronized StudentForumModerator createThreadModerator(ForumModerator mod,Student assignee, Forum forum, Connection con){

            StudentForumModerator newnod= new StudentForumModerator(assignee, con);
        
            return newnod;
    } 

}