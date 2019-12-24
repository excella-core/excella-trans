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

package org.bbreak.excella.trans.tag.sheet2sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.SheetData;
import org.bbreak.excella.core.SheetParser;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.trans.WorkbookTest;
import org.bbreak.excella.trans.tag.sheet2sql.converter.DefaultSheetToSqlDataConverter;
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlParseInfo;
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlSettingInfo;
import org.junit.Test;

/**
 * SheetToSqlExecuterテストクラス
 * 
 * @since 1.0
 */
public class SheetToSqlExecuterTest extends WorkbookTest {

    /**
     * コンストラクタ
     * 
     * @param version Excelファイルのバージョン
     */
    public SheetToSqlExecuterTest( String version) {
        super( version);
    }

    @Test
    @SuppressWarnings( "unchecked")
    public final void testSheetToSqlExecuter() throws ParseException, java.text.ParseException {

        Workbook workbook = getWorkbook();
        Sheet sheet = workbook.getSheetAt( 0);
        SheetToSqlExecuter executer = new SheetToSqlExecuter();
        
        SheetData sheetData = new SheetData( "SheetToSql");

        List<SheetToSqlParseInfo> sheet2SqlData = new ArrayList<SheetToSqlParseInfo>();
        List<SheetToSqlSettingInfo> sheet2SqlSettingData = new ArrayList<SheetToSqlSettingInfo>();

        // ===============================================
        // setDataConverter( SheetToSqlDataConverter dataConverter)
        // ===============================================
        DefaultSheetToSqlDataConverter converter = new DefaultSheetToSqlDataConverter();
        executer.setDataConverter( converter);
        
        // ===============================================
        // getDataConverter()
        // ===============================================
        assertEquals( converter, executer.getDataConverter());
        
        // ===============================================
        // postParse( Sheet sheet, SheetParser sheetParser, SheetData sheetData)
        // ===============================================
        String sheetName = "testSheet";
        String tagName = "@SheetToSql";
        String settingTagName = tagName + "Setting";

        // SheetToSqlParseInfo
        SheetToSqlParseInfo parseInfo1 = new SheetToSqlParseInfo();
        parseInfo1.setSheetName( sheetName);
        parseInfo1.setLogicalNameRowNum( 1);
        parseInfo1.setValueRowNum( 2);
        parseInfo1.setSettingTagName( settingTagName);
        sheet2SqlData.add( parseInfo1);

        // SheetToSqlSettingInfo
        SheetToSqlSettingInfo settingInfo1 = new SheetToSqlSettingInfo();
        settingInfo1.setTableName( "test_table1");
        settingInfo1.setColumnName( "col_char");
        settingInfo1.setValue( "@LNAME(文字列)");
        settingInfo1.setDataType( "文字列");
        settingInfo1.setSheetName( sheetName);
        SheetToSqlSettingInfo settingInfo2 = new SheetToSqlSettingInfo();
        settingInfo2.setTableName( "test_table1");
        settingInfo2.setColumnName( "columnName2");
        settingInfo2.setValue( 10);
        settingInfo2.setDataType( "整数");
        settingInfo2.setSheetName( sheetName);
        sheet2SqlSettingData.add( settingInfo1);
        sheet2SqlSettingData.add( settingInfo2);

        // シートデータにつめる
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        // シートパーサ
        SheetParser sheetParser = new SheetParser();
        sheetParser.addTagParser( new SheetToSqlParser());
        sheetParser.addTagParser( new SheetToSqlSettingParser());

        // No.1 postParse実行
        executer.postParse( sheet, sheetParser, sheetData);
        List<String> results = ( List<String>) sheetData.get( tagName);
        String sql1 = "insert into test_table1 (col_char,columnName2) values ('String1',10);";
        String sql2 = "insert into test_table1 (col_char,columnName2) values ('String2',10);";
        String sql3 = "insert into test_table1 (col_char,columnName2) values ('String3',10);";
        assertEquals( 3, results.size());
        assertEquals( sql1, results.get( 0));
        assertEquals( sql2, results.get( 1));
        assertEquals( sql3, results.get( 2));

        // No.2 SheetToSqlSettingInfoのデータが削除されていることを確認
        assertNull( sheetData.get( settingTagName));

        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        // No.3 第一引数にnullを指定
        try {
            executer.postParse( null, sheetParser, sheetData);
            fail();
        } catch ( NullPointerException e) {
            // 例外が発生
        }

        // No.4 第二引数にnullを指定
        try {
            executer.postParse( sheet, null, sheetData);
            fail();
        } catch ( NullPointerException e) {
            // 例外が発生
        }

        // No.5 第三引数にnullを指定
        try {
            executer.postParse( sheet, sheetParser, null);
            fail();
        } catch ( NullPointerException e) {
            // 例外が発生
        }

        // No.6 使用しないタグパーサを追加
        sheetParser.addTagParser( new SheetToSqlParser( "@UnusedSheetToSql")); /* 使用しないタグパーサ */
        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<String>) sheetData.get( tagName);
        assertEquals( 3, results.size());
        assertEquals( sql1, results.get( 0));
        assertEquals( sql2, results.get( 1));
        assertEquals( sql3, results.get( 2));

