package net.fishear.web.t5.base;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.fishear.utils.Numbers;
import net.fishear.utils.Texts;
import net.fishear.web.t5.exceptions.FieldValidationException;

import org.apache.tapestry5.Field;

public class LocaleData
{
    private NumberFormat currentcyFormat;

    private NumberFormat decimalFormat;

    private DecimalFormatSymbols decimalFormatSymbols;
    
	private String dateFormat;

	private String timeFormat;

	private AbstractComponent forComponent;

	LocaleData(AbstractComponent forComponent) {
		this.forComponent = forComponent;
	}

	public String getDateTimeFormat() {
        return getDateFormat() + " " + getTimeFormat();
	}

    public String getDateFormat() {
    	if(this.dateFormat == null) {
    		this.dateFormat = gmsg("date-format", "dd.MM.yyyy");
    	}
        return this.dateFormat;
    }

    public String getTimeFormat() {
    	if(this.timeFormat == null) {
    		this.timeFormat = gmsg("time-format", "HH:mm");
    	}
        return this.timeFormat;
    }

    private String gmsg(String key, String dft) {
        String s = forComponent.getMessage(key);
        if (s == null || s.length() == 0 || (s.startsWith("[[missing key:") && s.endsWith("]]"))) {
            return dft;
        }
        return s;
    }

    private DecimalFormatSymbols getNumberFormatSymbols() {
    	if(decimalFormatSymbols == null) {
	        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	        dfs.setCurrencySymbol(gmsg("currency-symbol", "Kč"));
	        dfs.setDecimalSeparator(gmsg("currency-decimal-symbol", ",").charAt(0));
	        dfs.setGroupingSeparator(gmsg("currency-grouping-char", " ").charAt(0));
	        decimalFormatSymbols = dfs;
    	}
        return decimalFormatSymbols;
    }

    public String getCurrencySymbol() {
        return getNumberFormatSymbols().getCurrencySymbol();
    }

    public NumberFormat getCurrencyFormat() {
        if (currentcyFormat == null) {
//            synchronized (getClass()) {
                if (currentcyFormat == null) {
                    currentcyFormat = new DecimalFormat(
                    		gmsg("currency-format", "###,###,###,###,###,###,###.00 ¤"), 
                    		getNumberFormatSymbols()
                    );
                }
//            }
        }
        return currentcyFormat;
    }

    public NumberFormat getDecimalFormat() {
        if (decimalFormat == null) {
//            synchronized (getClass()) {
                if (decimalFormat == null) {
                    decimalFormat = new DecimalFormat(gmsg("decimal-format", "###,###,###,###,###,###,##0.00"), getNumberFormatSymbols());
                }
//            }
        }
        return decimalFormat;
    }

    public String formatCurrency(Number value) {
        return value == null ? "" : getCurrencyFormat().format(value);
    }

    public Number parseCurrency(String text) throws ParseException {
        return Numbers.parseCurrency(text, getCurrencyFormat());
    }

    public String formatDecimal(Number value) {
        return getDecimalFormat().format(value);
    }

    public Number parseDecimal(String text) throws ParseException {
        if (text == null) {
            text = "0";
        }
        return Numbers.parseDecimal(text, getDecimalFormat());
    }

    /**
     * parse numeric value for given field.
     *
     * @param text string to be parsed
     * @param fld  field the value is designed for
     * @return
     * @throws FieldValidationException in case any error occurs
     */
    public Number parseDecimal(String text, Field fld) {
        if (text == null) {
            text = "0";
        }
        try {
            return Numbers.parseDecimal(text, getDecimalFormat());
        } catch (Exception ex) {
            throw new FieldValidationException(fld, "value-must-be-number", Texts.tos(fld.getLabel()), text);
        }
    }

    public String formatDate(Date date) {
        return new SimpleDateFormat(getDateFormat()).format(date);
    }

    public String formatDate(long millis) {
        return new SimpleDateFormat(getDateFormat()).format(millis);
    }

    public String formatDateTime(Date date) {
        return new SimpleDateFormat(getDateTimeFormat()).format(date);
    }

    public String formatDateTime(long millis) {
        return new SimpleDateFormat(getDateTimeFormat()).format(millis);
    }

}
