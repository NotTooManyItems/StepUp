package com.nottoomanyitems.stepup.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.nottoomanyitems.stepup.StepUp;

/**
 * @author jabelar
 *
 */

public class VersionChecker implements Runnable
{
    private static boolean isLatestVersion = false;
    private static String latestVersion = "";

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @SuppressWarnings("deprecation")
	@Override
    public void run() 
    {
        InputStream in = null;
        try{
            in = new URL("https://pastebin.com/raw/zRt8ZN0C").openStream();
        }catch (MalformedURLException e){
            // TODO Auto-generated catch block
        	isLatestVersion = true;
        	IOUtils.closeQuietly(in);
        	return;
        }catch (IOException e){
            // TODO Auto-generated catch block
        	isLatestVersion = true;
        	IOUtils.closeQuietly(in);
        	return;
        }

        try{
            latestVersion = IOUtils.readLines(in).get(0);
        }catch (IOException e){
            // TODO Auto-generated catch block
        	isLatestVersion = true;
        	IOUtils.closeQuietly(in);
            return;
        }finally{
            IOUtils.closeQuietly(in);
        }
        System.out.println("Latest mod version = "+latestVersion);
        if(StepUp.MOD_VERSION.equals(latestVersion)) {
        	isLatestVersion = true;
    	}
        System.out.println("Are you running latest version = "+isLatestVersion);
    }
    
    public static boolean isLatestVersion()
    {
    	return isLatestVersion;
    }
    
    public static String getLatestVersion()
    {
    	return latestVersion;
    }
}
