/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: SqlParserTest.java 35 2009-07-02 08:02:26Z yuta-takahashi $
 * $Revision: 35 $
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
 * SqlParserテストクラス
 *
 * @since 1.0
 */
public class SqlParserTest extends WorkbookTest {

    /**
     * コンストラクタ
     * 
     * @param version Excelファイルのバージョン
     */
    public SqlParserTest( String version) {
        super( version);
    }

    @Test
    public final void testSqlParser() throws ParseException {
        Workbook workbook = getWorkbook();
        Sheet sheet = workbook.getSheetAt( 0);
        SqlParser sqlParser = new SqlParser( "@Sql");
        Cell tagCell = null;
        Object data = null;
        List<String> list = null;

        // ===============================================
        // parse( Sheet sheet, Cell tagCell, Object data) 
        // ===============================================
        // No.1 パラメータ無し
        tagCell = sheet.getRow( 10).getCell( 0);
        list = sqlParser.parse( sheet, tagCell, data);
        assertEquals( "update table_name1 set password='init';", list.get( 0));
        assertEquals( "update table_name2 set password='init';;", list.get( 1));
        assertEquals( " ;", list.get( 2));
        assertEquals( 3, list.size());

        // No.2 パラメータ有り、null行を含む
        tagCell = sheet.getRow( 2).getCell( 4);
        list = sqlParser.parse( sheet, tagCell, data);
        assertEquals( "update table_name3 set password='init';", list.get( 0));
        assertEquals( "update table_name4 set password='init';", list.get( 1));
        assertEquals( "update table_name5 set password='init';", list.get( 2));
        assertEquals( 3, list.size());

        // No.3 パラメータ有り、nullセルを含む
        tagCell = sheet.getRow( 8).getCell( 12);
        list = sqlParser.parse( sheet, tagCell, data);
        assertEquals( "update table_name6 set password='init';", list.get( 0));
        assertEquals( "update table_name7 set password='init';", list.get( 1));
        assertEquals( 2, list.size());

        // No.4 例外テスト（データ無し）
        tagCell = sheet.getRow( 13).getCell( 20);
        list.clear();
        try {
            list = sqlParser.parse( sheet, tagCell, data);
            fail();
        } catch ( ParseException pe) {
            Cell cell = pe.getCell();
            assertEquals( 13, cell.getRow().getRowNum());
            assertEquals( 20, cell.getColumnIndex());
        }

        // No.5 デフォルトタグ
        SqlParser sqlParser2 = new SqlParser();
        tagCell = sheet.getRow( 8).getCell( 24);
        list.clear();
        list = sqlParser2.parse( sheet, tagCell, data);
        assertEquals( "update table_name8 set password='init';", list.get( 0));
        assertEquals( "update table_name9 set password='init';", list.get( 1));
        assertEquals( 2, list.size());
    }
}
