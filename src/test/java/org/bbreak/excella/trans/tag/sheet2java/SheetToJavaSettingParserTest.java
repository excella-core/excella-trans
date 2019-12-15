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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.trans.WorkbookTest;
import org.bbreak.excella.trans.tag.sheet2java.entity.TestEntity1;
import org.bbreak.excella.trans.tag.sheet2java.entity.TestEntity2;
import org.bbreak.excella.trans.tag.sheet2java.model.SheetToJavaSettingInfo;
import org.junit.Test;

/**
 * SheetToJavaSettingParserテストクラス
 * 
 * @since 1.0
 */
public class SheetToJavaSettingParserTest extends WorkbookTest {

    /**
     * コンストラクタ
     * 
     * @param version Excelファイルのバージョン
     */
    public SheetToJavaSettingParserTest( String version) {
        super( version);
    }

    @Test
    public final void testSheetToJavaSettingParser() throws ParseException {
        Workbook workbook = getWorkbook();
        Sheet sheet1 = workbook.getSheetAt( 0);
        Sheet sheet2 = workbook.getSheetAt( 1);
        Sheet sheet3 = workbook.getSheetAt( 2);
        Sheet sheet4 = workbook.getSheetAt( 3);
        Sheet sheet5 = workbook.getSheetAt( 4);
        String tag = "@SheetToJavaSetting";
        SheetToJavaSettingParser parser = new SheetToJavaSettingParser( tag);
        Cell tagCell = null;
        Object data = null;
        List<SheetToJavaSettingInfo> list = null;

        // ===============================================
        // parse( Sheet sheet, Cell tagCell, Object data)
        // ===============================================
        // No.1 パラメータ無し
        tagCell = sheet1.getRow( 0).getCell( 0);
        list = parser.parse( sheet1, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName2", list.get( 0).getSheetName());
        assertEquals( "sheetName3", list.get( 1).getSheetName());
        assertEquals( 2, list.get( 0).getValue());
        assertEquals( "value3", list.get( 1).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( TestEntity2.class, list.get( 1).getClazz());
        assertEquals( "propertyInt1", list.get( 0).getPropertyName());
        assertEquals( "propertyStr2", list.get( 1).getPropertyName());
        assertTrue( list.get( 0).isUnique());
        assertTrue( list.get( 1).isUnique());

        // No.2 パラメータ有り
        tagCell = sheet1.getRow( 0).getCell( 6);
        list.clear();
        list = parser.parse( sheet1, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "value4", list.get( 0).getValue());
        assertEquals( 5, list.get( 1).getValue());
        assertEquals( TestEntity2.class, list.get( 0).getClazz());
        assertEquals( TestEntity2.class, list.get( 1).getClazz());
        assertEquals( "propertyStr2", list.get( 0).getPropertyName());
        assertEquals( "propertyInt2", list.get( 1).getPropertyName());
        assertTrue( list.get( 0).isUnique());
        assertFalse( list.get( 1).isUnique());

        // No.3 シート名セルがnull
        tagCell = sheet2.getRow( 0).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( 2, list.get( 0).getValue());
        assertEquals( "value3", list.get( 1).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( TestEntity2.class, list.get( 1).getClazz());
        assertEquals( "propertyInt1", list.get( 0).getPropertyName());
        assertEquals( "propertyStr2", list.get( 1).getPropertyName());
        assertTrue( list.get( 0).isUnique());
        assertTrue( list.get( 1).isUnique());
        
        // No.4 値セルがnull
        tagCell = sheet2.getRow( 8).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 3, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "sheetName3", list.get( 2).getSheetName());
        assertEquals( "value4", list.get( 0).getValue());
        assertNull( list.get( 1).getValue());
        assertEquals( "value5", list.get( 2).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( TestEntity1.class, list.get( 1).getClazz());
        assertEquals( TestEntity2.class, list.get( 2).getClazz());
        assertEquals( "propertyStr1", list.get( 0).getPropertyName());
        assertEquals( "propertyInt1", list.get( 1).getPropertyName());
        assertEquals( "propertyStr2", list.get( 2).getPropertyName());
        assertTrue( list.get( 0).isUnique());
        assertTrue( list.get( 1).isUnique());
        assertTrue( list.get( 2).isUnique());

        // No.5 対象エンティティセルがnull
        tagCell = sheet2.getRow( 16).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 20, cell.getRow().getRowNum());
            assertEquals( 2, cell.getColumnIndex());
            System.out.println( "No.5:" + pe);
        }

        // No.6 対象プロパティセルがnull
        tagCell = sheet2.getRow( 24).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 26, cell.getRow().getRowNum());
            assertEquals( 3, cell.getColumnIndex());
            System.out.println( "No.6:" + pe);
        }

