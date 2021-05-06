/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.authentication.ldap;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authentication.Credentials;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;
import io.vertx.ext.auth.ldap.LdapAuthenticationOptions;
import io.vertx.reactivex.ext.auth.ldap.LdapAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

/**
 * @author Yuriy Stul
 */
public class LdapAuthenticationEx1 {
    private static final Logger logger = LoggerFactory.getLogger(LdapAuthenticationEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();

        vertx.setTimer(100, l -> test1(vertx).onComplete(__ -> vertx.close()));

        logger.info("<==main");
    }

    private static Future<Void> test1(final Vertx vertx) {
        var promise = Promise.<Void>promise();
        logger.info("==>test1");
        try {
            var ldapUrl = System.getenv("LDAP_URL");
            var ldapUser = System.getenv("LDAP_USER");
            var ldapPassword = System.getenv("LDAP_PASSWORD");
            logger.info("ldapUrl: {}, ldapUser: {}, ldapPassword: {}", ldapUrl, ldapUser, ldapPassword);

            var theVertx = io.vertx.reactivex.core.Vertx.newInstance(vertx);

            var env = new Hashtable<String, String>();
            env.put(Context.PROVIDER_URL, "ldap://dc4.webpalsltd.local:389");
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, ldapUrl);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            String principal = String.format(ldapUser);
            env.put(Context.SECURITY_PRINCIPAL, principal);
            env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
            var ctx = new InitialDirContext(env);
            logger.info("ctx: {}", ctx);

            var options = new LdapAuthenticationOptions()
                    .setUrl(ldapUrl)
                    .setAuthenticationQuery("CN={0},OU=MyBusiness,DC=webpalsltd,DC=local");

            var authenticationProvider = LdapAuthentication.create(theVertx, options);
            logger.info("authenticationProvider was created: {}", authenticationProvider.toString());

            var credentials = new UsernamePasswordCredentials("yuriy.s", "****");

            authenticationProvider.authenticate(credentials, res -> {
                if (res.succeeded()) {
                    logger.info("Authenticated. User: {}", res.result());
                } else {
                    logger.error("" + res.cause().toString());
                    logger.error("Authentication failed: " + res.cause().getMessage(), res.cause());
                }
                logger.info("<==test1");
                promise.complete();
            });
        } catch (Exception exception) {
            exception.printStackTrace();
            promise.fail("Failed");
        }
        return promise.future();
    }
}
