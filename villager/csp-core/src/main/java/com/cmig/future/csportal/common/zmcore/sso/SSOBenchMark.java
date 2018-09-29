package com.cmig.future.csportal.common.zmcore.sso;

/**
 * Created by zhangtao107@126.com on 2016/8/24.
 */

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SSOBenchMark {

    public static String getTicket(final String server, final String username, final String password,
                                   final String service) {
        notNull(server, "server must not be null");
        notNull(username, "username must not be null");
        notNull(password, "password must not be null");
        notNull(service, "service must not be null");

        return getServiceTicket(server, getTicketGrantingTicket(server, username, password, null), service);
    }

    /**
     * 取得ST
     *
     * @param server
     * @param ticketGrantingTicket
     * @param service
     */
    private static String getServiceTicket(final String server, final String ticketGrantingTicket, final String service) {
        if (ticketGrantingTicket == null)
            return null;

        final HttpClient client = new HttpClient();

        final PostMethod post = new PostMethod(server + "/" + ticketGrantingTicket);

        post.setRequestBody(new NameValuePair[]{new NameValuePair("service", service)});

        try {
            client.executeMethod(post);

            final String response = post.getResponseBodyAsString();
            System.out.println("getServiceTicket:" + response);
            switch (post.getStatusCode()) {
                case 200:

                    return response;

                default:
                    warning("Invalid response code (" + post.getStatusCode() + ") from CAS server!");
                    info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
                    break;
            }
        } catch (final IOException e) {
            warning(e.getMessage());

        } finally {
            post.releaseConnection();
        }

        return null;
    }

    /**
     * @param server
     * @param username
     * @param password
     */
    private static String getTicketGrantingTicket(final String server, final String username, final String password, final String identifyType) {
        final HttpClient client = new HttpClient();

        final PostMethod post = new PostMethod(server);

        post.setRequestBody(new NameValuePair[]{
                //		new NameValuePair("identifyType", identifyType),
                new NameValuePair("username", username),
                new NameValuePair("password", password)});

        try {
            client.executeMethod(post);

            final String response = post.getResponseBodyAsString();
//			info("TGT="+response);
            switch (post.getStatusCode()) {
                case 201: {
                    final Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*").matcher(response);

                    if (matcher.matches())
                        return matcher.group(1);

                    warning("Successful ticket granting request, but no ticket found!");
                    info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
                    break;
                }

                default:
                    warning("Invalid response code (" + post.getStatusCode() + ") from CAS server!");
                    info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
                    break;
            }
        } catch (final IOException e) {
            warning(e.getMessage());
        } finally {
            post.releaseConnection();
        }

        return null;
    }

    private static void ticketValidate(String serverValidate, String serviceTicket, String service) {
        notNull(serviceTicket, "paramter 'serviceTicket' is not null");
        notNull(service, "paramter 'service' is not null");

        final HttpClient client = new HttpClient();
        GetMethod post = null;

        try {
            post = new GetMethod(serverValidate + "?" + "ticket=" + serviceTicket + "&service=" + URLEncoder.encode(service, "UTF-8"));
            System.out.println(serverValidate + "?" + "ticket=" + serviceTicket + "&service=" + URLEncoder.encode(service, "UTF-8"));
            client.executeMethod(post);

            final String response = post.getResponseBodyAsString();
            info(response);
            switch (post.getStatusCode()) {
                case 200: {
                    final Matcher matcher = Pattern.compile(".*<cas:uid>(.*)</cas:uid>.*").matcher(response);

                    if (matcher.matches()) {
                        System.out.println("匹配成功");
                        System.out.println(matcher.group(1));
                        //return matcher.group(1);
                    }

                    info("成功取得用户数据");
                }
                default: {

                }
            }

        } catch (Exception e) {
            warning(e.getMessage());
        } finally {
            //释放资源
            post.releaseConnection();
        }

    }

    private static void notNull(final Object object, final String message) {
        if (object == null)
            throw new IllegalArgumentException(message);
    }

    public static void main(final String[] args) throws Exception {
//		final String server = "http://127.0.0.1:8080/cas/v1/tickets";
//		final String username = "redis7";
//		final String password = "redis7";
//		final String service = "http://127.0.0.1:8080/cas";
//		final String proxyValidate = "http://127.0.0.1:8080/cas/proxyValidate";

        final String server = "http://10.17.5.86/cas/v1/tickets";
        final String username = "redis1";
        final String password = "redis1";
        final String service = "http://10.17.5.86/cas";
        final String proxyValidate = "http://10.17.5.86/cas/proxyValidate";

        long totalStartTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            long startTime = System.currentTimeMillis();
            getTicketGrantingTicket(server, username, password, null);
//			ticketValidate(proxyValidate, getTicket(server, username, password, service), service);
            long endTime = System.currentTimeMillis();
            System.out.println(i + ":" + (endTime - startTime) / 1000.00);
        }
        long totalEndTime = System.currentTimeMillis();
        System.out.println("total:" + (totalEndTime - totalStartTime) / 1000.00);
    }

    private static void warning(String msg) {
        System.out.println(msg);
    }

    private static void info(String msg) {
        System.out.println(msg);
    }

}
