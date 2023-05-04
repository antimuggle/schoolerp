package com.studinstructor.data.access;

import java.sql.Connection;

//make into a sessionbean

public class Admin extends User{
   private int adminId = 0;
   private String name = null;
   private String occupation = null;
   private Connection con = null;


   public Admin(int Admin_Id, Connection con){
      
      super(UserType.ADMIN);
      this.con = con;
      this.adminId = Admin_Id; 
   }



   public static void createThreadModerator(){

   }
   public static void createForumModerator(){

   }
   public static void evokeModerator(){

   }
   public static void  createBanner(){

   }
   public static void assignForum(){

   };
   public static void deassigrForum(){

   };
   public static void closeForum(){
      
   }

   
}
