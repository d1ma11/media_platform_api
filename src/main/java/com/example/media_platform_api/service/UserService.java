package com.example.media_platform_api.service;

import com.example.media_platform_api.dto.friendRequest.FriendRequestDTO;
import com.example.media_platform_api.dto.friendRequest.FriendRequestResponseDTO;
import com.example.media_platform_api.dto.user.UserRegistrationRequest;
import com.example.media_platform_api.dto.user.UserResponseDTO;
import com.example.media_platform_api.exception.BadRequestException;
import com.example.media_platform_api.model.role.Role;
import com.example.media_platform_api.model.User;
import com.example.media_platform_api.repository.RoleRepository;
import com.example.media_platform_api.repository.UserRepository;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new OpenApiResourceNotFoundException("User not found with ID: " + id));
    }

    public User findFirstByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }

    public User findUserByUserName(String userName) {
        return userRepository.findUserByUsername(userName);
    }

    public User registerUser(UserRegistrationRequest registrationRequest) {
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setEmail(registrationRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("Default user role not found."));
        newUser.setRoles(Collections.singleton(defaultRole));

        return userRepository.save(newUser);
    }

    public User updateUser(Long id, User userToUpdate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found with ID: " + id));

        user.setUsername(userToUpdate.getUsername());
        user.setUsername(userToUpdate.getUsername());
        user.setEmail(userToUpdate.getEmail());

        return userRepository.save(user);
    }

    public void sendFriendRequest(Long userId, FriendRequestDTO friendRequestDTO) {
        User sender = getUserById(friendRequestDTO.getSenderId());
        User receiver = getUserById(friendRequestDTO.getReceiverId());

        if (sender.getSentFriendRequests().contains(receiver) || receiver.getSentFriendRequests().contains(sender) ||
                sender.getFriends().contains(receiver) || receiver.getFriends().contains(sender)) {
            throw new IllegalArgumentException("There is already a friend request or the users are already friends.");
        }

        sender.getSentFriendRequests().add(receiver);
        receiver.getReceivedFriendRequests().add(sender);

        userRepository.save(sender);
        userRepository.save(receiver);
    }

    public List<FriendRequestResponseDTO> getAllFriendRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found with id: " + userId));

        List<User> receivedFriendRequests = user.getReceivedFriendRequests();

        return receivedFriendRequests.stream()
                .map(this::convertToFriendRequestResponseDTO)
                .collect(Collectors.toList());
    }

    public void acceptFriendRequest(Long userId, Long senderId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found with id: " + userId));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found with id: " + senderId));

        if (!user.getReceivedFriendRequests().contains(sender)) {
            throw new BadRequestException("Invalid friend request.");
        }

        user.getFriends().add(sender);
        sender.getFriends().add(user);

        user.getReceivedFriendRequests().remove(sender);
        sender.getSentFriendRequests().remove(user);

        userRepository.save(user);
        userRepository.save(sender);
    }

    public void declineFriendRequest(Long userId, Long senderId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found with id: " + userId));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found with id: " + senderId));

        if (!user.getReceivedFriendRequests().contains(sender)) {
            throw new BadRequestException("Invalid friend request.");
        }

        user.getReceivedFriendRequests().remove(sender);
        sender.getSentFriendRequests().remove(user);

        userRepository.save(user);
        userRepository.save(sender);
    }

    public void deleteFriendRequest(Long userId, Long receiverId) {
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found with id: " + userId));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found with id: " + receiverId));

        if (!sender.getSentFriendRequests().contains(receiver)) {
            throw new IllegalArgumentException("Invalid friend request.");
        }

        sender.getSentFriendRequests().remove(receiver);
        receiver.getReceivedFriendRequests().remove(sender);

        userRepository.save(sender);
        userRepository.save(receiver);
    }

    public List<UserResponseDTO> getAllFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found with id: " + userId));

        return user.getFriends().stream().map(this::convertToUserResponseDTO).collect(Collectors.toList());
    }

    private UserResponseDTO convertToUserResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        return userResponseDTO;
    }

    private FriendRequestResponseDTO convertToFriendRequestResponseDTO(User user) {
        FriendRequestResponseDTO dto = new FriendRequestResponseDTO();
        dto.setSenderId(user.getId());
        dto.setSenderUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

}
