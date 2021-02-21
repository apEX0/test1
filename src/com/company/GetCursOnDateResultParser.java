package com.company;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.xerces.dom.ElementNSImpl;
import org.apache.xerces.dom.TextImpl;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.cbr.web.GetCursOnDateXMLResponse.GetCursOnDateXMLResult;

public class GetCursOnDateResultParser {

    public static class Valute{
        public String name;
        public String chCode;
        public int code;
        public BigDecimal nom;
        public BigDecimal curs;

        public Valute(){

        }

        public Valute(String vname, String vchcode, int vcode, BigDecimal vnom, BigDecimal vcurs){
            this.name = vname;
            this.chCode = vchcode;
            this.code = vcode;
            this.nom = vnom;
            this.curs = vcurs;
        }
    }

    public static Valute getValuteByValuteCh(String valuteCh, GetCursOnDateXMLResult result) throws Exception{

        Valute answer = new Valute();

        List<Object> list = result.getContent();
        com.sun.org.apache.xerces.internal.dom.ElementNSImpl e = (com.sun.org.apache.xerces.internal.dom.ElementNSImpl) list.get(0);
        NodeList chCodeList =   e.getElementsByTagName("VchCode");
        int length = chCodeList.getLength();


        boolean isFound = false;
        for (int i = 0; i< length; i++){
            if (isFound) break;

            Node valuteChNode = chCodeList.item(i);
            TextImpl textimpl = (TextImpl)valuteChNode.getFirstChild();
            String chVal = textimpl.getData();

            if (chVal.equalsIgnoreCase(valuteCh)){
                isFound = true;
                Node parent = valuteChNode.getParentNode();
                NodeList nodeList = parent.getChildNodes();
                int paramLength = nodeList.getLength();

                for (int j=0; j<paramLength; j++){
                    Node currentNode = nodeList.item(j);

                    String name = currentNode.getNodeName();
                    Node currentValue = currentNode.getFirstChild();
                    String value = currentValue.getNodeValue();
                    if (name.equalsIgnoreCase("Vname")){
                        answer.name = value;
                    }
                    if (name.equalsIgnoreCase("Nominal")){
                        answer.nom = new BigDecimal(value);
                    }
                    if (name.equalsIgnoreCase("Value")){
                        answer.curs = new BigDecimal(value);
                    }
                    if (name.equalsIgnoreCase("NumCode")){
                        answer.code = Integer.parseInt(value);
                    }
                    if (name.equalsIgnoreCase("CharCode")){
                        answer.chCode = value;
                    }
                }
            }
        }

        return answer;

    }

    /*public static Valute getValuteByValuteCode(String valuteCode, GetCursOnDateXMLResult result) throws Exception{

        Valute answer = new Valute();

        List<Object> list = result.getContent();
        com.sun.org.apache.xerces.internal.dom.ElementNSImpl e = (com.sun.org.apache.xerces.internal.dom.ElementNSImpl) list.get(0);
        NodeList chCodeList =   e.getElementsByTagName("Vcode");
        int length = chCodeList.getLength();

        boolean isFound = false;
        for (int i = 0; i< length; i++){
            if (isFound) break;

            Node valuteChNode = chCodeList.item(i);
            TextImpl textimpl = (TextImpl)valuteChNode.getFirstChild();
            String chVal = textimpl.getData();

            if (chVal.equalsIgnoreCase(valuteCode)){
                isFound = true;
                Node parent = valuteChNode.getParentNode();
                NodeList nodeList = parent.getChildNodes();
                int paramLength = nodeList.getLength();

                for (int j=0; j<paramLength; j++){
                    Node currentNode = nodeList.item(j);

                    String name = currentNode.getNodeName();
                    Node currentValue = currentNode.getFirstChild();
                    String value = currentValue.getNodeValue();
                    if (name.equalsIgnoreCase("Vname")){
                        answer.name = value;
                    }
                    if (name.equalsIgnoreCase("Vnom")){
                        answer.nom = new BigDecimal(value);
                    }
                    if (name.equalsIgnoreCase("Vcurs")){
                        answer.curs = new BigDecimal(value);
                    }
                    if (name.equalsIgnoreCase("Vcode")){
                        answer.code = Integer.parseInt(value);
                    }
                    if (name.equalsIgnoreCase("VchCode")){
                        answer.chCode = value;
                    }
                }
            }
        }

        return answer;

    }*/


    public static XMLGregorianCalendar getXMLGregorianCalendarNow()
            throws DatatypeConfigurationException
    {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now =
                datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        return now;
    }

}
