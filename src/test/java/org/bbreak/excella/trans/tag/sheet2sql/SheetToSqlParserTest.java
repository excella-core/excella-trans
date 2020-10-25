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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.trans.WorkbookTest;
import org.bbreak.excella.trans.tag.sheet2sql.model.SheetToSqlParseInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * SheetToJavaParserテストクラス
 * 
 * @since 1.0
 */
public class SheetToSqlParserTest extends WorkbookTest {

    @ParameterizedTest
    @CsvSource( WorkbookTest.VERSIONS)
    public final void testSheetToSqlParser( String version) throws ParseException, IOException {
        Workbook workbook = getWorkbook( version);
        Sheet sheet1 = workbook.getSheetAt( 0);
        Sheet sheet2 = workbook.getSheetAt( 1);
        Sheet sheet3 = workbook.getSheetAt( 2);
        Sheet sheet4 = workbook.getSheetAt( 3);
        Sheet sheet5 = workbook.getSheetAt( 4);
        String tag = "@SheetToSql";
        SheetToSqlParser parser = new SheetToSqlParser( tag);
        Object data = null;
        List<SheetToSqlParseInfo> list = null;

        // ===============================================
        // parse( Sheet sheet, Cell tagCell, Object data)
        // ===============================================
        // No.1 パラメータ無し
        Cell tagCell1 = sheet1.getRow( 0).getCell( 0);
        list = parser.parse( sheet1, tagCell1, data);
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
        Cell tagCell2 = sheet1.getRow( 0).getCell( 4);
        list.clear();
        list = parser.parse( sheet1, tagCell2, data);
        assertEquals( 2, list.size());
        assertEquals( "@SettingTagName", list.get( 0).getSettingTagName());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( new Integer( 7), list.get( 0).getLogicalNameRowNum());
        assertEquals( new Integer( 8), list.get( 1).getLogicalNameRowNum());
        assertEquals( new Integer( 10), list.get( 0).getValueRowNum());
        assertEquals( new Integer( 11), list.get( 1).getValueRowNum());

        // No.3 シート名セルがnull
        Cell tagCell3 = sheet2.getRow( 0).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell3, data);
        assertEquals( 2, list.size());
        assertEquals( "@SheetToSqlSetting", list.get( 0).getSettingTagName());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( new Integer( 2), list.get( 0).getLogicalNameRowNum());
        assertEquals( new Integer( 3), list.get( 1).getLogicalNameRowNum());
        assertEquals( new Integer( 5), list.get( 0).getValueRowNum());
        assertEquals( new Integer( 6), list.get( 1).getValueRowNum());

        // No.4 論理名行Noセルがnull
        Cell tagCell4 = sheet2.getRow( 8).getCell( 0);
        ParseException pe = assertThrows( ParseException.class, () -> parser.parse( sheet2, tagCell4, data));
        Cell cell = pe.getCell();
        assertEquals( 11, cell.getRow().getRowNum());
        assertEquals( 1, cell.getColumnIndex());
        System.out.println( "No.4:" + pe);

