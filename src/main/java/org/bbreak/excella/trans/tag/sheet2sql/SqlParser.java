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

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.core.tag.excel2java.ListParser;

/**
 * シートを解析し、SQL文字列を<BR>
 * List&lt;String&gt;で返却するパーサ
 * 
 * @since 1.0
 */
public class SqlParser extends ListParser {

    /**
     * デフォルトタグ
     */
    public static final String DEFAULT_TAG = "@Sql";

    /**
     * SQL接頭句
     */
    protected static final String SQL_PREFIX = "";

    /**
     * SQL接尾句
     */
    protected static final String SQL_SUFFIX = ";";

    /**
     * コンストラクタ
     */
    public SqlParser() {
        super( DEFAULT_TAG);
    }

    /**
     * コンストラクタ
     * 
     * @param tag タグ
     */
    public SqlParser( String tag) {
        super( tag);
    }

    /**
     * パース処理
     * 
     * @param sheet 対象シート
     * @param tagCell タグが定義されたセル
     * @param data TransProcessorのprocessBook, processSheetメソッドで<BR> 
     *              引数を渡した場合にTagParserまで引き継がれる処理データ<BR>
     * @return パース結果
     * @throws ParseException パース例外
     */
    @Override
    public List<String> parse( Sheet sheet, Cell tagCell, Object data) throws ParseException {

        return parse( sheet, tagCell, data, SQL_PREFIX, SQL_SUFFIX);
    }

    /**
     * 接頭句、接尾句を付与した文字列のリストを返す。
     * 
     * @param sheet シート
     * @param tagCell タグセル
     * @param data TransProcessorのprocessBook, processSheetメソッドで<BR> 
     *              引数を渡した場合にTagParser.parseメソッドまで引き継がれる処理データ<BR>
     * @param prefix 接頭句
     * @param suffix 接尾句
     * @return SQL文字列のリスト
     * @throws ParseException パース例外
     */
    public List<String> parse( Sheet sheet, Cell tagCell, Object data, String prefix, String suffix) throws ParseException {

        List<?> objList = super.parse( sheet, tagCell, data);
        List<String> resultList = new ArrayList<String>();

        for ( Object obj : objList) {

            if ( obj == null) {
                continue;
            }

            String sql = obj.toString();
            StringBuilder strBuild = new StringBuilder();

            // 接尾句を付与する
            strBuild.append( prefix);

            strBuild.append( sql);

            // 接尾句を付与する
            strBuild.append( suffix);

            sql = strBuild.toString();
            resultList.add( sql);
        }

        return resultList;
    }
}
