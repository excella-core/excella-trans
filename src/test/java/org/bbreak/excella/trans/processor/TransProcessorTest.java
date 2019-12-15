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

package org.bbreak.excella.trans.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.BookData;
import org.bbreak.excella.core.SheetData;
import org.bbreak.excella.core.SheetParser;
import org.bbreak.excella.core.exception.ExportException;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.core.exporter.book.BookExporter;
import org.bbreak.excella.core.exporter.sheet.SheetExporter;
import org.bbreak.excella.core.handler.ParseErrorHandler;
import org.bbreak.excella.core.listener.SheetParseListener;
import org.bbreak.excella.core.tag.TagParser;
import org.bbreak.excella.trans.WorkbookTest;
import org.bbreak.excella.trans.listener.TransProcessListener;
import org.junit.Test;

/**
 * TransProcessorテストクラス
 * 
 * @since 1.0
 */
public class TransProcessorTest extends WorkbookTest {

    /**
     * コンストラクタ
     * 
     * @param version Excelファイルのバージョン
     */
    public TransProcessorTest( String version) {
        super( version);
    }

    @Test
    public final void testTransProcessor() throws ParseException, ExportException, IOException {

        super.getWorkbook();
        String filePath = super.getFilepath();
        
        // ===============================================
        // TransProcessor( String filePath)
        // ===============================================
        // ファイルパスが正常
        TransProcessor processor = new TransProcessor( filePath);

        // ファイルパスが異常
        filePath = "";
        try {
            processor = new TransProcessor( filePath);
            fail();
        } catch ( IOException e) {
            // ファイル読込例外が発生
        }

        // ===============================================
        // processBook()
        // ===============================================
        // 引数なし
        processor.processBook();

        // ===============================================
        // processBook(Object data)
        // ===============================================
        // 引数あり
        String data = "abc";
        processor.processBook( data);

        // ===============================================
        // processSheet(String sheetName)
        // ===============================================
        // 第二引数なし
        String sheetName = "Sheet1";
        processor.processSheet( sheetName);

        // ===============================================
        // processSheet(String sheetName, Object data)
        // ===============================================
        // 第二引数あり
        processor.processSheet( sheetName, data);

        // 存在しないシート名
        sheetName = "Sheet2";
        try {
            processor.processSheet( sheetName, data);
            fail();
        } catch ( NullPointerException e) {
            // 例外が発生
        }

        // シート名にnullを設定
        try {
            processor.processSheet( null, data);
            fail();
        } catch ( NullPointerException e) {
            // 例外が発生
        }

        // ===============================================
        // isDefaultSqlTag( String tag)
        // ===============================================
        // 結果が真
        assertTrue( processor.isDefaultSqlTag( "@Sql"));
        assertTrue( processor.isDefaultSqlTag( "@Truncate"));
        assertTrue( processor.isDefaultSqlTag( "@TruncateCascade"));
        assertTrue( processor.isDefaultSqlTag( "@Delete"));
        assertTrue( processor.isDefaultSqlTag( "@RecreateSequence"));
        assertTrue( processor.isDefaultSqlTag( "@SheetToSql"));

        // 結果が偽
        assertFalse( processor.isDefaultSqlTag( "@SQL"));
        assertFalse( processor.isDefaultSqlTag( ""));

        // 引数にnullを指定
        assertFalse( processor.isDefaultSqlTag( null));

        // ===============================================
        // getSheetNames()
        // ===============================================
        List<String> sheetNames = processor.getSheetNames();
        assertEquals( 3, sheetNames.size());
        assertEquals( "Sheet1", sheetNames.get( 0));
        assertEquals( "-Sheet2", sheetNames.get( 1));
        assertEquals( "Sheet3", sheetNames.get( 2));

        // ===============================================
        // addTagParser( TagParser<?> tagParser)
        // ===============================================
        processor.addTagParser( new TestTagParser( "@Test1"));
        processor.addTagParser( new TestTagParser( "@Test2"));

        BookData bookData = processor.processBook();
        SheetData sheetData = bookData.getSheetData( "Sheet1");
        assertEquals( "@Test1", sheetData.get( "@Test1"));
        assertEquals( "@Test2", sheetData.get( "@Test2"));

        // ===============================================
        // removeTagParser( String tag)
        // ===============================================
        processor.removeTagParser( "@Test1");
        bookData = processor.processBook();
        sheetData = bookData.getSheetData( "Sheet1");
        assertNull( sheetData.get( "@Test1"));
        assertEquals( "@Test2", sheetData.get( "@Test2"));

        // ===============================================
        // addTagParser( String sheetName, TagParser<?> tagParser)
        // ===============================================
        processor.addTagParser( "Sheet3", new TestTagParser( "@Test1"));
        bookData = processor.processBook();
        sheetData = bookData.getSheetData( "Sheet1");
        assertNull( sheetData.get( "@Test1"));
        sheetData = bookData.getSheetData( "Sheet3");
        assertEquals( "@Test1", sheetData.get( "@Test1"));

        // ===============================================
        // clearTagParsers()
        // ===============================================
        processor.clearTagParsers();
        bookData = processor.processBook();
        sheetData = bookData.getSheetData( "Sheet1");
        assertNull( sheetData.get( "@Test1"));
        sheetData = bookData.getSheetData( "Sheet3");
        assertNull( sheetData.get( "@Test1"));

        // ===============================================
        // addBookExporter( BookExporter bookExporter)
        // ===============================================
        processor.addBookExporter( new TestBookExporter());
        processor.processBook();

        // ===============================================
        // clearBookExporters()
        // ===============================================
        processor.clearBookExporters();
        processor.processBook();

        // ===============================================
        // addSheetExporter( SheetExporter sheetExporter)
        // ===============================================
        processor.addSheetExporter( new TestSheetExporter());
        processor.processBook();

        // ===============================================
        // clearSheetExporters()
        // ===============================================
        processor.clearSheetExporters();
        processor.processBook();

        // ===============================================
        // addSheetExporter( String sheetName, SheetExporter sheetExporter)
        // ===============================================
        processor.addSheetExporter( "Sheet3", new TestSheetExporter());
        processor.processBook();

        // ===============================================
        // getErrorHandler()
        // ===============================================
        assertNull( processor.getErrorHandler());

        // ===============================================
        // setErrorHandler( ErroHandler errorHandler)
        // ===============================================
        TestErrorHandler testErrorHandler = new TestErrorHandler();
        processor.setErrorHandler( testErrorHandler);
        assertEquals( testErrorHandler, processor.getErrorHandler());

        // ===============================================
        // addSheetParseListener( SheetParseListener sheetParseListener)
        // ===============================================
        processor.addSheetParseListener( new TestSheetParseListener());
        processor.processBook();

        // ===============================================
        // clearSheetParseListeners()
        // ===============================================
        processor.clearSheetParseListeners();
        processor.processBook();

        // ===============================================
        // addSheetParseListener( String sheetName, SheetParseListener sheetParseListener)
        // ===============================================
        processor.addSheetParseListener( "Sheet3", new TestSheetParseListener());
        processor.processBook();
        
        // ===============================================
        // addTransProcessListener( TransProcessListener processListener)
        // ===============================================
        processor.addTransProcessListener( new TestProcessListener());
        processor.processBook();
        
        // ===============================================
        // clearTransProcessListeners()
        // ===============================================
        processor.clearTransProcessListeners();
        processor.processBook();
    }

