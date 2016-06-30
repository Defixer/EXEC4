package com.RandCollections;

import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;
import java.lang.NumberFormatException;

//OOP Collections
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.TreeMap;

//read write input into txt file
//import java.io.PrintWriter;
import java.io.File;
import java.io.*;

//MAVEN//write to file (Apache Commons IO)
import org.apache.commons.io.FileUtils;
//search substring in string (Apache Common Lang3)
import org.apache.commons.lang3.StringUtils;

public class StringFinder{
	private static Scanner scan = new Scanner(System.in);
	private final String SEARCH_FLAG = "Search";
	private final String EDIT_MENU = "editMenu",
						EDIT_FLAG = "Edit",
						EDIT_KEY = "editKey",
						EDIT_VALUE = "editValue",
						EDIT_BOTH = "editBoth",
						KEY_FLAG = "key",
						VALUE_FLAG = "value",
						REPLACE_KEY = "replaceKey",
						REPLACE_VALUE = "replaceValue",
						ROW_FLAG = "Row",
						ROW_INDEX_FLAG = "Row Index",
						COLUMN_FLAG = "Column",
						COLUMN_INDEX_FLAG = "Column Index",
						NULL_FLAG = "",
						PRINT_FLAG = "Print",
						DELIMITER = "¦",
						SPACE_DELIMITER = "©",
						CARRIAGE_RETURN_DELIMITER = "\r\n",
						ADD_ROW = "add row",
						GENERATE_ROW_FLAG = "generate row";
	private final int NULL_ROW = 0,
					NULL_COLUMN = 0,
					ASCII_MIN = 32,
					ASCII_MAX = 126,
					KEY_VALUE_PAIR_LENGTH = 6;
							
	public int inputDimensions(String dimensionType, String dimensionID, Validations val, int rowLength, int columnLength){
		
		boolean indexInputInvalid = false;
		int dimensionValue = 0;
		
		do{
			indexInputInvalid = false;
			
			dimensionValue = val.isInputMismatch(dimensionType, dimensionID);
			
			if(dimensionID.equalsIgnoreCase("Row") | dimensionID.equalsIgnoreCase("Column")){
				indexInputInvalid = val.isInvalid(dimensionType, dimensionValue);				
			}
			
			if(dimensionID.equalsIgnoreCase("Row Index") | dimensionID.equalsIgnoreCase("Column Index")){
				indexInputInvalid = val.isInvalid(dimensionType, dimensionID, dimensionValue, rowLength, columnLength);				
			}
			
		}while(indexInputInvalid);
		
		return dimensionValue;
	}
	
	public void menuChoices(int rowLength, int columnLength){
		System.out.print("\nRows: " + rowLength + "\tColumns: " + columnLength);
		System.out.println("\n======================================");
		System.out.println("[1] Search");
		System.out.println("[2] Edit");
		System.out.println("[3] Print");
		System.out.println("[4] Reset");
		System.out.println("[5] Add New Row");
		System.out.println("[6] Sort Rows by Key [ASC]");
		System.out.println("[7] Exit");

	}
	
	public void menu(int rowLength, int columnLength, ArrayList<LinkedHashMap<String,String>> fullMap, Validations val, String fileName) throws Exception{
		boolean menuInputInvalid = false, switchInputValid = true;	
		int menuChoice = 0;
		int newRowLength = 0;
		
		menuChoices(rowLength, columnLength);		
		
		//OOP
		do{
			do{
				menuInputInvalid = false;
			
				int tempMenuChoice = val.isInputMismatch("Menu", "Menu");			
			
				//menuInputInvalid = val.isInvalid("Menu", tempMenuChoice);
				
				menuChoice = tempMenuChoice;
			}while(menuInputInvalid);
			
			switch(menuChoice){
			case 1: //Search	
				fullMap = readFromFile(rowLength, columnLength, fileName);
				search(rowLength, columnLength, fullMap, val);
				display(rowLength, columnLength, fullMap, val, fileName);
				break;
			case 2: //Edit		
				fullMap = readFromFile(rowLength, columnLength, fileName);
				fullMap = edit(rowLength, columnLength, fullMap, val);
				writeToFile(fullMap,fileName,rowLength,columnLength);
				display(rowLength, columnLength, fullMap, val, fileName);
				break;
			case 3:	//Print		
				display(rowLength, columnLength, fullMap, val, fileName);
				break;
			case 4: //Reset		
				fullMap = reset("", rowLength, columnLength);
				writeToFile(fullMap,fileName,rowLength,columnLength);
				display(rowLength, columnLength, fullMap, val, fileName);
				break;
			case 5: //Add New Row
				fullMap = readFromFile(rowLength, columnLength, fileName);							
				newRowLength = rowLength  +  1;
				fullMap = addRow(newRowLength, columnLength, fullMap, val);				
				writeToFile(fullMap,fileName,newRowLength,columnLength);
				display(newRowLength, columnLength, fullMap, val, fileName);
				break;
			case 6: // Sort row by key ASC 
				fullMap = readFromFile(rowLength, columnLength, fileName);
				fullMap = sort(fullMap, rowLength, columnLength);
				writeToFile(fullMap,fileName,rowLength,columnLength);
				display(rowLength, columnLength, fullMap, val, fileName);
				break;
			case 7: //Exit
				System.exit(0);
				break;
			default:
				break;
			}						
			System.out.println();
		}while(switchInputValid);
	}
	
