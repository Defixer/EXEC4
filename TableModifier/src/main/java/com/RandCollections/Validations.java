package com.RandCollections;

import java.util.InputMismatchException;
import java.util.Scanner;
public class Validations{

	private Scanner sc = new Scanner(System.in);
	private final String ROW_FLAG = "Row",
								COLUMN_FLAG = "Column",
								ROW_INDEX = "Row Index",
								COLUMN_INDEX = "Column Index",
								MENU_FLAG = "Menu",
								ADD_ROW = "Add Row",
								EDIT_FLAG = "Edit",
								SEARCH_FLAG = "Search";

	public int isInputMismatch(String exceptFlag, String flagId){
		int integerValue = 0;
		boolean inputMismatch = false;
		
		if(exceptFlag.equalsIgnoreCase(ROW_FLAG) | exceptFlag.equalsIgnoreCase(COLUMN_FLAG) | exceptFlag.equalsIgnoreCase(ROW_INDEX) | exceptFlag.equalsIgnoreCase(COLUMN_INDEX)){
		
			do{
				inputMismatch = false;
				
				if(flagId.equalsIgnoreCase(ROW_FLAG) | flagId.equalsIgnoreCase(COLUMN_FLAG)){
					System.out.print("\tNumber of " + flagId + ": ");
				}				
				else if(flagId.equalsIgnoreCase(ROW_INDEX) | flagId.equalsIgnoreCase(COLUMN_INDEX)){
					System.out.print("\tInput " + exceptFlag + " Index : ");
				}
				try{
					int tempIntegerValue = sc.nextInt();sc.nextLine();
					integerValue = tempIntegerValue;	
				}
				catch(InputMismatchException ime){
					System.out.println("\tInvalid Input Format\n");
					sc.nextLine();
					inputMismatch = true;
				}
			}while(inputMismatch);			
		}
		else if(exceptFlag.equalsIgnoreCase(MENU_FLAG) | exceptFlag.equalsIgnoreCase(ADD_ROW) | exceptFlag.equalsIgnoreCase(EDIT_FLAG)){
			do{
				inputMismatch = false;
				try{
					if(exceptFlag.equalsIgnoreCase(ADD_ROW) | exceptFlag.equalsIgnoreCase(EDIT_FLAG)){
						if(exceptFlag.equalsIgnoreCase(EDIT_FLAG)){
							System.out.print("\n");
						}
						System.out.print("\t");
					}
					
					System.out.print("Enter choice: ");
					int tempIntegerValue = sc.nextInt();sc.nextLine();
					integerValue = tempIntegerValue;		
				}
				catch(InputMismatchException ime){
					if(exceptFlag.equalsIgnoreCase(EDIT_FLAG) | exceptFlag.equalsIgnoreCase(ADD_ROW)){
						System.out.print("\t");
					}
					System.out.println("Invalid Input Format\n");
					sc.nextLine();
					inputMismatch = true;
					continue;			
				}
				if(exceptFlag.equalsIgnoreCase(MENU_FLAG) | exceptFlag.equalsIgnoreCase(ADD_ROW)){
					final int MIN_CHOICE = 0;
					
					int maxChoice=0;
					if(exceptFlag.equalsIgnoreCase(MENU_FLAG)){						
						maxChoice = 7;
						exceptFlag =MENU_FLAG;
					}
					else if(exceptFlag.equalsIgnoreCase(ADD_ROW)){
						maxChoice = 2;
						exceptFlag =ADD_ROW;
					}
					if(integerValue <= MIN_CHOICE || integerValue > maxChoice){
						String addRowMenu = "";
						if(exceptFlag.equalsIgnoreCase(ADD_ROW)){
							System.out.print("\t");
							addRowMenu = " Menu";
						}
						inputMismatch = true;
						System.out.println("Invalid " + exceptFlag + addRowMenu + " Input\n");
					}				
				}
				
			}while(inputMismatch);					
		}
		
		return integerValue;		
	}
	
	public boolean isInvalid(String exceptFlag, String tempStr){
		if(exceptFlag.equalsIgnoreCase(SEARCH_FLAG)){
			if(tempStr.length() <= 0 || tempStr.length() > 6){
				System.out.println("Input not within range");
				return true;			
			}
			return false;
		}
		else if(exceptFlag.equalsIgnoreCase(EDIT_FLAG) | exceptFlag.equalsIgnoreCase(ADD_ROW)){
			String tempStrArr[] = tempStr.split("");
			int index = 0;
			final int MAX_INDEX = 3;
			boolean invalidHit = false;
			
			String invalidAsciiPattern = invalidAscii();
			
			if(tempStr.length() != 3){
				if(exceptFlag.equalsIgnoreCase(EDIT_FLAG)){
					System.out.print("\t");
				}
				System.out.println("Input not within range");
				return true;			
			}			
			
			while(index < MAX_INDEX){
				String tempStrToken = tempStrArr[index];
				
				if(tempStrArr[index].equalsIgnoreCase(" ")){
					index++;
					continue;
				}
				
				invalidHit = invalidAsciiPattern.contains(tempStrToken);
				if(invalidHit == true){
					if(exceptFlag.equalsIgnoreCase(ADD_ROW)){
						System.out.print("\t");
					}
					System.out.println("\tInvalid Input Format");
					return true;
				}
				index++;				
			}
		}
		
		return false;
	}
	
	public String invalidAscii(){
		int maxLowerAscii = 32, minLowerAscii = 0;
		int maxExtendedAscii = 255, minExtendedAscii = 127;
		
		StringBuilder sb = new StringBuilder();
		Scanner sc = new Scanner(System.in);
		
		char ch = 0;
		
		while(minLowerAscii<=maxLowerAscii){
			ch = (char)minLowerAscii;
			sb.append(ch);
			minLowerAscii++;
		}
		
		while(minExtendedAscii<=maxExtendedAscii){
			ch = (char)minExtendedAscii;
			sb.append(ch);
			minExtendedAscii++;
		}
		
		String invalidAsciiPattern = sb.toString();
		
		return invalidAsciiPattern;
	}	
	
	public boolean isInvalid(String exceptFlag, int exceptValue){
		if(exceptFlag.equalsIgnoreCase(ROW_FLAG) | exceptFlag.equalsIgnoreCase(COLUMN_FLAG)){
			if(exceptValue <= 0){
				System.out.println("\tInvalid " + exceptFlag + " Input\n");
				return true;
			}
		}		
		
		return false;		
	}
	
	public boolean isInvalid(String exceptFlag, String flagId, int exceptValue, int rows, int cols){
		int dimension = 0;
		
		if(exceptFlag.equalsIgnoreCase(ROW_FLAG)){
			dimension = rows;
		}
		else if(exceptFlag.equalsIgnoreCase(COLUMN_FLAG)){
			dimension = cols;
		}
	
		if(flagId.equalsIgnoreCase(ROW_INDEX) | flagId.equalsIgnoreCase(COLUMN_INDEX)){
			int maxDimension = dimension - 1;
			
			if(exceptValue>maxDimension){
				System.out.println("\tInvalid " + flagId + " Index. Max " + flagId + " Index is " + maxDimension + "\n");
				return true;
			}			
			else if(exceptValue < 0){
				System.out.println("\tInvalid " + flagId + " Index. Min " + flagId + " Index is 0\n ");
				return true;
			}		
		}
		
		return false;
	}
	
}
