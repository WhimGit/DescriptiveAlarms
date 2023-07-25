import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;

public class Main {
  public static void main(String[] args) throws Exception {
    Scanner in = new Scanner(System.in);
    FileReader fr = new FileReader("alarms.txt");
		Scanner fileScanner = new Scanner(fr);
    ArrayList<Alarm> alarms = new ArrayList<>();
    readInAlarms(fileScanner, alarms);
    fileScanner.close();
    writeToFile(alarms);
    
    Menu(alarms, in); //Recursive menu system the whole program ping pongs through
    
    in.close();
  }

  static class Alarm{
    String description;
    LocalDateTime deadline;
    
    Alarm(String description, LocalDateTime deadline){
      this.description = description;
      this.deadline = deadline;
    }
  }
  
  static void Menu(ArrayList<Alarm> alarms, Scanner in) throws Exception {
    System.out.print("\033[H\033[2J"); //clearscreen
    showAlarms(alarms);
    System.out.print("\n\n");
    System.out.println("Press 1 to add an alarm");
    System.out.println("Press 2 to delete an alarm");
    System.out.println("Press 3 to save changes");
    System.out.println("Press 4 to quit.");
    int choice = in.nextInt();
    in.nextLine();
    
    switch(choice){
      case 1:
        addAlarm(alarms, in);
        Menu(alarms, in);
        break;
    
      case 2:
        deleteAlarm(alarms, in);
        Menu(alarms, in);
        break;
    
      case 3:
        writeToFile(alarms);
        Menu(alarms, in);
        break;
    
      case 4:
        return;
    
      default:
        System.out.println("That is not a valid choice, returning to menu.");
        System.out.println("Press enter to continue");
        in.nextLine();
        Menu(alarms, in);
    }
  }

  static void showAlarms(ArrayList<Alarm> alarms){
    System.out.println("Alarms:\n\n");
    for (Alarm a : alarms){
      System.out.println("Description: " + a.description);
      LocalDateTime tempDeadl = a.deadline;
      LocalDateTime now = LocalDateTime.now();
      long days = DAYS.between(now, tempDeadl);
      tempDeadl = tempDeadl.minusDays(days);
      long hours = HOURS.between(now, tempDeadl);
      tempDeadl = tempDeadl.minusHours(hours);
      long mins = MINUTES.between(now, tempDeadl);
      System.out.println("Deadline: " + days + "d " + hours + "h " + mins + "m" + "\n");
    }
  }

  static void addAlarm(ArrayList<Alarm> alarms, Scanner in){
    System.out.println("\nCreating new alarm:");                      //Add alarm prompt+input block
    System.out.println("Enter a descriptive title for the alarm:");
    String tempDesc = in.nextLine();
    System.out.println("Enter how many days until alarm deadline:");
    int days = in.nextInt();
    in.nextLine();
    System.out.println("Enter how many hours until alarm deadline:");
    int hours = in.nextInt();
    in.nextLine();
    System.out.println("Enter how many minutes until alarm deadline:");
    int mins = in.nextInt();
    in.nextLine();

    LocalDateTime currTime = LocalDateTime.now();                    //create deadline block
    LocalDateTime tempDeadl = currTime.plusDays(days);
    tempDeadl = tempDeadl.plusHours(hours);
    tempDeadl = tempDeadl.plusMinutes(mins);
    Alarm tempAlarm = new Alarm(tempDesc, tempDeadl);
    
    alarms.add(tempAlarm);
  }

  static void deleteAlarm(ArrayList<Alarm> alarms, Scanner in){
    System.out.println("Select an alarm to delete [1]-[" + alarms.size() + "]");
    int deletable = in.nextInt()-1;
    in.nextLine();
    if(0 <= deletable && deletable < alarms.size()){
      alarms.remove(deletable);
    }
    else
    {
      System.out.println("Invalid choice, returning to menu (press enter)");
      in.nextLine();
    }
  }

  static ArrayList<Alarm> readInAlarms(Scanner fileScanner, ArrayList<Alarm> alarms){
    /*     //test/errorchecks kept as comments
    Alarm testAlarm = new Alarm("beep", LocalDateTime.now());
    alarms.add(testAlarm);
    testAlarm = new Alarm("boop", LocalDateTime.now().plusDays(1));
    alarms.add(testAlarm);
    */
    while (fileScanner.hasNext()){
      fileScanner.nextInt();
      String tempDesc = fileScanner.nextLine().substring(1);
      LocalDateTime tempDeadl = LocalDateTime.parse(fileScanner.nextLine());
      Alarm tempAlarm = new Alarm(tempDesc, tempDeadl);
      alarms.add(tempAlarm);
    }
    //showAlarms(alarms); 
    return alarms;
  }
  
  public static void writeToFile(ArrayList<Alarm> alarms) throws Exception {
    PrintWriter output = new PrintWriter("alarms.txt");
    int i = 1;
    for (Alarm a : alarms){
      output.write(i + " " + a.description + "\n" + a.deadline + "\n");
      i++;
    }
    output.close();
  }
}