	public void search(int rowLength, int columnLength, ArrayList<LinkedHashMap<String,String>> fullMap, Validations val) throws Exception{
		String searchStr = "";
		boolean searchInputInvalid = false;
		
		int searchStrOccur = 0,
				rowIndex = 0,
				columnIndex = 0;
				
		//OOP
		do{
			searchInputInvalid = false;
			System.out.print("Enter string to be searched: ");
			String tempSearchStr = scan.nextLine();
			searchInputInvalid = val.isInvalid("Search", tempSearchStr);		
			searchStr = tempSearchStr;
		}while(searchInputInvalid);
		
		System.out.println("Occurrence/s of '" + searchStr + "' \n\tfound at: ");
		
		map(SEARCH_FLAG,searchStr,fullMap,0,0);				
	}

	public int searchString(String searchFlag, String keyValueCompare,int rowIndex, int columnIndex){
		int fromIndex = 0,
				searchStrCtr = 0,
				searchStrOccur = 0;
				
		//Maven		
		searchStrOccur = StringUtils.countMatches(keyValueCompare,searchFlag);		
		
		/*while(fromIndex != -1){
			fromIndex = keyValueCompare.indexOf(searchFlag, fromIndex);
			
			if(fromIndex != -1){
				searchStrCtr++;
				fromIndex += searchFlag.length();
				searchStrOccur++;
			}					
		}*/
		
		if(searchStrOccur > 0){
			System.out.println("\t[" + rowIndex + "," + columnIndex + "]");
		}
		
		return searchStrOccur;
	}
	
	public ArrayList<LinkedHashMap<String,String>> edit(int rowLength, int columnLength, ArrayList<LinkedHashMap<String,String>> fullMap, Validations val) throws Exception{
		
		int rowIndex = 0, 
				columnIndex = 0;
		String keyValueChoice = "",
						newKeyContent = "",
						newValueContent = "",
						currentKeyContent = "",
						currentValueContent = "";
		
		
		int rowIndexCtr = 0, 
				columnIndexCtr = 0;
		
		//OOP
		rowIndex = inputDimensions(ROW_FLAG, ROW_INDEX_FLAG, val, rowLength, columnLength);
		columnIndex = inputDimensions(COLUMN_FLAG, COLUMN_INDEX_FLAG, val, rowLength, columnLength);
		
		keyValueChoice = editKeyValue(EDIT_MENU, val);
		if(keyValueChoice.equalsIgnoreCase(EDIT_KEY)){
			newKeyContent = editKeyValue(KEY_FLAG, val);
		}
		else if(keyValueChoice.equalsIgnoreCase(EDIT_VALUE)){
			newValueContent = editKeyValue(VALUE_FLAG, val);
		}
		else if(keyValueChoice.equalsIgnoreCase(EDIT_BOTH)){
			newKeyContent = editKeyValue(KEY_FLAG, val);
			newValueContent = editKeyValue(VALUE_FLAG, val);
		}

		currentValueContent = map(EDIT_FLAG, REPLACE_KEY, fullMap, rowIndex, columnIndex);
		currentKeyContent = map(EDIT_FLAG, REPLACE_VALUE, fullMap, rowIndex, columnIndex);
		
		fullMap = implementKeyValue(fullMap, keyValueChoice, newKeyContent, newValueContent, currentKeyContent, currentValueContent, rowIndex, columnIndex, rowLength, columnLength);
				
		return fullMap;
	}	
	
