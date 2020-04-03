package com.nottoomanyitems.stepup.config;

import com.nottoomanyitems.stepup.StepUp;
import com.nottoomanyitems.stepup.worker.StepChanger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
 
public class ConfigIO
{   
	public static String filePath = "config/"+ StepUp.CONFIG_FILE;
	public static String serverIP;
    public static int autoJumpState;

	public static void ServerIP() {
		ServerData serverData = Minecraft.getInstance().getCurrentServerData();
    	serverIP = serverData != null ? serverData.serverIP.replace(".", "") : "0000";
	}
	
	public static void createCFG()
    {
        File fileToBeModified = new File(filePath);
        String content = "# Configuration file" + System.lineSeparator() + System.lineSeparator();
        FileWriter writer = null;
         
        try
        {
            writer = new FileWriter(fileToBeModified);
            writer.write(content);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                //Closing the resources
                writer.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }
	
	public static void CheckForServerIP() {
		ServerIP();
		
		File fileToBeModified = new File(filePath);
        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;
        boolean end = false;

        try
        {
            reader = new BufferedReader(new FileReader(fileToBeModified));
            
            //Reading all the lines of input text file into oldContent
            String line = reader.readLine();
            String autoJumpStateLine = "";
             
            while (line != null && !end) 
            {
                oldContent = oldContent + line + System.lineSeparator();
                if(line.contains(serverIP)) {
                	autoJumpStateLine = reader.readLine();
                	end = true;
                }
                line = reader.readLine();
            }
             
            //If serverIP wasn't found create new entry at the end
            if(!end) {
            	autoJumpState = 0;
            	oldContent = oldContent + serverIP+" {" + System.lineSeparator() +"    S:autoJumpState="+autoJumpState + System.lineSeparator() + "}" + System.lineSeparator() + System.lineSeparator() + System.lineSeparator() ;
            	writer = new FileWriter(fileToBeModified);
                writer.write(oldContent);
            }else {
            	autoJumpStateLine = autoJumpStateLine.substring(autoJumpStateLine.length()-1, autoJumpStateLine.length());
            	autoJumpState = Integer.parseInt(autoJumpStateLine);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                //Closing the resources
                reader.close();
                if(!end) {
                	writer.close();
                }
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
	}
	
    public static void updateCFG()
    {
        File fileToBeModified = new File(filePath);
        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;
         
        try
        {
            reader = new BufferedReader(new FileReader(fileToBeModified));
             
            //Reading all the lines of input text file into oldContent
            String line = reader.readLine();
             
            while (line != null) 
            {
            	oldContent = oldContent + line + System.lineSeparator();
            	if(line.contains(serverIP)) {
            		line = reader.readLine();
            		line = line.substring(0, line.length()-1);
            		oldContent = oldContent + line + autoJumpState + System.lineSeparator();
                }
            	line = reader.readLine();
            }
             
            //Rewriting the input text file with newContent
            writer = new FileWriter(fileToBeModified);
            writer.write(oldContent);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                //Closing the resources
                reader.close();
                writer.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }
    
    public static void debugMessage(String m) {
    	StepChanger.player.sendMessage((ITextComponent) new StringTextComponent(m));
    }
}
