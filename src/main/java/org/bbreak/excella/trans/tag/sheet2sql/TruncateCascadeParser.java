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

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.bbreak.excella.core.exception.ParseException;

/**
 * シートを解析し、cascadeを付与したTruncate文の<BR>
 * SQL文字列をList&lt;String&gt;で返却するパーサ
 * 
 * @since 1.0
 */
public class TruncateCascadeParser extends SqlParser {

    /**
     * デフォルトタグ
     */
    public static final String DEFAULT_TAG = "@TruncateCascade";

    /**
     * SQL接頭句
     */
    protected static final String SQL_PREFIX = "truncate ";

    /**
     * SQL接尾句
     */
    protected static final String SQL_SUFFIX = " cascade;";

    /**
     * コンストラクタ
     */
    public TruncateCascadeParser() {
        super( DEFAULT_TAG);
    }

    /**
     * コンストラクタ
     * 
     * @param tag タグ
     */
    public TruncateCascadeParser( String tag) {
        super( tag);
    }

    /**
     * パース処理
     * 
     * @param sheet 対象シート
     * @param tagCell タグが定義されたセル
     * @param data TransProcessorのprocessBook, processSheetメソッドで<BR> 
     *              引数を渡した場合にTagParser.parseメソッドまで引き継がれる処理データ<BR>
     * @return パース結果
     * @throws ParseException パース例外
     */
    @Override
    public List<String> parse( Sheet sheet, Cell tagCell, Object data) throws ParseException {

        return super.parse( sheet, tagCell, data, SQL_PREFIX, SQL_SUFFIX);
    }
}
