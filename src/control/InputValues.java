package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.Person;

public class InputValues {

  private static int rows;
  private static int columns;

  private static List<List<Person>> passengers;
  private static List<Boolean> allPassengers;

  
  public InputValues() {

    passengers = new ArrayList<>();
    allPassengers = new ArrayList<>();
    File file = new File("input.txt");


    try (FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);) {

      // parse first line of the file for airplane dimensions
      String line = bufferedReader.readLine();
      String[] myData = line.split(" ");
      rows = Integer.parseInt(myData[0]);
      columns = Integer.parseInt(myData[1]);

      // parse every remaining line for passenger information
      line = bufferedReader.readLine();
      //make a method that can be used to generate random number of random sized groups with random distribution of prefernces. 
      while (line != null) {

        List<Person> passengerGroup = new ArrayList<>();
        myData = line.split(" ");

        for (String person : myData) {

          Person newPerson = new Person(person);
          passengerGroup.add(newPerson);
          allPassengers.add(newPerson.isPreference());

        }

        passengers.add(passengerGroup);
        line = bufferedReader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }

  public List<List<Person>> getPassengers() {
    return passengers;
  }

  public List<Boolean> getAllPassengers() {
    return allPassengers;
  }

  public void setAllPassengers(List<Boolean> allPassengers) {
    InputValues.allPassengers = allPassengers;
  }
  
  static Comparator<List<Person>> getCMP() {

    Comparator<List<Person>> cmp = new Comparator<List<Person>>() {
      public int compare(List<Person> o1, List<Person> o2) {
        if (o1 == null & o2 == null) {
          return 0;
        }
        if (o1 != null & o2 == null) {
          return -1;
        }
        if (o1 == null & o2 != null) {
          return 1;
        }
        return Integer.valueOf(o1.size()).compareTo(Integer.valueOf(o2.size()));
      }
    };
    return cmp;
  }
}