        // No.7 存在しないシート名を指定
        SheetToSqlParseInfo parseInfo2 = new SheetToSqlParseInfo();
        parseInfo2.setSheetName( "nonExistentSheet"); /* 存在しないシート名 */
        parseInfo2.setLogicalNameRowNum( 1);
        parseInfo2.setValueRowNum( 2);
        parseInfo2.setSettingTagName( settingTagName);
        sheet2SqlData.add( parseInfo2);

        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            fail();
        } catch ( ParseException pe) {
            // 例外が発生
            System.out.println( "No.7:" + pe);
        }

        // No.8 指定論理名行がnull行
        String sheetName2 = "testSheet (2)";
        SheetToSqlParseInfo parseInfo3 = new SheetToSqlParseInfo();
        parseInfo3.setSheetName( sheetName2);
        parseInfo3.setLogicalNameRowNum( 1); /* null行 */
        parseInfo3.setValueRowNum( 2);
        parseInfo3.setSettingTagName( settingTagName);
        sheet2SqlData.clear();
        sheet2SqlData.add( parseInfo3);

        SheetToSqlSettingInfo settingInfo3 = new SheetToSqlSettingInfo();
        settingInfo3.setTableName( "test_table1");
        settingInfo3.setColumnName( "columnName1");
        settingInfo3.setValue( "@LNAME(文字列)");
        settingInfo3.setDataType( "文字列");
        settingInfo3.setSheetName( sheetName2);
        sheet2SqlSettingData.clear();
        sheet2SqlSettingData.add( settingInfo3);

        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            fail();
        } catch ( ParseException pe) {
            // 例外が発生
            System.out.println( "No.8:" + pe);
        }

        // No.9 指定データ開始行がnull行
        SheetToSqlParseInfo parseInfo4 = new SheetToSqlParseInfo();
        parseInfo4.setSheetName( sheetName2);
        parseInfo4.setLogicalNameRowNum( 2);
        parseInfo4.setValueRowNum( 4); /* null行 */
        parseInfo4.setSettingTagName( settingTagName);
        sheet2SqlData.clear();
        sheet2SqlData.add( parseInfo4);

        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<String>) sheetData.get( tagName);
        assertEquals( 1, results.size());
        sql1 = "insert into test_table1 (columnName1) values ('String2');";
        assertEquals( sql1, results.get( 0));

        // No.10 データ行にnull行あり
        SheetToSqlParseInfo parseInfo5 = new SheetToSqlParseInfo();
        parseInfo5.setSheetName( sheetName2);
        parseInfo5.setLogicalNameRowNum( 2);
        parseInfo5.setValueRowNum( 3);
        parseInfo5.setSettingTagName( settingTagName);
        sheet2SqlData.clear();
        sheet2SqlData.add( parseInfo5);

        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<String>) sheetData.get( tagName);
        assertEquals( 2, results.size());
        sql1 = "insert into test_table1 (columnName1) values ('String1');";
        sql2 = "insert into test_table1 (columnName1) values ('String2');";
        assertEquals( sql1, results.get( 0));
        assertEquals( sql2, results.get( 1));

        // No.11 データ行にnullセルあり
        String sheetName3 = "testSheet (3)";
        SheetToSqlParseInfo parseInfo6 = new SheetToSqlParseInfo();
        parseInfo6.setSheetName( sheetName3);
        parseInfo6.setLogicalNameRowNum( 1);
        parseInfo6.setValueRowNum( 2);
        parseInfo6.setSettingTagName( settingTagName);
        sheet2SqlData.clear();
        sheet2SqlData.add( parseInfo6);

        SheetToSqlSettingInfo settingInfo4 = new SheetToSqlSettingInfo();
        settingInfo4.setTableName( "test_table1");
        settingInfo4.setColumnName( "columnName1");
        settingInfo4.setValue( "@LNAME(文字列)");
        settingInfo4.setDataType( "文字列");
        settingInfo4.setSheetName( sheetName3);
        sheet2SqlSettingData.clear();
        sheet2SqlSettingData.add( settingInfo4);

        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<String>) sheetData.get( tagName);
        assertEquals( 3, results.size());
        sql1 = "insert into test_table1 (columnName1) values ('String1');";
        sql2 = "insert into test_table1 (columnName1) values (null);";
        sql3 = "insert into test_table1 (columnName1) values ('String2');";
        assertEquals( sql1, results.get( 0));
        assertEquals( sql2, results.get( 1));
        assertEquals( sql3, results.get( 2));

        // No.12 論理名が文字列以外
        String sheetName4 = "testSheet (4)";
        SheetToSqlParseInfo parseInfo7 = new SheetToSqlParseInfo();
        parseInfo7.setSheetName( sheetName4);
        parseInfo7.setLogicalNameRowNum( 1);
        parseInfo7.setValueRowNum( 2);
        parseInfo7.setSettingTagName( settingTagName);
        sheet2SqlData.clear();
        sheet2SqlData.add( parseInfo7);

        SheetToSqlSettingInfo settingInfo5 = new SheetToSqlSettingInfo();
        settingInfo5.setTableName( "test_table1");
        settingInfo5.setColumnName( "propertyName3");
        settingInfo5.setValue( "@LNAME(2009/1/1)");
        settingInfo5.setDataType( "日付");
        settingInfo5.setSheetName( sheetName4);
        sheet2SqlSettingData.clear();
        sheet2SqlSettingData.add( settingInfo5);

        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 0, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.12:" + pe);
        }

        // No.13 シート名がnull
        String sheetName5 = "testSheet (5)";
        SheetToSqlParseInfo parseInfo8 = new SheetToSqlParseInfo();
        parseInfo8.setSheetName( sheetName5);
