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

        onDate = port.getLatestDateTime();
        GetCursOnDateXMLResult result = port.getCursOnDateXML(onDate);
        Valute list = null;

        try {

           list = GetCursOnDateResultParser.getValuteByValuteCh("USD", result);

       } catch (Exception e){

        }
       System.out.println(list.curs);

      /*  try {
                list = GetCursOnDateResultParser.getValuteByValuteCode("840", result);
            } catch (Exception e) {

            }
            System.out.println(list.curs);*/
        }
    }


