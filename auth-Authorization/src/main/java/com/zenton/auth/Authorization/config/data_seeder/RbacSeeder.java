package com.zenton.auth.Authorization.config.data_seeder;

import com.zenton.auth.Authorization.dtos.types.PermissionType;
import com.zenton.auth.Authorization.dtos.types.ResourceType;
import com.zenton.auth.Authorization.dtos.types.RoleType;
import com.zenton.auth.Authorization.entity.Permissions;
import com.zenton.auth.Authorization.entity.Role;
import com.zenton.auth.Authorization.repository.PermissionsRepository;
import com.zenton.auth.Authorization.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.security.Permission;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class RbacSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionsRepository permissionsRepository;

    @Override
    public void run(String... args) throws Exception {
        seedPermissions();
        seedRoles();
    }

    // permission data
    private void seedPermissions(){
        for(ResourceType resourceType : ResourceType.values()){   // loop for resource enum
            for(PermissionType permissionType : PermissionType.values()){  // loop for permission enum
                String permisionName =
                        resourceType.getValue()+":"+permissionType.getValue();
                if(!permissionsRepository.existsByName(permisionName)){
                    Permissions permissions =
                            Permissions.builder()
                                    .name(permisionName)
                                    .build();
                    permissionsRepository.save(permissions);
                }
            }
        }
    }

    private void seedRoles(){

        Role admin =
                roleRepository.findByName(RoleType.ADMIN.name())
                        .orElseGet(() ->
                                roleRepository.save(
                                        Role.builder()
                                                .name(RoleType.ADMIN.name())
                                                .build()
                                )
                        );

        Role user =
                roleRepository.findByName(RoleType.USER.name())
                        .orElseGet(() ->
                                roleRepository.save(
                                        Role.builder()
                                                .name(RoleType.USER.name())
                                                .build()
                                )
                        );

        Set<Permissions> allPermissions =
                new HashSet<>(permissionsRepository.findAll());

        admin.setPermissions(allPermissions);

        Permissions readPermission1 =
                permissionsRepository.findByName(
                        ResourceType.CarRegistry.getValue() + ":read"
                ).orElseThrow();
        Permissions readPermission2 =
                permissionsRepository.findByName(
                        ResourceType.CarRegistry.getValue() + ":write"
                ).orElseThrow();



        Set<Permissions> userPermissions = new HashSet<>();
        userPermissions.add(readPermission1);
        userPermissions.add(readPermission2);

        user.setPermissions(userPermissions);

        roleRepository.save(admin);
        roleRepository.save(user);


    }


}

/*
erro occure

Rule for Hibernate entities

Avoid putting immutable collections into entity relationships:

❌

List.of(...)
Set.of(...)
Map.of(...)
Collections.emptySet()

✅

new ArrayList<>()
new HashSet<>()
new HashMap<>()

For JPA/Hibernate entity fields (@OneToMany, @ManyToMany, @ElementCollection), always use mutable collections.

The exception is occurring at:

roleRepository.save(user);

because user.getPermissions() contains a Set.of(...) collection. Converting it to a HashSet will fix this startup failure.
 */
