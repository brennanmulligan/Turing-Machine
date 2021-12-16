import java.util.*;

/*****************************************
 *
 * Theoretical Foundations Project 2: Turing Machine *
 * Brennan Mulligan *
 * *
 * Program Description: This project simulates a Turing
 * Machine. Based on a predetermined TM with included directions
 * in a text file, the user can enter a string of binary nums to
 * be evaluated by the program. The program will accurately
 * determine if the string would halt and be processed by the TM.
 * *
 ************************************/
public class TuringMachineProject
{
    public static void main(String[] args)
    {
        //Declare Variables
        Scanner scanner = new Scanner(System.in);
        Turing TM = new Turing();
        String input;

        //Reads the text file and assigns the directions
        System.out.println(">>>Reading TM.txt…");
        TM.buildTuring();

        System.out.print(">>>Enter the starting tape with one leading and one trailing blank ( _ ): ");
        input = scanner.next();
        System.out.print("\n>>>Processing...");
        TM.process(input);
    }
}