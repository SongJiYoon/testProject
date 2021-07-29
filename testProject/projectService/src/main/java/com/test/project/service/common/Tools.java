package com.test.project.service.common;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Tools {
    private Tools() {}

    private static Logger       logger              = LoggerFactory.getLogger(Tools.class);

    // E-mail
    // ^(\w[-.\w]*)@[-\w]+(\.[\w][\w]+)+$/
    public final static String  EMAIL_PATTERN       = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z.]{2,4})*(\\.[a-zA-Z.]{2,5})$";

    // 6자 이상의 영문, 숫자 조합
    // /^\w\w\w\w\w\w+$/
    public final static String  PASSWD_PATTERN      = "^[a-zA-Z0-9]{6,12}$";

    // 2자 이상의 영문,숫자,한글 조합
    // /^[^-+=\{}\\[\]:;"'<>,.?\/`~!@#$%^&*()\s][^-+=\\{}\[\]:;"'<>,.?\/`~!@#$%^&*()\s]+$/
    public final static String  WORD_PATTERN        = "^[^-+={}\\[\\]:;\"'<>,.?\\/`~!@#$%^&*()\\s][^-+=\\{}\\[\\]:;\"'<>,.?\\/`~!@#$%^&*()\\s]+$";

    // 핸드폰 번호 체크
    public final static String  HPNO_PATTERN        = "^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$";

    public final static String  NOT_JS_PATTERN      = "[',\"\\[\\]]";

    public final static String  NUMBER_PATTERN      = "[0-9]";

    // url 패턴
    private final static String URL                 = "(?:\\b(?:(?:(?:(ftp|https?|mailto|telnet):\\/\\/)?(?:((?:[\\w$\\-" + "_\\.\\+\\!\\*\\\'\\(\\),;\\?&=]|%[0-9a-f][0-9a-f])+(?:\\:(?:[\\w$"
                                                            + "\\-_\\.\\+\\!\\*\\\'\\(\\),;\\?&=]|%[0-9a-f][0-9a-f])+)?)\\@)?((?" + ":[\\d]{1,3}\\.){3}[\\d]{1,3}|(?:[a-z0-9]+\\.|[a-z0-9][a-z0-9\\-]+"
                                                            + "[a-z0-9]\\.)+(?:biz|com|info|name|net|org|pro|aero|asia|cat|coop|" + "edu|gov|int|jobs|mil|mobi|museum|tel|travel|ero|gov|post|geo|cym|"
                                                            + "arpa|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|" + "bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bw|by|bz|ca|cc|cd|cf|cg|ch"
                                                            + "|ci|ck|cl|cm|cn|co|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|e" + "r|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|"
                                                            + "gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it" + "|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|l"
                                                            + "t|lu|lv|ly|ma|mc|me|md|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|" + "mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph"
                                                            + "|pk|pl|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|s" + "i|sk|sl|sm|sn|sr|st|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tr|"
                                                            + "tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|za|zm" + "|zw)|localhost)\\b(?:\\:([\\d]+))?)|(?:(file):\\/\\/\\/?)?([a-z]:"
                                                            + "))(?:\\/((?:(?:[~\\w$\\-\\.\\+\\!\\*\\(\\),;:@=ㄱ-ㅎㅏ-ㅣ가-힣]|%[" + "0-9a-f][0-9a-f]|&(?:nbsp|lt|gt|amp|cent|pound|yen|euro|sect|copy|"
                                                            + "reg);)*\\/)*)([^\\s\\/\\?:\\\"\\\'<>\\|#]*)(?:[\\?:;]((?:\\b[\\w]+" + "(?:=(?:[\\w\\$\\-\\.\\+\\!\\*\\(\\),;:=ㄱ-ㅎㅏ-ㅣ가-힣]|%[0-9a-f]"
                                                            + "[0-9a-f]|&(?:nbsp|lt|gt|amp|cent|pound|yen|euro|sect|copy|reg);)*" + ")?\\&?)*))*(#[\\w\\-ㄱ-ㅎㅏ-ㅣ가-힣]+)?)?)";

    /**
     * 색인 문자를 반환한다.
     * 
     * @param pStr
     * @return
     */
    private static final char[] CHO_SUNG            = {
                                                    // 0x3131, 0x3132, 0x3134, 0x3137 // ㄱ ㄲ ㄴ ㄷ
                                                    // , 0x3138, 0x3139, 0x3141, 0x3142 // ㄸ ㄹ ㅁ ㅂ
                                                    // , 0x3143, 0x3145, 0x3146, 0x3147 // ㅃ ㅅ ㅆ ㅇ
                                                    // , 0x3148, 0x3149, 0x314a, 0x314b // ㅈ ㅉ ㅊ ㅋ
                                                    // , 0x314c, 0x314d, 0x314e // ㅌ ㅍ ㅎ
            0x3131, 0x3131, 0x3134, 0x3137 // ㄱ ㄱ ㄴ ㄷ
            , 0x3137, 0x3139, 0x3141, 0x3142 // ㄷ ㄹ ㅁ ㅂ
            , 0x3142, 0x3145, 0x3145, 0x3147 // ㅂ ㅅ ㅅ ㅇ
            , 0x3148, 0x3148, 0x314a, 0x314b // ㅈ ㅈ ㅊ ㅋ
            , 0x314c, 0x314d, 0x314e               // ㅌ ㅍ ㅎ
                                                    };

    // 대소문자 구분 여부
    public final static int     CASE_INSENSITIVE    = 0x01;

    public final static String  DEFAULT_DATE_FORMAT = "yyyy/MM/dd";
    
    public final static String  DEFAULT_DATE_MONTH_FORMAT = "MM";

    // 한글 여부
    public final static String  KOREANPATTERN       = "[가-힣]{2,}";

    // 영문 여부
    public final static String  ENGLISHPATTERN      = "[a-zA-Z]{4,}";

    private final static int    MILLI_SECONDS       = 1000;
    private final static int    TIME_UNIT           = 60;
    private final static double FILE_SIZE_UNIT      = 1024d;

    /**
     * 문자열에 값을 대입한 문자열을 반환한다. getMessage("this value is {0}.", 1); -> this value is 1.
     * 
     * @param name
     * @param args
     * @return
     */
    public static String getMessage(String name, Object... args) {
        MessageFormat form = new MessageFormat(name);
        return form.format(args);
    }

    /**
     * 숫자에 3개 마다 콤마(,)를 추가 한다. addComma(12345678) -> "12,345,678"
     * 
     * @param value
     * @return
     */
    public static <T> String addComma(T value) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(value);
    }

    /**
     * Map으로 부터 key에 해당하는 value를 반환 한다. value 값이 null일 defaultValue를 반환 한다.
     * 
     * @param <S>
     *            key type
     * @param <T>
     *            value type
     * @param map
     *            HashMap
     * @param key
     *            key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <S, T, R> R getValue(Map<S, T> map, S key, R defaultValue) {
        T t = (T) map.get(key);
        if (t == null) {
            return defaultValue;
        }

        return (R) t;
    }

    /**
     * value 값이 null이면 defaultValue 값을 반환한다.
     * 
     * @param <T>
     * @param value
     * @param defaultValue
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Object value, T defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        return (T) value;
    }

    public static String getValue(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    /**
     * value 값이 null일 경우 빈 문자열을 반환한다. null이 아닐 경우 value 값을 반환한다.
     * 
     * @param value
     * @return
     */
    public static String getString(String value) {
        return getString(value, "");
    }

    /**
     * value 값이 null일 경우 defaultValue를 반환한다. null이 아닐 경우 value 값을 반환한다
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static String getString(String value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * value 숫자로 된 문자열일 경우 숫자로 변환한 값을 반환한다. 숫자가 아니거나 null일 경우 0을 반환 한다.
     * 
     * @param value
     * @return
     */
    public static int getInteger(String value) {
        return getInteger(value, 0);
    }

    public static float getFloat(String value) {
        return getFloat(value, (float) 0.0);
    }

    public static double getDouble(String value) {
        return getDouble(value, 0.0);
    }

    /**
     * value 숫자로 된 문자열일 경우 숫자로 변환한 값을 반환한다. 숫자가 아니거나 null일 경우defaultValue을 반환 한다.
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static int getInteger(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            // do nothing
        }

        return defaultValue;
    }

    /**
     * value 값이 null이거나 빈 문자열일 이거나 숫자가 아닐 경우 defaultValue 값을 반환 한다. null이 아닐 경우 value 값을 float 값으로 변환하여 반환 한다.
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static float getFloat(String value, float defaultValue) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            // do nothing
        }

        return defaultValue;
    }

    /**
     * value 값이 null이거나 빈 문자열일 이거나 숫자가 아닐 경우 defaultValue 값을 반환 한다. null이 아닐 경우 value 값을 double 값으로 변환하여 반환 한다.
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static double getDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            // do nothing
        }

        return defaultValue;
    }

    /**
     * String을 boolean으로 바꿔준다.
     * 
     * @param value
     * @return
     */
    public static boolean getBoolean(String value) {
        return getBoolean(value, false);
    }

    /**
     * value 값이 null이거나 빈 문자열일 경우 defaultValue 값을 반환한다. null이 아닐 경우 value 값을 boolean 값으로 변환하여 반환한다.
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(String value, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            // do nothing
        }

        return defaultValue;
    }    

    /**
     * 
     * @param map
     * @param key
     * @param defaultValue
     * @return
     */
    public static <T> T get(Map<String, T> map, String key, T defaultValue) {
        T value = (T) map.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * Map으로부터 key에 해당하는 value를 반환한다.
     * 
     * @param map
     * @param key
     * @return
     */
    public static <T> T get(Map<String, T> map, String key) {
        return (T) map.get(key);
    }

    /**
     * 시간을 yyyy/MM/dd 형식의 문자열로 변환한다.
     * 
     * @param time
     * @return
     */
    public static String getDateString(long time) {
        return getDateString(time, DEFAULT_DATE_FORMAT);
    }
    
    public static String getDateMonthString(long time) {
        return getDateString(time, DEFAULT_DATE_MONTH_FORMAT);
    }

    /**
     * Timestamp를 yyyy/MM/dd 형식의 문자열로 변환 한다.
     * 
     * @param time
     * @return
     */
    public static String getDateString(Timestamp time) {
        return getDateString(time, DEFAULT_DATE_FORMAT);
    }

    /**
     * Timestamp를 format 형식의 문자열로 변환 한다.
     * 
     * @param time
     * @param format
     * @return
     */
    public static String getDateString(Timestamp time, String format) {
        if (time == null) {
            return "";
        }
        return getDateString(time.getTime(), format);
    }

    /**
     * Timestamp를 format 형식의 문자열로 변환 한다.
     * 
     * @param time
     * @param format
     * @param locale
     * @return
     */
    public static String getDateString(Timestamp time, String format, Locale locale) {
        if (time == null) {
            return "";
        }
        return getDateString(time.getTime(), format, locale);
    }

    /**
     * 시간을 format 형식의 문자열로 변환 한다.
     * 
     * @param time
     * @param format
     * @return
     */
    public static String getDateString(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, java.util.Locale.KOREAN);
        return sdf.format(time);
    }

    /**
     * 시간을 format 형식의 문자열로 변환 한다.
     * 
     * @param time
     * @param format
     * @param locale
     * @return
     */
    public static String getDateString(long time, String format, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(time);
    }

    /**
     * 오늘 날짜를 yyyy/MM/dd 형식의 문자열로 돌려 준다.
     * 
     * @return
     */
    public static String today() {
        return today(DEFAULT_DATE_FORMAT);
    }

    /**
     * 오늘 날짜를 포멧 형태의 문자열로 돌려 준다.
     * 
     * @param format
     * @return
     */
    public static String today(String format) {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(today);
    }

    /**
     * 어제 날짜를 yyyy/MM/dd 형식의 문자열로 돌려 준다.
     * 
     * @return
     */
    public static String yesterday() {
        return yesterday(DEFAULT_DATE_FORMAT);
    }

    /**
     * 어제 날짜를 포멧 형태의 문자열로 돌려 준다.
     * 
     * @param format
     * @return
     */
    public static String yesterday(String format) {
        Date today = new Date();
        today.setTime(today.getTime() - (86400 * MILLI_SECONDS));
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(today);
    }

    /**
     * yyyy/MM/dd 형식의 문자열로 부터 milliseconds단위의 시간 값을 얻는다.
     * 
     * @param dateString
     * @return
     */
    public static long getTime(String dateString) {
        return getTime(dateString, DEFAULT_DATE_FORMAT);
    }

    /**
     * yyyy/MM/dd 형식의 문자열로 부터 milliseconds단위의 시간 값을 얻는다.
     * 
     * @param dateString
     * @param format
     * @return
     */
    public static long getTime(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dateString).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 시간값(milliseconds)으로 부터 요일 index 를 얻는다. 일요일 = 1, 토요일 = 7
     * 
     * @param time
     * @return
     */
    public static int getDayOfWeek(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * comma(,)로 연결된 숫자 값으로 구성된 문자열을 숫자 배열로 바꾸어 준다.
     * 
     * @param nos
     * @return
     */
    public static List<Integer> stringToIntArray(String nos) {
        ArrayList<Integer> intArray = new ArrayList<Integer>();
        if (nos != null && "".equals(nos) == false) {
            for (String s : nos.split(",")) {
                s = s.trim();
                if ("".equals(s) == false) {
                    intArray.add(Integer.parseInt(s));
                }
            }
        }

        return intArray;
    }

    public static String isChecked(String str1, String str2) {
        if (str1.equals(str2)) {
            return "checked";
        }
        return "";
    }

    /**
     * select박스가 select되어야 할지를 얻는다.
     * 
     * @param String
     *            str1
     * @param String
     *            str2
     * @return
     */
    public static String isSelected(String str1, String str2) {
        if (str1.equals(str2)) {
            return "selected";
        }
        return "";
    }

    /**
     * 현재 날짜로 부터 n만큼의 +한 날짜를 구한다.
     * 
     * @param int offset
     * @param String
     *            format
     * @return
     */
    public static String getFromDateString(int offset, String format) {
        Calendar cFrom = Calendar.getInstance();
        cFrom.set(Calendar.HOUR_OF_DAY, 0);
        cFrom.set(Calendar.MINUTE, 0);
        cFrom.set(Calendar.SECOND, 0);
        cFrom.add(Calendar.DATE, +offset);
        Timestamp tFrom = new Timestamp(cFrom.getTimeInMillis());
        return getDateString(tFrom, format);
    }

    /**
     * 플레이시간을 초로 받아 시간.분.초 로 리턴.
     * 
     * @param seconds
     *            플레잉타임
     * @return
     */
    public static String getPlayTimeStr(int seconds) {
        int temp = seconds;
        int minutes = 0;
        int hours = 0;
        if (temp > TIME_UNIT) {
            minutes = (int) temp / TIME_UNIT;
            temp -= (minutes * TIME_UNIT);
            if (minutes > TIME_UNIT) {
                // Convert minutes into hours
                hours = (int) minutes / TIME_UNIT;
                // Remove the converted minutes
                minutes = minutes - (hours * TIME_UNIT);
            }
        }

        return hours + "." + minutes + "." + temp;
    }

    /**
     * 플레이시간을 초로 받아 시간.분.초 로 리턴.
     * 
     * @param seconds
     *            플레잉타임
     * @return
     */
    public static String getPlayTimeStr(long seconds) {
        long temp = seconds;
        long minutes = 0;
        long hours = 0;
        if (temp > TIME_UNIT) {
            minutes = temp / TIME_UNIT;
            temp -= (minutes * TIME_UNIT);
            if (minutes > TIME_UNIT) {
                // Convert minutes into hours
                hours = minutes / TIME_UNIT;
                // Remove the converted minutes
                minutes = minutes - (hours * TIME_UNIT);
            }
        }
        return hours + "." + minutes + "." + temp;
    }

    /**
     * 구분자로 플레이시간을 초로 받아 시간+분+초 로 리턴.
     * 
     * @param seconds
     *            플레잉타임
     * @return
     */
    public static String getPlayTimeStr(int seconds, String separator) {
        int temp = seconds;
        int minutes = 0;
        int hours = 0;
        if (temp > TIME_UNIT) {
            minutes = (int) temp / TIME_UNIT;
            temp -= (minutes * TIME_UNIT);
            if (minutes > 60) {
                // Convert minutes into hours
                hours = (int) minutes / TIME_UNIT;
                // Remove the converted minutes
                minutes = minutes - (hours * TIME_UNIT);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", hours));
        sb.append(separator);
        sb.append(String.format("%02d", minutes));
        sb.append(separator);
        sb.append(String.format("%02d", temp));

        return sb.toString();
    }

    /**
     * byte 단위의 파일사이즈를 받아 문자열로 변환.
     * 
     * @param fileSize
     * @return
     */
    public static String getFileSizeToString(long fileSize) {
        BigDecimal bd = new BigDecimal(fileSize / FILE_SIZE_UNIT);
        BigDecimal result = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        double k = result.doubleValue();
        double m = 0d;
        double g = 0d;

        if (k > FILE_SIZE_UNIT) {
            bd = new BigDecimal(k / FILE_SIZE_UNIT);
            result = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
            m = result.doubleValue();
        }

        if (m > FILE_SIZE_UNIT) {
            bd = new BigDecimal(m / FILE_SIZE_UNIT);
            result = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
            g = result.doubleValue();
        }

        String str = null;
        if (g > 0d) {
            str = g + "G";
        } else if (m > 0d) {
            str = m + "M";
        } else {
            str = k + "K";
        }

        return str;
    }

    /**
     * maxlength 만큼 문자열을 자르고 "..." 을 붙여서 리턴한다.
     * 
     * @param str
     * @param maxlength
     * @return
     */
    public static String cutString(String str, int maxlength) {
        if (str == null) {
            return "";
        }

        int len = str.length();

        if (len >= maxlength) {
            return str.substring(0, maxlength) + "...";
        }

        return str.substring(0, len);
    }

    /**
     * maxlength 만큼 문자열을 자르고 리턴한다.
     * 
     * @param str
     * @param maxlength
     * @param dummy
     * @return
     */
    public static String cutString(int maxlength, String str) {
        String result = null;

        if (str == null) {
            return "";
        }

        if (str.length() == 0) {
            return str;
        }

        if (str.length() * 2 < maxlength) {
            return str;
        }

        char c = '\0';
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (c < 0xac00 || 0xd7a3 < c) {
                len++;
            } else {
                len += 2;
            }

            if (len > maxlength) {
                result = str.substring(0, i);
                break;
            }
        }

        return result;
    }

    /**
     * length 만큼 byte 로 자르고 ... 추가하여 반환
     * 
     * @param str
     * @param length
     * @return
     */
    public static String cutStringByByte(String str, int length) {
        if (str == null) {
            return "";
        }

        byte[] bytes = str.getBytes();
        int len = bytes.length;
        int counter = 0;

        if (length >= len) {
            return str;
        }

        for (int i = length - 1; i >= 0; i--) {
            if (((int) bytes[i] & 0x80) != 0) {
                counter++;
            }
        }
        return new String(bytes, 0, length - (counter % 2)) + "...";
    }

    public static int getCharLength(String str) {

        int length = str.length();
        int tempSize = 0, asc;

        for (int i = 1; i <= length; i++) {
            asc = (int) str.charAt(i - 1);
            // 한글일때
            if (asc > 127) {
                tempSize += 2;
                continue;
            }

            tempSize++;
        }
        return tempSize;
    }

    /**
     * URL에서 http://hostname:port 부분만 가져 온다.
     * 
     * @param url
     * @return
     */
    public static String getHostNameFromURL(String url) {
        String hostname1 = "(?:http)://[-\\w]+(\\.\\w[-\\w]*)*\\.";
        String subDomain = "(?i:[a-z0-9]|[a-z0-9][-a-z0-9]*[a-z0-9])";
        String topDomain = "(?i:([a-z]{2,4})\\b)";
        String hostname2 = "(?:" + subDomain + "\\.)+";
        String port = "(?::\\d+)?";
        String patternString = "(?x:\\b(" + hostname1 + "|" + hostname2 + ")" + topDomain + port + ")";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return url.substring(matcher.start(), matcher.end());
        }

        return url;
    }

    public static String trim(final String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    public static String trimToNull(String str) {
        if (str == null) {
            return null;
        }

        if ("".equals(str.trim())) {
            return null;
        }

        return str;
    }

    public static boolean isBlank(final String str) {
        return str == null || "".equals(str) || "".equals(str.trim());
    }

    public static boolean isNotBlank(final String str) {
        return false == isBlank(str);
    }

    public static String notNull(final String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    /**
     * Test if the given String starts with the specified prefix, ignoring upper/lower case.
     * 
     * @param str
     *            the String to check
     * @param prefix
     *            the prefix to look for
     * @see java.lang.String#startsWith
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        if (str.startsWith(prefix)) {
            return true;
        }
        if (str.length() < prefix.length()) {
            return false;
        }
        String lcStr = str.substring(0, prefix.length()).toLowerCase();
        String lcPrefix = prefix.toLowerCase();
        return lcStr.equals(lcPrefix);
    }

    /**
     * timestamp 형식을 문자열로 변환한다. 예) 24분 전, 1시간 1분 전, 381일 전...
     * 
     * @param t
     * @return
     */
    public static String timestampToString(Timestamp t) {
        if (t == null) {
            return "";
        }
        long gap = System.currentTimeMillis() - t.getTime();

        long m = 0L;
        long h = 0L;
        long d = 0L;

        m = gap / (TIME_UNIT * MILLI_SECONDS);

        if (m < 60L) {
            return m + "분 전";
        }

        h = m / TIME_UNIT;
        if (h < 24L) {
            return h + "시간 " + (m % TIME_UNIT) + "분 전";
        }

        d = h / 24;
        if (d < 30L) {
            return d + "일 전";
        }

        return new java.text.SimpleDateFormat("yyyy.MM.dd").format(t);
    }

    /**
     * 한글 체크 글자수는 2자 이상
     * 
     * @param value
     * @return
     */
    public static boolean checkOnlyKorean(String value) {
        return checkPattern(KOREANPATTERN, value);
    }

    /**
     * 영문자 체크 글자수는 4자 이상
     * 
     * @param value
     * @return
     */
    public static boolean checkOnlyEnglish(String value) {
        return checkPattern(ENGLISHPATTERN, value);
    }

    /**
     * 패턴 테스트
     * 
     * @param pattern
     * @param value
     * @return
     */
    public static boolean checkPattern(String pattern, String value) {
        return Pattern.compile(pattern).matcher(value).find();
    }

    /**
     * E-mail 패턴 확인 패턴과 일치할 경우 true, 일치하지 않으면 false
     * 
     * @param email
     * @return
     */
    public static boolean checkEmailPattern(String email) {
        return checkPattern(EMAIL_PATTERN, email);
    }

    /**
     * 핸드폰번호 패턴 확인 패턴과 일치할 경우 true, 일치하지 않으면 false
     * 
     * @param hpno
     * @return
     */
    public static boolean checkHpNoPattern(String hpno) {
        return checkPattern(HPNO_PATTERN, hpno);
    }

    /**
     * E-mail로 부터 username 부분을 리턴 한다.
     * 
     * @param email
     * @return
     */
    public static String getUsernameFromEmail(String email) {
        return getUsernameFromEmail(email, 0);
    }

    public static String getUsernameFromEmail(String email, int flags) {
        Matcher m = Pattern.compile(EMAIL_PATTERN).matcher(email);
        if (m.find()) {
            if ((flags & CASE_INSENSITIVE) != 0) {
                return m.group(1).toLowerCase();
            }
            return m.group(1);
        }
        return null;
    }

    /**
     * 구분자로 문자열을 나누고 공백을 제외한 값들을 list 에 담아 리턴.
     * 
     * @param str
     * @param delimiter
     * @return
     */
    public static List<String> splitExcludeEmpty(String str, String delimiter) {
        ArrayList<String> list = new ArrayList<String>();
        if (StringUtils.isBlank(str)) {
            return list;
        }

        String[] arr = StringUtils.splitPreserveAllTokens(str, delimiter);
        int arrCount = arr.length;
        for (int ii = 0; ii < arrCount; ii++) {
            String temp = arr[ii].trim();
            if (StringUtils.isNotBlank(temp)) {
                list.add(temp);
            }
        }

        return list;
    }

    /**
     * 숫자를 n을 00/00/00/000 형태의 문자열로 변환 한다.
     * 
     * @param number
     * @return
     */
    public static String makeImagePath(int number) {

        int n1 = number / 10000000; // 10,000,000 ~ 990,000,000
        int n2 = (number % 10000000) / 100000; // 100,000 ~ 9,900,000
        int n3 = (number % 100000) / 1000; // 1,000 ~ 99,000
        int n4 = number % 1000; // 0 ~ 999

        return String.format("%02d/%02d/%02d/%03d", n1, n2, n3, n4);
    }

    public static String makeUploadPath() {

        long time = System.currentTimeMillis();

        // yyyy/mm/dd/hh24/mi/time
        return String.format("/%s/%d", getDateString(time, "yyyy/MM/dd/HH/mm"), time);
    }

    /**
     * 주민등록 번호를 123456 - ******* 형태로 바꿔준다.
     * 
     * @param resno
     * @return
     */
    public static String getResnoString(String resno) {
        if (isBlank(resno) || resno.length() < 6) {
            return "";
        }

        String r1 = resno.substring(0, 6);
        String r2 = "*******";
        return r1 + " - " + r2;
    }

    /**
     * 문자열 끝에 '/' 추가
     * 
     * @param str
     * @return
     */
    public static String appendSlash(String str) {
        if (isBlank(str)) {
            return "/";
        }

        return str + (str.endsWith("/") ? "" : "/");
    }

    /**
     * condition 값이 참이면 trueValue를 거짓이면 falseValue를 반환 한다.
     * 
     * @param condition
     * @param trueValue
     * @param falseValue
     * @return
     */
    public static String ternaryString(boolean condition, String trueValue, String falseValue) {
        return condition ? trueValue : falseValue;
    }

    /**
     * DB 의 이미지 관련 경로 보정. DB 값이 하나로 통일되면 필요없는 메소드임 1. http://... 로 시작하는 경우 2. '/' 로 시작하는 경우 3. '/' 로 시작하지 않는 경우
     * 
     * @param imagePath
     * @param defaultImagePath
     * @param width
     *            이미지 너비
     * @return
     */
    public static String correctImagePath(String imagePath, String defaultImagePath) {
        return correctImagePath(imagePath, defaultImagePath, 0);
    }

    public static String correctImagePath(String imagePath, String defaultImagePath, int width) {
        if (StringUtils.isBlank(imagePath)) {
            return defaultImagePath;
        }

        String temp = "";
        if (width > 0) {
            int idx = imagePath.lastIndexOf('.');

            if (idx > 0) {
                temp = imagePath.substring(0, idx) + "_" + width + imagePath.substring(idx);
            }
        }

        if (StringUtils.startsWithAny(temp, new String[] { "http://", "/" })) {
            return temp;
        }

        return "/" + temp;
    }

    public static String br2nl(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("<[bB][rR]( *)?>", "\n");
    }

    public static String nl2br(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("\r?\n", "<br>");
    }

    /**
     * 이메일 인증 코드 발급
     * 
     * @return
     */
    public static String createAuthCode() {
        return UUID.randomUUID().toString();
    }

    /**
     * 휴대폰 인증코드 발급 (6자리 정수)
     * 
     * @return
     */
    public static String createPhoneAuthCode() {
        int num = (int) (Math.random() * 1000000) - 1;
        return String.format("%06d", num);
    }

    /**
     * boolean 값을 Y, N 문자열로 바꿔준다.
     * 
     * @param value
     * @return boolean 값이 true면 "Y", false면 "N"
     */
    public static String booleanToYN(boolean value) {
        return value ? "Y" : "N";
    }

    /**
     * "Y", "N" 문자열을 boolean 값으로 바꿔준다. Y: true, N: false
     * 
     * @param yn
     * @return
     */
    public static boolean ynToBoolean(String yn) {
        return "Y".equals(yn);
    }

    /**
     * &, <, > 등을 Symbolic HTML entity로 변환 한다. \r\n, \n, 글 앞 뒤 space 등은 skip 한다.
     * 
     * @param html
     * @return
     */
    public static String escapeHTML(String html) {
        if (html == null) {
            return "";
        }

        return html.replaceAll("\"", "&quot;").replaceAll("'", "&#39;").replaceAll("&(?!amp;)", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\\r?\\n", "").trim();
    }

    /**
     * &, ", ' 등을 Symbolic HTML unentity로 변환 한다. \r\n, \n, 글 앞 뒤 space 등은 skip 한다.
     * 
     * @param html
     * @return
     */
    public static String unescapeHTML(String html) {
        if (html == null) {
            return "";
        }

        return html.replaceAll("&quot;", "\"").replaceAll("&#39;", "'").replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("\\r?\\n", "").trim();
    }

    /**
     * &, <, >, \r\n, \n, 글 앞 뒤 space 등은 skip 한다.
     * 
     * @param text
     * @return
     */
    public static String escapeText(String text) {
        if (text == null) {
            return "";
        }

        return text.replaceAll("\"", "").replaceAll("'", "").replaceAll("&(?!amp;)", "").replaceAll("<", "").replaceAll(">", "").replaceAll("\\r?\\n", "").trim();
    }

    /**
     * <, > 등을 Symbolic HTML entity로 변환 한다. 글 앞 뒤 space 등은 skip 한다.
     * 
     * @param html
     * @return
     */
    public static String escapeHTML2(String html) {
        if (html == null) {
            return "";
        }

        return html.replaceAll("<", "&lt;").replaceAll(">", "&gt;").trim();
    }

    /**
     * '등의 글자 기호코드를 원래 문자로 변환 한다.
     * 
     * @param word
     * @return
     */
    public static String toSpecialLetter(String word) {
        if (word == null) {
            return "";
        }
        return word.replaceAll("&#39;", "'").replaceAll("&amp;#39;", "'");
    }

    public static String escapeSlash(String s) {
        if (s == null) {
            return "";
        }
        return s.replaceAll("\\/", "\\\\/");
    }

    public static boolean checkNameKr(String name) {
        Pattern p2 = Pattern.compile("[ㄱ-ㅎ가-흐0-9]+$");
        Matcher m2 = p2.matcher(name);
        return m2.matches();
    }

    public static boolean checkNameEn(String name) {
        Pattern p2 = Pattern.compile("[a-zA-Z0-9]+$");
        Matcher m2 = p2.matcher(name);
        return m2.matches();
    }

    /**
     * 쌍따옴표. 홑따옴표 치환
     * 
     * @param value
     * @return
     */
    public static String getQuotation(String value) {
        String temp = value.replaceAll("\"", "&quot;");
        temp = temp.replaceAll("'", "&#039;");
        return temp;
    }

    /**
     * 입력한 기간내에 현재 일시 포함여부 체크
     * 
     * @param sDate
     * @param eDate
     * @param format
     *            날짜형식
     * @return
     * @throws ParseException
     */
    public static boolean isBeweenToDate(String sDate, String eDate, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date startDate = formatter.parse(sDate); // 시작일 (Date타입)
        Date endDate = formatter.parse(eDate); // 종료일시 (Date타입)

        return isBeweenToDate(startDate, endDate);
    }

    /**
     * 입력한 기간내에 현재 일시 포함여부 체크
     * 
     * @param sDate
     * @param eDate
     * @return
     */
    public static boolean isBeweenToDate(Date sDate, Date eDate) {
        Date toDate = Calendar.getInstance().getTime();
        return sDate.before(toDate) && eDate.after(toDate);
    }

    /**
     * url 패턴을 추출하여 배열로 돌려준다
     * 
     * @param reg
     * @param modi
     * @return
     * @throws Exception
     */
    public static List<String> process(String reg, int modi) throws Exception {

        if (reg == null || reg.equals("")) {
            throw new Exception("reg is null");
        }

        Pattern p = Pattern.compile(URL, modi);
        ArrayList<String> r = new ArrayList<String>();
        Matcher m = p.matcher(reg);

        while (m.find()) {
            r.add(m.group());
        }

        return r;
    }

    /**
     * url 패턴을 추출하여 html 링크 태그로 돌려준다.
     * 
     * @param text
     * @return
     * @throws Exception
     */
    public static String getUrlPatternToLinkTag(String text) throws Exception {

        // 글자가 길 경우 find()에서 stackOverflow가 나므로 1000자로 제한
        if (text.length() >= 1000) {
            return text;
        }

        String[] mArr = text.split(" ");
        String subMsg = "";
        String tmpMsg = "";

        for (int i = 0; i < mArr.length; i++) {
            subMsg = mArr[i];
            if (subMsg.equals("") == false) {
                List<String> r = process(subMsg, Pattern.CASE_INSENSITIVE);
                if (r.size() > 0) {
                    for (int j = 0; j < r.size(); j++) {
                        String a = r.get(j).replaceAll("http://", "").replaceAll("https://", "");
                        subMsg = subMsg.replace(r.get(j), "<a style='color: #0000ff;' href='http://" + a + "' target='_blank'>" + r.get(j) + "</a>");
                    }
                }
            }
            tmpMsg += (subMsg + "&nbsp;");
        }

        return tmpMsg;

    }

    public static String getIndexChar(String pDisplayName) {
        String pStr = null;

        if (pDisplayName == null || pDisplayName.length() == 0) {
            return "";
        }

        pStr = pDisplayName;

        char indexChar = pStr.trim().charAt(0);
        int index = 0;
        // 가~ 힣
        if (indexChar >= 0xAC00 && indexChar <= 0xD7A3) {
            index = indexChar - 0xAC00;
            index = index / (21 * 28);

            return String.valueOf(CHO_SUNG[index]);
        }

        return String.valueOf(indexChar).toUpperCase();
    }

    /**
     * 이번 달 1일에 대한 문자열 반환 2011/08/01
     * 
     * @return
     */
    public static String getFirstDayOfThisMonth() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        cal.clear();
        cal.set(year, month, 1);
        return Tools.getDateString(cal.getTimeInMillis());
    }

    public static void main(String[] args) {
        long d = 480l;

        logger.debug(getPlayTimeStr(new Long(d)));

        String inputName = "woodlim_kr@naver.com";
        logger.debug("checkEmailPattern => " + Tools.checkEmailPattern(inputName));

        logger.debug("checkOnlyEnglish => " + Tools.checkOnlyEnglish(inputName));
        logger.debug("checkOnlyKorean => " + Tools.checkOnlyKorean(inputName));
        logger.debug("checkPattern => " + Tools.checkPattern("[',\"]", inputName));
        logger.debug("sdgf" + Tools.checkPattern(Tools.PASSWD_PATTERN, "dsffdsfdsfds"));
        if ("".equals(inputName) == false && Tools.checkOnlyEnglish(inputName) == false && Tools.checkOnlyKorean(inputName) == false || Tools.checkPattern(NOT_JS_PATTERN, inputName)) {
            logger.debug("Pass");
        }

    }
}