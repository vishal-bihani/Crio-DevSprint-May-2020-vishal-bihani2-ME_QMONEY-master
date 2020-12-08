
package com.crio.warmup.stock.dto;

public class AnnualizedReturn implements Comparable<AnnualizedReturn> {

  private final String symbol;
  private final Double annualizedReturn;
  private final Double totalReturns;

  public AnnualizedReturn(String symbol, Double annualizedReturn, Double totalReturns) {
    this.symbol = symbol;
    this.annualizedReturn = annualizedReturn;
    this.totalReturns = totalReturns;
  }

  public String getSymbol() {
    return symbol;
  }

  public Double getAnnualizedReturn() {
    return annualizedReturn;
  }

  public Double getTotalReturns() {
    return totalReturns;
  }

  @Override
  public int compareTo(AnnualizedReturn arg0) {
    return (this.annualizedReturn < arg0.getAnnualizedReturn() ? 1 : 
      (this.annualizedReturn.equals(arg0.getAnnualizedReturn()) ? 0 : -1));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((annualizedReturn == null) ? 0 : annualizedReturn.hashCode());
    result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
    result = prime * result + ((totalReturns == null) ? 0 : totalReturns.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    AnnualizedReturn other = (AnnualizedReturn) obj;
    if (annualizedReturn == null) {
      if (other.annualizedReturn != null) {
        return false;
      }
    } else if (!annualizedReturn.equals(other.annualizedReturn)) {
      return false;
    }
    if (symbol == null) {
      if (other.symbol != null) {
        return false;
      }
    } else if (!symbol.equals(other.symbol)) {
      return false;
    }
    if (totalReturns == null) {
      if (other.totalReturns != null) {
        return false;
      }
    } else if (!totalReturns.equals(other.totalReturns)) {
      return false;
    }
    return true;
  }
}
