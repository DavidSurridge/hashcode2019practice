package model;

public class Person {

  private int identifier;
  private int groupIdentifier;
  private boolean preference;
  private int[] seat;

  public Person(String input) {
    if (input.contains("W")) {
      preference = true;
      identifier = Integer.valueOf(input.substring(0,input.indexOf("W")));
    } else {
      preference = false;
      identifier = Integer.valueOf(input);
    }
  }

  public int getIdentifier() {
    
    return identifier;
  }

  public void setIdentifier(int identifier) {
    this.identifier = identifier;
  }

  public boolean isPreference() {
    return preference;
  }

  public void setPreference(boolean preference) {
    this.preference = preference;
  }

  public int[] getSeat() {
    return seat;
  }

  public void setSeat(int[] seat) {
    this.seat = seat;
  }

  public int getGroupIdentifier() {
   
    return groupIdentifier;
  }

  public void setGroupIdentifier(int groupIdentifier) {
    this.groupIdentifier = groupIdentifier;
  }

}
