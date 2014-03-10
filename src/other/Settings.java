package other;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import data.DataBase;
import data.DataCluster;

public abstract class Settings implements Printer {
	
	
	protected File settingsFile;
	
	protected DataCluster settingsCluster = new DataCluster("");


	protected void initSettingsFile() {
		if(settingsFile.exists()){
			loadSettingsFile();
		}else{
			saveSettingsFile();
		}
		
	}

	protected boolean saveSettingsFile() {
		try{
			FileOutputStream s = new FileOutputStream(settingsFile);
			DataOutputStream ds = new DataOutputStream(s);
			settingsCluster.write(ds);
			ds.close();
			s.close();
			println("Saved "+settingsFile.getPath());
			return true;
		}catch(Exception e){
			println("Failed to Save "+settingsFile.getPath());
			e.printStackTrace();
			return false;
		}
	}

	protected boolean loadSettingsFile() {
		try{
			FileInputStream s = new FileInputStream(settingsFile);
			DataInputStream ds = new DataInputStream(s);
			settingsCluster = (DataCluster) DataBase.readNew(ds);
			ds.close();
			s.close();
			println("Loaded "+settingsFile.getPath());
			return true;
		}catch(Exception e){
			println("Failed to Load "+settingsFile.getPath());
			e.printStackTrace();
			return false;
		}
	}
}
