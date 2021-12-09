package com.nottoomanyitems.stepup.config;

import com.nottoomanyitems.stepup.StepUp;
import com.nottoomanyitems.stepup.worker.StepChanger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.TextComponent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
 
public class ConfigIO{
	public static String CONFIG_FILE = StepUp.MODID+".cfg";
	public static String filePath = "config/"+ CONFIG_FILE;
	public static String serverIP;
    public static int autoJumpState = 0;
    public static int display_start_message = 1;
    public static int display_update_message = 1;

	public static void ServerIP() {
		ServerData serverData = Minecraft.getInstance().getCurrentServer();
    	serverIP = serverData != null ? serverData.ip.replace(".", "") : "0000";
	}
	
	public static void createCFG()
    {
        File fileToBeModified = new File(filePath);
        String content = "# Configuration file" + System.lineSeparator() + "# display_start_message=1" + System.lineSeparator() + "# display_update_message=1" + System.lineSeparator() + System.lineSeparator();
        FileWriter writer = null;
         
        try{
            writer = new FileWriter(fileToBeModified);
            writer.write(content);
        }catch (IOException e){
            e.printStackTrace();
        }
        finally{
            try{
            	if(writer != null) {
            		//Closing the resources
            		writer.close();
            	}
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	public static void CheckForServerIP() {
		if (Files.notExists(Paths.get("config", CONFIG_FILE))){
    		ConfigIO.createCFG();
        }
		
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
                if(line.contains("display_start_message")) {
                	display_start_message = Integer.parseInt(line.substring(line.length()-1, line.length()));
                }else if(line.contains("display_update_message")) {
                	display_update_message = Integer.parseInt(line.substring(line.length()-1, line.length()));
                }else if(line.contains(serverIP)) {
                	autoJumpStateLine = reader.readLine();
                	end = true;
                }
                line = reader.readLine();
            }
             
            //If serverIP wasn't found create new entry at the end
            if(!end) {
            	autoJumpState = 0;
            	oldContent = oldContent + serverIP+" {" + System.lineSeparator() +"    S:autoJumpState="+autoJumpState + System.lineSeparator() + "}" + System.lineSeparator() + System.lineSeparator();
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
            	if(reader != null){
            		reader.close();
            	}
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
            
            if(!line.contains("# Configuration file")) {
            	oldContent = oldContent + "# Configuration file" + System.lineSeparator();
            }else {
            	oldContent = oldContent + line + System.lineSeparator();
            	line = reader.readLine();
            }
            
            if(!line.contains("display_start_message")) {
            	oldContent = oldContent + "# display_start_message=1" + System.lineSeparator();
            }else{
            	oldContent = oldContent + line + System.lineSeparator();
            	line = reader.readLine();
            }
            
            if(!line.contains("display_update_message")) {
            	oldContent = oldContent + "# display_update_message=1" + System.lineSeparator() + System.lineSeparator();
            }else{
            	oldContent = oldContent + line + System.lineSeparator() + System.lineSeparator();
            	line = reader.readLine();
            }
             
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
        StepChanger.player.displayClientMessage(new TextComponent(m), true);
    }
}
