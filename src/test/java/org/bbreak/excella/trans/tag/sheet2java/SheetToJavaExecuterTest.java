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

package org.bbreak.excella.trans.tag.sheet2java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.SheetData;
import org.bbreak.excella.core.SheetParser;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.trans.WorkbookTest;
import org.bbreak.excella.trans.tag.sheet2java.entity.TestEntity1;
import org.bbreak.excella.trans.tag.sheet2java.entity.TestEntity2;
import org.bbreak.excella.trans.tag.sheet2java.model.SheetToJavaParseInfo;
import org.bbreak.excella.trans.tag.sheet2java.model.SheetToJavaSettingInfo;
import org.junit.Test;

/**
 * SheetToJavaExecuterテストクラス
 * 
 * @since 1.0
 */
public class SheetToJavaExecuterTest extends WorkbookTest {

    /**
     * コンストラクタ
     * 
     * @param version Excelファイルのバージョン
     */
    public SheetToJavaExecuterTest( String version) {
        super( version);
    }

    @Test
    @SuppressWarnings( "unchecked")
    public final void testSheetToJavaExecuter() throws ParseException, java.text.ParseException {

        Workbook workbook = getWorkbook();
        Sheet sheet = workbook.getSheetAt( 0);
        SheetToJavaExecuter executer = new SheetToJavaExecuter();
        SheetData sheetData = new SheetData( "SheetToJava");

        List<SheetToJavaParseInfo> sheet2JavaData = new ArrayList<SheetToJavaParseInfo>();
        List<SheetToJavaSettingInfo> sheet2JavaSettingData = new ArrayList<SheetToJavaSettingInfo>();

        // ===============================================
        // postParse( Sheet sheet, SheetParser sheetParser, SheetData sheetData)
        // ===============================================
        String sheetName = "testSheet";
        String tagName = "@SheetToJava";
        String settingTagName = tagName + "Setting";

        // SheetToJavaParseInfo
        SheetToJavaParseInfo parseInfo1 = new SheetToJavaParseInfo();
        parseInfo1.setSheetName( sheetName);
        parseInfo1.setLogicalNameRowNum( 1);
        parseInfo1.setValueRowNum( 2);
        parseInfo1.setSettingTagName( settingTagName);
        sheet2JavaData.add( parseInfo1);

        // SheetToJavaSettingInfo
        SheetToJavaSettingInfo settingInfo1 = new SheetToJavaSettingInfo();
        settingInfo1.setClazz( TestEntity1.class);
        settingInfo1.setPropertyName( "propertyStr1");
        settingInfo1.setValue( "@LNAME(文字列)");
        settingInfo1.setSheetName( sheetName);
        SheetToJavaSettingInfo settingInfo2 = new SheetToJavaSettingInfo();
        settingInfo2.setClazz( TestEntity1.class);
        settingInfo2.setPropertyName( "propertyInt1");
        settingInfo2.setValue( 10);
        settingInfo2.setSheetName( sheetName);
        sheet2JavaSettingData.add( settingInfo1);
        sheet2JavaSettingData.add( settingInfo2);

        // シートデータにつめる
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        // シートパーサ
        SheetParser sheetParser = new SheetParser();
        sheetParser.addTagParser( new SheetToJavaParser());
        sheetParser.addTagParser( new SheetToJavaSettingParser());

        // No.1 postParse実行
        executer.postParse( sheet, sheetParser, sheetData);
        List<Object> results = ( List<Object>) sheetData.get( tagName);
        assertEquals( 3, results.size());
        assertEquals( "String1", (( TestEntity1) results.get( 0)).getPropertyStr1());
        assertEquals( "String2", (( TestEntity1) results.get( 1)).getPropertyStr1());
        assertEquals( "String3", (( TestEntity1) results.get( 2)).getPropertyStr1());
        assertEquals( new Integer( 10), (( TestEntity1) results.get( 0)).getPropertyInt1());
        assertEquals( new Integer( 10), (( TestEntity1) results.get( 1)).getPropertyInt1());
        assertEquals( new Integer( 10), (( TestEntity1) results.get( 2)).getPropertyInt1());

        // No.2 SheetToJavaSettingInfoのデータが削除されていることを確認
        assertNull( sheetData.get( settingTagName));

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

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
        sheetParser.addTagParser( new SheetToJavaParser( "@UnusedSheetToJava")); /* 使用しないタグパーサ */
        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<Object>) sheetData.get( tagName);
        assertEquals( 3, results.size());
        assertEquals( "String1", (( TestEntity1) results.get( 0)).getPropertyStr1());
        assertEquals( "String2", (( TestEntity1) results.get( 1)).getPropertyStr1());
        assertEquals( "String3", (( TestEntity1) results.get( 2)).getPropertyStr1());
        assertEquals( new Integer( 10), (( TestEntity1) results.get( 0)).getPropertyInt1());
        assertEquals( new Integer( 10), (( TestEntity1) results.get( 1)).getPropertyInt1());
        assertEquals( new Integer( 10), (( TestEntity1) results.get( 2)).getPropertyInt1());

