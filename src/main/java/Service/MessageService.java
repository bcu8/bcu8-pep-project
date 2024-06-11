package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

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
    public boolean postMessage(Message message)
    {
        return messageDAO.insertMessage(message);
    }

    //deletes single message
    public boolean deleteMessage(int id)
    {
        return messageDAO.deleteMessage(id);
    }

    //updates message contents of single message with given text
    public boolean updateMessage(int id, String updateText)
    {
        return messageDAO.updateMessage(id, updateText);
    }

    //gets all messages from specified user
    public List<Message> getMessagesFromUser(int account_id)
    {
        return messageDAO.getMessagesFromUser(account_id);
    }
}
