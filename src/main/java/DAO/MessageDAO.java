package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Class for managing operations related to the Message table of our database
public class MessageDAO {

    //support method to make object from result
    private Message resultToMessage(ResultSet result) throws SQLException
    {
        return new Message(result.getInt("posted_by"),
        result.getString("message_text"),
        result.getInt("time_posted"));
    }
    
    //returns contents of message table
    public List<Message> getAllMessages() 
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();

        //declare empty list for returning messages
        List<Message> messages = new ArrayList<>();

        try 
        {
            //create and execute prepared statement
            String sql = "SELECT * FROM Message;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            //loop through results
            while(rs.next())
            {
                messages.add(resultToMessage(rs));
            }
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
        //return results (empty if fail)
        return messages;
    }

    //get message by id, return message obj or null if fail
    public Message getMessageById(int id)
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();

        try 
        {
            //create and execute prepared statement
            String sql = "SELECT * FROM Message WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            //return result if found
            if(rs.next()) return resultToMessage(rs);
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
        //return null if exception or id wasnt found
        return null;
    }

    //insert new message into Message table. Return true or false for success or fail
    public boolean insertMessage(Message message) 
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();
        try 
        {
            //create "insert" prepared statement
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted) VALUES (?,?,?);";
            PreparedStatement ps = connection.prepareStatement(sql);

            //configure prepared statement parameters
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            //update database
            ps.executeUpdate();

            //return success
            return true;
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        //return fail
        return false;
    }

    //deletes message with given id from messages table
    public boolean deleteMessage(int id)
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();
        try 
        {
            //create and execute prepared statement
            String sql = "DELETE FROM Message WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();

            //return success
            return true;
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        //return fail
        return false;
    }

    //update message text of given message id
    public boolean updateMessage(int id, String updateText)
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();

        try 
        {
            //create and execute prepared statement
            String sql = "UPDATE Message SET message_text = ? WHERE id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, updateText);
            ps.setInt(2, id);
            ps.executeUpdate();

            //return success
            return true;
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
            
        //return fail
        return false;
    }

    //returns list of messages written by specified user
    public List<Message> getMessagesFromUser(int account_id)
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();

        //declare empty list for returning messages
        List<Message> messages = new ArrayList<>();

        try 
        {
            //create and execute prepared statement
            String sql = "SELECT * FROM Message WHERE posted_by = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();

            //loop through results
            while(rs.next())
            {
                messages.add(resultToMessage(rs));
            }
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
            
        //return results (empty if fail)
        return messages;
    }
}
