
package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.crio.warmup.stock.quotes.StockQuoteServiceFactory;
import com.crio.warmup.stock.quotes.StockQuotesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {

  private StockQuotesService stockQuotesService;
  // Caution: Do not delete or modify the constructor, or else your build will
  // break!
  // This is absolutely necessary for backward compatibility

  // Caution: Do not delete or modify the constructor, or else your build will
  // break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
  }

  protected PortfolioManagerImpl(StockQuotesService stockQuotesService) {
    this.stockQuotesService = stockQuotesService;
  }

  // TODO: CRIO_TASK_MODULE_REFACTOR
  // Now we want to convert our code into a module, so we will not call it from
  // main anymore.
  // Copy your code from Module#3
  // PortfolioManagerApplication#calculateAnnualizedReturn
  // into #calculateAnnualizedReturn function here and make sure that it
  // follows the method signature.
  // Logic to read Json file and convert them into Objects will not be required
  // further as our
  // clients will take care of it, going forward.
  // Test your code using Junits provided.
  // Make sure that all of the tests inside PortfolioManagerTest using command
  // below -
  // ./gradlew test --tests PortfolioManagerTest
  // This will guard you against any regressions.
  // run ./gradlew build in order to test yout code, and make sure that
  // the tests and static code quality pass.

  // CHECKSTYLE:OFF

  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades, 
      LocalDate endDate) throws StockQuoteServiceException {
    List<AnnualizedReturn> returns = new ArrayList<>();
    for (PortfolioTrade t : portfolioTrades) {
      long days = java.time.temporal.ChronoUnit.DAYS.between(t.getPurchaseDate(), endDate);
      double years = (double) days / 365;
      List<Candle> candle = getStockQuote(t.getSymbol(), t.getPurchaseDate(), endDate);
      if (candle == null) {
        return null;
      }
      double buyPrice = candle.get(0).getOpen();
      double sellPrice = candle.get(candle.size() - 1).getClose();
      // int j = 1;
      // while (j <= candle.size()) {
      //   if (candle.get(candle.size() - j).getClose() != null) {
      //     sellPrice = candle.get(candle.size() - j).getClose();
      //     break;
      //   } else {
      //     j++;
      //   }
      // }
      double totalreturn = (sellPrice - buyPrice) / buyPrice;
      double annualizedReturns = Math.pow((1 + totalreturn), (double) (1 / years)) - 1;
      returns.add(new AnnualizedReturn(t.getSymbol(), annualizedReturns, totalreturn));
      Collections.sort(returns, getComparator());
    }
    return returns;
  }

  public Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  // CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  // Extract the logic to call Tiingo thirdparty APIs to a separate function.
  // It should be split into fto parts.
  // Part#1 - Prepare the Url to call Tiingo based on a template constant,
  // by replacing the placeholders.
  // Constant should look like
  // https://api.tiingo.com/tiingo/daily/<ticker>/prices?startDate=?&endDate=?&token=?
  // Where ? are replaced with something similar to <ticker> and then actual url
  // produced by
  // replacing the placeholders with actual parameters.





  
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws StockQuoteServiceException {
    return stockQuotesService.getStockQuote(symbol, from, to);
  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    String uriTemplate = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices?" + "startDate="
        + startDate.toString() + "&endDate=" + endDate.toString() 
        + "&token=ba4f9b6f7ae38f0aef613a47ec5f218ba5206b8b";
    return uriTemplate;
  }

  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturnParallel(List<PortfolioTrade> portfolioTrades,
      LocalDate endDate, int numThreads) throws InterruptedException, StockQuoteServiceException {
    try {
      ExecutorService service = Executors.newFixedThreadPool(numThreads);
      List<AnnualizedReturn> returns = new ArrayList<>();
      List<Callable<AnnualizedReturn>> callableList = new ArrayList<>();
      for(PortfolioTrade t : portfolioTrades) {
        Callable callable = () -> {
          long days = java.time.temporal.ChronoUnit.DAYS.between(t.getPurchaseDate(), endDate);
          double years = (double) days / 365;
          List<Candle> candle = getStockQuote(t.getSymbol(), t.getPurchaseDate(), endDate);
          if (candle == null) {
            return null;
          }
          double buyPrice = candle.get(0).getOpen();
          double sellPrice = candle.get(candle.size() - 1).getClose();
          // int j = 1;
          // while (j <= candle.size()) {
          //   if (candle.get(candle.size() - j).getClose() != null) {
          //     sellPrice = candle.get(candle.size() - j).getClose();
          //     break;
          //   } else {
          //     j++;
          //   }
          // }
          double totalreturn = (sellPrice - buyPrice) / buyPrice;
          double annualizedReturns = Math.pow((1 + totalreturn), (double) (1 / years)) - 1;
          return new AnnualizedReturn(t.getSymbol(), annualizedReturns, totalreturn);
        };
        callableList.add(callable);
      }
      List<Future<AnnualizedReturn>> future = service.invokeAll(callableList);
      for (Future<AnnualizedReturn> f : future) {
        try {
          returns.add(f.get());
        } catch (ExecutionException e) {
        // TODO Auto-generated catch block
          throw new StockQuoteServiceException("Exception", e);
        }
      }
      service.shutdown();
      Collections.sort(returns, getComparator());
    return returns;
    } catch (Exception e){
      throw new StockQuoteServiceException("Exception occured", e);
    }
  }


  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  // Modify the function #getStockQuote and start delegating to calls to
  // stockQuoteService provided via newly added constructor of the class.
  // You also have a liberty to completely get rid of that function itself,
  // however, make sure
  // that you do not delete the #getStockQuote function.

}
