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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bbreak.excella.core.BookController;
import org.bbreak.excella.core.BookData;
import org.bbreak.excella.core.SheetData;
import org.bbreak.excella.core.exception.ExportException;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.core.exporter.book.BookExporter;
import org.bbreak.excella.core.exporter.sheet.SheetExporter;
import org.bbreak.excella.core.handler.ParseErrorHandler;
import org.bbreak.excella.core.listener.SheetParseListener;
import org.bbreak.excella.core.tag.TagParser;
import org.bbreak.excella.trans.listener.TransProcessListener;

/**
 * データ移行プロセッサ
 * 
 * @since 1.0
 */
public class TransProcessor {

    /**
     * コメントのプレフィックス
     */
    public static final String COMMENT_PREFIX = BookController.COMMENT_PREFIX;

    /**
     * /** ブックコントローラ
     */
    private BookController controller = null;

    /**
     * ワークブック
     */
    private Workbook workbook = null;

    /**
     * プロセスリスナのリスト
     */
    private List<TransProcessListener> processListeners = new ArrayList<TransProcessListener>();

    /**
     * コンストラクタ
     * 
     * @param filePath 対象Excelファイルパス
     * @throws IOException ファイルの読み込みに失敗した場合
     */
    public TransProcessor( String filePath) throws IOException {
        // 初期化処理
        init( filePath);
    }

    /**
     * 初期化処理
     * 
     * @param filePath 対象Excelファイルパス
     * @throws IOException ファイルの読み込みに失敗した場合
     */
    private void init( String filePath) throws IOException {

        // ワークブック取得
        this.workbook = getWorkbook( filePath);

        // コントローラ生成
        this.controller = new BookController( workbook);

        // デフォルトタグパーサ追加
        for ( TagParser<?> tagParser : TransCreateHelper.getDefaultTagParsers()) {
            addTagParser( tagParser);
        }

        // デフォルトシート処理リスナ追加
        for ( SheetParseListener listener : TransCreateHelper.getDefaultSheetParseListeners()) {
            addSheetParseListener( listener);
        }
    }

    /**
     * 指定したパスのワークブックを取得する
     * 
     * @param filePath 対象Excelファイルパス
     * @return workbook パース例外
     * @throws IOException ファイルの読み込みに失敗した場合
     */
    private Workbook getWorkbook( String filePath) throws IOException {

        Workbook workbook = null;
        if ( filePath.endsWith( BookController.XSSF_SUFFIX)) {
            // XSSF形式
            workbook = new XSSFWorkbook( filePath);
        } else {
            // HSSF形式
            FileInputStream stream = new FileInputStream( filePath);
            POIFSFileSystem fs = new POIFSFileSystem( stream);
            workbook = new HSSFWorkbook( fs);
            stream.close();
        }
        return workbook;
    }

    /**
     * ブック解析を実行する
     * 
     * @return bookData ブックデータ
     * @throws ParseException パース例外
     * @throws ExportException 出力処理例外
     */
    public BookData processBook() throws ParseException, ExportException {
        return processBook( null);
    }

    /**
     * ブック解析を実行する
     * 
     * @param data TagParser.parseメソッドまで引き継がれる処理データ
     * @return bookData ブックデータ
     * @throws ParseException パース例外
     * @throws ExportException 出力処理例外
     */
    public BookData processBook( Object data) throws ParseException, ExportException {

        // ブック解析前処理
        for ( TransProcessListener listener : processListeners) {
            listener.preBookParse( workbook);
        }

        controller.parseBook( data);

        // ブック解析後処理
        for ( TransProcessListener listener : processListeners) {
            listener.postBookParse( workbook, controller.getBookData());
        }

        return controller.getBookData();
    }

    /**
     * シート解析を実行する
     * 
     * @param sheetName シート名
     * @return sheetData シートデータ
     * @throws ParseException パース例外
     * @throws ExportException 出力処理例外
     */
    public SheetData processSheet( String sheetName) throws ParseException, ExportException {

        return processSheet( sheetName, null);

    }

    /**
     * シート解析を実行する
     * 
     * @param sheetName シート名
     * @param data TagParser.parseメソッドまで引き継がれる処理データ
     * @return sheetData シートデータ
     * @throws ParseException パース例外
     * @throws ExportException 出力処理例外
     */
    public SheetData processSheet( String sheetName, Object data) throws ParseException, ExportException {

        SheetData sheetData = null;
        sheetData = controller.parseSheet( sheetName, data);
        return sheetData;
    }

