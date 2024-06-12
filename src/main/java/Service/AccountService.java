package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    //exceptions communicate to the caller (in this case the controller) that something specific
    //went wrong
    public class AccountException extends Exception {
        public AccountException(String message) {
            super(message);
        }
    }
    
    //Constructor
    public AccountService() 
    {
        //initialize our DAO
        accountDAO = new AccountDAO();
    }

    //operation for creating new user account
    public Account processUserRegistration(String username, String password) throws AccountException
    {
        // Check if username empty
        if (username.length() == 0) throw new AccountException("Username cannot be empty");
    
        // Check if password is too short
        if (password.length() < 4) throw new AccountException("Password must be at least 4 characters long");

        // Check if username already exists
        if (accountDAO.checkForAccount(username)) throw new AccountException("Username already exists");
    
        //attempt insert account to db using dao
        accountDAO.insertAccount(username, password);
        
        //return newly created account (will be null if dao failed)
        return accountDAO.getAccountByUsername(username);
    }

    //operation for a user attempted sign in
    public Account login(String username, String password) throws AccountException
    {
        //check if provided credentials are correct using dao
        if (accountDAO.checkForAccount(username, password))
        {
            return accountDAO.getAccountByUsername(username);
        }
        throw new AccountException("Incorrect username or password.");
    }
}