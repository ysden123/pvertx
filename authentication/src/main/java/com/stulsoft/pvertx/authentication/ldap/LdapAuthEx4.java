/*
 * Copyright (c) 2021. Webpals
 */

package com.stulsoft.pvertx.authentication.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author Yuriy Stul
 */
public class LdapAuthEx4 {
    private static final Logger logger = LoggerFactory.getLogger(LdapAuthEx4.class);
    private static final String SEARCH_BASE = "OU=MyBusiness, DC=webpalsltd,DC=local";

    public static void main(String[] args) {
        logger.info("==>main");
        Scanner in = new Scanner(System.in);
        System.out.println("Enter username");
        var username = in.nextLine();
        System.out.println("Enter password");
        var password = in.nextLine();
        test1(username, password);
    }

    private static void test1(final String username, final String password){
        logger.info("==>test1");
        // Init JNDI
        Hashtable<String, Object> env = new Hashtable<>(11);
        env.put(Context.PROVIDER_URL, System.getenv("LDAP_URL"));
        env.put(Context.SECURITY_PRINCIPAL, System.getenv("LDAP_USER"));
        env.put(Context.SECURITY_CREDENTIALS, System.getenv("LDAP_PASSWORD"));
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

        DirContext ctx;
        try {
            ctx = new InitialDirContext(env);
            logger.info("ctx was created");
            ctx.getEnvironment().forEach((k, v) -> logger.info("k={}, v={}", k, v.toString()));

            String[] attrIds = {"distinguishedName", "sn", "givenname", "mail", "sAMAccountName", "memberOf", "userPassword", "Picture", "thumbnailPhoto"};
            String searchFilter = "sAMAccountName=" + username;
            SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(attrIds);

            // Specify the search scope
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> answer = ctx.search(SEARCH_BASE, searchFilter, searchControls);

            while (answer.hasMoreElements()) {
                logger.info("Received answer");
                var result = answer.next();
                var distinguishedName = result.getNameInNamespace();
                logger.info("distinguishedName={}", distinguishedName);

                // attempt another authentication, now with the user
                Properties authEnv = new Properties();
                authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                authEnv.put(Context.PROVIDER_URL, ctx.getEnvironment().get(Context.PROVIDER_URL));
                authEnv.put(Context.SECURITY_PRINCIPAL, distinguishedName);
                authEnv.put(Context.SECURITY_CREDENTIALS,  password);

                DirContext dctx = new InitialDirContext(authEnv);

                var answer2 = dctx.search(SEARCH_BASE, searchFilter, searchControls);
                while(answer2.hasMoreElements()){
                    logger.info("Received answer2");
                    var result2 = answer2.next();
                    logger.info("result2: {}", result2);
                    var distinguishedName2 = result2.getNameInNamespace();
                    logger.info("distinguishedName2={}", distinguishedName2);
                    var thumbnailPhoto = result2.getAttributes().get("thumbnailPhoto");
                    if (thumbnailPhoto != null){
                        logger.info("Received thumbnailPhoto");
                        byte[] pic = (byte[])thumbnailPhoto.get();
                        logger.info("thumbnailPhotoL pic.length= {}", pic.length);
                    }else{
                        logger.info("Did not receive thumbnailPhoto");
                    }
                }
                dctx.close();
            }
            ctx.close();
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            System.exit(1);
        }
    }
}
