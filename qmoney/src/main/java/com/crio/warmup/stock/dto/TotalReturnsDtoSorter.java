package com.crio.warmup.stock.dto;

import java.util.ArrayList;
import java.util.Collections;

public class TotalReturnsDtoSorter {
  private ArrayList<TotalReturnsDto> arrayList = new ArrayList<>();

  public TotalReturnsDtoSorter(ArrayList<TotalReturnsDto> array) {
    this.arrayList = array;
  }

  public ArrayList<TotalReturnsDto> getSortedTotalReturnsDtoBySymbol() {
    Collections.sort(this.arrayList);
    return this.arrayList;
  }
}