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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlSettingInfo;
import org.junit.jupiter.api.Test;

/**
 * DefaultSheetToSqlDataConverterテストクラス
 * 
 * @since 1.0
 */
public class DefaultSheetToSqlDataConverterTest {

    @Test
    public final void testDefaultSheetToSqlDataConverter() throws ParseException, java.text.ParseException {

        DefaultSheetToSqlDataConverter converter = new DefaultSheetToSqlDataConverter();
        
        SheetToSqlSettingInfo settingInfo = new SheetToSqlSettingInfo();
        // ===============================================
        // convert( Object object, String dataType)
        // ===============================================
        // No.1 データ型：文字列、値：文字列
        String dataTypeString = "文字列";
        Object object1 = "あああ";
        String result = converter.convert( object1, dataTypeString, settingInfo);
        assertEquals( "'あああ'", result);
        
        // No.2 データ型：文字列、値：数値
        Object object2 = new Double(10.0);
        result = converter.convert( object2, dataTypeString, settingInfo);
        assertEquals( "'10.0'", result);
        
        // No.3 データ型：文字列、値：整数
        Object object3 = new Integer(10);
        result = converter.convert( object3, dataTypeString, settingInfo);
        assertEquals( "'10'", result);
        
        // No.4 データ型：文字列、値：日付
        Calendar cal = Calendar.getInstance();
        cal.setTime( DateFormat.getDateInstance().parse( "2009/06/08"));
        Object object4 = cal.getTime();
        result = converter.convert( object4, dataTypeString, settingInfo);
        Date expectedDate = cal.getTime();
        assertEquals( "'" + expectedDate.toString() + "'", result);
        
        // No.5 データ型：数値、値：数値
        String dataTypeNumeric = "数値";
        Object object5 = new Double(10.0);
        result = converter.convert( object5, dataTypeNumeric, settingInfo);
        assertEquals( "10.0", result);
        
        // No.6 データ型：数値、値：文字列
        Object object6 = "あああ";
        assertThrows( ParseException.class, () -> converter.convert( object6, dataTypeNumeric, settingInfo));
        
        // No.7 データ型：整数、値：数値
        String dataTypeInteger = "整数";
        Object object7 = new Integer(10);
        result = converter.convert( object7, dataTypeInteger, settingInfo);
        assertEquals( "10", result);
        
        // No.8 データ型：整数、値：小数
        Object object8 = new Double(10.0);
        result = converter.convert( object8, dataTypeInteger, settingInfo);
        assertEquals( "10", result);
        
        // No.9 データ型：整数、値：小数
        Object object9 = new Double(10.5);
        assertThrows( ParseException.class, () -> converter.convert( object9, dataTypeInteger, settingInfo));
        
        // No.10 データ型：数値、値：文字列
        Object object10 = "あああ";
        assertThrows( ParseException.class, () -> converter.convert( object10, dataTypeNumeric, settingInfo));
        
        // No.11 データ型：論理値、値：真
        String dataTypeBoolean = "論理値";
        Object object11 = Boolean.TRUE;
        result = converter.convert( object11, dataTypeBoolean, settingInfo);
        assertEquals( "TRUE", result);
        
        Object object11StrLower = "true";
        result = converter.convert( object11StrLower, dataTypeBoolean, settingInfo);
        assertEquals( "TRUE", result);

        Object object11StrUpper = "TRUE";
        result = converter.convert( object11StrUpper, dataTypeBoolean, settingInfo);
        assertEquals( "TRUE", result);

        Object object11StrT = "t";
        result = converter.convert( object11StrT, dataTypeBoolean, settingInfo);
        assertEquals( "TRUE", result);
        
        Object object11StrNum1 = "T";
        result = converter.convert( object11StrNum1, dataTypeBoolean, settingInfo);
        assertEquals( "TRUE", result);
        
        Object object11StrYLower = "y";
        result = converter.convert( object11StrYLower, dataTypeBoolean, settingInfo);
        assertEquals( "TRUE", result);

        Object object11StrYUpper = "Y";
        result = converter.convert( object11StrYUpper, dataTypeBoolean, settingInfo);
        assertEquals( "TRUE", result);
        
        Object object11StrYesLower = "yes";
        result = converter.convert( object11StrYesLower, dataTypeBoolean, settingInfo);
        assertEquals( "TRUE", result);
        
        Object object11StrYesUpper = "YES";
        result = converter.convert( object11StrYesUpper, dataTypeBoolean, settingInfo);
        assertEquals( "TRUE", result);
        
        Object object11StrNumOne = "1";
        result = converter.convert( object11StrNumOne, dataTypeBoolean, settingInfo);
        assertEquals( "TRUE", result);
        
        Object object11IntOne = new Integer(1);
        result = converter.convert( object11IntOne, dataTypeBoolean, settingInfo);
        assertEquals( "TRUE", result);
        
        // No.12 データ型：論理値、値：偽
        Object object12 = Boolean.FALSE;
        result = converter.convert( object12, dataTypeBoolean, settingInfo);
        assertEquals( "FALSE", result);
        
        Object object12StrLower = "false";
        result = converter.convert( object12StrLower, dataTypeBoolean, settingInfo);
        assertEquals( "FALSE", result);

        Object object12StrUpper = "FALSE";
        result = converter.convert( object12StrUpper, dataTypeBoolean, settingInfo);
        assertEquals( "FALSE", result);

        Object object12StrFLower = "f";
        result = converter.convert( object12StrFLower, dataTypeBoolean, settingInfo);
        assertEquals( "FALSE", result);
        
        Object object12StrFUpper = "F";
        result = converter.convert( object12StrFUpper, dataTypeBoolean, settingInfo);
        assertEquals( "FALSE", result);
        
        Object object12StrNLower = "n";
        result = converter.convert( object12StrNLower, dataTypeBoolean, settingInfo);
        assertEquals( "FALSE", result);

        Object object12StrNUpper = "N";
        result = converter.convert( object12StrNUpper, dataTypeBoolean, settingInfo);
        assertEquals( "FALSE", result);
        
        Object object12StrNoLower = "no";
        result = converter.convert( object12StrNoLower, dataTypeBoolean, settingInfo);
        assertEquals( "FALSE", result);
        
        Object object12StrNoUpper = "NO";
        result = converter.convert( object12StrNoUpper, dataTypeBoolean, settingInfo);
        assertEquals( "FALSE", result);
        
        Object object12StrZero = "0";
        result = converter.convert( object12StrZero, dataTypeBoolean, settingInfo);
        assertEquals( "FALSE", result);
        
        Object object12IntZero = new Integer(0);
        result = converter.convert( object12IntZero, dataTypeBoolean, settingInfo);
        assertEquals( "FALSE", result);
        
        // No.13 データ型：論理値、値：文字列
        Object object13 = "あああ";
        assertThrows( ParseException.class, () -> converter.convert( object13, dataTypeBoolean, settingInfo));

        // No.14 データ型：日付、値：日付
        String dataTypeDate = "日付";
        Object object14 = cal.getTime();
        result = converter.convert( object14, dataTypeDate, settingInfo);
        assertEquals( "'2009-06-08'", result);
        
        // No.15 データ型：日付、値：時間
        cal.set( 2009, 5, 8, 18, 1, 2);
        Object object15 = cal.getTime();
        result = converter.convert( object15, dataTypeDate, settingInfo);
        assertEquals( "'2009-06-08'", result);
        
        // No.16 データ型：日付、値：文字列
        Object object16 = "あああ";
        assertThrows( ParseException.class, () -> converter.convert( object16, dataTypeDate, settingInfo));
        
        // No.17 データ型：時間、値：時間
        String dataTypeTime = "時間";
        cal.set( 2009, 5, 8, 18, 1, 2);
        Object object17 = cal.getTime();
        result = converter.convert( object17, dataTypeTime, settingInfo);
        assertEquals( "'18:01:02.0'", result);
        
        // No.18 データ型：時間、値：日付
        cal.setTime( DateFormat.getDateInstance().parse( "2009/06/08"));
        Object object18 = cal.getTime();
        result = converter.convert( object18, dataTypeTime, settingInfo);
        assertEquals( "'00:00:00.0'", result);

        // No.19 データ型：時間、値：文字列
        Object object19 = "あああ";
        assertThrows( ParseException.class, () -> converter.convert( object19, dataTypeTime, settingInfo));
        
        // No.20 データ型：タイムスタンプ、値：時間
        String dataTypeTimestamp = "タイムスタンプ";
        cal.set( 2009, 5, 8, 18, 1, 2);
        Object object20 = cal.getTime();
        result = converter.convert( object20, dataTypeTimestamp, settingInfo);
        assertEquals( "'2009-06-08 18:01:02.0'", result);
        
        // No.21 データ型：タイムスタンプ、値：日付
        cal.setTime( DateFormat.getDateInstance().parse( "2009/06/08"));
        Object object21 = cal.getTime();
        result = converter.convert( object21, dataTypeTimestamp, settingInfo);
        assertEquals( "'2009-06-08 00:00:00.0'", result);
        
        // No.22 データ型：時間、値：文字列
        Object object22 = "あああ";
        assertThrows( ParseException.class, () -> converter.convert( object22, dataTypeTimestamp, settingInfo));
        
        // No.23 データ型：関数、値：文字列
        String dataTypeFunction = "関数";
        Object object23 = "CURRENT_TIMESTAMP";
        result = converter.convert( object23, dataTypeFunction, settingInfo);
        assertEquals( "CURRENT_TIMESTAMP", result);
        
        // No.24 データ型：関数、値：数値
        Object object24 = new Double(10.0);
        result = converter.convert( object24, dataTypeFunction, settingInfo);
        assertEquals( "10.0", result);
        
        // No.25 データ型：関数、値：整数
        Object object25 = new Integer(10);
        result = converter.convert( object25, dataTypeFunction, settingInfo);
        assertEquals( "10", result);
        
        // No.26 データ型：関数、値：日付
        cal.setTime( DateFormat.getDateInstance().parse( "2009/06/08"));
        Object object26 = cal.getTime();
        result = converter.convert( object26, dataTypeFunction, settingInfo);
        expectedDate = cal.getTime();
        assertEquals( expectedDate.toString(), result);
        
        // No.27 値：null
        Object object27 = null;
        result = converter.convert( object27, dataTypeFunction, settingInfo);
        assertNull( result);
        
        // No.28 データ型：存在しないデータ型
        String dataTypeUnknown = "存在しないデータ型";
        Object object28 = "あああ";
        assertThrows( ParseException.class, () -> converter.convert( object28, dataTypeUnknown, settingInfo));
    }
}
