package Service;
import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    public Account getAccount(int userId) {
        return accountDAO.getAccount(userId);
    }

    public Account addAccount(Account account) {
        return accountDAO.insertAccount(account);
    }

    public Account getAccountByUsername(String username) {
        return accountDAO.getAccountByUserName(username);
    }

    public Account login(Account account) {
        return accountDAO.login(account);
    }
}
