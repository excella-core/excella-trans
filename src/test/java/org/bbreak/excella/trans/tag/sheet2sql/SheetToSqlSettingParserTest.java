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
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlSettingInfo;
import org.junit.Test;

/**
 * SheetToSqlSettingParserテストクラス
 * 
 * @since 1.0
 */
public class SheetToSqlSettingParserTest extends WorkbookTest {

    /**
     * コンストラクタ
     * 
     * @param version Excelファイルのバージョン
     */
    public SheetToSqlSettingParserTest( String version) {
        super( version);
    }

    @Test
    public final void testSheetToSqlSettingParser() throws ParseException {
        Workbook workbook = getWorkbook();
        Sheet sheet1 = workbook.getSheetAt( 0);
        Sheet sheet2 = workbook.getSheetAt( 1);
        Sheet sheet3 = workbook.getSheetAt( 2);
        Sheet sheet4 = workbook.getSheetAt( 3);
        Sheet sheet5 = workbook.getSheetAt( 4);
        String tag = "@SheetToSqlSetting";
        SheetToSqlSettingParser parser = new SheetToSqlSettingParser( tag);
        Cell tagCell = null;
        Object data = null;
        List<SheetToSqlSettingInfo> list = null;

        // ===============================================
        // parse( Sheet sheet, Cell tagCell, Object data)
        // ===============================================
        // No.1 パラメータ無し
        tagCell = sheet1.getRow( 0).getCell( 0);
        list = parser.parse( sheet1, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName2", list.get( 0).getSheetName());
        assertEquals( "sheetName3", list.get( 1).getSheetName());
        assertEquals( "value2", list.get( 0).getValue());
        assertEquals( "value3", list.get( 1).getValue());
        assertEquals( "tableName2", list.get( 0).getTableName());
        assertEquals( "tableName3", list.get( 1).getTableName());
        assertEquals( "columnName2", list.get( 0).getColumnName());
        assertEquals( "columnName3", list.get( 1).getColumnName());
        assertTrue( list.get( 0).isUnique());
        assertTrue( list.get( 1).isUnique());
        assertEquals( "dataType2", list.get( 0).getDataType());
        assertEquals( "dataType3", list.get( 1).getDataType());

        // No.2 パラメータ有り
        tagCell = sheet1.getRow( 0).getCell( 7);
        list.clear();
        list = parser.parse( sheet1, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "value4", list.get( 0).getValue());
        assertEquals( "value5", list.get( 1).getValue());
        assertEquals( "tableName4", list.get( 0).getTableName());
        assertEquals( "tableName5", list.get( 1).getTableName());
        assertEquals( "columnName4", list.get( 0).getColumnName());
        assertEquals( "columnName5", list.get( 1).getColumnName());
        assertTrue( list.get( 0).isUnique());
        assertFalse( list.get( 1).isUnique());
        assertEquals( "dataType4", list.get( 0).getDataType());
        assertEquals( "dataType5", list.get( 1).getDataType());

        // No.3 シート名セルがnull
        tagCell = sheet2.getRow( 0).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "value2", list.get( 0).getValue());
        assertEquals( "value3", list.get( 1).getValue());
        assertEquals( "tableName2", list.get( 0).getTableName());
        assertEquals( "tableName3", list.get( 1).getTableName());
        assertEquals( "columnName2", list.get( 0).getColumnName());
        assertEquals( "columnName3", list.get( 1).getColumnName());
        assertEquals( "dataType2", list.get( 0).getDataType());
        assertEquals( "dataType3", list.get( 1).getDataType());
        
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
        assertEquals( "tableName4", list.get( 0).getTableName());
        assertEquals( "tableName5", list.get( 1).getTableName());
        assertEquals( "tableName6", list.get( 2).getTableName());
        assertEquals( "columnName4", list.get( 0).getColumnName());
        assertEquals( "columnName5", list.get( 1).getColumnName());
        assertEquals( "columnName6", list.get( 2).getColumnName());
        assertEquals( "dataType4", list.get( 0).getDataType());
        assertEquals( "dataType5", list.get( 1).getDataType());
        assertEquals( "dataType6", list.get( 2).getDataType());

        // No.5 対象テーブルセルがnull
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

        // No.6 対象カラムセルがnull
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
        assertEquals( "value13", list.get( 1).getValue());
        assertEquals( "value14", list.get( 2).getValue());
        assertEquals( "tableName12", list.get( 0).getTableName());
        assertEquals( "tableName13", list.get( 1).getTableName());
        assertEquals( "tableName14", list.get( 2).getTableName());
        assertEquals( "columnName12", list.get( 0).getColumnName());
        assertEquals( "columnName13", list.get( 1).getColumnName());
        assertEquals( "columnName14", list.get( 2).getColumnName());
        assertTrue( list.get( 0).isUnique());
        assertFalse( list.get( 1).isUnique());
        assertTrue( list.get( 2).isUnique());
        assertEquals( "dataType12", list.get( 0).getDataType());
        assertEquals( "dataType13", list.get( 1).getDataType());
        assertEquals( "dataType14", list.get( 2).getDataType());

        // No.8 データ型セルがnull
        tagCell = sheet2.getRow( 40).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 3, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "sheetName3", list.get( 2).getSheetName());
        assertEquals( "value15", list.get( 0).getValue());
        assertEquals( "value16", list.get( 1).getValue());
        assertEquals( "value17", list.get( 2).getValue());
        assertEquals( "tableName15", list.get( 0).getTableName());
        assertEquals( "tableName16", list.get( 1).getTableName());
        assertEquals( "tableName17", list.get( 2).getTableName());
        assertEquals( "columnName15", list.get( 0).getColumnName());
        assertEquals( "columnName16", list.get( 1).getColumnName());
        assertEquals( "columnName17", list.get( 2).getColumnName());
        assertTrue( list.get( 0).isUnique());
        assertTrue( list.get( 1).isUnique());
        assertTrue( list.get( 2).isUnique());
        assertEquals( "dataType15", list.get( 0).getDataType());
        assertEquals( "dataType16", list.get( 1).getDataType());
        assertNull( list.get( 2).getDataType());

