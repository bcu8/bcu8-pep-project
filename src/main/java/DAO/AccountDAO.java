package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    //support method to make account object from result
    private Account resultToAccount(ResultSet result) throws SQLException
    {
        return new Account(result.getInt("account_id"),
        result.getString("username"),
        result.getString("password"));
    }
    
    //get account from account table with given username
    public Account getAccountByUsername(String username)
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();

        try 
        {
            //create and execute prepared statement
            String sql = "SELECT * FROM Account WHERE username = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            //return result if found
            if(rs.next()) return resultToAccount(rs);
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
        //return null if exception or id wasnt found
        return null;
    }

    //get account from account table with given id
    public Account getAccountById(int id)
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();

        try 
        {
            //create and execute prepared statement
            String sql = "SELECT * FROM Account WHERE account_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            //return result if found
            if(rs.next()) return resultToAccount(rs);
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
        //return null if exception or id wasnt found
        return null;
    }
    
    //insert new account to account table with given username/password
    public boolean insertAccount(String username, String password)
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();
        try 
        {
            //create "insert" prepared statement
            String sql = "INSERT INTO Account (username, password) VALUES (?,?);";
            PreparedStatement ps = connection.prepareStatement(sql);

            //configure prepared statement parameters
            ps.setString(1, username);
            ps.setString(2, password);

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
    
    //checks if an account exists with given username and password
    public boolean checkForAccount(String username, String password)
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();
        try 
        {
            //create prepared statement
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            //configure prepared statement parameters
            ps.setString(1, username);
            ps.setString(2, password);

            //get results
            ResultSet rs = ps.executeQuery();

            //return true if account found, otherwise false
            return rs.next();
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        //return fail
        return false;
    }

    //checks if an account exists with given username
    public boolean checkForAccount(String username)
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();
        try 
        {
            //create prepared statement
            String sql = "SELECT * FROM Account WHERE username = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            //configure prepared statement parameters
            ps.setString(1, username);

            //get results
            ResultSet rs = ps.executeQuery();

            //return true if account found, otherwise false
            return rs.next();
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        //return fail
        return false;
    }
}
