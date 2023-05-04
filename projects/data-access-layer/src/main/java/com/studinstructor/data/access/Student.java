package com.studinstructor.data.access;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.sql.rowset.spi.SyncProviderException;

enum ModeratorValue{
    THREAD, FORUM, SUBFORUM, ROOM;
}

//provide refresh values static method to refresh serializable values
//provide save static method to commit changes with batch updates

public final class Student extends User{
    private int studId = 0;
    private String courseName = null;
    private String name = null;
    private String email = null;
    private String username = null;
    private Set<Contact> contacts = null;
    //add badges
    private Connection con = null;
    private CachedRowSet sportrowset = null;
    private CachedRowSet activityrowset = null;
    private CachedRowSet associationrowset = null;
    private CachedRowSet clubrowset = null;



   public  Student(int studId, Connection con) throws IllegalArgumentException{
        super(UserType.STUDENT);
        if(studId == 0) throw new IllegalArgumentException();
        this.studId = studId;
        this.con = con;
    }

    public Student(String username, Connection con){ 
        super(UserType.STUDENT);
        try{
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("SELECT Stud_Id from v_StudentInfo WHERE username=" + username,
                                                            ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            ResultSet set = stmt.executeQuery();
            int studId = set.getInt(1);
            if(studId==0)
                //set inconsistent state error  
                
            this.studId = studId;    
        }
        catch(SQLException  ex){
            ex.printStackTrace();
        }
        this.username= username;
        this.con = con;
    }
//static function to check for at least one cocurricular activity
/* 
    protected Student(int studId, LocalDate dob, String courseName, 
                        String username,String name, List<? extends Cocurricular> cocurriculars, Connection con){
        
        super();
        this.username = username;
        this.courseName = getCourse();
        this.name = getName();
        this.con = con;
        sortCocurriculars(cocurricular);

                    
    }
*/
    //sort coccurs
    private static void sortCocurriculars(List<? extends Cocurricular> cocurricular,boolean checkforupdates){

    }

    public int getStudId() throws IllegalStateException{
       if(this.studId == 0)
            throw new IllegalStateException();

       return this.studId;
    }
    
    
    
    public String getCourse(){
        if(this.courseName != null || this.courseName != ""){
            
            return this.courseName;
        }
        else{   
            try{
                con.setAutoCommit(false);
                PreparedStatement stmt = con.prepareStatement("SELECT Course_Name FROM v_studentcoursenames WHERE Stud_Id=" + this.studId,
                                                                    ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                ResultSet set = stmt.executeQuery();
                String course = set.getString(1);
                this.courseName = course;
            }
            catch(SQLException  ex){
                ex.printStackTrace();
            }
        }
        return this.courseName;
    }
    
    public void setUserName(String name) throws IllegalStateException{
        try{
            CallableStatement stmt = con.prepareCall("EXEC pr_changeUserName("+this.studId+ ",'"+ this.name +"')");
            stmt.execute();    

        }
        catch(SQLException sql){
            sql.printStackTrace();
        }
        this.username = name;

    }

    public String getUserName(boolean checkupdates){
        if((!checkupdates) && (this.username !=null || this.username!="" )){

                return this.username;
        }            
        else{

            //throw exeption if the username keeps returning  null after database fetch to prevent looping
            try{
                con.setAutoCommit(false);
                PreparedStatement stmt = con.prepareStatement("SELECT username FROM v_StudentInfo WHERE Stud_Id=" + this.studId,
                                                                ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                ResultSet set = stmt.executeQuery();
                String username = set.getString(1);
                if (username== "" || username == null)
                    //specify exception message
                    throw new IllegalArgumentException();
                this.username= username;
                

            }
            catch(SQLException  ex){
                ex.printStackTrace();
            }
        }
        return this.username;   
    }

    public String getName(){
        if(this.name == null || this.name == "")
            try{
                con.setAutoCommit(false);
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM v_Studentname WHERE Stud_Id=" + this.studId,
                                                                ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                ResultSet set = stmt.executeQuery();
                StringBuffer namebuilder = new StringBuffer();
                set.first();
                namebuilder.append(set.getString("F_Name"));
                namebuilder.append(set.getString("S_Name"));
                namebuilder.append(set.getString("L_Name"));
                this.name = namebuilder.toString();
        }
        catch(SQLException  ex){
            ex.printStackTrace();
        }
         return this.name;
    }

    //move all setters to ejb and leave this as a data access object
    public Set<Club> getClubs(boolean checkforupdates){ 
        Set<Club> list = new HashSet<Club>();
        if(clubrowset == null || checkforupdates){
                initClubrowSet();
        }
        try{
            clubrowset.first();
            while (clubrowset.next()){
                Club club = new Club(clubrowset.getInt("Club_Id"), this.con);
                list.add(club); 
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

        return list;
    }

    public void addClubs(Set<Club> clubs, boolean update) throws SyncProviderException{
        if(clubrowset == null){
            initClubrowSet();
        }
        try{
            for(Club club: clubs){
                clubrowset.moveToInsertRow();
                clubrowset.updateInt("Stud_Id",this.studId);
                clubrowset.updateInt("Club_Id", club.getClubId());
                clubrowset.insertRow();
                clubrowset.moveToCurrentRow();
            }
            if(update){
                update(activityrowset);
                clubrowset.close();
                clubrowset = null;
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    private void initClubrowSet(){
       
        try{
            RowSetFactory factory = RowSetProvider.newFactory();
            clubrowset = factory.createCachedRowSet();
            clubrowset.setCommand("select * from CLUB_MEMBERS where Stud_Id ="+ this.studId);
            clubrowset.execute(con);
            int[] keys={1,2};
            clubrowset.setKeyColumns(keys);
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

    }

    //catch the exception in the ejbs
    private void update(CachedRowSet rowset) throws SyncProviderException{
        rowset.acceptChanges();    
    }

    public void removeClubs(Set<Club> clubs){
        for(Club club: clubs){
            try{
                clubrowset.first();
                while(clubrowset.next()){
                
                    if(club.getClubId()==clubrowset.getInt("Club_Id"))  
                    clubrowset.deleteRow();
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }
    
    public Set<Sport> getSports(boolean checkforupdates){
        Set<Sport> list = new HashSet<Sport>();
        if(sportrowset == null || checkforupdates){
                initSportrowSet();
        }
        try{
            sportrowset.first();
            while (sportrowset.next()){
                list.add(new Sport(sportrowset.getInt("Sport_Id"), con));
            }
        }
        catch(SQLException ex){

            ex.printStackTrace();
        }
        return list;
    }

    public void addSports(Set<Sport> sports, boolean update){
        if(sportrowset == null){
            initClubrowSet();
        }
        try{
            for(Sport sport: sports){
                clubrowset.moveToInsertRow();
                clubrowset.updateInt("Stud_Id",this.studId);
                clubrowset.updateInt("Sport_Id", sport.getId());
                clubrowset.insertRow();
                clubrowset.moveToCurrentRow();
            }
            if(update){
                update(activityrowset);
                clubrowset.close();
                clubrowset = null;
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void removeSports(Set<Sport> sports){
        for(Sport sport: sports){
            try{
                sportrowset.first();
                while(sportrowset.next()){
                    if(sport.getId()==sportrowset.getInt("Sport_Id"))  
                        sportrowset.deleteRow();
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    private void initSportrowSet(){
        try{
            RowSetFactory factory = RowSetProvider.newFactory();
            sportrowset = factory.createCachedRowSet();
            sportrowset.setCommand("select * from SPORT_MEMBERS where Stud_Id ="+ this.studId);
            sportrowset.execute(con);
            int[] keys={1,2};
            sportrowset.setKeyColumns(keys);
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    
    public Set<Activity> getActivities(boolean checkforupdates){
        Set<Activity> list = new HashSet<Activity>();
        
        if(activityrowset == null || checkforupdates){
                initActivityrowSet();
        }
        try{
            activityrowset.first();
            while (activityrowset.next()){
                list.add(new Activity(activityrowset.getInt("Activity_Id"), con));
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return list;
    }

    public void addActivities(Set<Activity> acts, boolean update){    
        if(activityrowset == null){
            initActivityrowSet();

        }
          //get values from rowset as a set, do a check using for loop to check if any value matches any of the instances from the acts set and if it does break 
         //said loop until one is found that doesnt match and that is added into the update
        try{
            for(Activity act: acts){
                activityrowset.moveToInsertRow();
                activityrowset.updateInt("Stud_Id",this.studId);
                activityrowset.updateInt("Activity_Id", act.getId());
                activityrowset.insertRow();
                activityrowset.moveToCurrentRow();
          
            //check the local cached set for duplicates and remove them
            }
            if(update){
                update(activityrowset);
                activityrowset.close();
                activityrowset= null;
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void removeActivities(Set<Activity> acts){     
        for(Activity act: acts){
            try{
                activityrowset.first();
                while(activityrowset.next()){ 
                    if(act.getId() == activityrowset.getInt("Activity_Id"))  
                        activityrowset.deleteRow();
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    private void initActivityrowSet(){
        try{
            RowSetFactory factory = RowSetProvider.newFactory();
            activityrowset=factory.createCachedRowSet();
            activityrowset.setCommand("select * from ACTIVITY_MEMBERS where Stud_Id ="+ this.studId);
            activityrowset.execute(con);
            int[] keys={1,2};
            activityrowset.setKeyColumns(keys);
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

   
//add unmodifiable List as return datatypes
    public Set<Association> getAssocs(){
        Set<Association> list = new HashSet<Association>();
        
        if(associationrowset == null){
                initAssocRowSet();
        }
        try{
            associationrowset.first();
            while(associationrowset.next()){
                list.add(new Association(associationrowset.getInt("Ass_Id"), this.con));
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }   
        return list;
    }
    
    public void addAssocs(Set<Association> asses, boolean update){
        if(associationrowset == null){
            initAssocRowSet();
        }
        try{
            for(Association assoc: asses){
                associationrowset.moveToInsertRow();
                associationrowset.updateInt("Stud_Id",this.studId);
                associationrowset.updateInt("Ass_Id", assoc.getId());
                associationrowset.insertRow();
                associationrowset.moveToCurrentRow();
            }
            if(update){
                update(associationrowset);
                associationrowset.close();
                associationrowset= null;
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private void initAssocRowSet(){
        try{
            RowSetFactory factory = RowSetProvider.newFactory();
            associationrowset=factory.createCachedRowSet();
            associationrowset.setCommand("select * from ASSOCIATION_MEMBERS where Stud_Id ="+ this.studId);
            associationrowset.execute(con);
            int[] keys={1,2};
            associationrowset.setKeyColumns(keys);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }

    }

    public void removeAssocs(Set<Association> ass){
      
        for(Association assoc: ass){
            try{
                associationrowset.first();
                while(associationrowset.next()){
                if(assoc.getId()== associationrowset.getInt("Ass_Id"))  
                    associationrowset.deleteRow();
            }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }
    //future recommendations for batch updates with variations of methods in the class that take in statement objects as parameters to add in data for execution
    protected void update(){
    //create and throw exceptions if fields are missing or not valid        
    }

    protected void refresh(){
        this.activityrowset = null;
        this.associationrowset = null;
        this.clubrowset = null;;
        this.sportrowset = null;
        this.username = null;
        this.email = null;
    }

}