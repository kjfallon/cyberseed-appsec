package edu.syr.cyberseed.sage.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled=true)
public class AuthenticationConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ApplicationSecurity applicationSecurity() {
        return new ApplicationSecurity();
    }

    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            // allow stateless authorized requests and apply per service role based permissions
            http.httpBasic().and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**").denyAll()
                    .antMatchers(HttpMethod.TRACE, "/**").denyAll()
                    .antMatchers("/createPatient").access("hasRole('ROLE_ADD_PATIENT')")
                    .antMatchers("/createDoctor").access("hasRole('ROLE_ADD_DOCTOR')")
                    .antMatchers("/createNurse").access("hasRole('ROLE_ADD_NURSE')")
                    .antMatchers("/createSysAdmin").access("hasRole('ROLE_ADD_SYSTEM_ADMIN')")
                    .antMatchers("/createMedAdmin").access("hasRole('ROLE_ADD_MEDICAL_ADMIN')")
                    .antMatchers("/createInsAdmin").access("hasRole('ROLE_ADD_INSURANCE_ADMIN')")
                    .antMatchers("/editPerm").access("hasRole('ROLE_ASSIGN_PERMISSIONS')")
                    .antMatchers("/addDoctorExamRecord").access("hasAnyRole('ROLE_DOCTOR','ROLE_NURSE','ROLE_MEDICAL_ADMIN')")
                    .antMatchers("/addTestResult").access("hasAnyRole('ROLE_DOCTOR','ROLE_NURSE','ROLE_MEDICAL_ADMIN')")
                    .antMatchers("/addDiagnosisRecord").access("hasRole('ROLE_DOCTOR')")
                    .antMatchers("/addInsuranceClaimRecord").access("hasAnyRole('ROLE_MEDICAL_ADMIN','ROLE_INSURANCE_ADMIN')")
                    .antMatchers("/addRawRecord").access("hasRole('ROLE_USER')")
                    .antMatchers("/createCorrespondenceRecord").access("hasAnyRole('ROLE_DOCTOR','ROLE_PATIENT')")
                    .antMatchers("/addCorrespondenceNote").access("hasAnyRole('ROLE_DOCTOR','ROLE_PATIENT')")
                    .antMatchers("/listRecords").access("hasRole('ROLE_USER')")
                    .antMatchers("/viewRecord").access("hasRole('ROLE_USER')")
                    .antMatchers("/editRecordPerm").access("hasRole('ROLE_USER')")
                    .antMatchers("/editPatient").access("hasRole('ROLE_EDIT_PATIENT')")
                    .antMatchers("/editDoctor").access("hasRole('ROLE_EDIT_DOCTOR')")
                    .antMatchers("/editNurse").access("hasRole('ROLE_EDIT_NURSE')")
                    .antMatchers("/editSysAdmin").access("hasRole('ROLE_EDIT_SYSTEM_ADMIN')")
                    .antMatchers("/editMedAdmin").access("hasRole('ROLE_EDIT_MEDICAL_ADMIN')")
                    .antMatchers("/editInsAdmin").access("hasRole('ROLE_EDIT_INSURANCE_ADMIN')")
                    .antMatchers("/viewPatientProfile").access("hasRole('ROLE_VIEW_PII')")
                    .antMatchers("/viewRecoveryPhrase").access("hasRole('ROLE_SYSTEM_ADMIN')")
                    .antMatchers("/loadBackupcfg").access("hasRole('ROLE_SYSTEM_ADMIN')")
                    .antMatchers("/getBackupcfg").access("hasRole('ROLE_SYSTEM_ADMIN')")
                    .antMatchers("/removeUserProfile").access("hasRole('ROLE_DELETE_USER_PROFILE')");

            http.csrf().disable();
        }
    }
}
