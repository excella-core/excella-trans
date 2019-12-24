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
