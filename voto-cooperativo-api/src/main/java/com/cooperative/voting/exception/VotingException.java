package com.cooperative.voting.exception;

public class VotingException extends RuntimeException {

  private final int statusCode;

  public VotingException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public VotingException(String message, int statusCode, Throwable cause) {
    super(message, cause);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public int getStatus() {
    return getStatusCode();
  }
}
