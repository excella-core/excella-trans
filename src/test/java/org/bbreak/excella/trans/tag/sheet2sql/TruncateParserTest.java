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

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.trans.WorkbookTest;
import org.junit.Test;

/**
 * TruncateParserテストクラス
 * 
 * @since 1.0
 */
public class TruncateParserTest extends WorkbookTest {

    /**
     * コンストラクタ
     * 
     * @param version Excelファイルのバージョン
     */
    public TruncateParserTest( String version) {
        super( version);
    }

    @Test
    public final void testTruncateParser() throws ParseException {
        Workbook workbook = getWorkbook();
        Sheet sheet = workbook.getSheetAt( 0);
        TruncateParser truncateParser = new TruncateParser( "@Truncate");
        Cell tagCell = null;
        Object data = null;
        List<String> list = null;

        // ===============================================
        // parse( Sheet sheet, Cell tagCell, Object data)
        // ===============================================
        // No.1 パラメータ無し
        tagCell = sheet.getRow( 10).getCell( 0);
        list = truncateParser.parse( sheet, tagCell, data);
        assertEquals( "truncate table_name1;", list.get( 0));
        assertEquals( "truncate table_name2;", list.get( 1));
        assertEquals( "truncate  ;", list.get( 2));
        assertEquals( 3, list.size());

        // No.2 パラメータ有り、null行を含む
        tagCell = sheet.getRow( 2).getCell( 4);
        list = truncateParser.parse( sheet, tagCell, data);
        assertEquals( "truncate table_name3;", list.get( 0));
        assertEquals( "truncate table_name4;", list.get( 1));
        assertEquals( "truncate table_name5;", list.get( 2));
        assertEquals( 3, list.size());

        // No.3 パラメータ有り、nullセルを含む
        tagCell = sheet.getRow( 8).getCell( 12);
        list = truncateParser.parse( sheet, tagCell, data);
        assertEquals( "truncate table_name6;", list.get( 0));
        assertEquals( "truncate table_name7;", list.get( 1));
        assertEquals( 2, list.size());

        // No.4 デフォルトタグ
        TruncateParser truncateParser2 = new TruncateParser();
        tagCell = sheet.getRow( 8).getCell( 21);
        list = truncateParser2.parse( sheet, tagCell, data);
        assertEquals( "truncate table_name8;", list.get( 0));
        assertEquals( "truncate table_name9;", list.get( 1));
        assertEquals( 2, list.size());
    }
}