        // No.9 行がnull
        tagCell = sheet2.getRow( 48).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "value18", list.get( 0).getValue());
        assertEquals( "value19", list.get( 1).getValue());
        assertEquals( "tableName18", list.get( 0).getTableName());
        assertEquals( "tableName19", list.get( 1).getTableName());
        assertEquals( "columnName18", list.get( 0).getColumnName());
        assertEquals( "columnName19", list.get( 1).getColumnName());
        assertTrue( list.get( 0).isUnique());
        assertFalse( list.get( 1).isUnique());
        assertEquals( "dataType17", list.get( 0).getDataType());
        assertEquals( "dataType18", list.get( 1).getDataType());

        // No.10 対象セルがすべてnullの行
        tagCell = sheet2.getRow( 56).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "value20", list.get( 0).getValue());
        assertEquals( "value21", list.get( 1).getValue());
        assertEquals( "tableName20", list.get( 0).getTableName());
        assertEquals( "tableName21", list.get( 1).getTableName());
        assertEquals( "columnName20", list.get( 0).getColumnName());
        assertEquals( "columnName21", list.get( 1).getColumnName());
        assertTrue( list.get( 0).isUnique());
        assertFalse( list.get( 1).isUnique());
        assertEquals( "dataType19", list.get( 0).getDataType());
        assertEquals( "dataType20", list.get( 1).getDataType());

        // No.11 値が論理名タグ
        tagCell = sheet2.getRow( 64).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "@LNAME(カラム名22)", list.get( 0).getValue());
        assertEquals( "@LNAME(カラム名23)", list.get( 1).getValue());
        assertEquals( "tableName22", list.get( 0).getTableName());
        assertEquals( "tableName23", list.get( 1).getTableName());
        assertEquals( "columnName22", list.get( 0).getColumnName());
        assertEquals( "columnName23", list.get( 1).getColumnName());
        assertTrue( list.get( 0).isUnique());
        assertFalse( list.get( 1).isUnique());
        assertEquals( "dataType21", list.get( 0).getDataType());
        assertEquals( "dataType22", list.get( 1).getDataType());

        // No.12 マイナス範囲指定
        tagCell = sheet3.getRow( 5).getCell( 0);
        list.clear();
        list = parser.parse( sheet3, tagCell, data);
        assertEquals( 3, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "sheetName3", list.get( 2).getSheetName());
        assertEquals( "value1", list.get( 0).getValue());
        assertEquals( "value2", list.get( 1).getValue());
        assertEquals( "value3", list.get( 2).getValue());
        assertEquals( "tableName1", list.get( 0).getTableName());
        assertEquals( "tableName2", list.get( 1).getTableName());
        assertEquals( "tableName3", list.get( 2).getTableName());
        assertEquals( "columnName1", list.get( 0).getColumnName());
        assertEquals( "columnName2", list.get( 1).getColumnName());
        assertEquals( "columnName3", list.get( 2).getColumnName());
        assertTrue( list.get( 0).isUnique());
        assertTrue( list.get( 1).isUnique());
        assertTrue( list.get( 2).isUnique());
        assertEquals( "dataType1", list.get( 0).getDataType());
        assertEquals( "dataType2", list.get( 1).getDataType());
        assertEquals( "dataType3", list.get( 2).getDataType());

        // No.13 DataRowFrom > DataRowTo
        tagCell = sheet3.getRow( 9).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet3, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 9, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.13:" + pe);
        }

        // No.14 DataRowFrom不正（値が空）
        tagCell = sheet3.getRow( 17).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet3, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 17, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.14:" + pe);
        }

        // No.15 DataRowTo不正（値が空）
        tagCell = sheet3.getRow( 21).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet3, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 21, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.15:" + pe);
        }

        // No.16 ResultKey指定
        tagCell = sheet3.getRow( 25).getCell( 0);
        list.clear();
        list = parser.parse( sheet3, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "value7", list.get( 0).getValue());
        assertEquals( "value8", list.get( 1).getValue());
        assertEquals( "tableName7", list.get( 0).getTableName());
        assertEquals( "tableName8", list.get( 1).getTableName());
        assertEquals( "columnName7", list.get( 0).getColumnName());
        assertEquals( "columnName8", list.get( 1).getColumnName());
        assertTrue( list.get( 0).isUnique());
        assertTrue( list.get( 1).isUnique());
        assertEquals( "dataType7", list.get( 0).getDataType());
        assertEquals( "dataType8", list.get( 1).getDataType());

        // No.17 DataRowFrom不正テスト（1行目でデータ開始行にマイナスを指定）
        tagCell = sheet4.getRow( 0).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet4, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 0, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.17:" + pe);
        }

        // No.18 DataRowTo不正テスト（1行目でデータ終了行にマイナスを指定）
        tagCell = sheet4.getRow( 0).getCell( 4);
        list.clear();
        try {
            list = parser.parse( sheet4, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 0, cell.getRow().getRowNum());
            assertEquals( 4, cell.getColumnIndex());
            System.out.println( "No.18:" + pe);
        }

        // No.19 DataRowFrom不正テスト（最終行でデータ開始行にプラスを指定）
        tagCell = sheet4.getRow( 15).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet4, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 15, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.19:" + pe);
        }

        // No.20 DataRowTo不正テスト（最終行でデータ終了行にプラスを指定）
        tagCell = sheet4.getRow( 15).getCell( 4);
        list.clear();
        try {
            list = parser.parse( sheet4, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 15, cell.getRow().getRowNum());
            assertEquals( 4, cell.getColumnIndex());
            System.out.println( "No.20:" + pe);
        }

        // No.21 デフォルトタグ
        parser = new SheetToSqlSettingParser();
        tagCell = sheet1.getRow( 0).getCell( 0);
        list = parser.parse( sheet1, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName2", list.get( 0).getSheetName());
        assertEquals( "sheetName3", list.get( 1).getSheetName());
        assertEquals( "value2", list.get( 0).getValue());
        assertEquals( "value3", list.get( 1).getValue());
        assertEquals( "tableName2", list.get( 0).getTableName());
        assertEquals( "tableName3", list.get( 1).getTableName());
        assertEquals( "columnName2", list.get( 0).getColumnName());
        assertEquals( "columnName3", list.get( 1).getColumnName());
        assertTrue( list.get( 0).isUnique());
        assertTrue( list.get( 1).isUnique());
        assertEquals( "dataType2", list.get( 0).getDataType());
        assertEquals( "dataType3", list.get( 1).getDataType());

        // No.22 存在しないシート名
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

        // No.23 シート名がブランク
        tagCell = sheet5.getRow( 10).getCell( 0);
        list.clear();
        list = parser.parse( sheet5, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "value4", list.get( 0).getValue());
        assertEquals( "value5", list.get( 1).getValue());
        assertEquals( "tableName4", list.get( 0).getTableName());
        assertEquals( "tableName5", list.get( 1).getTableName());
        assertEquals( "columnName4", list.get( 0).getColumnName());
        assertEquals( "columnName5", list.get( 1).getColumnName());
        assertTrue( list.get( 0).isUnique());
        assertTrue( list.get( 1).isUnique());
        assertEquals( "dataType4", list.get( 0).getDataType());
        assertEquals( "dataType5", list.get( 1).getDataType());

    }
}
