
/*
 *
 * Saleh Alghusson
 * Ovais Panjwani
 * CS f361 assignment 2
 * https://www.cs.utexas.edu/~byoung/cs361/assignment2-zhao.html
 *
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * Constant security levels
 */
enum Level {
   HIGH, LOW 
}

/*
 *	Class CovertChannel is the driver class of the program
 */
public class CovertChannel{
	
	private static CovertChannel instance = null;
	public static SubjectManager sm = new SubjectManager();
	public static ReferenceMonitor rm = new ReferenceMonitor();
	
	/*	main function	*/
	public static void main(String[] args) {	

		sm.createSubject("lyle", Level.LOW);
		sm.createSubject("hal", Level.HIGH);
		
		/*
		long startTime = System.currentTimeMillis();
		*/
		read(args);
		/*
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		
		System.out.println("Ended with time -- " + totalTime);
		*/
	}
	
	/* 	will print the state of the program	*/
	void printState(){
		System.out.print(rm.om.printObjects());
		System.out.print(sm.printSubjects());
	}
	
	/*	default constructor	*/
	private CovertChannel(){}
	
	/*	for singleton purposes	*/
	public static CovertChannel getCov(){
		if( instance == null)
			instance = new CovertChannel();
		return instance;
	}
	
	/*	helper method to read input, will call read() in Reader class	*/
	static void read(String[] args){
		try {
			if (args.length < 1){
				System.out.println("No input file provided, program will exit");
				System.exit(1);
			}
			if(args.length >= 1){
				if(args[0].equalsIgnoreCase("v"))
					ReaderWriter.read("v", args[1]);
				else
					ReaderWriter.read(null, args[0]);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		catch (Exception e){
			e.printStackTrace();
			System.err.println("Invalid arguments count:" + args.length);
			System.exit(0);
		}
	}
	/* will extract bith from a byte and send it to execute()	*/
	static void executeHelper(byte[] bytearray){
		for(byte by: bytearray)
			for (int i = 7; i >-1; i--)
				execute( ((by >> i) & 1) != 0 );
	}
	/* chain of commands to transmit either a 0 or a 1 over a Covert Channel	*/
	static void execute(boolean bit){
		try{
			if (! bit) {
				
		    	ReferenceMonitor.runInstruction(new InstructionObject("CREATE HAL OBJ").isValid());
		    	ReferenceMonitor.runInstruction(new InstructionObject("CREATE LYLE OBJ").isValid());
		    	ReferenceMonitor.runInstruction(new InstructionObject("WRITE LYLE OBJ 1").isValid());
		    	ReferenceMonitor.runInstruction(new InstructionObject("READ LYLE OBJ").isValid());
		    	ReferenceMonitor.runInstruction(new InstructionObject("DESTROY LYLE OBJ").isValid());
		    	ReferenceMonitor.runInstruction(new InstructionObject("RUN LYLE").isValid());   	 
			} 
			else{
				
		    	ReferenceMonitor.runInstruction(new InstructionObject("CREATE LYLE OBJ").isValid());
		    	ReferenceMonitor.runInstruction(new InstructionObject("WRITE LYLE OBJ 1").isValid());
		    	ReferenceMonitor.runInstruction(new InstructionObject("READ LYLE OBJ").isValid());
		    	ReferenceMonitor.runInstruction(new InstructionObject("DESTROY LYLE OBJ").isValid());
		    	ReferenceMonitor.runInstruction(new InstructionObject("RUN LYLE").isValid()); 
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return;
		}
	}
}

/*
 * Class is responsible to evaluate the dominates function 	
 */
class SecurityLevel{
	/*	method will return a boolean representing the dominates relation	*/
	static boolean dominates(Level x, Level y){
		if ((x == Level.LOW) && (y == Level.HIGH)){
			return false;
		}
		return true;
	}
}

/*
 * Class SubjectManager will manage Subjects
 */
class SubjectManager{
	private static ArrayList<Subject> list = new ArrayList<Subject>();
	
	/*	Inner class that constitutes a Subject 	*/
	private class Subject{
		String name;
		int temp;
		Level level;
		
		Subject(String name, int temp, Level level){
			this.name = name;
			this.temp = temp;
			this.level = level;
		}
		/* method will set the temp value of a Subject 	*/
		void setTemp(int temp){
			this.temp = temp;
		}
		/* method will get the temp value of a Subject 	*/
		int getTemp(){
			return temp;
		}
	}
	/*	method to create a Subject 	*/
	boolean createSubject(String name, Level level){
		if(subjectExists(name))
				return false;
		list.add(new Subject(name, 0, level));
		return true;
	}
	/*	method will return true if a Subject exists 	*/
	boolean subjectExists(String name){
		for (Subject x: list)
			if(x.name.equalsIgnoreCase(name))
				return true;
		return false;
	}
	/*	method will print subjects as required by the project requirements 	*/
	String printSubjects(){
		String s = "";
		for (Subject x: list)
			s = s + "\t" + x.name + " has recently read: " + x.temp + "\n";
		return s+"\n";
	}
	/*	method will return the level of the Subject with provided name 	*/
	Level subjectLevel(String name){
		for (Subject x: list)
			if(x.name.equalsIgnoreCase(name))
				return x.level;
		return null;
	}
	/*	method will set the temp value of a Subject 	*/
	void setTemp(String name, int temp){
		for (Subject x: list)
			if(x.name.equalsIgnoreCase(name))
				x.setTemp(temp);
	}
	/*	method will get the temp value of a Subject 	*/
	Integer getTemp(String name){
		for (Subject x: list)
			if(x.name.equalsIgnoreCase(name))
				return x.getTemp();
		return null;
	}
}

/*
 * Class will be responsible for running instructions and has the ObjectManager inside of it
 */
class ReferenceMonitor{
	static ObjectManager om = new ObjectManager();
	static private int counter = 8;
	static private byte temp_byte = 0;
	
	/*	method will run instructions 	*/
	static void runInstruction(InstructionObject ins) throws Exception{
		
		if (ins != null && ins.valid){
			ReaderWriter.log(ins);
			if (ins.op.equals("reads"))
				executeRead(ins);
			else if (ins.op.equals("writes"))
				executeWrite(ins);
			else if (ins.op.equals("runs"))
				executeRun(ins);
			else if (ins.op.equals("creates"))
				executeCreate(ins);
			else if (ins.op.equals("destroys"))
				executeDestroy(ins);
		}
	}
	
	/*
	 * Inner Class which will manage the objects
	 */
	static class ObjectManager{
		private static ArrayList<Object> list = new ArrayList<Object>();
		
		/*
		 * The object class
		 */
		private class Object{
			String name;
			int value;
			Level level;
			/*	constructor		*/
			Object(String name, int value, Level level){
				this.name = name;
				this.value = value;
				this.level = level;
			}
			/*	method will set an object's value 	*/
			void setValue(int x){
				this.value = x;
			}
			/*	method will return an object's value 	*/
			int getValue(){
				return value;
			}
		}
		/* method will create Objects 	*/
		boolean createObject(String name, int value, Level level){
			if(objectExists(name))
					return false;
			list.add(new Object(name, value, level));
			return true;
		}
		/* method will destroy Objects 	*/
		boolean destroyObject(String name){
			if(!objectExists(name))
					return false;
			Iterator<Object> it = list.iterator();
			while (it.hasNext()) {
			  Object object = it.next();
			  if (object.name.equals(name)) {
			    it.remove();
			    return true;
			  }
			}
			return false;
		}

		/* method will check if an Object exists or not 	*/
		boolean objectExists(String name){
			for (Object x: list)
				if(x.name.equalsIgnoreCase(name))
					return true;
			return false;
		}

		/* method will print Objects in the format required by document specs 	*/
		String printObjects(){
			String s = "The current state is: \n";
			for (Object x: list)
				s = s  + "\t" + x.name + " has value: " + x.value + "\n";
			return s;
		}

		/* method will return an Object's level 	*/
		Level objectLevel(String name){
			for (Object x: list)
				if(x.name.equalsIgnoreCase(name))
					return x.level;
			return null;
		}

		/*	method will return an Object provided it's name	*/
		Object getObject(String name){
			for (Object x: list)
				if(x.name.equalsIgnoreCase(name))
					return x;
			return null;
		}

		/*	method will set an Object's value	*/
		void setValue(String name, int value){
			for (Object x: list)
				if(x.name.equalsIgnoreCase(name))
					x.setValue(value);
		}
		/*	method will return an Object's value	*/
		Integer getValue(String name){
			for (Object x: list)
				if(x.name.equalsIgnoreCase(name))
					return x.getValue();
			return null;
		}
	}
	
	/*	method will execute a read instruction	*/
	static void executeRead(InstructionObject ins){
		Level subLevel = CovertChannel.sm.subjectLevel(ins.sub);
		Level objLevel = CovertChannel.rm.om.objectLevel(ins.obj);
		if(SecurityLevel.dominates(subLevel, objLevel) || subLevel == objLevel){
			CovertChannel.sm.setTemp(ins.sub, CovertChannel.rm.om.getObject(ins.obj).value);
		} else {
			CovertChannel.sm.setTemp(ins.sub, 0);
		}
	}
	
	/*	method will execute a write instruction	*/
	static void executeWrite(InstructionObject ins){
		Level subLevel = CovertChannel.sm.subjectLevel(ins.sub);
		Level objLevel = CovertChannel.rm.om.objectLevel(ins.obj);
		if(!SecurityLevel.dominates(subLevel, objLevel) || subLevel == objLevel){
			CovertChannel.rm.om.setValue(ins.obj, ins.value);
		}
	}
	
	/*	method will execute a run instruction	*/
	static void executeRun(InstructionObject ins)throws Exception{
		counter--;
		int val = CovertChannel.sm.getTemp(ins.sub);
		
		temp_byte = (byte) ( (temp_byte << 1) | val);
		
		if (counter == 0) {
			counter = 8;
			ReaderWriter.write(temp_byte);
		}
	}
	
	/*	method will execute a create instruction	*/
	static void executeCreate(InstructionObject ins){
		CovertChannel.rm.om.createObject(ins.obj, 0, CovertChannel.getCov().sm.subjectLevel(ins.sub));
	}
	
	/*	method will execute a destroy instruction	*/
	static void executeDestroy(InstructionObject ins){
		Level subLevel = CovertChannel.sm.subjectLevel(ins.sub);
		Level objLevel = CovertChannel.rm.om.objectLevel(ins.obj);
		if(!SecurityLevel.dominates(subLevel, objLevel) || subLevel == objLevel){
			CovertChannel.rm.om.destroyObject(ins.obj);
		}
	}
}
/*
* Class will represent Instructions
*/
class InstructionObject{
	
	String line;
	String op;
	String sub;
	String obj;
	int value;
	boolean valid = true;
	
	/*	default constructor		*/
	InstructionObject(){
	}
	
	/* constructor		*/
	InstructionObject(String line){
		this.line = line;
	}
	/*	toString method for printing */
	public String toString() {
		if(op == null)
			return "NULL";
		else if(op.equalsIgnoreCase("reads"))
			return(sub + " " + op + " " + obj + " with value " + CovertChannel.getCov().rm.om.getValue(obj));
		else if(op.equalsIgnoreCase("writes"))
			return sub + " " + op + " value " + value + " to " + obj;
		else if(op.equalsIgnoreCase("creates"))
			return sub + " " + op + " object: " + obj + " with default value: " + value;
		else if(op.equalsIgnoreCase("runs"))
			return sub + " ran";
		else
			return sub + " destroys object: " + obj + ", val = " +CovertChannel.sm.getTemp(sub);
	}
	/*	method will decide if an instruction is valid or not	*/
	InstructionObject isValid(){
		String[] tokens = line.split("\\s+");
		
		if(tokens.length < 2){
			return new BadInstruction();
		}
		else if (! CovertChannel.getCov().sm.subjectExists(tokens[1])){		//subject doesn't exist
			return new BadInstruction();
		}
		sub = tokens[1].toLowerCase();
		if (tokens[0].equalsIgnoreCase("READ") && tokens.length == 3){
			
			if(! CovertChannel.getCov().rm.om.objectExists(tokens[2])){
				return new BadInstruction();
			}
			op = "reads";
			obj = tokens[2].toLowerCase();
			
			return this;
		}
		else if (tokens[0].equalsIgnoreCase("WRITE") && tokens.length == 4){
			
			if(! CovertChannel.getCov().rm.om.objectExists(tokens[2])){
				return new BadInstruction();
			}
			try{
				value = Integer.parseInt(tokens[3]);
			}
			catch(NumberFormatException e){
				return new BadInstruction();
			}
			op = "writes";
			obj = tokens[2].toLowerCase();
			
			return this;
		}
		else if (tokens[0].equalsIgnoreCase("CREATE") && tokens.length == 3){
			if( CovertChannel.getCov().rm.om.objectExists(tokens[2])){		//object musn't exist
				return new BadInstruction();
			}
			op = "creates";
			obj = tokens[2].toLowerCase();
			
			return this;
		}
		else if (tokens[0].equalsIgnoreCase("DESTROY") && tokens.length == 3){
			
			if(! CovertChannel.getCov().rm.om.objectExists(tokens[2])){
				return new BadInstruction();
			}
			op = "destroys";
			obj = tokens[2].toLowerCase();
			return this;
		}
		else if (tokens[0].equalsIgnoreCase("RUN") && tokens.length == 2){
			op = "runs";
			return this;
		}
		else{
			return new BadInstruction();
		}
	}
	/*
	 * Class BadInstruction is invoked when there is a bad instruction
	 */
	static final class BadInstruction extends InstructionObject {
		String message = "Bad Instruction";
		
		BadInstruction(){
			valid = false;
		}
		public String toString() {
	        return message;
	    }

	}
}

/*
* ReaderWriter is a class that is responsible of reading and writing to files
*/
class ReaderWriter {
	static private String path = "";
	static private boolean verbose = false;
	
	/*	read method will read the file	*/
	static void read (String arg0, String arg1) throws Exception{
		
		path = arg1;
		File input = new File(arg1);
		
		if(arg0 != null && arg0.equals("v"))
			verbose = true;
		
		else
			verbose = false;
		
		if(!input.exists() || input.isDirectory()) { 
			throw new FileNotFoundException("File not found at path: " + input.getPath());
		}
		
	    BufferedReader br = null;
	    try {
	    	clear();
	    	String line = "";
		    br = new BufferedReader(new FileReader(input));
		    /*	feed the input file to the cover channel line by line	*/
		    while((line = br.readLine())!=null){
		    	CovertChannel.executeHelper(line.getBytes());
		    	write( (byte) 0xD);
		    }
	    } 
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				if (br != null)
					br.close();
		    } 
			catch (IOException ex) {
				ex.printStackTrace();
		    }
		}
	}
	
	/*	clear method will clear the output files before program execution	*/
	static void clear() throws IOException{
		FileOutputStream out = new FileOutputStream(path + ".out", false);
    	out.close();
    	if(path.contains("/"))
    		out = new FileOutputStream(path.substring(0, path.lastIndexOf("/")) + "/log", false);
    	else
    		out = new FileOutputStream("log", false);
    	out.close();
	}
	
	/*	write method will write to the file	*/
	static void write (byte temp_byte) throws Exception{
		FileOutputStream out = new FileOutputStream(path + ".out", true);
		out.write(temp_byte);
		out.close();
	}
	
	/*	log method will log the commands to the file	*/
	static void log(InstructionObject ins) throws IOException{
		if(!verbose)
			return;
		File fout;
		if(path.contains("/"))
    		fout = new File(path.substring(0, path.lastIndexOf("/")) + "/log");
    	else
    		fout = new File("log");
		
		FileOutputStream fos = new FileOutputStream(fout, true);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		bw.write(ins.toString()+"\n");
		bw.close();
	}
}