    /**
     * テスト用タグパーサ
     */
    private class TestTagParser extends TagParser<String> {

        /**
         * コンストラクタ
         * @param tag タグ
         */
        public TestTagParser( String tag) {
            super( tag);
        }

        @Override
        public String parse( Sheet sheet, Cell tagCell, Object data) throws ParseException {
            String result = getTag();
            return result;
        }
    }

    /**
     * テスト用ブックエクスポータ
     */
    private class TestBookExporter implements BookExporter {

        public void export( Workbook book, BookData bookdata) throws ExportException {
            System.out.println( "TestBookExporter : export");
        }

        public void setup() {
            System.out.println( "TestBookExporter : setup");
        }

        public void tearDown() {
            System.out.println( "TestBookExporter : tearDown");
        }
    }

    /**
     * テスト用シートエクスポータ
     */
    private class TestSheetExporter implements SheetExporter {

        public void export( Sheet sheet, SheetData sheetdata) throws ExportException {
            System.out.println( "TestSheetExporter : export");
        }

        public void setup() {
            System.out.println( "TestSheetExporter : setup");
        }

        public void tearDown() {
            System.out.println( "TestSheetExporter : tearDown");
        }
    }

    /**
     * テスト用エラーハンドラ
     */
    private class TestErrorHandler implements ParseErrorHandler {

        public void notifyException( Workbook workbook, Sheet sheet, ParseException exception) {
            System.out.println( "notifyException");
        }
    }

    /**
     * テスト用シート処理リスナ
     */
    private class TestSheetParseListener implements SheetParseListener {

        public void preParse( Sheet sheet, SheetParser sheetParser) throws ParseException {
            System.out.println( "TestSheetParseListener : preParse");
        }

        public void postParse( Sheet sheet, SheetParser sheetParser, SheetData sheetData) throws ParseException {
            System.out.println( "TestSheetParseListener : postParse");
        }
    }

    /**
     * テスト用シート処理リスナ
     */
    private class TestProcessListener implements TransProcessListener {

        public void preBookParse( Workbook workbook) {
            System.out.println( "TestProcessListener : preBookParse");
        }

        public void postBookParse( Workbook workbook, BookData bookData) {
            System.out.println( "TestProcessListener : postBookParse");
        }
    }
}
