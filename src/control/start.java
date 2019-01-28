package control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.Person;

public class start {

  static int windowRowCounter;
  static boolean windowSeatsAvailable;
  static InputValues input;

  // Airplame info
  static int seats;
  static int rows;
  static int totalSeats;
  static int totalWindowSeats;
  static int seatsCounter;
  // SeatsLayout represents an output solution
  static int[][] airplaneSeatsLayout;

  // Passenger and Group info
  static List<List<Person>> passengerGroups;
  static int numberOfGroups;
  static int sizeOfSmallestGroup;

  // Helper object to keep track of groups and passengers. To be used later for
  // tracking and extracting groups that have been added when trying to alter existing output solutions.
  static Map<Integer, List<Person>> assignedGroupsAndPassgengers;

  public static void main(String[] args) {
    // build 10 random solutions and keep highest scoring
    double maxScore = 0;
    int[][] maxSolution = null;
    for (int i = 0; i < 11; i++) {
      initaliseValues();
      initialSolution();
      double tempScore = score(input, airplaneSeatsLayout);
      if (tempScore > maxScore) {
        maxScore=tempScore;
        maxSolution = airplaneSeatsLayout;
      }

    }
    System.out.println("The Max score was: " + maxScore);
    System.out.println(Arrays.deepToString(maxSolution));

  }

  public static void initialSolution() {
    while (seatsCounter >= sizeOfSmallestGroup && assignedGroupsAndPassgengers.size() != passengerGroups.size()) {

      Random rn = new Random();
      // Random search for groups not yet added.
      int randomGroupIdentifier = rn.nextInt(numberOfGroups);
      while (assignedGroupsAndPassgengers.containsKey(randomGroupIdentifier)) {
        randomGroupIdentifier = rn.nextInt(numberOfGroups);
      }

      List<Person> passengerGroup = new ArrayList<>();

      // if window seats are available then check for passengers with preference and
      // assign
      if (windowSeatsAvailable) {
        iterateGroupLookingForWindowPreferenceIfWindowSeatsAvailable(randomGroupIdentifier, passengerGroup);
      }
      // used if window seats are no longer available or person has no preference. It
      // can lead to passengers with no preference in window seats.
      iterateGroupforNormalOrIfNoWindowSeatsAvailable(rn, randomGroupIdentifier, passengerGroup);

      assignedGroupsAndPassgengers.put(randomGroupIdentifier, passengerGroup);

    }
    System.out.println(Arrays.deepToString(airplaneSeatsLayout));


  }

  public static void iterateGroupLookingForWindowPreferenceIfWindowSeatsAvailable(int randomGroupIdentifier,
      List<Person> passengerGroup) {
    for (Person person : passengerGroups.get(randomGroupIdentifier)) {
      // person instance of Group Identifier not used yet but could be used later
      person.setGroupIdentifier(randomGroupIdentifier);

      if (person.isPreference() && windowSeatsAvailable) {

        while ((windowRowCounter != rows - 1) && (airplaneSeatsLayout[windowRowCounter][0] != 0)
            && (airplaneSeatsLayout[windowRowCounter][seats - 1] != 0)) {
          windowRowCounter++;
        }

        if (airplaneSeatsLayout[windowRowCounter][0] == 0) {
          airplaneSeatsLayout[windowRowCounter][0] = person.getIdentifier();
          int[] assignedSeating = { windowRowCounter, 0 };

          // used later to make sure this person can't be added again in subsequent
          // method.
          person.setSeat(assignedSeating);

        } else if (airplaneSeatsLayout[windowRowCounter][seats - 1] == 0) {
          airplaneSeatsLayout[windowRowCounter][seats - 1] = person.getIdentifier();
          int[] assignedSeating = { windowRowCounter, seats - 1 };

          person.setSeat(assignedSeating);
        }

        totalWindowSeats--;
        seatsCounter--;
        passengerGroup.add(person);

        if (totalWindowSeats < 1) {
          windowSeatsAvailable = false;
        }
      }
    }
  }

  public static void iterateGroupforNormalOrIfNoWindowSeatsAvailable(Random rn, int randomGroupIdentifier,
      List<Person> passengerGroup) {
    for (Person person : passengerGroups.get(randomGroupIdentifier)) {

      if ((!person.isPreference() || !windowSeatsAvailable) && (seatsCounter >= sizeOfSmallestGroup)
          && person.getSeat() == null) {
        int randomRow = rn.nextInt(rows);
        int randomSeat = rn.nextInt(seats);

        while (airplaneSeatsLayout[randomRow][randomSeat] != 0) {
          randomRow = rn.nextInt(rows);
          randomSeat = rn.nextInt(seats);
        }

        int[] assignedSeating = { randomRow, randomSeat };
        person.setSeat(assignedSeating);

        airplaneSeatsLayout[randomRow][randomSeat] = person.getIdentifier();
        seatsCounter = seatsCounter - 1;
        if (randomSeat == 0 || randomSeat == seats - 1) {
          totalWindowSeats--;
          if (totalWindowSeats < 1) {
            windowSeatsAvailable = false;
          }
        }
        passengerGroup.add(person);

      }
    }
  }

  public static double score(InputValues input, int[][] airplaneSeatsLayout) {

    int totalPassengers = 0;
    int totalPreferenceMet = 0;

    for (int[] row : airplaneSeatsLayout) {

      for (int i = 0; i < row.length; i++) {

        int person = row[i];
        int rowSize = row.length;
        int rownumber = airplaneSeatsLayout.length;

        if (person != 0) {
          Boolean preference = input.getAllPassengers().get(person - 1);
          totalPassengers++;
          if (!preference)

            totalPreferenceMet++;
          if (preference && (i == 0 || i == row.length - 1)) {

            totalPreferenceMet++;
          }

        }
      }
    }
    double percentage = Double.valueOf(totalPreferenceMet) / Double.valueOf(totalPassengers);
    System.out.println(totalPreferenceMet + " " + totalPassengers + " " + percentage);
    return percentage;
  }

  private static void initaliseValues() {
    input = new InputValues();
    windowRowCounter = 0;

    windowSeatsAvailable = true;
    input = new InputValues();

    // Airplame info
    seats = input.getColumns();
    rows = input.getRows();
    totalSeats = seats * rows;
    totalWindowSeats = 2 * rows;
    seatsCounter = totalSeats;
    // SeatsLayout represents an output solution
    airplaneSeatsLayout = new int[rows][seats];

    // Passenger and Group info
    passengerGroups = input.getPassengers();
    numberOfGroups = passengerGroups.size();
    sizeOfSmallestGroup = Collections.min(passengerGroups, InputValues.getCMP()).size();

    // Helper object to keep track of groups and passengers. To be used later for
    // tracking and extracting groups that have been added.
    assignedGroupsAndPassgengers = new HashMap<>();

  }

}