//        parseInfo8.setLogicalNameRowNum( 1);
        parseInfo8.setValueRowNum( 2);
        parseInfo8.setSettingTagName( settingTagName);
        sheet2SqlData.clear();
        sheet2SqlData.add( parseInfo8);

        SheetToSqlSettingInfo settingInfo6 = new SheetToSqlSettingInfo();
        settingInfo6.setTableName( "test_table1");
        settingInfo6.setColumnName( "propertyName1");
        settingInfo6.setValue( "@LNAME(文字列)");
        settingInfo6.setDataType( "文字列");
        settingInfo6.setSheetName( sheetName5);
        sheet2SqlSettingData.clear();
        sheet2SqlSettingData.add( settingInfo6);

        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            fail();
        } catch ( NullPointerException e) {
            // 例外が発生
        }
     
        // No.14 論理名行Noがnull
        SheetToSqlParseInfo parseInfo9 = new SheetToSqlParseInfo();
        parseInfo9.setSheetName( sheetName5);
//        parseInfo9.setLogicalNameRowNum( 1);
        parseInfo9.setValueRowNum( 2);
        parseInfo9.setSettingTagName( settingTagName);
        sheet2SqlData.clear();
        sheet2SqlData.add( parseInfo9);

        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            fail();
        } catch ( NullPointerException e) {
            // 例外が発生
        }

        // No.15 データ開始行Noがnull
        SheetToSqlParseInfo parseInfo10 = new SheetToSqlParseInfo();
        parseInfo10.setSheetName( sheetName5);
        parseInfo10.setLogicalNameRowNum( 1);
