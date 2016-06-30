package com.RandCollections;

//import com.exec.model.StringFinder;
//import com.exec.model.Validations;

//OOP Collections
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;


public class StringFinderMain{
	public static void main(String[] args) throws Exception{
		int rowLength = 0, 
				columnLength = 0;
		
		Validations val = new Validations();
		StringFinder sf = new StringFinder();
		ArrayList<LinkedHashMap<String,String>> fullMap = new ArrayList<LinkedHashMap<String,String>>();
		
		String fileName = args[0];
		
		columnLength = sf.readColumnLength(fileName);
		rowLength = sf.readRowLength(fileName);			
		fullMap = sf.readFromFile(rowLength,columnLength,fileName);
		
		sf.menu(rowLength,columnLength,fullMap,val,fileName);
	}
}
