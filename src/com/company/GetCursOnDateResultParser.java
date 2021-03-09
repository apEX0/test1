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

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

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

    public static Valute getValuteByValuteCh(GetCursOnDateXMLResult result) throws Exception{


        Valute answer = new Valute();

        Connection connection = null;
        Statement stmt = null;
        String host="rds-database.ct2niq9pm0qk.eu-central-1.rds.amazonaws.com";
        String port1="5432";
        String db_name="myDatabase";
        String username="vladimir";
        String password="24091996";



        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port1+"/"+db_name+"", ""+username+"", ""+password+"");
            if (connection != null) {
                System.out.println("Connection OK");
            } else {
                System.out.println("Connection Failed");
            }






            List<Object> list = result.getContent();
            com.sun.org.apache.xerces.internal.dom.ElementNSImpl ex = (com.sun.org.apache.xerces.internal.dom.ElementNSImpl) list.get(0);
            NodeList chCodeList = ex.getElementsByTagName("VchCode");
            int length = chCodeList.getLength();


            boolean isFound = false;
            for (int i = 0; i< length; i++){
                if (isFound) break;

                Node valuteChNode = chCodeList.item(i);
                com.sun.org.apache.xerces.internal.dom.TextImpl textimpl = (com.sun.org.apache.xerces.internal.dom.TextImpl)valuteChNode.getFirstChild();
                String chVal = textimpl.getData();


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








                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                String sql = "INSERT INTO \"currency_rate\" (currency_name,rate,upload_date) "
                        + "VALUES ('"+answer.name+"','"+answer.curs+"','"+getXMLGregorianCalendarNow()+"');";

                stmt.executeUpdate(sql);




            }

            stmt.close();
            connection.commit();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;

    }





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