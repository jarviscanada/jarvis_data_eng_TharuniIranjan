package ca.jrvs.apps.trading;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.entity.Account;
import ca.jrvs.apps.trading.entity.Position;
import ca.jrvs.apps.trading.model.Quote;
import ca.jrvs.apps.trading.entity.SecurityOrder;
import ca.jrvs.apps.trading.model.MarketOrder;
import ca.jrvs.apps.trading.service.OrderService;
import ca.jrvs.apps.trading.util.TradingAppTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Captor
    ArgumentCaptor<SecurityOrder> captorSecurityOrder;

    @Mock
    private AccountDao accountDao;
    @Mock
    private SecurityOrderDao securityOrderDao;
    @Mock
    private QuoteDao quoteDao;
    @Mock
    private PositionDao positionDao;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testExecuteMarketOrder_BuyOrder_Success() throws ParseException {
        // Arrange
        MarketOrder orderData = new MarketOrder();
        orderData.setAccountId(1);
        orderData.setTicker("AAPL");
        orderData.setSize(10);

        Account account = new Account();
        account.setId(1);
        account.setAmount(1000.0);

        Quote quote = new Quote();
        quote.setId("AAPL");
        quote.setLastPrice(100.0);

        when(accountDao.findById(1)).thenReturn(Optional.of(account));
        when(quoteDao.findById("AAPL")).thenReturn(Optional.of(quote));
        when(securityOrderDao.save(any(SecurityOrder.class))).thenReturn(new SecurityOrder());

        // Act
        SecurityOrder securityOrder = orderService.executeMarketOrder(orderData);

        // Assert
        verify(securityOrderDao, times(2)).save(captorSecurityOrder.capture());
        SecurityOrder capturedOrder = captorSecurityOrder.getAllValues().get(1);  // Get the second call to save
        assertEquals("FILLED", capturedOrder.getStatus());
        assertEquals(100.0, capturedOrder.getPrice(), 0.01);
        assertEquals(10, capturedOrder.getSize());
    }

    @Test
    public void testExecuteMarketOrder_SellOrder_Success() throws ParseException {
        // Arrange
        MarketOrder orderData = new MarketOrder();
        orderData.setAccountId(1);
        orderData.setTicker("AAPL");
        orderData.setSize(-10);

        Account account = new Account();
        account.setId(1);
        account.setAmount(1000.0);

        Quote quote = new Quote();
        quote.setId("AAPL");
        quote.setLastPrice(100.0);

        Position position = new Position();
        position.setAccountId(1);
        position.setId(1);
        position.setDate_of_position(TradingAppTools.convertStringtoDate("2024-05-17"));

        when(accountDao.findById(1)).thenReturn(Optional.of(account));
        when(quoteDao.findById("AAPL")).thenReturn(Optional.of(quote));
        when(positionDao.findByAccountIdAndTicker(1, "AAPL")).thenReturn(Optional.of(position));
        when(securityOrderDao.save(any(SecurityOrder.class))).thenReturn(new SecurityOrder());

        // Act
        SecurityOrder securityOrder = orderService.executeMarketOrder(orderData);

        // Assert
        verify(securityOrderDao, times(2)).save(captorSecurityOrder.capture());
        SecurityOrder capturedOrder = captorSecurityOrder.getAllValues().get(1);  // Get the second call to save
        assertEquals("FILLED", capturedOrder.getStatus());
        assertEquals(100.0, capturedOrder.getPrice(), 0.01);
        assertEquals(-10, capturedOrder.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteMarketOrder_InvalidTicker() throws ParseException {
        // Arrange
        MarketOrder orderData = new MarketOrder();
        orderData.setAccountId(1);
        orderData.setTicker("INVALID");
        orderData.setSize(10);

        when(quoteDao.findById("INVALID")).thenReturn(Optional.empty());

        // Act
        orderService.executeMarketOrder(orderData);

        // Assert - Expect exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteMarketOrder_InsufficientBalance() throws ParseException {
        // Arrange
        MarketOrder orderData = new MarketOrder();
        orderData.setAccountId(1);
        orderData.setTicker("AAPL");
        orderData.setSize(10);

        Account account = new Account();
        account.setId(1);
        account.setAmount(50.0); // Not enough balance for the order

        Quote quote = new Quote();
        quote.setId("AAPL");
        quote.setLastPrice(100.0);

        when(accountDao.findById(1)).thenReturn(Optional.of(account));
        when(quoteDao.findById("AAPL")).thenReturn(Optional.of(quote));

        // Act
        orderService.executeMarketOrder(orderData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteMarketOrder_InsufficientPosition() throws ParseException {
        // Arrange
        MarketOrder orderData = new MarketOrder();
        orderData.setAccountId(1);
        orderData.setTicker("AAPL");
        orderData.setSize(-20); // Trying to sell more than the position

        Account account = new Account();
        account.setId(1);
        account.setAmount(1000.0);

        Quote quote = new Quote();
        quote.setId("AAPL");
        quote.setLastPrice(100.0);

        Position position = new Position();
        position.setAccountId(1);
        position.setId(1);
        position.setDate_of_position(TradingAppTools.convertStringtoDate("2024-05-17"));

        when(accountDao.findById(1)).thenReturn(Optional.of(account));
        when(quoteDao.findById("AAPL")).thenReturn(Optional.of(quote));
        when(positionDao.findByAccountIdAndTicker(1, "AAPL")).thenReturn(Optional.of(position));

        // Act
        orderService.executeMarketOrder(orderData);
    }
}
