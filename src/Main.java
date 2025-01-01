import java.io.*;
import java.util.Scanner;
import java.util.Random;
import java.text.DecimalFormat;

class BankAccount
{

        private static String Name = "";
        private static int pin;
        private static float balance;

    public static final DecimalFormat df = new DecimalFormat( "$###,###.00" );
    private static final Scanner sc = new Scanner(System.in);
    private static final Random ra = new Random();



    public static void main(String[] args)
    {

        System.out.println("Welcome to Blu Bank!\n");

        menu(sc);
    }



    private static void menu(Scanner sc)
    {

        System.out.println("Choose an action");
        System.out.println("1. Create Account");
        System.out.println("2. Login");
        System.out.println("3. Quit");
        int action = sc.nextInt();

        switch (action)
            {
            case 1:
                createAccount(sc, new Random());
                break;
            case 2:
                login(sc);
                break;
            case 3:
                systemProperties();
                break;
            default: System.out.println("Invalid Action");
            }
    }



    private static void createAccount(Scanner sc, Random ra)
    {

        System.out.println("Let's create a new account!\n");

        System.out.print("Enter your first name: ");
        Name = sc.next();

        System.out.println("\nHello "  + Name);

        //pin
        System.out.println("\nPlease make a numerical pin (4+ digits) to keep your account safe!\n");
        System.out.println("If you would like a randomly generated pin, enter '0'\n");
        pin = sc.nextInt();

        if (pin == 0)
            {
            pin = ra.nextInt(9999)+1000;
            System.out.println("Your randomly generated pin is: " + pin);
            }

        while (pin < 1000)
            {
            System.out.println("Please enter at least a four digit pin.");
            System.out.print("New pin: ");
            pin = sc.nextInt();
            if (pin == 0)
                {
                pin = ra.nextInt(9999)+1000;
                System.out.println("Your randomly generated pin is: " + pin);
                }
            }

        System.out.println("\n__________");
        System.out.println("Account successfully created " + Name + " with a pin of " + pin);
        System.out.println("Login to access your new account!\n\n\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("account.txt", true))) {
        writer.write(Name + "," + pin + "," + balance);
        writer.newLine();
        System.out.println("Account details saved to account.txt");
        } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Sending back to menu...");
        menu(sc);
    }



    private static void login (Scanner sc)
    {

        if (Name.isEmpty())
            {
            System.out.println("Create an account first... sending back to menu\n\n");
            menu(sc);
            }

        String nameInput = "GibberishlyGibberishing";
        while (!(nameInput.equals(Name)))
            {
            System.out.print("Enter your name: ");
            nameInput = sc.next();
            if (nameInput.equals(Name))
                System.out.println("Correct name!\n");
            else
                System.out.println("Incorrect name, please try again!\n");
            }

        int pinInput;

            do
        {
        System.out.print("Enter your pin: ");
        pinInput = sc.nextInt();
        if (pinInput == pin)
            System.out.println("\nLogged in!\n\n");
        else
            System.out.println("\nIncorrect pin, please try again!\n");
        }
            while (pinInput != pin);

            accMenu(sc);
    }





    private static void accMenu(Scanner sc)
    {
        System.out.println("\n\nChoose an action\n");
        System.out.println("1. View Balance");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Change Pin");
        System.out.println("5. Log out");
        System.out.println("6. Quit");

        System.out.println("Enter your choice: ");
        int action = sc.nextInt();

        System.out.println();

        switch (action)
            {
            case 1:
                viewBalance(sc, df);
                break;

            case 2:
                deposit((sc));
                break;

            case 3:
                withdraw((sc));
                break;

            case 4:
                changePin(sc, ra);
                break;

            case 5:
                logout();
                break;

            case 6:
                systemProperties();
                break;

            default: System.out.println("\nInvalid Input\n");
            }


    }



        private static boolean firstLogin = true;

    private static void viewBalance (Scanner sc, DecimalFormat df)
    {

        if (firstLogin)
            {
            balance +=5;
            System.out.println("We have added $5.00 into your account for being a new account holder!");
            firstLogin = false;
            }

        System.out.println("Your current balance is " + df.format(balance));
        accMenu(sc);
    }



    private static void deposit (Scanner sc)
    {

        System.out.println("How much would you like to deposit from your wallet?");
        float depositInput = sc.nextFloat();

        if (depositInput > 0)
            {
            balance += depositInput;
            viewBalance(sc, df);
            }
        else if (depositInput < 0)
            {
            System.out.println("Error: deposits must be greater than 0.");
            deposit(sc);
            }
        else
            {
            System.out.println("Exiting deposit page to menu...");
            }

        accMenu(sc);
    }



    private static void withdraw (Scanner sc)
    {

        System.out.println("How much would you like to withdraw from your account?");
        float withdrawInput = sc.nextFloat();

        if (withdrawInput > 0)
            {
            balance -= withdrawInput;

            //Debt Checker
            if (balance < 0)
                {
                System.out.println("Warning, proceeding will bring you into a debt of " + df.format(balance));
                System.out.println("Do you wish to continue?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                int choice = sc.nextInt();

                if (choice == 2)
                    {
                    balance += withdrawInput;
                    System.out.println("Withdrawal Cancelled.");
                    }
                }

            viewBalance(sc, df);
            }
        else if (withdrawInput < 0)
            {
            System.out.println("Error: withdrawals must be greater than $0.00 .");
            deposit(sc);
            }
        else
            {
            System.out.println("Exiting deposit page to menu...");
            }

        accMenu(sc);
    }



        private static int pinAttempts = 0;

    private static void changePin (Scanner sc, Random ra)
    {

        System.out.println("Enter your current pin:");
        int oldPin = sc.nextInt();

        if (pinAttempts >=5)
            {
            System.out.println("Too many attempts, try again later. Sending back to menu...");
            pinAttempts = 0;
            menu(sc);
            }

        if(oldPin != pin)
            {
            System.out.println("Incorrect.");
            changePin(sc, ra);
            pinAttempts++;
            }

        System.out.println("If you would like a randomly generated pin, enter '0'\n");
        System.out.println("\nEnter your new pin:\n");
        pin = sc.nextInt();

        if (pin == 0)
            {
            pin = ra.nextInt(9999)+1000;
            System.out.println("Your randomly generated pin is: " + pin);
            }

        while (pin < 1000)
            {
            System.out.println("Please enter at least a four digit pin.");
            System.out.print("New pin: ");
            pin = sc.nextInt();
            if (pin == 0)
                {
                pin = ra.nextInt(9999)+1000;
                System.out.println("Your randomly generated pin is: " + pin);
                }
            }
        System.out.println("New account pin for " + Name + " is " + pin + "\n\n");
        accMenu(sc);
    }



    private static void logout ()
    {

        System.out.println("Logging out " + Name + "...");
        System.out.println("Goodbye!");
        menu((sc));
    }



    private static void systemProperties ()
    {

        System.out.println("\n\n\n");
        System.out.println("Java Version: " + System.getProperty("java.version") + " by " + System.getProperty("java.vendor"));
        System.out.println("User: " + System.getProperty("user.name"));  //<-- TURNED OFF FOR PRIVACY IN THE VIDEO
        System.out.print("Operating System: " + System.getProperty("os.name"));
        System.out.println(" " + System.getProperty("os.version"));
        System.out.println("Architecture: " + System.getProperty("os.arch"));
        System.out.println ("By: Blu");
        System.out.println ("Coded for Hack Club High Seas on December 30th 2024");
        System.exit(0);
    }

}