package Service;

import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    //exceptions communicate to the caller (in this case the controller) that something specific
    //went wrong
    public class MessageException extends Exception {
        public MessageException(String message) {
            super(message);
        }
    }
    
    //constructor
    public MessageService()
    {
        //init our DAO
        messageDAO = new MessageDAO();
    }

    //retrieves all messages from db using DAO
    public List<Message> getAllMessages()
    {
        return messageDAO.getAllMessages();
    }

    //gets single message by id
    public Message getMessageById(int id)
    {
        return messageDAO.getMessageById(id);
    }

    //adds single message to db
    public Message postMessage(Message message) throws MessageException
    {
        if (message.getMessage_text().length() == 0) throw new MessageException("Message cannot be empty");
        if (message.getMessage_text().length() > 255) throw new MessageException("Message cannot be longer than 255 characters");
        
        //check if message was posted by existing user
        AccountDAO accountDAO = new AccountDAO();
        if (accountDAO.getAccountById(message.getPosted_by()) == null) throw new MessageException("Could not find account with given id");

        //return the newly posted message
        return messageDAO.insertMessage(message);
    }

    //deletes single message
    public Message deleteMessage(int id)
    {
        return messageDAO.deleteMessage(id);
    }

    //updates message contents of single message with given text
    public Message updateMessage(int id, String updateText) throws MessageException
    {
        if (messageDAO.getMessageById(id) == null) throw new MessageException("Message " + id +" does not exist");
        if (updateText.length() == 0) throw new MessageException("Message cannot be empty");
        if (updateText.length() > 255) throw new MessageException("Message cannot be longer than 255 characters");
        return messageDAO.updateMessage(id, updateText);
    }

    //gets all messages from specified user
    public List<Message> getMessagesFromUser(int account_id)
    {
        return messageDAO.getMessagesFromUser(account_id);
    }
}