//        parseInfo10.setValueRowNum( 2);
        parseInfo10.setSettingTagName( settingTagName);
        sheet2SqlData.clear();
        sheet2SqlData.add( parseInfo10);

        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            fail();
        } catch ( NullPointerException e) {
            // 例外が発生
        }

        // No.16 Settingタグ名がnull
        SheetToSqlParseInfo parseInfo11 = new SheetToSqlParseInfo();
        parseInfo11.setSheetName( sheetName5);
        parseInfo11.setLogicalNameRowNum( 1);
        parseInfo11.setValueRowNum( 2);
//        parseInfo11.setSettingTagName( settingTagName);
        sheet2SqlData.clear();
        sheet2SqlData.add( parseInfo11);

        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            fail();
        } catch ( NullPointerException e) {
            // 例外が発生
        }
        
        // No.17 SheetToSqlParseInfoが設定されていない
        sheet2SqlData.clear();
        
        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<String>) sheetData.get( tagName);
        assertEquals( 0, results.size());
        
        // No.18 SheetToSqlSettingInfoが設定されていない
        SheetToSqlParseInfo parseInfo12 = new SheetToSqlParseInfo();
        parseInfo12.setSheetName( sheetName5);
        parseInfo12.setLogicalNameRowNum( 1);
        parseInfo12.setValueRowNum( 2);
        parseInfo12.setSettingTagName( settingTagName);
        sheet2SqlData.clear();
        sheet2SqlData.add( parseInfo12);

        sheet2SqlSettingData.clear();
        
        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<String>) sheetData.get( tagName);
        assertEquals( 0, results.size());
        
        // No.19 重複不可プロパティ
        String sheetName6 = "testSheet (6)";
        SheetToSqlParseInfo parseInfo13 = new SheetToSqlParseInfo();
        parseInfo13.setSheetName( sheetName6);
        parseInfo13.setLogicalNameRowNum( 1);
        parseInfo13.setValueRowNum( 2);
        parseInfo13.setSettingTagName( settingTagName);
        sheet2SqlData.clear();
        sheet2SqlData.add( parseInfo13);
        
        SheetToSqlSettingInfo settingInfo7 = new SheetToSqlSettingInfo();
        settingInfo7.setTableName( "test_table1");
        settingInfo7.setColumnName( "columnName1");
        settingInfo7.setValue( "@LNAME(文字列)");
        settingInfo7.setDataType( "文字列");
        settingInfo7.setSheetName( sheetName6);
        settingInfo7.setUnique( true); /* 重複不可 */
        SheetToSqlSettingInfo settingInfo8 = new SheetToSqlSettingInfo();
        settingInfo8.setTableName( "test_table1");
        settingInfo8.setColumnName( "columnName2");
        settingInfo8.setValue( "@LNAME(整数)");
        settingInfo8.setDataType( "整数");
        settingInfo8.setUnique( true); /* 重複不可 */
        settingInfo8.setSheetName( sheetName6);
        SheetToSqlSettingInfo settingInfo9 = new SheetToSqlSettingInfo();
        settingInfo9.setTableName( "test_table1");
        settingInfo9.setColumnName( "columnName3");
        settingInfo9.setValue( "@LNAME(日付)");
        settingInfo9.setDataType( "日付");
        settingInfo9.setUnique( false); /* 重複可 */
        settingInfo9.setSheetName( sheetName6);
        sheet2SqlSettingData.clear();
        sheet2SqlSettingData.add( settingInfo7);
        sheet2SqlSettingData.add( settingInfo8);
        sheet2SqlSettingData.add( settingInfo9);

        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);
        
        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<String>) sheetData.get( tagName);
        assertEquals( 3, results.size());
        sql1 = "insert into test_table1 (columnName1,columnName2,columnName3) values ('String1',100,'2009-01-01');";
        sql2 = "insert into test_table1 (columnName1,columnName2,columnName3) values ('String2',null,'2009-03-01');";
        sql3 = "insert into test_table1 (columnName1,columnName2,columnName3) values ('String3',300,'2009-04-01');";
        assertEquals( sql1, results.get( 0));
        assertEquals( sql2, results.get( 1));
        assertEquals( sql3, results.get( 2));
        
        // No.20 複数エンティティ
        // test_table2の設定を追加する
        SheetToSqlSettingInfo settingInfo10 = new SheetToSqlSettingInfo();
        settingInfo10.setTableName( "test_table2");
        settingInfo10.setColumnName( "columnName1");
        settingInfo10.setValue( "@LNAME(文字列)");
        settingInfo10.setDataType( "文字列");
        settingInfo10.setSheetName( sheetName6);
        settingInfo10.setUnique( true); /* 重複不可 */
        SheetToSqlSettingInfo settingInfo11 = new SheetToSqlSettingInfo();
        settingInfo11.setTableName( "test_table2");
        settingInfo11.setColumnName( "columnName2");
        settingInfo11.setValue( "@LNAME(整数)");
        settingInfo11.setDataType( "整数");
        settingInfo11.setUnique( false); /* 重複可 */
        settingInfo11.setSheetName( sheetName6);
        SheetToSqlSettingInfo settingInfo12 = new SheetToSqlSettingInfo();
        settingInfo12.setTableName( "test_table2");
        settingInfo12.setColumnName( "columnName3");
        settingInfo12.setValue( "@LNAME(日付)");
        settingInfo12.setDataType( "日付");
        settingInfo12.setUnique( true); /* 重複不可 */
        settingInfo12.setSheetName( sheetName6);

        // settingInfo10, 11, 12を追加
        sheet2SqlSettingData.add( settingInfo10);
        sheet2SqlSettingData.add( settingInfo11);
        sheet2SqlSettingData.add( settingInfo12);
    
        // シートデータ作成
        sheetData = new SheetData( "SheetToSql");
        sheetData.put( tagName, sheet2SqlData);
        sheetData.put( settingTagName, sheet2SqlSettingData);
        
        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<String>) sheetData.get( tagName);
        assertEquals( 7, results.size());
        sql1 = "insert into test_table1 (columnName1,columnName2,columnName3) values ('String1',100,'2009-01-01');";
        sql2 = "insert into test_table1 (columnName1,columnName2,columnName3) values ('String2',null,'2009-03-01');";
        sql3 = "insert into test_table1 (columnName1,columnName2,columnName3) values ('String3',300,'2009-04-01');";
        String sql4 = "insert into test_table2 (columnName1,columnName2,columnName3) values ('String1',100,'2009-01-01');";
        String sql5 = "insert into test_table2 (columnName1,columnName2,columnName3) values ('String1',100,'2009-02-01');";
        String sql6 = "insert into test_table2 (columnName1,columnName2,columnName3) values ('String2',null,'2009-03-01');";
        String sql7 = "insert into test_table2 (columnName1,columnName2,columnName3) values ('String3',300,'2009-04-01');";
        assertEquals( sql1, results.get( 0));
        assertEquals( sql2, results.get( 1));
        assertEquals( sql3, results.get( 2));
        assertEquals( sql4, results.get( 3));
        assertEquals( sql5, results.get( 4));
        assertEquals( sql6, results.get( 5));
        assertEquals( sql7, results.get( 6));
    }
}