	public String editKeyValue(String editFlag, Validations val){	
		boolean editInputInvalid = false;
		String newContent = "";
		int keyValueChoice = 0;
	
		if(editFlag.equalsIgnoreCase(EDIT_MENU)){
			do{
				editInputInvalid = false;
				System.out.print("Edit:");
				System.out.print("\n\t[1] Key");
				System.out.print("\n\t[2] Value");
				System.out.print("\n\t[3] Key-Value");
				int tempKeyValueChoice = val.isInputMismatch(EDIT_FLAG, NULL_FLAG);
				keyValueChoice = tempKeyValueChoice;
				if(keyValueChoice == 1){
					return EDIT_KEY;			
				}
				else if(keyValueChoice == 2){
					return EDIT_VALUE;
				}
				else if(keyValueChoice == 3){
					return EDIT_BOTH;
				}
				else{
					System.out.println("Invalid Edit Menu Input");
					editInputInvalid = true;
				}
			}while(editInputInvalid);			
		}
		else if(editFlag.equalsIgnoreCase(KEY_FLAG) | editFlag.equalsIgnoreCase(VALUE_FLAG)){
			do{
				editInputInvalid = false;
				
				System.out.println();
				System.out.print("\tEnter new " + editFlag + ": ");
				String tempContent = scan.nextLine();
				editInputInvalid = val.isInvalid(EDIT_FLAG, tempContent);
				newContent = tempContent;
			}while(editInputInvalid);
		}
		return newContent;	
	}
	
	public ArrayList<LinkedHashMap<String,String>> implementKeyValue(ArrayList<LinkedHashMap<String,String>> fullMap, String keyValueChoice, String newKeyContent, String newValueContent, String currentKeyContent, String currentValueContent, int rowIndex, int columnIndex, int rowLength, int columnLength){		
									
		int rowIndexCtr = 0,
				columnIndexCtr = 0;		
		String keyContent = "",
						valueContent = "";
		LinkedHashMap<String,String> columnMap = new LinkedHashMap<String,String>();
		
		for(LinkedHashMap<String,String> keyValuePairMap : fullMap){	
			
			for(Map.Entry<String,String> mapEntry : keyValuePairMap.entrySet()){
				if(rowIndexCtr == rowIndex){
					keyContent = mapEntry.getKey();
					valueContent = mapEntry.getValue();
				}
				
				if(columnIndexCtr == columnIndex && rowIndexCtr == rowIndex){
					if(keyValueChoice.equalsIgnoreCase(EDIT_KEY)){			
						columnMap.put(newKeyContent, currentValueContent);
					}
					else if(keyValueChoice.equalsIgnoreCase(EDIT_VALUE)){
						columnMap.put(currentKeyContent, newValueContent);
					}
					else if(keyValueChoice.equalsIgnoreCase(EDIT_BOTH)){
						columnMap.put(newKeyContent, newValueContent);
					}
					columnIndexCtr++;
					continue;
					
				}
				if(rowIndexCtr == rowIndex){
					columnMap.put(keyContent,valueContent);
				}		
				
				columnIndexCtr++;
			}
			if(rowIndexCtr == rowIndex){
				fullMap.remove(rowIndex);
				fullMap.add(rowIndex,columnMap);
				break;
			}
			columnIndexCtr = 0;
			rowIndexCtr++;
		}
				
		return fullMap;
	}
	
	public void display(int rowLength, int columnLength, ArrayList<LinkedHashMap<String,String>> fullMap, Validations val, String fileName) throws Exception{
		columnLength = readColumnLength(fileName);
		rowLength = readRowLength(fileName);
		
		fullMap = readFromFile(rowLength, columnLength, fileName);
		
		print(rowLength, columnLength, fullMap);
		
		menu(rowLength, columnLength, fullMap, val, fileName);	
	}
	
	public void print(int rowLength, int columnLength, ArrayList<LinkedHashMap<String,String>> fullMap) throws Exception{
		System.out.println();
		System.out.print("   |\t  ");
		for(int columnHeader = 0; columnHeader < columnLength; columnHeader++){
			System.out.print(columnHeader + "      |      ");
		}
		System.out.println();
		map(PRINT_FLAG, NULL_FLAG, fullMap, NULL_ROW, NULL_COLUMN);
		
			
	}
	
