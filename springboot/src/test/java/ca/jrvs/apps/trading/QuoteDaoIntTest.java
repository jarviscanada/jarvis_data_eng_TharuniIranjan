package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteDaoIntTest {
    @Autowired
    private QuoteDao quoteDao;
    private Quote savedQuote;

    @Before
    public void insertOne() {
        savedQuote.setAskPrice(10d);
        savedQuote.setAskSize(10);
        savedQuote.setBidPrice(10.2d);
        savedQuote.setBidSize(10);
        savedQuote.setLastPrice(10.1d);
        savedQuote.setId("AAPL");
        quoteDao.save(savedQuote);
    }

    @After
    public void deleteOne() {
        quoteDao.deleteById(savedQuote.getId());
    }


}
