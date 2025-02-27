package br.com.zup_academy.alisson_prado.proposta.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                authorizeRequests
                        .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/v1/propostas/*").hasAuthority("SCOPE_scope-propostas")
                        .antMatchers(HttpMethod.GET, "/api/v1/biometrias/*").hasAuthority("SCOPE_scope-propostas")
                        .antMatchers(HttpMethod.POST, "/api/v1/propostas").hasAuthority("SCOPE_scope-propostas")
                        .antMatchers(HttpMethod.POST, "/api/v1/cartao/**").hasAuthority("SCOPE_scope-propostas")
                        .anyRequest().authenticated() // Qualquer outra requisição deverá ser autenticadas
        ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }


}