/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: UserOrgDataTransExecuter.java 38 2009-07-02 09:17:36Z yuta-takahashi $
 * $Revision: 38 $
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
package org.bbreak.excella.trans.samples;

import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import org.bbreak.excella.core.BookData;
import org.bbreak.excella.core.SheetData;
import org.bbreak.excella.core.exporter.book.ConsoleExporter;
import org.bbreak.excella.core.handler.DebugErrorHandler;
import org.bbreak.excella.trans.processor.TransProcessor;

/**
 * データ移行サンプルクラス
 * 
 * @since 1.0
 */
public class UserOrgDataTransExecuter {

    @SuppressWarnings( {"unchecked", "unused" })
    public static void main( String[] args) throws Exception {

        // クラスの場所から読み込むファイルのパスを取得
        String filename = "移行データサンプル.xls";
        URL url = UserOrgDataTransExecuter.class.getResource( filename);
        String filePath = URLDecoder.decode( url.getFile(), "UTF-8");

        // プロセッサ生成
        TransProcessor processor = new TransProcessor( filePath);

        // エラーハンドラ設定
        processor.setErrorHandler( new DebugErrorHandler());

        // ブックエクスポータ追加
        processor.addBookExporter( new ConsoleExporter());
        
        // プロセス実行
        BookData bookData = processor.processBook();

        // シート名リストの取得
        List<String> sheetNames = processor.getSheetNames();

        // シート単位でループ
        for ( String sheetName : sheetNames) {

            if ( !sheetName.startsWith( TransProcessor.COMMENT_PREFIX)) {
                // コメントアウトされていないシートの場合

                // シートデータの取得
                SheetData sheetData = bookData.getSheetData( sheetName);

                // タグ名リストの取得
                List<String> tagNames = sheetData.getKeyList();

                // タグ名リストでループ
                for ( String tagName : tagNames) {

                    if ( processor.isDefaultSqlTag( tagName)) {
                        // 結果がSQLの場合

                        List<String> sqlList = ( List<String>) sheetData.get( tagName);

                        /* SQLの実行処理を記述 */

                    } else {
                        // それ以外の場合
                        
                        List<Object> entityList = ( List<Object>) sheetData.get( tagName);
                        
                        /* エンティティの処理を記述 */

                    }
                }
            }
        }
    }
}
