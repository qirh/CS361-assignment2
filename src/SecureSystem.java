
/*
*
* Saleh Alghusson
* Ovais Panjwani
* CS f361 assignment 1
* assignment1 details: https://www.cs.utexas.edu/~byoung/cs361/assignment1-nonthreaded-zhao.html
*
*/

import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/*
* Constant security levels
*/
enum Level {
   HIGH, LOW 
}

/*
*	Class SecureSystem is the driver class of the program
*/
public class SecureSystem{
	
	private static SecureSystem instance = null;
	public static SubjectManager sm = new SubjectManager();
	public static ReferenceMonitor rm = new ReferenceMonitor();
	
	public static void main(String[] args) {
		
		
		sm.createSubject("lyle", Level.LOW);
		sm.createSubject("hal", Level.HIGH);
		
		read(args);
		
	}
	/* 	will print the state of the program	*/
	void printState(){
		System.out.print(rm.om.printObjects());
		System.out.print(sm.printSubjects());
	}
	
	/*	default constructor	*/
	private SecureSystem(){}
	
	/*	for singleton purposes	*/
	public static SecureSystem getSys(){
		if( instance == null)
			instance = new SecureSystem();
		return instance;
	}
	
	/*	helper method to read input, will call read() in Reader class	*/
	static void read(String[] args){
		try {
			if (args.length < 1){
				System.out.println("No instructionlist file provided, program will exit");
				System.exit(1);
			}
			if(args.length > 1)
				Reader.read(args[0], args[1]);
			else
				Reader.read(args[0], null);
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
* Class will manage Subjects
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
	/*	method will return a subject 	
	Subject getSubject(String name){
		for (Subject x: list)
			if(x.name.equalsIgnoreCase(name))
				return x;
		return null;
	}
	*/
	/*	method will set the temp value of a Subject 	*/
	void setTemp(String name, int temp){
		for (Subject x: list)
			if(x.name.equalsIgnoreCase(name))
				x.setTemp(temp);
	}		
}

/*
* Class will be responsible for running instructions and has the ObjectManager inside of it
*/
class ReferenceMonitor{
	static ObjectManager om = new ObjectManager();
	
	/*	method will run instructions 	*/
	static void runInstruction(InstructionObject ins){
		System.out.println(ins);
		if (ins != null && ins.valid){
			if (ins.op.equals("reads"))
				executeRead(ins);
			else if (ins.op.equals("writes"))
				executeWrite(ins);
			else if (ins.op.equals("runs"))
				executeRun(ins);
			else if (ins.op.equals("creates"))
				executeRun(ins);
			else if (ins.op.equals("destroys"))
				executeRun(ins);
		}
		SecureSystem.getSys().printState();
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
			
			Object(String name, int value, Level level){
				this.name = name;
				this.value = value;
				this.level = level;
			}
			/*	method will set an object's value 	*/
			void setValue(int x){
				this.value = x;
			}
		}
		/* method will create Objects 	*/
		boolean createObject(String name, int value, Level level){
			if(objectExists(name))
					return false;
			list.add(new Object(name, value, level));
			return true;
		}
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
	}
	
	/*	method will execute a read instruction	*/
	static void executeRead(InstructionObject ins){
		Level subLevel = SecureSystem.sm.subjectLevel(ins.sub);
		Level objLevel = SecureSystem.rm.om.objectLevel(ins.obj);
		if(SecurityLevel.dominates(subLevel, objLevel) || subLevel == objLevel){
			SecureSystem.sm.setTemp(ins.sub, SecureSystem.rm.om.getObject(ins.obj).value);
		} else {
			SecureSystem.sm.setTemp(ins.sub, 0);
		}
	}
	
	/*	method will execute a write instruction	*/
	static void executeWrite(InstructionObject ins){
		Level subLevel = SecureSystem.sm.subjectLevel(ins.sub);
		Level objLevel = SecureSystem.rm.om.objectLevel(ins.obj);
		if(!SecurityLevel.dominates(subLevel, objLevel) || subLevel == objLevel){
			SecureSystem.rm.om.setValue(ins.obj, ins.value);
		}
	}
	static void executeRun(InstructionObject ins){
		
	}
	static void executeCreate(InstructionObject ins){
		Level subLevel = SecureSystem.sm.subjectLevel(ins.sub);
		Level objLevel = SecureSystem.rm.om.objectLevel(ins.obj);
		if(!SecurityLevel.dominates(subLevel, objLevel) || subLevel == objLevel){
			SecureSystem.rm.om.createObject(ins.obj, 0, SecureSystem.getSys().sm.subjectLevel(ins.sub));
		}
	}
	static void executeDestroy(InstructionObject ins){
		Level subLevel = SecureSystem.sm.subjectLevel(ins.sub);
		Level objLevel = SecureSystem.rm.om.objectLevel(ins.obj);
		if(!SecurityLevel.dominates(subLevel, objLevel) || subLevel == objLevel){
			SecureSystem.rm.om.destroyObject(ins.obj);
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
	
	InstructionObject(){
	}
	
	InstructionObject(String line){
		this.line = line;
	}
	/*	toString method for printing */
	public String toString() {
		if (op.equalsIgnoreCase("reads"))
			return(sub + " " + op + " " + obj);
		else
			return sub + " " + op + " value " + value + " to " + obj;
   }
	/*	method will decide if an instruction is valid or not	*/
	InstructionObject isValid(){
		String[] tokens = line.split("\\s+");
		
		if(tokens.length < 2){
			return new BadInstruction();
		}
		else if (! SecureSystem.getSys().sm.subjectExists(tokens[1])){		//subject doesn't exist
				return new BadInstruction();
		}
		sub = tokens[1].toLowerCase();
		if (tokens[0].equalsIgnoreCase("READ") && tokens.length == 3){
			
			if(! SecureSystem.getSys().rm.om.objectExists(tokens[2])){
				return new BadInstruction();
			}
			op = "reads";
			obj = tokens[2].toLowerCase();
			
			return this;
		}
		else if (tokens[0].equalsIgnoreCase("WRITE") && tokens.length == 4){
			
			if(! SecureSystem.getSys().rm.om.objectExists(tokens[2])){
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
		
			if( SecureSystem.getSys().rm.om.objectExists(tokens[2])){		//object musn't exist
				return new BadInstruction();
			}
			op = "creates";
			obj = tokens[2].toLowerCase();
			
			return this;
		}
		else if (tokens[0].equalsIgnoreCase("DESTROY") && tokens.length == 3){
			
			if(! SecureSystem.getSys().rm.om.objectExists(tokens[2])){
				return new BadInstruction();
			}
			op = "destorys";
			obj = tokens[2].toLowerCase();
			return this;
		}
		else if (tokens[0].equalsIgnoreCase("RUN") && tokens.length == 2){
			
			sub = "runs";
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

class CovertChannel{
	CovertChannel(){
		
	}
	
	boolean execute(boolean bit){
		if (! bit) {
	    	 ReferenceMonitor.runInstruction(new InstructionObject("RUN HAL").isValid());
	    	 ReferenceMonitor.runInstruction(new InstructionObject("CREATE HAL OBJ").isValid());
	    	 ReferenceMonitor.runInstruction(new InstructionObject("CREATE LYLE OBJ").isValid());
	    	 ReferenceMonitor.runInstruction(new InstructionObject("WRITE LYLE OBJ 1").isValid());
	    	 ReferenceMonitor.runInstruction(new InstructionObject("READ LYLE OBJ").isValid());
	    	 ReferenceMonitor.runInstruction(new InstructionObject("DESTROY LYLE OBJ").isValid());
	    	 ReferenceMonitor.runInstruction(new InstructionObject("RUN LYLE").isValid());
		} 
		else {
	    	 ReferenceMonitor.runInstruction(new InstructionObject("CREATE LYLE OBJ").isValid());
	    	 ReferenceMonitor.runInstruction(new InstructionObject("WRITE LYLE OBJ 1").isValid());
	    	 ReferenceMonitor.runInstruction(new InstructionObject("READ LYLE OBJ").isValid());
	    	 ReferenceMonitor.runInstruction(new InstructionObject("DESTROY LYLE OBJ").isValid());
	    	 ReferenceMonitor.runInstruction(new InstructionObject("RUN LYLE").isValid());
		}
		return false;
	}
}

/*
* Reader is a class that is responsible of reading the instructionlist file
*/
class Reader {
	static boolean v = false;
	/*	read method will read the file	*/
	static void read (String arg0, String arg1) throws FileNotFoundException{
		
		File input = null;
		if(arg0.equals("v")){
			input = new File(arg1);
			v = true;
		}
		else{
			input = new File(arg1);
			v = false;
		}
		
		if(!input.exists() || input.isDirectory()) { 
			throw new FileNotFoundException("File not found at path: " + input.getPath());
		}
		
	    BufferedReader br = null;

	    try {
	    	String line;
		     br = new BufferedReader(new FileReader(input));
		     while ((line = br.readLine()) != null) {
		    	 ReferenceMonitor.runInstruction(new InstructionObject(line).isValid());
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
}
