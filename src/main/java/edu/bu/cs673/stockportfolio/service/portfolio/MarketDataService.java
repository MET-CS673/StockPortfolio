package edu.bu.cs673.stockportfolio.service.portfolio;

/**********************************************************************************************************************
 * The MarketDataService defines a template method for making API requests to IEX Cloud endpoints. The template method
 * controls the order or execution, but allows subclasses to override which endpoints are targeted.
 *********************************************************************************************************************/
public interface MarketDataService {

    /**
     * A Template Method defining the sequence of IEX Cloud API requests.
     */
    default void processIexCloudRequests() {
        doGetQuotes();
        doGetSector();
    }

    void doGetQuotes();

    void doGetSector();
}
