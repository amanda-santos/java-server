/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author Amanda
 */
public class Util {

    static String formatDateGMT(Date date) {
        // cria um formato para o GMT espeficicado pelo HTTP
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date data = new Date();
        // formata a data para o padrão
        return formatter.format(data) + " GMT";
    }

}