    /**
     * タグがデフォルトSqlパーサのタグかどうかを判別し結果を返す
     * 
     * @param tag 判定するタグ
     * @return デフォルトSqlパーサのタグの場合はtrue、それ以外の場合はfalse
     */
    public Boolean isDefaultSqlTag( String tag) {

        boolean result = false;

        List<String> defaultSqlTags = TransCreateHelper.getDefaultSqlTags();
        if ( defaultSqlTags.contains( tag)) {
            result = true;
        }

        return result;
    }

    /**
     * ブックに含まれるシート名の一覧取得(コメントシート含む)
     * 
     * @return シート名の一覧
     */
    public List<String> getSheetNames() {
        return controller.getSheetNames();
    }

    /**
     * タグパーサの追加
     * 
     * @param tagParser 追加するタグパーサ
     */
    public void addTagParser( TagParser<?> tagParser) {
        this.controller.addTagParser( tagParser);
    }

    /**
     * 対象シート指定でのタグパーサの追加
     * 
     * @param sheetName 対象シート名
     * @param tagParser 追加するタグパーサ
     */
    public void addTagParser( String sheetName, TagParser<?> tagParser) {
        this.controller.addTagParser( sheetName, tagParser);
    }

    /**
     * 指定タグのタグパーサ情報を削除する
     * 
     * @param tag タグ
     */
    public void removeTagParser( String tag) {
        this.controller.removeTagParser( tag);
    }

    /**
     * すべてのタグパーサを削除する
     */
    public void clearTagParsers() {
        this.controller.clearTagParsers();
    }

    /**
     * ブック出力処理クラスの追加
     * 
     * @param bookExporter ブック出力処理クラス
     */
    public void addBookExporter( BookExporter bookExporter) {
        this.controller.addBookExporter( bookExporter);
    }

    /**
     * すべてのブック出力処理クラスを削除する
     */
    public void clearBookExporters() {
        this.controller.clearBookExporters();
    }

    /**
     * シート出力処理クラスの追加
     * 
     * @param sheetExporter シート出力処理クラス
     */
    public void addSheetExporter( SheetExporter sheetExporter) {
        this.controller.addSheetExporter( sheetExporter);
    }

    /**
     * 対象シート指定でのシート出力処理クラス
     * 
     * @param sheetName 対象シート名
     * @param sheetExporter シート出力処理クラス
     */
    public void addSheetExporter( String sheetName, SheetExporter sheetExporter) {
        this.controller.addSheetExporter( sheetName, sheetExporter);
    }

    /**
     * すべてのシート出力処理クラスを削除する
     */
    public void clearSheetExporters() {
        this.controller.clearSheetExporters();
    }

    /**
     * エラーハンドラの取得
     * 
     * @return エラーハンドラ
     */
    public ParseErrorHandler getErrorHandler() {
        return this.controller.getErrorHandler();
    }

    /**
     * エラーハンドラの設定
     * 
     * @param errorHandler エラーハンドラ
     */
    public void setErrorHandler( ParseErrorHandler errorHandler) {
        this.controller.setErrorHandler( errorHandler);
    }

    /**
     * シート処理リスナクラスの追加
     * 
     * @param sheetParseListener シート処理リスナクラス
     */
    public void addSheetParseListener( SheetParseListener sheetParseListener) {
        this.controller.addSheetParseListener( sheetParseListener);
    }

    /**
     * 対象シート指定でのシート処理リスナクラスの追加
     * 
     * @param sheetName 対象シート名
     * @param sheetParseListener シート処理リスナクラス
     */
    public void addSheetParseListener( String sheetName, SheetParseListener sheetParseListener) {
        this.controller.addSheetParseListener( sheetName, sheetParseListener);
    }

    /**
     * すべてのシート処理リスナクラスを削除する
     */
    public void clearSheetParseListeners() {
        this.controller.clearSheetParseListeners();
    }

    /**
     * プロセスリスナクラスを追加する
     * 
     * @param processListener 追加するプロセスリスナクラス
     */
    public void addTransProcessListener( TransProcessListener processListener) {
        this.processListeners.add( processListener);
    }

    /**
     * すべてのプロセスリスナクラスを削除する
     */
    public void clearTransProcessListeners() {
        this.processListeners.clear();
    }
}