	public String map(String mapFlag, String stringContent, ArrayList<LinkedHashMap<String,String>> fullMap, int rowLength, int columnLength) throws Exception{
		int rowIndex = 0,
				columnIndex = 0,
				fromIndex = 0,
				searchStrCtr = 0,
				searchStrOccur = 0,
				rowHeader = 0;
		
		for(LinkedHashMap<String,String> keyValuePairMap : fullMap){
			if(mapFlag.equalsIgnoreCase(PRINT_FLAG)){
				if(rowHeader <= 9){
					System.out.print(rowHeader++ + "  |  ");
				}	
				else if(rowHeader > 9){
					System.out.print(rowHeader++ + " |  ");
				}
			}			
			for(Map.Entry<String,String> mapEntry : keyValuePairMap.entrySet()){
				if(mapFlag.equalsIgnoreCase(PRINT_FLAG)){
					System.out.print(mapEntry.getKey() + " , " + mapEntry.getValue() + "  |  ");
				}
				else if(mapFlag.equalsIgnoreCase(SEARCH_FLAG)){
					String keyValueCompare = mapEntry.getKey();
					keyValueCompare += mapEntry.getValue();
					searchStrOccur += searchString(stringContent,keyValueCompare,rowIndex,columnIndex);
					
				}
				else if(mapFlag.equalsIgnoreCase(EDIT_FLAG)){
					if(columnIndex == columnLength && rowIndex == rowLength){
						if(stringContent.equalsIgnoreCase(REPLACE_KEY)){
							String currentValueContent = mapEntry.getValue();		

							return currentValueContent;
						}
						else if(stringContent.equalsIgnoreCase(REPLACE_VALUE)){
							String currentKeyContent = mapEntry.getKey();	
							
							return currentKeyContent;
						}				
					}
				}
				
				if(mapFlag.equalsIgnoreCase(SEARCH_FLAG) | mapFlag.equalsIgnoreCase(EDIT_FLAG)){
					columnIndex++;
				}
			}
			if(mapFlag.equalsIgnoreCase(PRINT_FLAG)){
					System.out.println("");
				}
				else if(mapFlag.equalsIgnoreCase(SEARCH_FLAG) | mapFlag.equalsIgnoreCase(EDIT_FLAG)){
					columnIndex = 0;
					rowIndex++;
				}			
		}
		if(mapFlag.equalsIgnoreCase(SEARCH_FLAG)){
			System.out.println("TOTAL OCCURENCE: " + searchStrOccur);
		}
		return "";
	}
	
	public ArrayList<LinkedHashMap<String,String>> reset(String flagId, int rowLength, int columnLength){
		
		int randomNumber = 0,
				charCtr = 0,
				range = 0;
		char ch = 0;
		
		int maxColumnLength = columnLength - 1;
		int maxRowLength = rowLength - 1;
		
		Random random = new Random();
		
		//OOP Collections
		ArrayList<LinkedHashMap<String,String>> fullMap = new ArrayList<LinkedHashMap<String,String>>();
		LinkedHashMap<String,String> columnMap;
		
		range = ASCII_MAX - ASCII_MIN + 1;
		
		for(int rowCtr = 0; rowCtr < rowLength; rowCtr++){
			columnMap = new LinkedHashMap<String,String>();
			
			for(int columnCtr = 0; columnCtr < columnLength; columnCtr++){
				String keyContent = "";
				String valueContent = "";
				charCtr = KEY_VALUE_PAIR_LENGTH;
		
				while(charCtr > 0 && charCtr <=6){
					randomNumber = random.nextInt(range) + ASCII_MIN;
			
					ch = (char)randomNumber;
			
					if(charCtr > 3 && charCtr <= 6){					
						keyContent += ch;
					}
					else if(charCtr > 0 && charCtr <= 3){
						valueContent += ch;
					}
					charCtr--;						
				}	
				columnMap.put(keyContent,valueContent);
			}
			fullMap.add(columnMap);
		}		
		
		return fullMap;		
	}
	
