package nl.hva.miw.c27.team1.cryptobanking.repository.repository;

import nl.hva.miw.c27.team1.cryptobanking.model.*;
import nl.hva.miw.c27.team1.cryptobanking.repository.dao.ProfileDao;
import nl.hva.miw.c27.team1.cryptobanking.repository.dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// class needs db queries
@Repository
public class RootRepository {

    private List<User> userList;
    private final UserDao userDao;
    private final ProfileDao profileDao;

    private final Logger logger = LogManager.getLogger(RootRepository.class);

    @Autowired
    public RootRepository(UserDao userDao, ProfileDao profileDao) {
        this.profileDao = profileDao;
        this.userList = new ArrayList<>();
        fillUserList();
        this.userDao = userDao;
        logger.info("New RootRepository");
    }

    public void save(User user) {
        userDao.save(user);
        profileDao.save(user.getProfile());
    }

    // testlijst, is niet gelinkt aan DAO
    public void fillUserList() {
        userList.add(new Customer(1, "Client"));
        userList.add(new Admin(2, "Admin"));
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers().get();
    }

    public Optional <User> getUserById(int id) {
        return userDao.findById(id);
    }

    public User getUserByRole(String role) {
        return userList.stream().filter(u -> u.getRole().equals(role)).findFirst().get();
    }

    public User update(User user) {
        int indexOfUserToUpdate = userList.indexOf(user);
        if (indexOfUserToUpdate >= 0) {
            userList.set(indexOfUserToUpdate, user);
            return userList.get(indexOfUserToUpdate);
        } else {
            return null;
        }
    }

    public Optional<Profile> getProfileOfUser(String userName) {

        return profileDao.findByUserName(userName);
    }

    public void delete(int id) {
        Optional<User> user = getUserById(id);
        int indexOfUserToUpdate = userList.indexOf(user);
        System.out.println(indexOfUserToUpdate);
        if (indexOfUserToUpdate >= 0) {
            userList.remove(indexOfUserToUpdate);
        } else {
            System.out.println("niet gelukt");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    // getters & setters
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public ProfileDao getProfileDao() {
        return profileDao;
    }

    public Logger getLogger() {
        return logger;
    }

}

