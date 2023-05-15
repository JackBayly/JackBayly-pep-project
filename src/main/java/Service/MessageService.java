package Service;
import java.util.List;
import Model.Message;
import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    public Message addMessage(Message message) {
        return messageDAO.insertMessage(message);
    }
    public Message deleteMessage(int id) {
        return messageDAO.deleteMessage(id);
    }
    public Message getMessage(int id){
        return messageDAO.getMessage(id);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message updateMessage(int messageId, String updatedtext) {
        return messageDAO.updateMessage(messageId, updatedtext);
    }

    public List<Message> getAllMessagesByUserId(int accountId) {
        return messageDAO.getAllMessagesByUserId(accountId);
    }
}
