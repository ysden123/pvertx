/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.authentication.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import java.util.Hashtable;

/**
 * @author Yuriy Stul
 */
public class LdapAuthEx2 {
    private static final Logger logger = LoggerFactory.getLogger(LdapAuthEx2.class);

    public static void main(String[] args) {
        logger.info("==>main");
        // Set up environment for creating initial context
        Hashtable<String, Object> env = new Hashtable<>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");

        // Specify LDAPS URL
        env.put(Context.PROVIDER_URL, "ldap://dc4.webpalsltd.local:389");

        // Authenticate as S. User and password "mysecret"
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
//        env.put(Context.SECURITY_PRINCIPAL, "CN=WEBPALSLTD\\palsys,OU=MyBusiness,DC=webpalsltd,DC=local");
        env.put(Context.SECURITY_PRINCIPAL, "WEBPALSLTD\\palsys");
        env.put(Context.SECURITY_CREDENTIALS, "prodSES99*&");

        try {
            // Create initial context
//            DirContext ctx = new InitialDirContext(env);

            LdapContext ctx = new InitialLdapContext(env, null);

//            System.out.println(ctx.lookup("ou=NewHires"));

            // ... do something useful with ctx

            // Close the context when we're done
/*
            logger.debug("before lookup");
            var ttt = ctx.lookup("OU=MyBusiness,DC=webpalsltd,DC=local");
            logger.debug("after lookup");
            logger.info("ttt: {}", ttt);
*/


            ctx.close();
        } catch (NamingException e) {
            logger.info(e.getMessage(), e);
            e.printStackTrace();
        }finally {
            logger.info("<==main");
        }
    }
}