	public ArrayList<LinkedHashMap<String,String>> addRow(int rowLength, int columnLength, ArrayList<LinkedHashMap<String,String>> fullMap, Validations val){
		int addRowChoice = 0;
		boolean addRowInputInvalid = false;
		
		
		System.out.println("\nAdd Row");
		System.out.println("\t[1] Generated Key-Value Pairs");
		System.out.println("\t[2] User Inputted");
		
		do{
			addRowInputInvalid = false;

			System.out.println();
			int tempAddRowChoice = val.isInputMismatch(ADD_ROW, NULL_FLAG);
			addRowChoice = tempAddRowChoice;
			
		}while(addRowInputInvalid);
		
		if(addRowChoice == 1){
			fullMap = addGeneratedRow(rowLength, columnLength, fullMap);
		}
		else if(addRowChoice == 2){
			fullMap = addInputtedRow(rowLength, columnLength, fullMap, val);
		}
		
		return fullMap;
	
	}
	
	public ArrayList<LinkedHashMap<String,String>> addGeneratedRow(int rowLength, int columnLength, ArrayList<LinkedHashMap<String,String>> fullMap){

		int randomNumber = 0,
				charCtr = 0,
				range = 0;
		char ch = 0;
		
		Random random = new Random();
		
		LinkedHashMap<String,String> columnMap = new LinkedHashMap<String,String>();
		
		range = ASCII_MAX - ASCII_MIN + 1;
		
		for(int columnCtr = 0; columnCtr < columnLength; columnCtr++){
			String keyContent = "";
			String valueContent = "";
			charCtr = KEY_VALUE_PAIR_LENGTH;
	
			while(charCtr > 0 && charCtr <=6){
				randomNumber = random.nextInt(range) + ASCII_MIN;
		
				ch = (char)randomNumber;
		
				if(charCtr > 3 && charCtr <= 6){					
					keyContent += ch;
				}
				else if(charCtr > 0 && charCtr <= 3){
					valueContent += ch;
				}
				charCtr--;						
			}	
			columnMap.put(keyContent,valueContent);		
		}
		fullMap.add(columnMap);		
		
		return fullMap;
	
	}
	
	public ArrayList<LinkedHashMap<String,String>> addInputtedRow(int rowLength, int columnLength, ArrayList<LinkedHashMap<String,String>> fullMap, Validations val){

		int columnIndex = 0;
		boolean userInputInvalid = false;
		String keyContent = "", 
						valueContent = "";
				
		int maxRowLength = rowLength - 1;
		int maxColumnLength = columnLength - 1;
		
		LinkedHashMap<String,String> columnMap = new LinkedHashMap<String,String>();		

		System.out.println("\t\tUser Inputted:");
		while(columnIndex<columnLength){
			
			System.out.println("\t\t\t[" + (maxRowLength) + ", " + columnIndex + "] of [" + (maxRowLength) + ", " + (maxColumnLength) + "]");
			
			do{
				userInputInvalid = false;
				System.out.print("\t\t\tEnter key: ");
				String tempKeyContent = scan.nextLine();				
				userInputInvalid = val.isInvalid(ADD_ROW, tempKeyContent);
				keyContent = tempKeyContent;
				
			}while(userInputInvalid);
			
			do{
				System.out.print("\t\t\tEnter value: ");
				String tempValueContent = scan.nextLine();
				userInputInvalid = val.isInvalid(ADD_ROW, tempValueContent);
				valueContent = tempValueContent;
				
			}while(userInputInvalid);
			System.out.println();
			
			columnMap.put(keyContent,valueContent);
			columnIndex++;
		}
		
		fullMap.add(columnMap);
		
		return 	fullMap;
	}	
	
	public ArrayList<LinkedHashMap<String,String>> sort(ArrayList<LinkedHashMap<String,String>> fullMap, int rowLength, int columnLength){
		
		int rowIndex = 0;
		
		while(rowIndex < rowLength){
			fullMap = sortRowByKeyAsc(fullMap, rowIndex, columnLength);
			rowIndex++;
		}
		
		return fullMap;
		
	}
	
	public ArrayList<LinkedHashMap<String,String>> sortRowByKeyAsc(ArrayList<LinkedHashMap<String,String>> fullMap, int rowIndex, int columnLength){
	
		int rowIndexCtr = 0,
				columnIndexCtr = 0;		
		String keyContent = "",
						valueContent = "";
						
		TreeMap<String,String> sortedMap = new TreeMap<String,String>();
		LinkedHashMap<String,String> columnMap = new LinkedHashMap<String,String>();
		
		for(LinkedHashMap<String,String> keyValuePairMap : fullMap){	
			
			for(Map.Entry<String,String> mapEntry : keyValuePairMap.entrySet()){
				if(rowIndexCtr == rowIndex){
					keyContent = mapEntry.getKey();
					valueContent = mapEntry.getValue();
					sortedMap.put(keyContent,valueContent);
					columnIndexCtr++;
				}				
			}
			if(rowIndexCtr == rowIndex){
				columnMap.putAll(sortedMap);
				fullMap.remove(rowIndex);
				fullMap.add(rowIndex,columnMap);
				break;
			}
			columnIndexCtr = 0;
			rowIndexCtr++;
		}
		
		return fullMap;
	}
	
