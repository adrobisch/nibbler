package de.androbit.nibbler.http;

public class Header {
  public static Header ContentType = new Header("Content-Type");
  public static Header Accept = new Header("Accept");
  public static Header Location = new Header("Location");

  String name;

  public Header(String name) {
    this.name = name;
  }

  public String name() {
    return name;
  }

  public Header valueOf(String name) {
    return new Header(name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Header header = (Header) o;

    if (name != null ? !name.equals(header.name) : header.name != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Header{" +
      "name='" + name + '\'' +
      '}';
  }

}
