package com.example.android.tva11ydemo.model;

/*
 * Movie class provides the simplest media data input resource.
 */
public class Movie {

  private long id;
  private String title;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return "Movie{" +
        "id=" + id +
        ", title='" + title + '\'' +
        '}';
  }
}
