package lk.bit.web.config;

import lk.bit.web.api.filter.JwtRequestFilter;
import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.business.custom.SystemUserBO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomerBO customerBO;
    @Autowired
    private SystemUserBO systemUserBO;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerBO).passwordEncoder(passwordEncoder());
        auth.userDetailsService(systemUserBO).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/*/users/**", "/api/*/supplierInvoices/**",
                        "/api/*/users/**", "/api/*/supplierInvoices/**").hasRole("ADMIN")
                .antMatchers("/api/*/customers/**","/api/*/categories/**",
                        "/api/*/products/**", "/api/v*/shops/**", "/api/*/complains/**"
                        ,"/api/*/solutions/**" , "/api/*/shopCategories/**").hasAnyRole("CUSTOMER", "ADMIN")
                .antMatchers("/api/v1/authenticate","/api/v1/registers/**", "/files/**", "/api/*/offers/**",
                        "/api/*/categories/**", "/api/*/products/**","/api/*/subcategories/**",
                        "/api/v*/orderInvoices/**","/api/*/advertisements/**", "/api/*/returns/**", "/api/*/creditors/**")
                .permitAll().anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // has a role SALESPERSON
    }

}
