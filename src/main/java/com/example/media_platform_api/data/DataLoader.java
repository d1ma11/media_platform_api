package com.example.media_platform_api.seed;

import com.example.media_platform_api.model.*;
import com.example.media_platform_api.repository.*;
import com.example.media_platform_api.model.role.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final RoleRepository roleRepository;

    public DataLoader(UserRepository userRepository,
                      PostRepository postRepository,
                      RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        User user1 = new User();
        user1.setEmail("dimdima@example.com");
        user1.setUsername("DimDima");
        user1.setPassword("password1");

        User user2 = new User();
        user2.setEmail("dima.pond@example.com");
        user2.setUsername("DimaPond");
        user2.setPassword("password2");

        User user3 = new User();
        user3.setEmail("dima.radiant@example.com");
        user3.setUsername("RadiantDima");
        user3.setPassword("password3");

        User user4 = new User();
        user4.setEmail("dima.dying@example.com");
        user4.setUsername("DyingDima");
        user4.setPassword("password4");

        User user5 = new User();
        user5.setEmail("dima.panic@example.com");
        user5.setUsername("PanicDima");
        user5.setPassword("password5");

        userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5));

        Post post1 = new Post();
        postRepository.save(post1);

        Role role1 = new Role();
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setName("ROLE_ADMIN");

        roleRepository.saveAll(Arrays.asList(role1, role2));

        user1.setRoles(new HashSet<>(List.of(role1)));
        user2.setRoles(new HashSet<>(List.of(role1)));
        user3.setRoles(new HashSet<>(List.of(role1)));
        user4.setRoles(new HashSet<>(List.of(role1)));
        user5.setRoles(new HashSet<>(List.of(role1)));

        userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5));
    }
}