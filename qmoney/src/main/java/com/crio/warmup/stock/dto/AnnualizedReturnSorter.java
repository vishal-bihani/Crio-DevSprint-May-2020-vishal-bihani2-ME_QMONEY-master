package com.crio.warmup.stock.dto;

import java.util.ArrayList;
import java.util.Collections;

public class AnnualizedReturnSorter {
  private ArrayList<AnnualizedReturn> returns = new ArrayList<>();
  
  public AnnualizedReturnSorter(ArrayList<AnnualizedReturn> returns) {
    this.returns = returns;
  }

  public ArrayList<AnnualizedReturn> getSortedAnnualizedReturnListDesc() {
    Collections.sort(this.returns);
    return this.returns;
  }
}