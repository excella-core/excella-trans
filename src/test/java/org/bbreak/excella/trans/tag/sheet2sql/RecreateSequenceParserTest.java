/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: RecreateSequenceParserTest.java 66 2009-11-19 02:13:38Z akira-yokoi $
 * $Revision: 66 $
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
import org.junit.Test;

/**
 * RecreateSequenceParserテストクラス
 * 
 * @since 1.0
 */
public class RecreateSequenceParserTest extends WorkbookTest {

    /**
     * コンストラクタ
     * 
     * @param version Excelファイルのバージョン
     */
    public RecreateSequenceParserTest( String version) {
        super( version);
    }

    @Test
    public final void testRecreateSequenceParser() throws ParseException {
        Workbook workbook = getWorkbook();
        Sheet sheet1 = workbook.getSheetAt( 0);
        Sheet sheet2 = workbook.getSheetAt( 1);
        Sheet sheet3 = workbook.getSheetAt( 2);
        RecreateSequenceParser recreateSequenceParser = new RecreateSequenceParser( "@RecreateSequence");
        Cell tagCell = null;
        Object data = null;
        List<String> list = null;

        // ===============================================
        // parse( Sheet sheet, Cell tagCell, Object data)
        // ===============================================
        // No.1 パラメータ無し
        tagCell = sheet1.getRow( 10).getCell( 0);
        list = recreateSequenceParser.parse( sheet1, tagCell, data);
        assertEquals( 3, list.size());
        assertEquals( "drop sequence table_name1;\ncreate sequence table_name1 start with 100;", list.get( 0));
        assertEquals( "drop sequence table_name2;\ncreate sequence table_name2 start with 200;", list.get( 1));
        assertEquals( "drop sequence  ;\ncreate sequence   start with 300;", list.get( 2));

        // No.2 パラメータ有り、null行を含む
        tagCell = sheet1.getRow( 2).getCell( 4);
        list.clear();
        list = recreateSequenceParser.parse( sheet1, tagCell, data);
        assertEquals( 3, list.size());
        assertEquals( "drop sequence table_name3;\ncreate sequence table_name3 start with 100;", list.get( 0));
        assertEquals( "drop sequence table_name4;\ncreate sequence table_name4 start with 200;", list.get( 1));
        assertEquals( "drop sequence table_name5;\ncreate sequence table_name5 start with 300;", list.get( 2));

        // No.3 シーケンス名にnullセルを含む
        tagCell = sheet2.getRow( 5).getCell( 0);
        list.clear();
        try {
            list = recreateSequenceParser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 5, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.3:" + pe);
        }

        // No.4 現在値にnullセルを含む
        tagCell = sheet2.getRow( 16).getCell( 0);
        list.clear();
        try {
            list = recreateSequenceParser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 16, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.3:" + pe);
        }

        // No.5 シーケンス名・現在値がnullセル
        tagCell = sheet2.getRow( 28).getCell( 0);
        list.clear();
        try {
            list = recreateSequenceParser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 28, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.3:" + pe);
        }

        // No.6 配列のサイズ不足
        tagCell = sheet2.getRow( 41).getCell( 0);
        list.clear();
        try {
            list = recreateSequenceParser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 41, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.3:" + pe);
        }

        // No.7 配列のサイズ不足
        tagCell = sheet2.getRow( 53).getCell( 0);
        list.clear();
        try {
            list = recreateSequenceParser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 53, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.3:" + pe);
        }

        // No.8 現在値が小数
        tagCell = sheet2.getRow( 66).getCell( 0);
        list.clear();
        list = recreateSequenceParser.parse( sheet2, tagCell, data);
        assertEquals( 1, list.size());
        assertEquals( "drop sequence table_name11;\ncreate sequence table_name11 start with 1;", list.get( 0));

        // No.9 現在値が数値文字列
        tagCell = sheet2.getRow( 78).getCell( 0);
        list.clear();
        list = recreateSequenceParser.parse( sheet2, tagCell, data);
        assertEquals( 1, list.size());
        assertEquals( "drop sequence table_name12;\ncreate sequence table_name12 start with 10;", list.get( 0));

        // No.10 現在値が文字列
        tagCell = sheet2.getRow( 90).getCell( 0);
        list.clear();
        try {
            list = recreateSequenceParser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 90, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.3:" + pe);
        }

        // No.11 例外テスト（データ無し）
        tagCell = sheet2.getRow( 102).getCell( 0);
        list.clear();
        try {
            list = recreateSequenceParser.parse( sheet2, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 102, cell.getRow().getRowNum());
            assertEquals( 0, cell.getColumnIndex());
            System.out.println( "No.3:" + pe);
        }

        // No.12 デフォルトタグ
        tagCell = sheet3.getRow( 5).getCell( 0);
        RecreateSequenceParser recreateSequenceParser2 = new RecreateSequenceParser();
        list.clear();
        list = recreateSequenceParser2.parse( sheet3, tagCell, data);
        assertEquals( "drop sequence table_name1;\ncreate sequence table_name1 start with 1000;", list.get( 0));
        assertEquals( 1, list.size());
    }
}
