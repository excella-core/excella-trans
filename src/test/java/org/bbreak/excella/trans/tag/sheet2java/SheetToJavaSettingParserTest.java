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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.trans.WorkbookTest;
import org.bbreak.excella.trans.tag.sheet2java.entity.TestEntity1;
import org.bbreak.excella.trans.tag.sheet2java.entity.TestEntity2;
import org.bbreak.excella.trans.tag.sheet2java.model.SheetToJavaSettingInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * SheetToJavaSettingParserテストクラス
 * 
 * @since 1.0
 */
public class SheetToJavaSettingParserTest extends WorkbookTest {

    @ParameterizedTest
    @CsvSource( WorkbookTest.VERSIONS)
    public final void testSheetToJavaSettingParser( String version) throws ParseException, IOException {
        Workbook workbook = getWorkbook( version);
        Sheet sheet1 = workbook.getSheetAt( 0);
        Sheet sheet2 = workbook.getSheetAt( 1);
        Sheet sheet3 = workbook.getSheetAt( 2);
        Sheet sheet4 = workbook.getSheetAt( 3);
        Sheet sheet5 = workbook.getSheetAt( 4);
        String tag = "@SheetToJavaSetting";
        SheetToJavaSettingParser parser = new SheetToJavaSettingParser( tag);
        Object data = null;
        List<SheetToJavaSettingInfo> list = null;

        // ===============================================
        // parse( Sheet sheet, Cell tagCell, Object data)
        // ===============================================
        // No.1 パラメータ無し
        Cell tagCell1 = sheet1.getRow( 0).getCell( 0);
        list = parser.parse( sheet1, tagCell1, data);
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
        Cell tagCell2 = sheet1.getRow( 0).getCell( 6);
        list.clear();
        list = parser.parse( sheet1, tagCell2, data);
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
        Cell tagCell3 = sheet2.getRow( 0).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell3, data);
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
        Cell tagCell4 = sheet2.getRow( 8).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell4, data);
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
        Cell tagCell5 = sheet2.getRow( 16).getCell( 0);
        ParseException pe = assertThrows( ParseException.class, () -> parser.parse( sheet2, tagCell5, data));
        Cell cell = pe.getCell();
        assertEquals( 20, cell.getRow().getRowNum());
        assertEquals( 2, cell.getColumnIndex());
        System.out.println( "No.5:" + pe);

        // No.6 対象プロパティセルがnull
        Cell tagCell6 = sheet2.getRow( 24).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet2, tagCell6, data));
        cell = pe.getCell();
        assertEquals( 26, cell.getRow().getRowNum());
        assertEquals( 3, cell.getColumnIndex());
        System.out.println( "No.6:" + pe);

        // No.7 重複不可セルがnull
        Cell tagCell7 = sheet2.getRow( 32).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell7, data);
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
        Cell tagCell8 = sheet2.getRow( 40).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell8, data);
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
        Cell tagCell9 = sheet2.getRow( 48).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell9, data);
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
        Cell tagCell10 = sheet2.getRow( 56).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell10, data);
        assertEquals( 1, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "@LNAME(プロパティ1)", list.get( 0).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( "propertyStr1", list.get( 0).getPropertyName());
        assertFalse( list.get( 0).isUnique());

        // No.11 値が論理名タグ、対象プロパティセルがnull
        Cell tagCell11 = sheet2.getRow( 62).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet2, tagCell11, data));
        cell = pe.getCell();
        assertEquals( 64, cell.getRow().getRowNum());
        assertEquals( 3, cell.getColumnIndex());
        System.out.println( "No.11:" + pe);

        // No.12 値がカスタムプロパティタグ
        Cell tagCell12 = sheet2.getRow( 68).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell12, data);
        assertEquals( 1, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "@TestChildEntity{childPropertyStr1=@LNAME(子プロパティ文字列1),childPropertyInt1=@LNAME(子プロパティ整数1)}", list.get( 0).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertNull( list.get( 0).getPropertyName());
        assertFalse( list.get( 0).isUnique());

        // No.13 値がカスタムプロパティタグ、対象プロパティセルがnull
        Cell tagCell13 = sheet2.getRow( 74).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell13, data);
        assertEquals( 1, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "@TestChildEntity{childPropertyStr1=@LNAME(子プロパティ文字列1),childPropertyInt1=@LNAME(子プロパティ整数1)}", list.get( 0).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertNull( list.get( 0).getPropertyName());
        assertFalse( list.get( 0).isUnique());

        // No.14 値がカスタムプロパティタグ、重複不可が○
        Cell tagCell14 = sheet2.getRow( 80).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell14, data);
        assertEquals( 1, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "@TestChildEntity{childPropertyStr1=@LNAME(子プロパティ文字列1),childPropertyInt1=@LNAME(子プロパティ整数1)}", list.get( 0).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertNull( list.get( 0).getPropertyName());
        assertFalse( list.get( 0).isUnique());

        // No.15 値がカスタムプロパティタグ、パラメータ部分が不正
        Cell tagCell15 = sheet2.getRow( 86).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet2, tagCell15, data));
        cell = pe.getCell();
        assertEquals( 88, cell.getRow().getRowNum());
        assertEquals( 1, cell.getColumnIndex());

        // No.16 文字列プロパティに数値
        Cell tagCell16 = sheet2.getRow( 92).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell16, data);
        assertEquals( 1, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "10", list.get( 0).getValue());
        assertEquals( TestEntity1.class, list.get( 0).getClazz());
        assertEquals( "propertyStr1", list.get( 0).getPropertyName());
        assertFalse( list.get( 0).isUnique());

        // No.17 整数プロパティに文字列
        Cell tagCell17 = sheet2.getRow( 98).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet2, tagCell17, data));
        cell = pe.getCell();
        assertEquals( 100, cell.getRow().getRowNum());
        assertEquals( 1, cell.getColumnIndex());
        System.out.println( "No.17:" + pe);

        // No.18 日付プロパティに文字列
        Cell tagCell18 = sheet2.getRow( 104).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet2, tagCell18, data));
        cell = pe.getCell();
        assertEquals( 106, cell.getRow().getRowNum());
        assertEquals( 1, cell.getColumnIndex());
        System.out.println( "No.18:" + pe);

        // No.19 存在しないエンティティ
        Cell tagCell19 = sheet2.getRow( 110).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet2, tagCell19, data));
        cell = pe.getCell();
        assertEquals( 112, cell.getRow().getRowNum());
        assertEquals( 2, cell.getColumnIndex());
        System.out.println( "No.19:" + pe);

        // No.20 存在しないプロパティ
        Cell tagCell20 = sheet2.getRow( 116).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet2, tagCell20, data));
        cell = pe.getCell();
        assertEquals( 118, cell.getRow().getRowNum());
        assertEquals( 3, cell.getColumnIndex());
        System.out.println( "No.20:" + pe);

        // No.21 対象エンティティが抽象クラス
        Cell tagCell21 = sheet2.getRow( 122).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet2, tagCell21, data));
        cell = pe.getCell();
        assertEquals( 124, cell.getRow().getRowNum());
        assertEquals( 3, cell.getColumnIndex());
        System.out.println( "No.21:" + pe);

        // No.22 マイナス範囲指定
        Cell tagCell22 = sheet3.getRow( 5).getCell( 0);
        list.clear();
        list = parser.parse( sheet3, tagCell22, data);
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
        Cell tagCell23 = sheet3.getRow( 9).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet3, tagCell23, data));
        cell = pe.getCell();
        assertEquals( 9, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.23:" + pe);

        // No.24 DataRowFrom不正（値が空）
        Cell tagCell24 = sheet3.getRow( 17).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet3, tagCell24, data));
        cell = pe.getCell();
        assertEquals( 17, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.24:" + pe);

        // No.25 DataRowTo不正（値が空）
        Cell tagCell25 = sheet3.getRow( 21).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet3, tagCell25, data));
        cell = pe.getCell();
        assertEquals( 21, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.25:" + pe);

        // No.26 ResultKey指定
        Cell tagCell26 = sheet3.getRow( 25).getCell( 0);
        list.clear();
        list = parser.parse( sheet3, tagCell26, data);
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
        Cell tagCell27 = sheet4.getRow( 0).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet4, tagCell27, data));
        cell = pe.getCell();
        assertEquals( 0, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.27:" + pe);

        // No.28 DataRowTo不正テスト（1行目でデータ終了行にマイナスを指定）
        Cell tagCell28 = sheet4.getRow( 0).getCell( 4);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet4, tagCell28, data));
        cell = pe.getCell();
        assertEquals( 0, cell.getRow().getRowNum());
        assertEquals( 4, cell.getColumnIndex());
        System.out.println( "No.28:" + pe);

        // No.29 DataRowFrom不正テスト（最終行でデータ開始行にプラスを指定）
        Cell tagCell29 = sheet4.getRow( 15).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet4, tagCell29, data));
        cell = pe.getCell();
        assertEquals( 15, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.29:" + pe);

        // No.30 DataRowTo不正テスト（最終行でデータ終了行にプラスを指定）
        Cell tagCell30 = sheet4.getRow( 15).getCell( 4);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet4, tagCell30, data));
        cell = pe.getCell();
        assertEquals( 15, cell.getRow().getRowNum());
        assertEquals( 4, cell.getColumnIndex());
        System.out.println( "No.30:" + pe);

        // No.31 デフォルトタグ
        SheetToJavaSettingParser defaultParser = new SheetToJavaSettingParser();
        Cell tagCell31 = sheet1.getRow( 0).getCell( 0);
        list = defaultParser.parse( sheet1, tagCell31, data);
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
        Cell tagCell32 = sheet5.getRow( 0).getCell( 0);
        pe = assertThrows( ParseException.class, () -> defaultParser.parse( sheet5, tagCell32, data));
        cell = pe.getCell();
        assertEquals( 4, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.21:" + pe);

        // No.33 シート名がブランク
        Cell tagCell33 = sheet5.getRow( 10).getCell( 0);
        list.clear();
        list = defaultParser.parse( sheet5, tagCell33, data);
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