        // No.7 重複不可セルがnull
        tagCell = sheet2.getRow( 32).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 3, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "sheetName3", list.get( 2).getSheetName());
        assertEquals( "value12", list.get( 0).getValue());
        assertEquals( 13, list.get( 1).getValue());
        assertEquals( "value14", list.get( 2).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( TestEntity1.class, list.get( 1).getClazz());
        assertEquals( TestEntity2.class, list.get( 2).getClazz());
        assertEquals( "propertyStr1", list.get( 0).getPropertyName());
        assertEquals( "propertyInt1", list.get( 1).getPropertyName());
        assertEquals( "propertyStr2", list.get( 2).getPropertyName());
        assertTrue( list.get( 0).isUnique());
        assertFalse( list.get( 1).isUnique());
        assertTrue( list.get( 2).isUnique());

        // No.8 行がnull
        tagCell = sheet2.getRow( 40).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "value15", list.get( 0).getValue());
        assertEquals( "value16", list.get( 1).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( TestEntity2.class, list.get( 1).getClazz());
        assertEquals( "propertyStr1", list.get( 0).getPropertyName());
        assertEquals( "propertyStr2", list.get( 1).getPropertyName());
        assertTrue( list.get( 0).isUnique());
        assertFalse( list.get( 1).isUnique());

        // No.9 対象セルがすべてnullの行
        tagCell = sheet2.getRow( 48).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "value17", list.get( 0).getValue());
        assertEquals( "value18", list.get( 1).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( TestEntity2.class, list.get( 1).getClazz());
        assertEquals( "propertyStr1", list.get( 0).getPropertyName());
        assertEquals( "propertyStr2", list.get( 1).getPropertyName());
        assertTrue( list.get( 0).isUnique());
        assertFalse( list.get( 1).isUnique());

        // No.10 値が論理名タグ
        tagCell = sheet2.getRow( 56).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 1, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "@LNAME(プロパティ1)", list.get( 0).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( "propertyStr1", list.get( 0).getPropertyName());
        assertFalse( list.get( 0).isUnique());

        // No.11 値が論理名タグ、対象プロパティセルがnull
        tagCell = sheet2.getRow( 62).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 64, cell.getRow().getRowNum());
            assertEquals( 3, cell.getColumnIndex());
            System.out.println( "No.11:" + pe);
        }

        // No.12 値がカスタムプロパティタグ
        tagCell = sheet2.getRow( 68).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 1, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "@TestChildEntity{childPropertyStr1=@LNAME(子プロパティ文字列1),childPropertyInt1=@LNAME(子プロパティ整数1)}", list.get( 0).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertNull( list.get( 0).getPropertyName());
        assertFalse( list.get( 0).isUnique());

        // No.13 値がカスタムプロパティタグ、対象プロパティセルがnull
        tagCell = sheet2.getRow( 74).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 1, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "@TestChildEntity{childPropertyStr1=@LNAME(子プロパティ文字列1),childPropertyInt1=@LNAME(子プロパティ整数1)}", list.get( 0).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertNull( list.get( 0).getPropertyName());
        assertFalse( list.get( 0).isUnique());

        // No.14 値がカスタムプロパティタグ、重複不可が○
        tagCell = sheet2.getRow( 80).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 1, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "@TestChildEntity{childPropertyStr1=@LNAME(子プロパティ文字列1),childPropertyInt1=@LNAME(子プロパティ整数1)}", list.get( 0).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertNull( list.get( 0).getPropertyName());
        assertFalse( list.get( 0).isUnique());

        // No.15 値がカスタムプロパティタグ、パラメータ部分が不正
        tagCell = sheet2.getRow( 86).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet2, tagCell, data);
            fail();
        } catch (ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 88, cell.getRow().getRowNum());
            assertEquals( 1, cell.getColumnIndex());
        }

        // No.16 文字列プロパティに数値
        tagCell = sheet2.getRow( 92).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 1, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "10", list.get( 0).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( "propertyStr1", list.get( 0).getPropertyName());
        assertFalse( list.get( 0).isUnique());

        // No.17 整数プロパティに文字列
        tagCell = sheet2.getRow( 98).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 100, cell.getRow().getRowNum());
            assertEquals( 1, cell.getColumnIndex());
            System.out.println( "No.17:" + pe);
        }

        // No.18 日付プロパティに文字列
        tagCell = sheet2.getRow( 104).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 106, cell.getRow().getRowNum());
            assertEquals( 1, cell.getColumnIndex());
            System.out.println( "No.18:" + pe);
        }

        // No.19 存在しないエンティティ
        tagCell = sheet2.getRow( 110).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 112, cell.getRow().getRowNum());
            assertEquals( 2, cell.getColumnIndex());
            System.out.println( "No.19:" + pe);
        }

        // No.20 存在しないプロパティ
        tagCell = sheet2.getRow( 116).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 118, cell.getRow().getRowNum());
            assertEquals( 3, cell.getColumnIndex());
            System.out.println( "No.20:" + pe);
        }

        // No.21 対象エンティティが抽象クラス
        tagCell = sheet2.getRow( 122).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 124, cell.getRow().getRowNum());
            assertEquals( 3, cell.getColumnIndex());
            System.out.println( "No.21:" + pe);
        }

        // No.22 マイナス範囲指定
        tagCell = sheet3.getRow( 5).getCell( 0);
        list.clear();
        list = parser.parse( sheet3, tagCell, data);
        assertEquals( 3, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "sheetName3", list.get( 2).getSheetName());
        assertEquals( "value1", list.get( 0).getValue());
        assertEquals( 2, list.get( 1).getValue());
        assertEquals( "value3", list.get( 2).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( TestEntity1.class, list.get( 1).getClazz());
        assertEquals( TestEntity2.class, list.get( 2).getClazz());
        assertEquals( "propertyStr1", list.get( 0).getPropertyName());
        assertEquals( "propertyInt1", list.get( 1).getPropertyName());
        assertEquals( "propertyStr2", list.get( 2).getPropertyName());
        assertTrue( list.get( 0).isUnique());
        assertTrue( list.get( 1).isUnique());
        assertTrue( list.get( 2).isUnique());

        // No.23 DataRowFrom > DataRowTo
        tagCell = sheet3.getRow( 9).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet3, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 9, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.23:" + pe);
        }

        // No.24 DataRowFrom不正（値が空）
        tagCell = sheet3.getRow( 17).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet3, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 17, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.24:" + pe);
        }

        // No.25 DataRowTo不正（値が空）
        tagCell = sheet3.getRow( 21).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet3, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 21, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.25:" + pe);
        }

        // No.26 ResultKey指定
        tagCell = sheet3.getRow( 25).getCell( 0);
        list.clear();
        list = parser.parse( sheet3, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "value7", list.get( 0).getValue());
        assertEquals( 8, list.get( 1).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( TestEntity1.class, list.get( 1).getClazz());
        assertEquals( "propertyStr1", list.get( 0).getPropertyName());
        assertEquals( "propertyInt1", list.get( 1).getPropertyName());
        assertTrue( list.get( 0).isUnique());
        assertTrue( list.get( 1).isUnique());

        // No.27 DataRowFrom不正テスト（1行目でデータ開始行にマイナスを指定）
        tagCell = sheet4.getRow( 0).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet4, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 0, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.27:" + pe);
        }

        // No.28 DataRowTo不正テスト（1行目でデータ終了行にマイナスを指定）
        tagCell = sheet4.getRow( 0).getCell( 4);
        list.clear();
        try {
            list = parser.parse( sheet4, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 0, cell.getRow().getRowNum());
            assertEquals( 4, cell.getColumnIndex());
            System.out.println( "No.28:" + pe);
        }

        // No.29 DataRowFrom不正テスト（最終行でデータ開始行にプラスを指定）
        tagCell = sheet4.getRow( 15).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet4, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 15, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.29:" + pe);
        }

        // No.30 DataRowTo不正テスト（最終行でデータ終了行にプラスを指定）
        tagCell = sheet4.getRow( 15).getCell( 4);
        list.clear();
        try {
            list = parser.parse( sheet4, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 15, cell.getRow().getRowNum());
            assertEquals( 4, cell.getColumnIndex());
            System.out.println( "No.30:" + pe);
        }

        // No.31 デフォルトタグ
        parser = new SheetToJavaSettingParser();
        tagCell = sheet1.getRow( 0).getCell( 0);
        list = parser.parse( sheet1, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName2", list.get( 0).getSheetName());
        assertEquals( "sheetName3", list.get( 1).getSheetName());
        assertEquals( 2, list.get( 0).getValue());
        assertEquals( "value3", list.get( 1).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( TestEntity2.class, list.get( 1).getClazz());
        assertEquals( "propertyInt1", list.get( 0).getPropertyName());
        assertEquals( "propertyStr2", list.get( 1).getPropertyName());
        assertTrue( list.get( 0).isUnique());
        assertTrue( list.get( 1).isUnique());
        
        // No.32 存在しないシート名
        tagCell = sheet5.getRow( 0).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet5, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 4, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.21:" + pe);
        }

        // No.33 シート名がブランク
        tagCell = sheet5.getRow( 10).getCell( 0);
        list.clear();
        list = parser.parse( sheet5, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "value4", list.get( 0).getValue());
        assertEquals( 5, list.get( 1).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( TestEntity1.class, list.get( 1).getClazz());
        assertEquals( "propertyStr1", list.get( 0).getPropertyName());
        assertEquals( "propertyInt1", list.get( 1).getPropertyName());
        assertTrue( list.get( 0).isUnique());
        assertTrue( list.get( 1).isUnique());
    }
}