	public int readColumnLength(String fileName) throws Exception{		
		int columnCtr = 0,
				totalColumns = 0,
				fromIndex = 0;
		
		//Scanner read = new Scanner(new File(fileName));
		String fileFirstLine = "";
		
		//Maven
		List<String> fileContents = FileUtils.readLines(new File(fileName));
		fileFirstLine = fileContents.get(0);
		
		//Maven
		totalColumns = StringUtils.countMatches(fileFirstLine,DELIMITER);
		

		/*fileFirstLine = read.nextLine();
		
		while(fromIndex != -1){
		  	
		  	fromIndex = fileFirstLine.indexOf(DELIMITER, fromIndex);
		  	
		  	if(fromIndex != -1){
		  		totalColumns++;
		  		fromIndex += DELIMITER.length();
		  	}	  	
		}	
		read.close();*/
		return totalColumns;
	}
	
	public int readRowLength(String fileName) throws Exception{		
		int totalRows = 0;
		
		List<String> fileContents = FileUtils.readLines(new File(fileName));
		
		totalRows = fileContents.size();
		
		/*Scanner read = new Scanner(new File(fileName));
		
		String rowEntry = "";
		
		while(read.hasNext()){
			rowEntry = read.nextLine();
		  	totalRows++;
		}
		read.close();*/		
		return totalRows;
	}
	
	public ArrayList<LinkedHashMap<String,String>> readFromFile(int rowLength, int columnLength, String fileName) throws Exception{
		ArrayList<LinkedHashMap<String,String>> fromFileMap = new ArrayList<LinkedHashMap<String,String>>();
		LinkedHashMap<String, String> columnMap = new LinkedHashMap<String,String>();
		
		String keyRead = "", 
					valueRead = "",
					nullRead = "";
		int rowIndex = 0, 
				columnIndex = 0;
		
		Scanner read = new Scanner(new File(fileName));
		read.useDelimiter("¦|©|\\r\\n");
		
		while(read.hasNext() & rowIndex<rowLength){

			keyRead = read.next();
			valueRead = read.next();				
			
			columnMap.put(keyRead, valueRead);
			columnIndex++;

			if(columnIndex >= columnLength){
				fromFileMap.add(rowIndex, columnMap);
				columnMap = new LinkedHashMap<String,String>();
				columnIndex = 0;
				rowIndex++;
			}
		}
		read.close();
		
		return fromFileMap;
	}
	
	public void writeToFile(ArrayList<LinkedHashMap<String,String>> fullMap, String fileName, int rowLength, int columnLength) throws Exception{
		//PrintWriter writer = new PrintWriter(fileName);
		File file = new File(fileName);
		
		String keyContent = "", 
					valueContent = "", 
					rowEntry = "";		
		int rowIndex = 0, 
				columnIndex = 0;
		boolean doAppend = true;
				
		for(LinkedHashMap<String,String> keyValuePairMap : fullMap){	
			
			for(Map.Entry<String,String> mapEntry : keyValuePairMap.entrySet()){
				keyContent = mapEntry.getKey();
				valueContent = mapEntry.getValue();
				rowEntry += keyContent + DELIMITER + valueContent;
				if(columnIndex < columnLength && columnIndex != columnLength-1){
					rowEntry += SPACE_DELIMITER;
				}
				columnIndex++;
			}
			
			if(rowIndex == 0){
				FileUtils.writeStringToFile(file, rowEntry);
				//writer.print(rowEntry);
			}
			else if(rowIndex > 0){
				rowEntry = CARRIAGE_RETURN_DELIMITER + rowEntry;
				FileUtils.writeStringToFile(file, rowEntry, doAppend);
				//writer.print("\r\n" + rowEntry);
			}			
			columnIndex = 0;
			rowIndex++;
			rowEntry = "";
		}	
	}	
}
