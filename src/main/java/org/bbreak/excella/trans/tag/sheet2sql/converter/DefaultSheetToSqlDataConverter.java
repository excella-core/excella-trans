/*-
 * #%L
 * excella-trans
 * %%
 * Copyright (C) 2009 - 2019 bBreak Systems and contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package org.bbreak.excella.trans.tag.sheet2sql.converter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlSettingInfo;

/**
 * デフォルトで使用するデータコンバータ。<BR>
 * PostgreSQL 8.3.7のデータ型に対応。<BR>
 * 
 * @since 1.0
 */
public class DefaultSheetToSqlDataConverter implements SheetToSqlDataConverter {

    /**
     * データ型：文字列<BR>
     * 対象とするデータ型：<BR>
     * bit[(n)], bit varying[(n)], box, bytea, character varying[(n)],<BR>
     * character[(n)], cidr, circle, inet, interval[(p)], line, lseg,<BR>
     * macaddr, money, path, point, polygon, text, tsquery, tsvector,<BR>
     * txid_snapshot, uuid, xml<BR>
     */
    public static final String DATA_TYPE_STRING = "文字列";

    /**
     * データ型：数値 対象とするデータ型：<BR>
     * bigint, bigserial, integer, smallint, serial, uuid, xml<BR>
     */
    public static final String DATA_TYPE_NUMERIC = "数値";

    /**
     * データ型：整数 対象とするデータ型：<BR>
     * double precision, numeric[(p,s)], real<BR>
     */
    public static final String DATA_TYPE_INTEGER = "整数";

    /**
     * データ型：論理値 対象とするデータ型：<BR>
     * boolean<BR>
     */
    public static final String DATA_TYPE_BOOLEAN = "論理値";

    /**
     * データ型：日付 対象とするデータ型：<BR>
     * date<BR>
     */
    public static final String DATA_TYPE_DATE = "日付";

    /**
     * データ型：時間 対象とするデータ型：<BR>
     * time[(p)] [without time zone], time[(p)] with time zone<BR>
     */
    public static final String DATA_TYPE_TIME = "時間";

    /**
     * データ型：タイムスタンプ 対象とするデータ型：<BR>
     * timestamp[(p)] [without time zone], timestamp[(p)] with time zone<BR>
     */
    public static final String DATA_TYPE_TIMESTAMP = "タイムスタンプ";

    /**
     * データ型：関数 対象とするデータ型：<BR>
     * SQL関数<BR>
     */
    public static final String DATA_TYPE_FUNCTION = "関数";

    /**
     * シングルクオーテーション
     */
    private static final String SINGLE_QUOTE = "'";

    /**
     * 日付区切り文字列
     */
    private static final String DATE_SEPARATOR = "-";

    /**
     * 時間区切り文字列
     */
    private static final String TIME_SEPARATOR = ":";

    /**
     * ピリオド
     */
    private static final String PERIOD = ".";

    /**
     * データ型に基づき、コンバート処理を実行する<BR>
     * オブジェクトがnullの場合はnullを返却する。<BR>
     * データ型がnullの場合はシングルクオーテーションを付与した文字列を返却する。<BR>
     * 
     * @param object オブジェクト
     * @param dataType データ型
     * @param settingInfo SQL変換設定情報
     * @return コンバートされた文字列
     * @throws ParseException オブジェクト変換中に例外が発生した場合<BR>
     *                         存在しないデータ型が指定された場合<BR>
     */
    public String convert( Object object, String dataType, SheetToSqlSettingInfo settingInfo) throws ParseException {

        String result = null;

        if ( object == null) {
            // オブジェクトがnullの場合
            return null;
        }

        if ( dataType == null || DATA_TYPE_STRING.equals( dataType)) {
            // データ型がnull または 文字列の場合

            // シングルクオーテーションを付ける
            String value = object.toString();
            result = getSingleQuotedString( value);

        } else if ( DATA_TYPE_NUMERIC.equals( dataType)) {
            // データ型が数値の場合

            try {
                Double value = Double.parseDouble( object.toString());
                result = value.toString();
            } catch ( Exception e) {
                throw new ParseException( settingInfo.getValueCell(), "値が数値ではありません：" + object.toString());
            }

        } else if ( DATA_TYPE_INTEGER.equals( dataType)) {
            // データ型が整数の場合

            try {
                Double value = Double.parseDouble( object.toString());
                Integer valueInt = value.intValue();
                result = valueInt.toString();
            } catch ( Exception e) {
                throw new ParseException( settingInfo.getValueCell(), "値が整数ではありません：" + object.toString());
            }

        } else if ( DATA_TYPE_BOOLEAN.equals( dataType)) {
            // データ型が論理値の場合

            String value = object.toString();
            if ( isTrueExpression( value)) {
                result = "TRUE";
            } else if ( isFalseExpression( value)) {
                result = "FALSE";
            } else {
                throw new ParseException( settingInfo.getValueCell(), "値が論理値ではありません：" + object.toString());
            }

        } else if ( DATA_TYPE_DATE.equals( dataType)) {
            // データ型が日付の場合

            try {
                Date date = ( Date) object;
                GregorianCalendar gCal = new GregorianCalendar();
                gCal.setTime( date);
                String ymd = getYmdString( gCal);
                result = getSingleQuotedString( ymd);
            } catch ( Exception e) {
                throw new ParseException( settingInfo.getValueCell(), "値が日付ではありません：" + object.toString());
            }

        } else if ( DATA_TYPE_TIME.equals( dataType)) {
            // データ型が時間の場合

            try {
                Date date = ( Date) object;
                GregorianCalendar gCal = new GregorianCalendar();
                gCal.setTime( date);
                String time = getTimeString( gCal);
                result = getSingleQuotedString( time);
            } catch ( Exception e) {
                throw new ParseException( settingInfo.getValueCell(), "値が時間ではありません：" + object.toString());
            }

        } else if ( DATA_TYPE_TIMESTAMP.equals( dataType)) {
            // データ型がタイムスタンプの場合

            try {
                Date date = ( Date) object;
                GregorianCalendar gCal = new GregorianCalendar();
                gCal.setTime( date);
                String timestamp = getTimestampString( gCal);
                result = getSingleQuotedString( timestamp);
            } catch ( Exception e) {
                throw new ParseException( settingInfo.getValueCell(), "値がタイムスタンプではありません：" + object.toString());
            }

        } else if ( DATA_TYPE_FUNCTION.equals( dataType)) {
            // データ型が関数の場合

            // シングルクオーテーションを付けない
            result = object.toString();

        } else {
            // 該当がない場合
            throw new ParseException( settingInfo.getDataTypeCell(), "データ型不正：" + dataType);
        }

        return result;
    }