        // No.5 データ開始行Noセルがnull
        Cell tagCell5 = sheet2.getRow( 16).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet2, tagCell5, data));
        cell = pe.getCell();
        assertEquals( 20, cell.getRow().getRowNum());
        assertEquals( 2, cell.getColumnIndex());
        System.out.println( "No.5:" + pe);

        // No.6 行がnull
        Cell tagCell6 = sheet2.getRow( 24).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell6, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( new Integer( 17), list.get( 0).getLogicalNameRowNum());
        assertEquals( new Integer( 18), list.get( 1).getLogicalNameRowNum());
        assertEquals( new Integer( 19), list.get( 0).getValueRowNum());
        assertEquals( new Integer( 20), list.get( 1).getValueRowNum());

        // No.7 対象セルがすべてnullの行
        Cell tagCell7 = sheet2.getRow( 32).getCell( 0);
        list.clear();
        list = parser.parse( sheet2, tagCell7, data);
        assertEquals( 2, list.size());
        assertEquals( "sheetName1", list.get( 0).getSheetName());
        assertEquals( "sheetName2", list.get( 1).getSheetName());
        assertEquals( new Integer( 21), list.get( 0).getLogicalNameRowNum());
        assertEquals( new Integer( 22), list.get( 1).getLogicalNameRowNum());
        assertEquals( new Integer( 23), list.get( 0).getValueRowNum());
        assertEquals( new Integer( 24), list.get( 1).getValueRowNum());

        // No.8 
        Cell tagCell8 = sheet2.getRow( 40).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet2, tagCell8, data));
        cell = pe.getCell();
        assertEquals( 42, cell.getRow().getRowNum());
        assertEquals( 1, cell.getColumnIndex());
        System.out.println( "No.8:" + pe);

        // No.9 
        Cell tagCell9 = sheet2.getRow( 46).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet2, tagCell9, data));
        cell = pe.getCell();
        assertEquals( 48, cell.getRow().getRowNum());
        assertEquals( 2, cell.getColumnIndex());
        System.out.println( "No.9:" + pe);

        // No.10 マイナス範囲指定
        Cell tagCell10 = sheet3.getRow( 5).getCell( 0);
        list.clear();
        list = parser.parse( sheet3, tagCell10, data);
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
        Cell tagCell11 = sheet3.getRow( 9).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet3, tagCell11, data));
        cell = pe.getCell();
        assertEquals( 9, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.11:" + pe);

        // No.12 DataRowFrom不正
        Cell tagCell12 = sheet3.getRow( 17).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet3, tagCell12, data));
        cell = pe.getCell();
        assertEquals( 17, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.12:" + pe);
        
        // No.13 DataRowTo不正
        Cell tagCell13 = sheet3.getRow( 21).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet3, tagCell13, data));
        cell = pe.getCell();
        assertEquals( 21, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.13:" + pe);

        // No.14 SettingTagName不正
        Cell tagCell14 = sheet3.getRow( 25).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet3, tagCell14, data));
        cell = pe.getCell();
        assertEquals( 25, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.14:" + pe);

        // No.15 ResultKey指定
        Cell tagCell15 = sheet3.getRow( 29).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet3, tagCell15, data));
        cell = pe.getCell();
        assertEquals( 29, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.15:" + pe);

        // No.16 DataRowFrom不正テスト（1行目でデータ開始行にマイナスを指定）
        Cell tagCell16 = sheet4.getRow( 0).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet4, tagCell16, data));
        cell = pe.getCell();
        assertEquals( 0, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.16:" + pe);

        // No.17 DataRowTo不正テスト（1行目でデータ終了行にマイナスを指定）
        Cell tagCell17 = sheet4.getRow( 0).getCell( 4);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet4, tagCell17, data));
        cell = pe.getCell();
        assertEquals( 0, cell.getRow().getRowNum());
        assertEquals( 4, cell.getColumnIndex());
        System.out.println( "No.17:" + pe);

        // No.18 DataRowFrom不正テスト（最終行でデータ開始行にプラスを指定）
        Cell tagCell18 = sheet4.getRow( 15).getCell( 0);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet4, tagCell18, data));
        cell = pe.getCell();
        assertEquals( 15, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.18:" + pe);

        // No.19 DataRowTo不正テスト（最終行でデータ終了行にプラスを指定）
        Cell tagCell19 = sheet4.getRow( 15).getCell( 4);
        pe = assertThrows( ParseException.class, () -> parser.parse( sheet4, tagCell19, data));
        cell = pe.getCell();
        assertEquals( 15, cell.getRow().getRowNum());
        assertEquals( 4, cell.getColumnIndex());
        System.out.println( "No.19:" + pe);

        // No.20 デフォルトコンストラクタ
        SheetToSqlParser defaultParser = new SheetToSqlParser();
        Cell tagCell20 = sheet1.getRow( 0).getCell( 0);
        list = defaultParser.parse( sheet1, tagCell20, data);
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
        Cell tagCell21 = sheet5.getRow( 0).getCell( 0);
        pe = assertThrows( ParseException.class, () -> defaultParser.parse( sheet5, tagCell21, data));
        cell = pe.getCell();
        assertEquals( 4, cell.getRow().getRowNum());
        assertEquals( 0, cell.getColumnIndex());
        System.out.println( "No.21:" + pe);

        // No.22 シート名がブランク
        Cell tagCell22 = sheet5.getRow( 10).getCell( 0);
        list.clear();
        list = defaultParser.parse( sheet5, tagCell22, data);
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
