package com.zenton.auth.Authorization.entity;

import com.zenton.auth.Authorization.dtos.types.ResourceType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name="permissions")
public class Permissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
/* we  dismiss the idea for new resource / feature table as that type of dynamic flexibilty will be of rarely use
   and as authe-service provider like keyclock any manymore do that
*/

    private ResourceType resourceType;

    private String name;
}
