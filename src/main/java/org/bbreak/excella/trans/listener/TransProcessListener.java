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

package org.bbreak.excella.trans.listener;

import org.apache.poi.ss.usermodel.Workbook;
import org.bbreak.excella.core.BookData;
import org.bbreak.excella.core.exception.ParseException;

/**
 * ブック処理前後に呼び出されるイベントリスナのインターフェイス
 * 
 * @since 1.0
 */
public interface TransProcessListener {

    /**
     * ブック処理前に呼び出されるメソッド
     * 
     * @param workbook ワークブック
     * @throws ParseException パース例外
     */
    void preBookParse( Workbook workbook) throws ParseException;

    /**
     * ブック処理後に呼び出されるメソッド
     * 
     * @param workbook ワークブック
     * @param bookData 処理結果のデータ
     * @throws ParseException パース例外
     */
    void postBookParse( Workbook workbook, BookData bookData) throws ParseException;
}
