import java.io.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.Scanner;

//This class acts as an object for the TuringMachineProject
//Its main purpose, buildTuring, is to read a text file
//and build a TM with proper directions for any character
//at each state. It also details an accepting state and number
//of states. This class then is able to fully process the string
//while printing the output and when it halts.
public class Turing {
    //Declare Variables
    ArrayList<ArrayList<Character>> directions = new ArrayList<ArrayList<Character>>();
    ArrayList<ArrayList<Integer>> states = new ArrayList<ArrayList<Integer>>();
    String tape = "";
    int haltingState = 0;
    int numOfStates = 0;
    int numOfDirections = 0;
    int currentState = 0;
    int index = 0;
    boolean finished = false;
    boolean earlyHalt = false;
    boolean validLine = false;

    //Runs through the TM.txt file and assigns the information
    //inside to variables for use later.
    void buildTuring() {
        Scanner scanner;
        try {
            scanner = new Scanner(new File("TM.txt"));
        } catch (Exception e) {
            System.out.println("Invalid File");
            return;
        }

        //Assigns the number of states in the Machine
        numOfStates = scanner.nextInt();
        scanner.nextLine();

        //Assigns which state is the halting state
        haltingState = scanner.nextInt();

        //Defines the directions of each state based on current char
        //This counter helps go through each line correctly
        int item = 0;
        while ((scanner.hasNextLine())) {
            String nextLine = scanner.nextLine();
            //Index of first space in line. Helpful for state
            int spaceIndex = nextLine.indexOf(' ');

            directions.add(new ArrayList<Character>());
            states.add(new ArrayList<Integer>());
            for (int x = 0; x < nextLine.length(); x++) {
                //Adds directions
                if ((nextLine.charAt(x) != ' ') && x > spaceIndex && item < 4) {
                    directions.get(numOfDirections).add(nextLine.charAt(x));
                    validLine = true;
                    item++;
                }
                //Adds double digit state to direction
                else if (x == 0 && spaceIndex > 1) {
                    states.get(numOfDirections).add(
                            Integer.parseInt(nextLine.substring(0, spaceIndex)));
                    item++;
                }
                //Adds single digit state to direction
                else if (x == 0 && spaceIndex == 1) {
                    states.get(numOfDirections).add(
                            Character.getNumericValue(nextLine.charAt(x)));
                    item++;
                }
                //Adds next state to direction
                else if ((nextLine.charAt(x) != ' ') && item == 4) {
                    states.get(numOfDirections).add(
                            Integer.parseInt(nextLine.substring(x)));
                    item++;
                }
            }

            //Only increments numOfDirections if line was valid
            if(validLine) {
                numOfDirections++;
                item = 0;
                validLine = false;
            }
        }
    }

    //Loops through machine, printing and changing the string
    //until it halts.
    void process(String input) {
        tape = input;
        while(!finished) {
            printOutput();
            //Can either end at halting state or if there's nowhere to go
            if((currentState == haltingState) || earlyHalt) {
                finished = true;
                System.out.print("\t[HALT]");
            }
            else
                changeAndMove(tape);
        }

    }

    //Prints the tape while indicating the state and current char
    void printOutput() {
        System.out.print("\n" + currentState + ": ");
        //Adds beginning of string
        //Avoids out of bounds if already at beginning
        if(index == 0)
            System.out.print("[" + tape.charAt(index));
        else
            System.out.print(tape.substring(0, index) + "[" + tape.charAt(index));

        //Adds end of string
        //Avoids out of bounds if already at end
        if(index == (tape.length() - 1))
            System.out.print("]");
        else
            System.out.print("]" + tape.substring(index + 1));
    }

    //Properly changes current char and moves left or right according to TM
    void changeAndMove(String input) {
        boolean validCharacter  = false;
        for(int x = 0; x < numOfDirections; x++) {
            //Only changes if there is a direction for current char in current state
            if(states.get(x).get(0) == currentState &&
                    (directions.get(x).get(0) == tape.charAt(index)))
            {
                //Adds beginning of string
                //Avoids out of bounds if already at beginning
                if(index == 0)
                    tape = ((directions.get(x)).get(1)).toString();
                else {
                    tape = input.substring(0, index) +
                            ((directions.get(x)).get(1)).toString();
                }

                //Adds rest of string
                //Avoids out of bounds if already at end of string
                if(index != (input.length() - 1))
                    tape = tape + input.substring(index + 1);

                //Move left or right
                if(directions.get(x).get(2) == 'R')
                    index++;
                else if(directions.get(x).get(2) == 'L')
                    index--;

                //currentState = Character.getNumericValue((directions.get(x)).get(3));
                currentState = states.get(x).get(1);
                x = numOfDirections;
                validCharacter = true;
            }
        }

        //If there is nowhere to go with current char, halt early
        if(!validCharacter)
            earlyHalt = true;
    }
}
