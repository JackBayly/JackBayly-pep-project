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
        System.out.println("het thereee");
        System.out.println(message.message_text);

        return messageDAO.insertMessage(message);
    }
 }
