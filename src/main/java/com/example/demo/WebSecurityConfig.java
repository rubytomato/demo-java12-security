package com.example.demo;

import com.example.demo.auth.SimpleUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private SimpleUserDetailsService simpleUserDetailsService;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public void setSimpleUserDetailsService(SimpleUserDetailsService simpleUserDetailsService) {
    this.simpleUserDetailsService = simpleUserDetailsService;
  }
  @Autowired
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        // ### AuthenticationManagerBuilder
        .eraseCredentials(true)
        // ### DaoAuthenticationConfigurer
        .userDetailsService(simpleUserDetailsService)
        // ### DaoAuthenticationConfigurer
        .passwordEncoder(passwordEncoder)
    ;

/*
    auth
        // ### InMemoryUserDetailsManagerConfigurer
        .inMemoryAuthentication()
        //.passwordEncoder(passwordEncoder)
        .withUser("user")
          .password(passwordEncoder.encode("user"))
        .roles("USER")
        .and()
        .withUser("admin")
          .password(passwordEncoder.encode("admin"))
          .roles("ADMIN");
*/
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    // @formatter:off
    web
      .debug(false)
      // ### IgnoredRequestConfigurer
      .ignoring()
        .antMatchers("/images/**", "/js/**", "/css/**")
      .and()
    ;
    // @formatter:on
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
      // ### ExpressionUrlAuthorizationConfigurer
      .authorizeRequests()
        .mvcMatchers("/", "/signup", "/menu").permitAll()
        .mvcMatchers("/error/**").permitAll()
        .mvcMatchers("/memo/**").hasAnyRole("USER", "ADMIN")
        .mvcMatchers("/account/**").fullyAuthenticated()
        .mvcMatchers("/admin/**").hasRole("ADMIN")
        .mvcMatchers("/user/**").hasRole("USER")
        .anyRequest().authenticated()
      .and()
      // ### ExceptionHandlingConfigurer
      .exceptionHandling()
        // #accessDeniedUrl: the URL to the access denied page (i.e. /errors/401)
        .accessDeniedPage("/error/denied")
        // #accessDeniedHandler: the {@link AccessDeniedHandler} to be used
        //.accessDeniedHandler(accessDeniedHandler)
      .and()
      // ### FormLoginConfigurer
      .formLogin()
        // #loginPage: the login page to redirect to if authentication is required (i.e."/login")
        .loginPage("/signin")
        // #loginProcessingUrl: the URL to validate username and password
        .loginProcessingUrl("/login")
        .usernameParameter("email")
        .passwordParameter("password")
        // #defaultSuccessUrl: the default success url
        // #alwaysUse: true if the {@code defaultSuccesUrl} should be used after authentication despite if a protected page had been previously visited
        // #{SavedRequestAwareAuthenticationSuccessHandler}
        //.defaultSuccessUrl("/signin/success", false)
        .defaultSuccessUrl("/", false)
        // #successHandler: the {@link AuthenticationSuccessHandler}.
        //.successHandler(successHandler)
        // #authenticationFailureUrl: the URL to send users if authentication fails (i.e."/login?error").
        // #{SimpleUrlAuthenticationFailureHandler}
        .failureUrl("/signin?error")
        // #authenticationFailureHandler: the {@link AuthenticationFailureHandler} to use
        //.failureHandler(failureHandler)
        .permitAll()
      .and()
      // ### LogoutConfigurer
      .logout()
        // #logoutUrl: the URL that will invoke logout.
        .logoutUrl("/logout")
        // #logoutSuccessHandler: the {@link LogoutSuccessHandler} to use after a user
        .logoutSuccessHandler(logoutSuccessHandler)
        // #logoutSuccessUrl: the URL to redirect to after logout occurred
        //.logoutSuccessUrl("/")
        // #invalidateHttpSession: true if the {@link HttpSession} should be invalidated (default), or false otherwise.
        .invalidateHttpSession(false)
        // #cookieNamesToClear: the names of cookies to be removed on logout success.
        .deleteCookies("JSESSIONID", "XSRF-TOKEN")
        .permitAll()
      .and()
      // ### CsrfConfigurer
      .csrf()
        .csrfTokenRepository(new CookieCsrfTokenRepository())
      .and()
      // ### RememberMeConfigurer
      .rememberMe()
        // #alwaysRemember: set to true to always trigger remember me, false to use the remember-me parameter.
        .alwaysRemember(false)
        // #rememberMeParameter: the HTTP parameter used to indicate to remember the user
        .rememberMeParameter("remember-me")
        // #useSecureCookie: set to {@code true} to always user secure cookies, {@code false} to disable their use.
        .useSecureCookie(true)
        // #rememberMeCookieName: the name of cookie which store the token for remember
        .rememberMeCookieName("REMEMBERME")
        // Allows specifying how long (in seconds) a token is valid for
        .tokenValiditySeconds(daysToSeconds(3))
        // #key: the key to identify tokens created for remember me authentication
        .key("PgHahck5y6pz7a0Fo#[G)!kt")
        //.disable()
        .and()
      // ### SessionManagementConfigurer
      .sessionManagement()
        .sessionFixation()
          .changeSessionId()
        // #invalidSessionStrategy: the strategy to use when an invalid session ID is submitted.
        //.invalidSessionStrategy(invalidSessionStrategy)
        // #invalidSessionUrl: the URL to redirect to when an invalid session is detected
        .invalidSessionUrl("/error/invalid")
        // ### ConcurrencyControlConfigurer
        // #maximumSessions: the maximum number of sessions for a user
        .maximumSessions(1)
          // #maxSessionsPreventsLogin: true to have an error at time of authentication, else false (default)
          .maxSessionsPreventsLogin(false)
          // #expiredUrl: the URL to redirect to
          .expiredUrl("/error/expired")
          .and()
        .and()
      ;
    // @formatter:on
  }

  private LogoutSuccessHandler logoutSuccessHandler = (req, res, auth) -> {
    log.debug("logoutSuccessHandler req:{}", req.getRequestURI());
    if (res.isCommitted()) {
      log.debug("Response has already been committed. Unable to redirect to ");
      return;
    }
    if (req.isRequestedSessionIdValid()) {
      log.debug("requestedSessionIdValid session id:{}", req.getRequestedSessionId());
      req.changeSessionId();
    }
    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    redirectStrategy.sendRedirect(req, res, "/");
  };

  private int daysToSeconds(int days) {
    return 60 * 60 * 24 * days;
  }

}