        // No.7 存在しないシート名を指定
        SheetToJavaParseInfo parseInfo2 = new SheetToJavaParseInfo();
        parseInfo2.setSheetName( "nonExistentSheet"); /* 存在しないシート名 */
        parseInfo2.setLogicalNameRowNum( 1);
        parseInfo2.setValueRowNum( 2);
        parseInfo2.setSettingTagName( settingTagName);
        sheet2JavaData.add( parseInfo2);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            fail();
        } catch ( ParseException pe) {
            // 例外が発生
            System.out.println( "No.7:" + pe);
        }

        // No.8 指定論理名行がnull行
        String sheetName2 = "testSheet (2)";
        SheetToJavaParseInfo parseInfo3 = new SheetToJavaParseInfo();
        parseInfo3.setSheetName( sheetName2);
        parseInfo3.setLogicalNameRowNum( 1); /* null行 */
        parseInfo3.setValueRowNum( 2);
        parseInfo3.setSettingTagName( settingTagName);
        sheet2JavaData.clear();
        sheet2JavaData.add( parseInfo3);

        SheetToJavaSettingInfo settingInfo3 = new SheetToJavaSettingInfo();
        settingInfo3.setClazz( TestEntity1.class);
        settingInfo3.setPropertyName( "propertyInt1");
        settingInfo3.setValue( "@LNAME(整数)");
        settingInfo3.setSheetName( sheetName2);
        sheet2JavaSettingData.clear();
        sheet2JavaSettingData.add( settingInfo3);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            fail();
        } catch ( ParseException pe) {
            // 例外が発生
            System.out.println( "No.8:" + pe);
        }

        // No.9 指定データ開始行がnull行
        SheetToJavaParseInfo parseInfo4 = new SheetToJavaParseInfo();
        parseInfo4.setSheetName( sheetName2);
        parseInfo4.setLogicalNameRowNum( 2);
        parseInfo4.setValueRowNum( 4); /* null行 */
        parseInfo4.setSettingTagName( settingTagName);
        sheet2JavaData.clear();
        sheet2JavaData.add( parseInfo4);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<Object>) sheetData.get( tagName);
        assertEquals( 1, results.size());
        assertEquals( new Integer( 20), (( TestEntity1) results.get( 0)).getPropertyInt1());

        // No.10 データ行にnull行あり
        SheetToJavaParseInfo parseInfo5 = new SheetToJavaParseInfo();
        parseInfo5.setSheetName( sheetName2);
        parseInfo5.setLogicalNameRowNum( 2);
        parseInfo5.setValueRowNum( 3);
        parseInfo5.setSettingTagName( settingTagName);
        sheet2JavaData.clear();
        sheet2JavaData.add( parseInfo5);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<Object>) sheetData.get( tagName);
        assertEquals( 2, results.size());
        assertEquals( new Integer( 10), (( TestEntity1) results.get( 0)).getPropertyInt1());
        assertEquals( new Integer( 20), (( TestEntity1) results.get( 1)).getPropertyInt1());

        // No.11 データ行にnullセルあり
        String sheetName3 = "testSheet (3)";
        SheetToJavaParseInfo parseInfo6 = new SheetToJavaParseInfo();
        parseInfo6.setSheetName( sheetName3);
        parseInfo6.setLogicalNameRowNum( 1);
        parseInfo6.setValueRowNum( 2);
        parseInfo6.setSettingTagName( settingTagName);
        sheet2JavaData.clear();
        sheet2JavaData.add( parseInfo6);

        SheetToJavaSettingInfo settingInfo4 = new SheetToJavaSettingInfo();
        settingInfo4.setClazz( TestEntity1.class);
        settingInfo4.setPropertyName( "propertyDate1");
        settingInfo4.setValue( "@LNAME(日付)");
        settingInfo4.setSheetName( sheetName3);
        sheet2JavaSettingData.clear();
        sheet2JavaSettingData.add( settingInfo4);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            results.clear();
            results = ( List<Object>) sheetData.get( tagName);
            assertEquals( 3, results.size());
            assertEquals( DateFormat.getDateInstance().parse( "2009/1/1"), (( TestEntity1) results.get( 0)).getPropertyDate1());
            assertNull( (( TestEntity1) results.get( 1)).getPropertyDate1());
            assertEquals( DateFormat.getDateInstance().parse( "2009/2/1"), (( TestEntity1) results.get( 2)).getPropertyDate1());
        } catch ( RuntimeException e1) {
            e1.printStackTrace();
        }

        // No.12 論理名が文字列以外
        String sheetName4 = "testSheet (4)";
        SheetToJavaParseInfo parseInfo7 = new SheetToJavaParseInfo();
        parseInfo7.setSheetName( sheetName4);
        parseInfo7.setLogicalNameRowNum( 1);
        parseInfo7.setValueRowNum( 2);
        parseInfo7.setSettingTagName( settingTagName);
        sheet2JavaData.clear();
        sheet2JavaData.add( parseInfo7);

        SheetToJavaSettingInfo settingInfo5 = new SheetToJavaSettingInfo();
        settingInfo5.setClazz( TestEntity1.class);
        settingInfo5.setPropertyName( "propertyDate1");
        settingInfo5.setValue( "@LNAME(2009/1/1)");
        settingInfo5.setSheetName( sheetName4);
        sheet2JavaSettingData.clear();
        sheet2JavaSettingData.add( settingInfo5);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

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
        SheetToJavaParseInfo parseInfo8 = new SheetToJavaParseInfo();
        parseInfo8.setSheetName( sheetName5);
        // parseInfo8.setLogicalNameRowNum( 1);
        parseInfo8.setValueRowNum( 2);
        parseInfo8.setSettingTagName( settingTagName);
        sheet2JavaData.clear();
        sheet2JavaData.add( parseInfo8);

        SheetToJavaSettingInfo settingInfo6 = new SheetToJavaSettingInfo();
        settingInfo6.setClazz( TestEntity1.class);
        settingInfo6.setPropertyName( "propertyStr1");
        settingInfo6.setValue( "@LNAME(文字列)");
        settingInfo6.setSheetName( sheetName5);
        sheet2JavaSettingData.clear();
        sheet2JavaSettingData.add( settingInfo6);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            fail();
        } catch ( NullPointerException e) {
            // 例外が発生
        }

        // No.14 論理名行Noがnull
        SheetToJavaParseInfo parseInfo9 = new SheetToJavaParseInfo();
        parseInfo9.setSheetName( sheetName5);
        // parseInfo9.setLogicalNameRowNum( 1);
        parseInfo9.setValueRowNum( 2);
        parseInfo9.setSettingTagName( settingTagName);
        sheet2JavaData.clear();
        sheet2JavaData.add( parseInfo9);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            fail();
        } catch ( NullPointerException e) {
            // 例外が発生
        }

        // No.15 データ開始行Noがnull
        SheetToJavaParseInfo parseInfo10 = new SheetToJavaParseInfo();
        parseInfo10.setSheetName( sheetName5);
        parseInfo10.setLogicalNameRowNum( 1);
        // parseInfo10.setValueRowNum( 2);
        parseInfo10.setSettingTagName( settingTagName);
        sheet2JavaData.clear();
        sheet2JavaData.add( parseInfo10);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            fail();
        } catch ( NullPointerException e) {
            // 例外が発生
        }

        // No.16 Settingタグ名がnull
        SheetToJavaParseInfo parseInfo11 = new SheetToJavaParseInfo();
        parseInfo11.setSheetName( sheetName5);
        parseInfo11.setLogicalNameRowNum( 1);
        parseInfo11.setValueRowNum( 2);
        // parseInfo11.setSettingTagName( settingTagName);
        sheet2JavaData.clear();
        sheet2JavaData.add( parseInfo11);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        try {
            executer.postParse( sheet, sheetParser, sheetData);
            fail();
        } catch ( NullPointerException e) {
            // 例外が発生
        }

        // No.17 SheetToJavaParseInfoが設定されていない
        sheet2JavaData.clear();

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<Object>) sheetData.get( tagName);
        assertEquals( 0, results.size());

        // No.18 SheetToJavaSettingInfoが設定されていない
        SheetToJavaParseInfo parseInfo12 = new SheetToJavaParseInfo();
        parseInfo12.setSheetName( sheetName5);
        parseInfo12.setLogicalNameRowNum( 1);
        parseInfo12.setValueRowNum( 2);
        parseInfo12.setSettingTagName( settingTagName);
        sheet2JavaData.clear();
        sheet2JavaData.add( parseInfo12);

        sheet2JavaSettingData.clear();

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<Object>) sheetData.get( tagName);
        assertEquals( 0, results.size());

        // No.19 重複不可プロパティ
        String sheetName6 = "testSheet (6)";
        SheetToJavaParseInfo parseInfo13 = new SheetToJavaParseInfo();
        parseInfo13.setSheetName( sheetName6);
        parseInfo13.setLogicalNameRowNum( 1);
        parseInfo13.setValueRowNum( 2);
        parseInfo13.setSettingTagName( settingTagName);
        sheet2JavaData.clear();
        sheet2JavaData.add( parseInfo13);

        SheetToJavaSettingInfo settingInfo7 = new SheetToJavaSettingInfo();
        settingInfo7.setClazz( TestEntity1.class);
        settingInfo7.setPropertyName( "propertyStr1");
        settingInfo7.setValue( "@LNAME(文字列)");
        settingInfo7.setSheetName( sheetName6);
        settingInfo7.setUnique( true); /* 重複不可 */
        SheetToJavaSettingInfo settingInfo8 = new SheetToJavaSettingInfo();
        settingInfo8.setClazz( TestEntity1.class);
        settingInfo8.setPropertyName( "propertyInt1");
        settingInfo8.setValue( "@LNAME(整数)");
        settingInfo8.setUnique( true); /* 重複不可 */
        settingInfo8.setSheetName( sheetName6);
        SheetToJavaSettingInfo settingInfo9 = new SheetToJavaSettingInfo();
        settingInfo9.setClazz( TestEntity1.class);
        settingInfo9.setPropertyName( "propertyDate1");
        settingInfo9.setValue( "@LNAME(日付)");
        settingInfo9.setUnique( false); /* 重複可 */
        settingInfo9.setSheetName( sheetName6);

        sheet2JavaSettingData.clear();
        sheet2JavaSettingData.add( settingInfo7);
        sheet2JavaSettingData.add( settingInfo8);
        sheet2JavaSettingData.add( settingInfo9);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<Object>) sheetData.get( tagName);
        assertEquals( 3, results.size());
        assertEquals( "String1", (( TestEntity1) results.get( 0)).getPropertyStr1());
        assertEquals( "String2", (( TestEntity1) results.get( 1)).getPropertyStr1());
        assertEquals( "String3", (( TestEntity1) results.get( 2)).getPropertyStr1());
        assertEquals( new Integer( 100), (( TestEntity1) results.get( 0)).getPropertyInt1());
        assertNull( (( TestEntity1) results.get( 1)).getPropertyInt1());
        assertEquals( new Integer( 300), (( TestEntity1) results.get( 2)).getPropertyInt1());
        assertEquals( DateFormat.getDateInstance().parse( "2009/1/1"), (( TestEntity1) results.get( 0)).getPropertyDate1());
        assertEquals( DateFormat.getDateInstance().parse( "2009/3/1"), (( TestEntity1) results.get( 1)).getPropertyDate1());
        assertEquals( DateFormat.getDateInstance().parse( "2009/4/1"), (( TestEntity1) results.get( 2)).getPropertyDate1());

        // No.20 複数エンティティ
        // TestEntity2の設定を追加する
        SheetToJavaSettingInfo settingInfo10 = new SheetToJavaSettingInfo();
        settingInfo10.setClazz( TestEntity2.class);
        settingInfo10.setPropertyName( "propertyStr2");
        settingInfo10.setValue( "@LNAME(文字列)");
        settingInfo10.setSheetName( sheetName6);
        settingInfo10.setUnique( true); /* 重複不可 */
        SheetToJavaSettingInfo settingInfo11 = new SheetToJavaSettingInfo();
        settingInfo11.setClazz( TestEntity2.class);
        settingInfo11.setPropertyName( "propertyInt2");
        settingInfo11.setValue( "@LNAME(整数)");
        settingInfo11.setUnique( false); /* 重複可 */
        settingInfo11.setSheetName( sheetName6);
        SheetToJavaSettingInfo settingInfo12 = new SheetToJavaSettingInfo();
        settingInfo12.setClazz( TestEntity2.class);
        settingInfo12.setPropertyName( "propertyDate2");
        settingInfo12.setValue( "@LNAME(日付)");
        settingInfo12.setUnique( true); /* 重複不可 */
        settingInfo12.setSheetName( sheetName6);

        // settingInfo10, 11, 12を追加
        sheet2JavaSettingData.add( settingInfo10);
        sheet2JavaSettingData.add( settingInfo11);
        sheet2JavaSettingData.add( settingInfo12);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<Object>) sheetData.get( tagName);
        assertEquals( 7, results.size());
        assertEquals( "String1", (( TestEntity1) results.get( 0)).getPropertyStr1());
        assertEquals( "String2", (( TestEntity1) results.get( 1)).getPropertyStr1());
        assertEquals( "String3", (( TestEntity1) results.get( 2)).getPropertyStr1());
        assertEquals( "String1", (( TestEntity2) results.get( 3)).getPropertyStr2());
        assertEquals( "String1", (( TestEntity2) results.get( 4)).getPropertyStr2());
        assertEquals( "String2", (( TestEntity2) results.get( 5)).getPropertyStr2());
        assertEquals( "String3", (( TestEntity2) results.get( 6)).getPropertyStr2());
        assertEquals( new Integer( 100), (( TestEntity1) results.get( 0)).getPropertyInt1());
        assertNull( (( TestEntity1) results.get( 1)).getPropertyInt1());
        assertEquals( new Integer( 300), (( TestEntity1) results.get( 2)).getPropertyInt1());
        assertEquals( new Integer( 100), (( TestEntity2) results.get( 3)).getPropertyInt2());
        assertEquals( new Integer( 100), (( TestEntity2) results.get( 4)).getPropertyInt2());
        assertNull( (( TestEntity2) results.get( 5)).getPropertyInt2());
        assertEquals( new Integer( 300), (( TestEntity2) results.get( 6)).getPropertyInt2());
        assertEquals( DateFormat.getDateInstance().parse( "2009/1/1"), (( TestEntity1) results.get( 0)).getPropertyDate1());
        assertEquals( DateFormat.getDateInstance().parse( "2009/3/1"), (( TestEntity1) results.get( 1)).getPropertyDate1());
        assertEquals( DateFormat.getDateInstance().parse( "2009/4/1"), (( TestEntity1) results.get( 2)).getPropertyDate1());
        assertEquals( DateFormat.getDateInstance().parse( "2009/1/1"), (( TestEntity2) results.get( 3)).getPropertyDate2());
        assertEquals( DateFormat.getDateInstance().parse( "2009/2/1"), (( TestEntity2) results.get( 4)).getPropertyDate2());
        assertEquals( DateFormat.getDateInstance().parse( "2009/3/1"), (( TestEntity2) results.get( 5)).getPropertyDate2());
        assertEquals( DateFormat.getDateInstance().parse( "2009/4/1"), (( TestEntity2) results.get( 6)).getPropertyDate2());

        // ===============================================
        // addPropertyParser( SheetToJavaPropertyParser parser)
        // ===============================================
        // No.21 カスタムプロパティパーサ追加
        executer.addPropertyParser( new TestChildEntityParser());
        String sheetName7 = "testSheet (7)";
        SheetToJavaParseInfo parseInfo14 = new SheetToJavaParseInfo();
        parseInfo14.setSheetName( sheetName7);
        parseInfo14.setLogicalNameRowNum( 1);
        parseInfo14.setValueRowNum( 2);
        parseInfo14.setSettingTagName( settingTagName);
        sheet2JavaData.clear();
        sheet2JavaData.add( parseInfo14);

        SheetToJavaSettingInfo settingInfo13 = new SheetToJavaSettingInfo();
        settingInfo13.setClazz( TestEntity1.class);
        settingInfo13.setPropertyName( "propertyStr1");
        settingInfo13.setValue( "@LNAME(文字列)");
        settingInfo13.setSheetName( sheetName7);
        settingInfo13.setUnique( true); /* 重複不可 */
        SheetToJavaSettingInfo settingInfo14 = new SheetToJavaSettingInfo();
        settingInfo14.setClazz( TestEntity1.class);
        settingInfo14.setPropertyName( "child");
        settingInfo14.setValue( "@TestChildEntity{childPropertyStr1=子文字列1}");
        settingInfo14.setSheetName( sheetName7);

        sheet2JavaSettingData.clear();
        sheet2JavaSettingData.add( settingInfo13);
        sheet2JavaSettingData.add( settingInfo14);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<Object>) sheetData.get( tagName);
        assertEquals( 3, results.size());
        assertEquals( "String1", (( TestEntity1) results.get( 0)).getPropertyStr1());
        assertEquals( "String2", (( TestEntity1) results.get( 1)).getPropertyStr1());
        assertEquals( "String3", (( TestEntity1) results.get( 2)).getPropertyStr1());
        assertEquals( "子文字列1", (( TestEntity1) results.get( 0)).getChild().getChildPropertyStr1());
        assertEquals( "子文字列1", (( TestEntity1) results.get( 1)).getChild().getChildPropertyStr1());
        assertEquals( "子文字列1", (( TestEntity1) results.get( 2)).getChild().getChildPropertyStr1());

        // No.22 カスタムプロパティパーサで論理名を使用
        SheetToJavaSettingInfo settingInfo15 = new SheetToJavaSettingInfo();
        settingInfo15.setClazz( TestEntity1.class);
        settingInfo15.setPropertyName( "child");
        settingInfo15.setValue( "@TestChildEntity{childPropertyStr1=@LNAME(子文字列)}");
        settingInfo15.setSheetName( sheetName7);
        sheet2JavaSettingData.clear();
        sheet2JavaSettingData.add( settingInfo13);
        sheet2JavaSettingData.add( settingInfo15);

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<Object>) sheetData.get( tagName);
        assertEquals( 3, results.size());
        assertEquals( "String1", (( TestEntity1) results.get( 0)).getPropertyStr1());
        assertEquals( "String2", (( TestEntity1) results.get( 1)).getPropertyStr1());
        assertEquals( "String3", (( TestEntity1) results.get( 2)).getPropertyStr1());
        assertEquals( "ChildString1", (( TestEntity1) results.get( 0)).getChild().getChildPropertyStr1());
        assertEquals( "ChildString3", (( TestEntity1) results.get( 1)).getChild().getChildPropertyStr1());
        assertNull( (( TestEntity1) results.get( 2)).getChild().getChildPropertyStr1());

        // ===============================================
        // clearPropertyParsers()
        // ===============================================
        // No.23 カスタムプロパティパーサクリア
        executer.clearPropertyParsers();

        // シートデータ作成
        sheetData = new SheetData( "SheetToJava");
        sheetData.put( tagName, sheet2JavaData);
        sheetData.put( settingTagName, sheet2JavaSettingData);

        executer.postParse( sheet, sheetParser, sheetData);
        results.clear();
        results = ( List<Object>) sheetData.get( tagName);
        assertEquals( 3, results.size());
        assertEquals( "String1", (( TestEntity1) results.get( 0)).getPropertyStr1());
        assertEquals( "String2", (( TestEntity1) results.get( 1)).getPropertyStr1());
        assertEquals( "String3", (( TestEntity1) results.get( 2)).getPropertyStr1());
        assertNull( (( TestEntity1) results.get( 0)).getChild());
        assertNull( (( TestEntity1) results.get( 1)).getChild());
        assertNull( (( TestEntity1) results.get( 2)).getChild());
    }
}
