package com.adminportal.service;

import com.adminportal.domain.User;
import com.adminportal.domain.UserBilling;
import com.adminportal.domain.UserPayment;
import com.adminportal.domain.UserShipping;
import com.adminportal.domain.security.PasswordResetToken;
import com.adminportal.domain.security.UserRole;

import java.util.Optional;
import java.util.Set;

public interface UserService {
    PasswordResetToken getPasswordResetToken(final String token);
    void createPasswordResetTokenForUser(final User user, final String token);

    User findByUsername(String username);
    User findByEmail(String email);
    Optional<User> findById(Long id);

    User createUser(User user, Set<UserRole> userRoles) throws Exception;

    User save(User user);

}
