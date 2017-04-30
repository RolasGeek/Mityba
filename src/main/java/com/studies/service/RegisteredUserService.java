package com.studies.service;

import com.studies.model.RegisteredUser;
import java.util.List;

public interface RegisteredUserService {
    public RegisteredUser findUserByUsername(String username);
    public void saveUser(RegisteredUser user);
    public void removeUser(RegisteredUser user);
    public List<RegisteredUser> getUsers();
}
