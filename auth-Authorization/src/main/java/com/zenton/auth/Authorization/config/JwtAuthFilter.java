package com.zenton.auth.Authorization.config;

import com.zenton.auth.Authorization.dtos.Authdtos.JwtClaimsDto;
import com.zenton.auth.Authorization.dtos.Cachedtos.CachedUser;
import com.zenton.auth.Authorization.dtos.Securitydtos.AuthenticatedUser;
import com.zenton.auth.Authorization.dtos.types.CacheTtl;
import com.zenton.auth.Authorization.dtos.types.CacheType;
import com.zenton.auth.Authorization.dtos.types.RoleType;
import com.zenton.auth.Authorization.entity.User;
import com.zenton.auth.Authorization.repository.UserRepository;
import com.zenton.auth.Authorization.service.CacheService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
// In jwtfilter we store the principle

    private final AuthUtil authUtil;
    private final UserRepository repo;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final CacheService cacheService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{

            final String requestTokenHeader = request.getHeader("Authorization");
            if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }
            String token = requestTokenHeader.split("Bearer ")[1];
            System.out.println("bearer token :--- "+token);

            JwtClaimsDto jwtClaimsDto = authUtil.getUserClaim(token);
            String username = jwtClaimsDto.getUsername();
            // check for blacklisted token
            String blackListed = cacheService.get(CacheType.blackListedJwt, jwtClaimsDto.getJti(),String.class);
            if(blackListed != null){
                throw new RuntimeException("JWT token revoked");
            }
            // for else part jwt parseSignedClaims check for expiration internally and threo execption.

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
//

                CachedUser cachedUser = cacheService.get(CacheType.user,username,CachedUser.class);
                if(cachedUser != null){
//                    List<SimpleGrantedAuthority> authorities =
//                            cachedUser.getRoles()
//                                    .stream()
//                                    .map(SimpleGrantedAuthority::new)
//                                    .collect(Collectors.toSet());
                    System.out.println("got user from cache:--- "+cachedUser.getUsername());

//                    Set<RoleType> roles =
//                            cachedUser.getRoles()
//                                    .stream()
//                                    .map(RoleType::valueOf)
//                                    .collect(Collectors.toSet());

                    Set<RoleType> roles =
                            cachedUser.getRoles()
                                    .stream()
                                    .map(RoleType::valueOf)
                                    .collect(Collectors.toSet());

                    AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                            .username(cachedUser.getUsername())
                            .id(cachedUser.getId())
                            .roles(roles)
                            .build();

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(authenticatedUser,null,authenticatedUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }else{
                    User user = repo.findByUsername(username).orElseThrow();
                    System.out.println("got user from db call:--- "+user.getUsername());


                    CachedUser cachedUser1 =
                                CachedUser.builder()
                                        .id(user.getId())
                                        .username(user.getUsername())
                                        .roles(
                                                user.getRoles()
                                                        .stream()
                                                        .map(RoleType::name)
                                                        .collect(Collectors.toSet())
                                        )
                                        .build();
                    Set<RoleType> roles =
                            cachedUser1.getRoles()
                                    .stream()
                                    .map(RoleType::valueOf)
                                    .collect(Collectors.toSet());


                    AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                                    .id(user.getId())
                                    .username(user.getUsername())
                                    .roles(roles)
                                    .build();

                        cacheService.save(CacheType.user, cachedUser1.getUsername(), cachedUser1, CacheTtl.USER.getDuration());
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(authenticatedUser,null,authenticatedUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }



            }
            filterChain.doFilter(request,response);
        }catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
