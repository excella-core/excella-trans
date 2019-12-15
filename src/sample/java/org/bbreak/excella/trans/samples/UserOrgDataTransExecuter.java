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
