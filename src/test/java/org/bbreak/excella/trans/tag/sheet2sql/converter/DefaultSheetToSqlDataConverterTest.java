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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlSettingInfo;
import org.junit.Test;

/**
 * DefaultSheetToSqlDataConverterテストクラス
 * 
 * @since 1.0
 */
public class DefaultSheetToSqlDataConverterTest {

    @Test
    public final void testDefaultSheetToSqlDataConverter() throws ParseException, java.text.ParseException {

        Object object = null;
        
        DefaultSheetToSqlDataConverter converter = new DefaultSheetToSqlDataConverter();
        
        SheetToSqlSettingInfo settingInfo = new SheetToSqlSettingInfo();
        // ===============================================
        // convert( Object object, String dataType)
        // ===============================================
        // No.1 データ型：文字列、値：文字列
        String dataType = "文字列";
        object = "あああ";
        String result = converter.convert( object, dataType, settingInfo);
        assertEquals( "'あああ'", result);
        
        // No.2 データ型：文字列、値：数値
        object = new Double(10.0);
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "'10.0'", result);
        
        // No.3 データ型：文字列、値：整数
        object = new Integer(10);
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "'10'", result);
        
        // No.4 データ型：文字列、値：日付
        Calendar cal = Calendar.getInstance();
        cal.setTime( DateFormat.getDateInstance().parse( "2009/06/08"));
        object = cal.getTime();
        result = converter.convert( object, dataType, settingInfo);
        Date expectedDate = cal.getTime();
        assertEquals( "'" + expectedDate.toString() + "'", result);
        
        // No.5 データ型：数値、値：数値
        dataType = "数値";
        object = new Double(10.0);
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "10.0", result);
        
        // No.6 データ型：数値、値：文字列
        object = "あああ";
        try {
            result = converter.convert( object, dataType, settingInfo);
            fail();
        } catch (ParseException pe) {
            // 例外が発生
        }
        
        // No.7 データ型：整数、値：数値
        dataType = "整数";
        object = new Integer(10);
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "10", result);
        
        // No.8 データ型：整数、値：小数
        dataType = "整数";
        object = new Double(10.0);
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "10", result);
        
        // No.9 データ型：整数、値：小数
        dataType = "整数";
        object = new Double(10.5);
        try {
            result = converter.convert( object, dataType, settingInfo);
        } catch (ParseException pe) {
            // 例外が発生
        }
        
        // No.10 データ型：数値、値：文字列
        object = "あああ";
        try {
            result = converter.convert( object, dataType, settingInfo);
            fail();
        } catch (ParseException pe) {
            // 例外が発生
        }
        
        // No.11 データ型：論理値、値：真
        dataType = "論理値";
        object = Boolean.TRUE;
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "TRUE", result);
        
        object = "true";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "TRUE", result);

        object = "TRUE";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "TRUE", result);

        object = "t";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "TRUE", result);
        
        object = "T";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "TRUE", result);
        
        object = "y";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "TRUE", result);

        object = "Y";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "TRUE", result);
        
        object = "yes";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "TRUE", result);
        
        object = "YES";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "TRUE", result);
        
        object = "1";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "TRUE", result);
        
        object = new Integer(1);
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "TRUE", result);
        
        // No.12 データ型：論理値、値：偽
        object = Boolean.FALSE;
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "FALSE", result);
        
        object = "false";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "FALSE", result);

        object = "FALSE";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "FALSE", result);

        object = "f";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "FALSE", result);
        
        object = "F";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "FALSE", result);
        
        object = "n";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "FALSE", result);

        object = "N";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "FALSE", result);
        
        object = "no";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "FALSE", result);
        
        object = "NO";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "FALSE", result);
        
        object = "0";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "FALSE", result);
        
        object = new Integer(0);
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "FALSE", result);
        
        // No.13 データ型：論理値、値：文字列
        object = "あああ";
        try {
            result = converter.convert( object, dataType, settingInfo);
            fail();
        } catch (ParseException pe) {
            // 例外が発生
        }

        // No.14 データ型：日付、値：日付
        dataType = "日付";
        object = cal.getTime();
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "'2009-06-08'", result);
        
        // No.15 データ型：日付、値：時間
        cal.set( 2009, 5, 8, 18, 1, 2);
        object = cal.getTime();
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "'2009-06-08'", result);
        
        // No.16 データ型：日付、値：文字列
        object = "あああ";
        try {
            result = converter.convert( object, dataType, settingInfo);
            fail();
        } catch (ParseException pe) {
            // 例外が発生
        }
        
        // No.17 データ型：時間、値：時間
        dataType = "時間";
        cal.set( 2009, 5, 8, 18, 1, 2);
        object = cal.getTime();
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "'18:01:02.0'", result);
        
        // No.18 データ型：時間、値：日付
        cal.setTime( DateFormat.getDateInstance().parse( "2009/06/08"));
        object = cal.getTime();
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "'00:00:00.0'", result);

        // No.19 データ型：時間、値：文字列
        object = "あああ";
        try {
            result = converter.convert( object, dataType, settingInfo);
            fail();
        } catch (ParseException pe) {
            // 例外が発生
        }
        
        // No.20 データ型：タイムスタンプ、値：時間
        dataType = "タイムスタンプ";
        cal.set( 2009, 5, 8, 18, 1, 2);
        object = cal.getTime();
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "'2009-06-08 18:01:02.0'", result);
        
        // No.21 データ型：タイムスタンプ、値：日付
        cal.setTime( DateFormat.getDateInstance().parse( "2009/06/08"));
        object = cal.getTime();
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "'2009-06-08 00:00:00.0'", result);
        
        // No.22 データ型：時間、値：文字列
        object = "あああ";
        try {
            result = converter.convert( object, dataType, settingInfo);
            fail();
        } catch (ParseException pe) {
            // 例外が発生
        }
        
        // No.23 データ型：関数、値：文字列
        dataType = "関数";
        object = "CURRENT_TIMESTAMP";
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "CURRENT_TIMESTAMP", result);
        
        // No.24 データ型：関数、値：数値
        object = new Double(10.0);
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "10.0", result);
        
        // No.25 データ型：関数、値：整数
        object = new Integer(10);
        result = converter.convert( object, dataType, settingInfo);
        assertEquals( "10", result);
        
        // No.26 データ型：関数、値：日付
        cal.setTime( DateFormat.getDateInstance().parse( "2009/06/08"));
        object = cal.getTime();
        result = converter.convert( object, dataType, settingInfo);
        expectedDate = cal.getTime();
        assertEquals( expectedDate.toString(), result);
        
        // No.27 値：null
        object = null;
        result = converter.convert( object, dataType, settingInfo);
        assertNull( result);
        
        // No.28 データ型：存在しないデータ型
        dataType = "存在しないデータ型";
        object = "あああ";
        try {
            result = converter.convert( object, dataType, settingInfo);
            fail();
        } catch (ParseException pe) {
            // 例外が発生
        }
    }
}