    /**
     * 日時をYYYY-MM-DD hh:mm:ss.mmm型の文字列に変換し、返却する
     * 
     * @param gCal グレゴリアンカレンダー
     * @return タイムスタンプ文字列
     */
    private String getTimestampString( GregorianCalendar gCal) {

        StringBuilder strBuild = new StringBuilder();

        strBuild.append( getYmdString( gCal));
        strBuild.append( " ");
        strBuild.append( getTimeString( gCal));

        return strBuild.toString();
    }

    /**
     * 日時をhh:mm:ss.mmm型の文字列に変換し、返却する
     * 
     * @param gCal グレゴリアンカレンダー
     * @return hh:mm:ss.mmm型の文字列
     */
    private String getTimeString( GregorianCalendar gCal) {

        StringBuilder strBuild = new StringBuilder();
        strBuild.append( getTwoDigitString( gCal.get( Calendar.HOUR_OF_DAY)));
        strBuild.append( TIME_SEPARATOR);
        strBuild.append( getTwoDigitString( gCal.get( Calendar.MINUTE)));
        strBuild.append( TIME_SEPARATOR);
        strBuild.append( getTwoDigitString( gCal.get( Calendar.SECOND)));
        strBuild.append( PERIOD);
        strBuild.append( gCal.get( Calendar.MILLISECOND));

        return strBuild.toString();
    }

    /**
     * 日時をYYYY-MM-DD型の文字列に変換し、返却する
     * 
     * @param gCal グレゴリアンカレンダー
     * @return YYYY-MM-DD型の文字列
     */
    private String getYmdString( GregorianCalendar gCal) {

        StringBuilder strBuild = new StringBuilder();
        strBuild.append( gCal.get( Calendar.YEAR));
        strBuild.append( DATE_SEPARATOR);
        strBuild.append( getTwoDigitString( gCal.get( Calendar.MONTH) + 1));
        strBuild.append( DATE_SEPARATOR);
        strBuild.append( getTwoDigitString( gCal.get( Calendar.DATE)));

        return strBuild.toString();
    }

    /**
     * 文字列にシングルクオーテーションを付与し、返却する
     * 
     * @param string 文字列
     * @return シングルクオーテーションを付与した文字列
     */
    private String getSingleQuotedString( String string) {

        StringBuilder strBuild = new StringBuilder();
        strBuild.append( SINGLE_QUOTE).append( string).append( SINGLE_QUOTE);
        return strBuild.toString();
    }

    /**
     * 文字列がBoolean型のSQLのTRUE表現か<BR>
     * どうかを判定し、結果を返す<BR>
     * 
     * @param string 文字列
     * @return TRUE表現の場合はtrue、それ以外の場合はfalse
     */
    private boolean isTrueExpression( String string) {

        boolean result = false;

        List<String> trueStrings = new ArrayList<String>();
        trueStrings.add( "true");
        trueStrings.add( "TRUE");
        trueStrings.add( "t");
        trueStrings.add( "T");
        trueStrings.add( "y");
        trueStrings.add( "Y");
        trueStrings.add( "yes");
        trueStrings.add( "YES");
        trueStrings.add( "1");

        if ( trueStrings.contains( string)) {
            result = true;
        }
        return result;
    }

    /**
     * 文字列がBoolean型のSQLのFALSE表現か<BR>
     * どうかを判定し、結果を返す<BR>
     * 
     * @param string 文字列
     * @return FALSE表現の場合はtrue、それ以外の場合はfalse
     */
    private boolean isFalseExpression( String string) {

        boolean result = false;

        List<String> falseStrings = new ArrayList<String>();
        falseStrings.add( "false");
        falseStrings.add( "FALSE");
        falseStrings.add( "f");
        falseStrings.add( "F");
        falseStrings.add( "n");
        falseStrings.add( "N");
        falseStrings.add( "no");
        falseStrings.add( "NO");
        falseStrings.add( "0");

        if ( falseStrings.contains( string)) {
            result = true;
        }
        return result;
    }

    /**
     * 数値を二桁の文字列に変換し、返却する。<BR>
     * 数値が一桁の場合はゼロ埋めを行う。<BR>
     * 
     * @param num
     * @return 二桁の数値文字列
     */
    private String getTwoDigitString( int num) {

        DecimalFormat decimalFormat = new DecimalFormat( "00");
        return decimalFormat.format( num);
    }
}
