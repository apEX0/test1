package com.company;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import com.company.GetCursOnDateResultParser;
import com.company.GetCursOnDateResultParser.Valute;
import ru.cbr.web.*;
import ru.cbr.web.EnumValutesResponse.EnumValutesResult;
import ru.cbr.web.GetCursOnDateResponse.GetCursOnDateResult;
import ru.cbr.web.GetCursOnDate;
import ru.cbr.web.GetCursOnDateXMLResponse.GetCursOnDateXMLResult;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {


    public static void main(String[] args) {


        DailyInfo service = new DailyInfo();
        DailyInfoSoap port = service.getDailyInfoSoap();

        XMLGregorianCalendar onDate = null;
        try {
            onDate = GetCursOnDateResultParser.getXMLGregorianCalendarNow();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        EnumValutesResult en = port.enumValutes(false);
        GetCursOnDateResult curs = port.getCursOnDate(onDate);

        System.out.println(onDate);

        onDate = port.getLatestDateTime();
        GetCursOnDateXMLResult result = port.getCursOnDateXML(onDate);
        Valute list = null;

        try {

            list = GetCursOnDateResultParser.getValuteByValuteCh(result);

        } catch (Exception e) {

        }

    }
}


