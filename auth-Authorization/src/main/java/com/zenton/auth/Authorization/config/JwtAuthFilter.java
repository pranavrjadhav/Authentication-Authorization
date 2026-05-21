package com.zenton.auth.Authorization.config;

import com.zenton.auth.Authorization.dtos.CachedUser;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{


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

            String username = authUtil.getUsernameFromToken(token);

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
//

                CachedUser cachedUser = cacheService.get(username);
                if(cachedUser != null){
                    List<SimpleGrantedAuthority> authorities =
                            cachedUser.getRoles()
                                    .stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList();
                    System.out.println("got user from cache:--- "+cachedUser.getUsername());

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(cachedUser,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }else{
                    User user = repo.findByUsername(username).orElseThrow();
                    System.out.println("got user from db call:--- "+user.getUsername());


                    CachedUser cachedUser1 =
                                CachedUser.builder()
                                        .id(user.getId())
                                        .username(user.getUsername())
                                        .roles(
                                                user.getAuthorities()
                                                        .stream()
                                                        .map(GrantedAuthority::getAuthority)
                                                        .toList()
                                        )
                                        .build();

                        cacheService.save(cachedUser1);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }



            }
            filterChain.doFilter(request,response);
        }catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
