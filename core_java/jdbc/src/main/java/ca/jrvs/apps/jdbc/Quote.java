package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Quote extends GlobalQuote {
    @JsonProperty("Global Quote")
    private GlobalQuote globalQuote;

    public GlobalQuote getGlobalQuote() { return globalQuote; }

    public void setGlobalQuote(GlobalQuote globalQuote) { this.globalQuote = globalQuote; }
}
