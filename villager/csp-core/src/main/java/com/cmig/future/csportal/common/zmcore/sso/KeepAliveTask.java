package com.cmig.future.csportal.common.zmcore.sso;

import com.cmig.future.csportal.common.config.Global;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


public class KeepAliveTask extends Thread {
    private static Logger log= LoggerFactory.getLogger(KeepAliveTask.class);

    private int n;
    private String url;
    private String TGT;
    private String ST;


    public KeepAliveTask(int n, String url, String TGT, String ST) {
        this.n = n;
        this.url = url;
        this.TGT = TGT;
        this.ST = ST;
    }


    @Override
    public void run() {
        log.info("TGT:"+TGT+" timeout: " + n + " begin:" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())));
        try {
            Thread.sleep(3 * 1000);
            keepAlive(url+"?timeout="+n, TGT, ST);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("TGT:"+TGT+" timeout: " + n + " end:" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())));

    }


    private void keepAlive(String url, String TGT, String ST) {
        final HttpClient client = new HttpClient();
        GetMethod post = null;

        try {
            CoreSSOClientUtils.notNull(TGT, "paramter 'TGT' is not null");
            CoreSSOClientUtils.notNull(ST, "paramter 'ST' is not null");

            post = new GetMethod(url);
            post.addRequestHeader("Cookie", "TGT=" + TGT + ";ST=" + ST + ";");
	        if(Global.getBoolean("HSP.ENABLE_FLAG",false)){
		        post.addRequestHeader("Authorization", Global.getHspAuthorization());
	        }
            log.debug(url);
            client.executeMethod(post);
            final String response = post.getResponseBodyAsString();
            log.debug(response);
            switch (post.getStatusCode()) {
                case 200: {
                    log.info("TGT {} ST {} KeepAlive Success!",TGT,ST);
                }
                default: {

                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            post.releaseConnection();
        }
    }


    public String getTGT() {
        return TGT;
    }


    public String getST() {
        return ST;
    }


    public int hashCode() {
        return (this.TGT + ":" + this.ST).hashCode();
    }


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof KeepAliveTask) {
            KeepAliveTask other = (KeepAliveTask) o;
            if (this.TGT == null && other.getTGT() != null) {
                return false;
            }
            if (this.ST == null && other.getST() != null) {
                return false;
            }
            if (this.TGT.equals(other.getTGT()) && this.ST.equals(other.getST())) {
                return true;
            }
        }
        return false;
    }


}