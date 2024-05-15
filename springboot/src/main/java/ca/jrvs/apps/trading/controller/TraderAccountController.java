package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.entity.Trader;
import ca.jrvs.apps.trading.model.TraderAccountView;
import ca.jrvs.apps.trading.service.TraderAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Api(value = "Trader")
@Controller
@RequestMapping("/trader")
public class TraderAccountController {
    private final TraderAccountService traderAccountService;
    private static Logger logger = LoggerFactory.getLogger(QuoteController.class);

    @Autowired
    public TraderAccountController(TraderAccountService traderAccountService) {
        this.traderAccountService = traderAccountService;
    }

    @ApiOperation(value = "Create a trader and account instance using DTO", notes = "TraderId and AccountId are auto-generated, and are identical. Assume each trader has exact one account.")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(path="/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TraderAccountView createTrader(@RequestBody Trader defaultTrader) {
        try {
            return traderAccountService.createTraderAndAccount(defaultTrader);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @ApiOperation(value = "Create a trader and account instance", notes = "TraderId and AccountId are auto-generated, and are identical. Assume each trader has exact one account.")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(path="/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TraderAccountView createTrader(@PathVariable String firstname, @PathVariable String lastname,
                                          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dob, @PathVariable String country, @PathVariable String email) {
        // convert localdate to date
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date date = Date.from(dob.atStartOfDay(defaultZoneId).toInstant());

        try {
            Trader createTrader = new Trader();
            createTrader.setFirst_name(firstname);
            createTrader.setFirst_name(lastname);
            createTrader.setDob(date);
            createTrader.setEmail(email);
            return traderAccountService.createTraderAndAccount(createTrader);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    @ApiOperation(value = "Delete a trader", notes = "Trader, along with its associated Account and SecurityOrders info will be deleted IFF the account amount is 0 and there's no open positions")
    @DeleteMapping(path="/traderId/{traderId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTrader(@PathVariable Integer traderId) {
        try {
            traderAccountService.deleteTraderById(traderId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
