package de.androbit.nibbler.http;

public enum Header {
  ContentType("Content-Type"),
  Accept("Accept");

  String name;

  Header(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "Header{" +
      "name='" + name + '\'' +
      '}';
  }
}
