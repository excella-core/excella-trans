/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: SheetToSqlParserTest.java 36 2009-07-02 08:02:41Z yuta-takahashi $
 * $Revision: 36 $
 *
 * This file is part of ExCella Trans.
 *
 * ExCella Trans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * ExCella Trans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the COPYING.LESSER file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with ExCella Trans.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0-standalone.html>
 * for a copy of the LGPLv3 License.
 *
 ************************************************************************/
package org.bbreak.excella.trans.tag.sheet2sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.trans.WorkbookTest;
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlParseInfo;
import org.junit.Test;

/**
 * SheetToJavaParserテストクラス
 * 
 * @since 1.0
 */
public class SheetToSqlParserTest extends WorkbookTest {

    /**
     * コンストラクタ
     * 
     * @param version Excelファイルのバージョン
     */
    public SheetToSqlParserTest( String version) {
        super( version);
    }

    @Test
    public final void testSheetToSqlParser() throws ParseException {
        Workbook workbook = getWorkbook();
        Sheet sheet1 = workbook.getSheetAt( 0);
        Sheet sheet2 = workbook.getSheetAt( 1);
        Sheet sheet3 = workbook.getSheetAt( 2);
        Sheet sheet4 = workbook.getSheetAt( 3);
        Sheet sheet5 = workbook.getSheetAt( 4);
        String tag = "@SheetToSql";
        SheetToSqlParser parser = new SheetToSqlParser( tag);
        Cell tagCell = null;
        Object data = null;
        List<SheetToSqlParseInfo> list = null;

        // ===============================================
        // parse( Sheet sheet, Cell tagCell, Object data)
        // ===============================================
        // No.1 パラメータ無し
        tagCell = sheet1.getRow( 0).getCell( 0);
        list = parser.parse( sheet1, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( tag + "Setting", list.get( 0).getSettingTagName());
        assertEquals( tag + "Setting", list.get( 1).getSettingTagName());
        assertEquals( "sheetName2", list.get( 0).getSheetName());
        assertEquals( "sheetName3", list.get( 1).getSheetName());
        assertEquals( new Integer( 2), list.get( 0).getLogicalNameRowNum());
        assertEquals( new Integer( 3), list.get( 1).getLogicalNameRowNum());
        assertEquals( new Integer( 5), list.get( 0).getValueRowNum());
        assertEquals( new Integer( 6), list.get( 1).getValueRowNum());

        // No.2 パラメータ有り
        tagCell = sheet1.getRow( 0).getCell( 4);
        list.clear();
        list = parser.parse( sheet1, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "@SettingTagName", list.get( 0).getSettingTagName());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( new Integer( 7), list.get( 0).getLogicalNameRowNum());
        assertEquals( new Integer( 8), list.get( 1).getLogicalNameRowNum());
        assertEquals( new Integer( 10), list.get( 0).getValueRowNum());
        assertEquals( new Integer( 11), list.get( 1).getValueRowNum());

        // No.3 シート名セルがnull
        tagCell = sheet2.getRow( 0).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "@SheetToSqlSetting", list.get( 0).getSettingTagName());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( new Integer( 2), list.get( 0).getLogicalNameRowNum());
        assertEquals( new Integer( 3), list.get( 1).getLogicalNameRowNum());
        assertEquals( new Integer( 5), list.get( 0).getValueRowNum());
        assertEquals( new Integer( 6), list.get( 1).getValueRowNum());

        // No.4 論理名行Noセルがnull
        tagCell = sheet2.getRow( 8).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 11, cell.getRow().getRowNum());
            assertEquals( 1, cell.getColumnIndex());
            System.out.println( "No.4:" + pe);
        }

        // No.5 データ開始行Noセルがnull
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

        // No.6 行がnull
        tagCell = sheet2.getRow( 24).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( new Integer( 17), list.get( 0).getLogicalNameRowNum());
        assertEquals( new Integer( 18), list.get( 1).getLogicalNameRowNum());
        assertEquals( new Integer( 19), list.get( 0).getValueRowNum());
        assertEquals( new Integer( 20), list.get( 1).getValueRowNum());

        // No.7 対象セルがすべてnullの行
        tagCell = sheet2.getRow( 32).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( new Integer( 21), list.get( 0).getLogicalNameRowNum());
        assertEquals( new Integer( 22), list.get( 1).getLogicalNameRowNum());
        assertEquals( new Integer( 23), list.get( 0).getValueRowNum());
        assertEquals( new Integer( 24), list.get( 1).getValueRowNum());

        // No.8 
        tagCell = sheet2.getRow( 40).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet2, tagCell, data);
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 42, cell.getRow().getRowNum());
            assertEquals( 1, cell.getColumnIndex());
            System.out.println( "No.8:" + pe);
        }

        // No.9 
        tagCell = sheet2.getRow( 46).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet2, tagCell, data);
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 48, cell.getRow().getRowNum());
            assertEquals( 2, cell.getColumnIndex());
            System.out.println( "No.9:" + pe);
        }

        // No.10 マイナス範囲指定
        tagCell = sheet3.getRow( 5).getCell( 0);
        list.clear();
        list = parser.parse( sheet3, tagCell, data);
        assertEquals( 3, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( "sheetName3", list.get( 2).getSheetName());
        assertEquals( new Integer( 1), list.get( 0).getLogicalNameRowNum());
        assertEquals( new Integer( 2), list.get( 1).getLogicalNameRowNum());
        assertEquals( new Integer( 3), list.get( 2).getLogicalNameRowNum());
        assertEquals( new Integer( 4), list.get( 0).getValueRowNum());
        assertEquals( new Integer( 5), list.get( 1).getValueRowNum());
        assertEquals( new Integer( 6), list.get( 2).getValueRowNum());

        // No.11 DataRowFrom > DataRowTo
        tagCell = sheet3.getRow( 9).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet3, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 9, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.11:" + pe);
        }

        // No.12 DataRowFrom不正
        tagCell = sheet3.getRow( 17).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet3, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 17, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.12:" + pe);
        }
        
        // No.13 DataRowTo不正
        tagCell = sheet3.getRow( 21).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet3, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 21, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.13:" + pe);
        }

        // No.14 SettingTagName不正
        tagCell = sheet3.getRow( 25).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet3, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 25, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.14:" + pe);
        }

        // No.15 ResultKey指定
        tagCell = sheet3.getRow( 29).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet3, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 29, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.15:" + pe);
        }

        // No.16 DataRowFrom不正テスト（1行目でデータ開始行にマイナスを指定）
        tagCell = sheet4.getRow( 0).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet4, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 0, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.16:" + pe);
        }

        // No.17 DataRowTo不正テスト（1行目でデータ終了行にマイナスを指定）
        tagCell = sheet4.getRow( 0).getCell( 4);
        list.clear();
        try {
            list = parser.parse( sheet4, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 0, cell.getRow().getRowNum());
            assertEquals( 4, cell.getColumnIndex());
            System.out.println( "No.17:" + pe);
        }

        // No.18 DataRowFrom不正テスト（最終行でデータ開始行にプラスを指定）
        tagCell = sheet4.getRow( 15).getCell( 0);
        list.clear();
        try {
            list = parser.parse( sheet4, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 15, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.18:" + pe);
        }

        // No.19 DataRowTo不正テスト（最終行でデータ終了行にプラスを指定）
        tagCell = sheet4.getRow( 15).getCell( 4);
        list.clear();
        try {
            list = parser.parse( sheet4, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 15, cell.getRow().getRowNum());
            assertEquals( 4, cell.getColumnIndex());
            System.out.println( "No.19:" + pe);
        }

        // No.20 デフォルトコンストラクタ
        parser = new SheetToSqlParser();
        tagCell = sheet1.getRow( 0).getCell( 0);
        list = parser.parse( sheet1, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( tag + "Setting", list.get( 0).getSettingTagName());
        assertEquals( tag + "Setting", list.get( 1).getSettingTagName());
        assertEquals( "sheetName2", list.get( 0).getSheetName());
        assertEquals( "sheetName3", list.get( 1).getSheetName());
        assertEquals( new Integer( 2), list.get( 0).getLogicalNameRowNum());
        assertEquals( new Integer( 3), list.get( 1).getLogicalNameRowNum());
        assertEquals( new Integer( 5), list.get( 0).getValueRowNum());
        assertEquals( new Integer( 6), list.get( 1).getValueRowNum());
        

        // No.21 存在しないシート名
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

        // No.22 シート名がブランク
        tagCell = sheet5.getRow( 10).getCell( 0);
        list.clear();
        list = parser.parse( sheet5, tagCell, data);
        assertEquals( 2, list.size());
        assertEquals( tag + "Setting", list.get( 0).getSettingTagName());
        assertEquals( tag + "Setting", list.get( 1).getSettingTagName());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( new Integer( 4), list.get( 0).getLogicalNameRowNum());
        assertEquals( new Integer( 5), list.get( 1).getLogicalNameRowNum());
        assertEquals( new Integer( 7), list.get( 0).getValueRowNum());
        assertEquals( new Integer( 8), list.get( 1).getValueRowNum());
    }
}
